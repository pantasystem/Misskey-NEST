package org.panta.misskeynest.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import org.panta.misskeynest.R
import org.panta.misskeynest.entity.EmojiProperty
import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.INoteClickListener
import org.panta.misskeynest.interfaces.ITimeFormat
import org.panta.misskeynest.interfaces.IUserClickListener
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.usecase.NoteAdjustment
import org.panta.misskeynest.util.InjectionImage
import org.panta.misskeynest.util.InjectionText
import org.panta.misskeynest.view_data.NoteViewData
import java.util.*
import kotlin.collections.ArrayList

abstract class AbsNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var contentClickListener: INoteClickListener? = null
    private var userClickListener: IUserClickListener? = null

    abstract val timelineItem: RelativeLayout
    abstract val whoReactionUserLink: Button
    abstract val userIcon: ImageView
    abstract val userName: TextView
    abstract val userId: TextView
    abstract val noteText: TextView
    abstract val elapsedTime: TextView

    /*private val imageView1: ImageView = itemView.image_1
    private val imageView2: ImageView = itemView.image_2
    private val imageView3: ImageView = itemView.image_3
    private val imageView4: ImageView = itemView.image_4*/
    abstract val imageViewList: List<ImageView>

    abstract val subNote : RelativeLayout
    abstract val subUserIcon: ImageView
    abstract val subUserName: TextView
    abstract val subUserId: TextView
    abstract val subNoteText: TextView

    abstract val reactionView: RecyclerView

    abstract val replyButton: ImageButton
    abstract val replyCount: TextView
    abstract val reNoteButton: ImageButton
    abstract val reNoteCount: TextView
    abstract val reactionButton: ImageButton
    abstract val descriptionButton: ImageButton

    abstract val showThreadButton: Button

    abstract val mediaPlayButton: Button

    abstract val mTimeFormatter: ITimeFormat

    private val injectionText = InjectionText()
    private val injectionImage = InjectionImage()

    private val mLinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    fun onBind(content: NoteViewData){
        if(content.reactionCountPairList.isNotEmpty()){

        }else{
            invisibleReactionCount()
        }
        when(content.type){
            NoteAdjustment.NoteType.REPLY -> {
                setReply(content)
            }
            NoteAdjustment.NoteType.NOTE -> {
                //これはNote
                setNote(content)
            }
            NoteAdjustment.NoteType.RE_NOTE -> {
                //これはリノート
                setReNote(content)
            }
            NoteAdjustment.NoteType.QUOTE_RE_NOTE -> {
                setQuoteReNote(content)
            }
            else -> throw IllegalAccessException("利用が許されていない値です $${content.type}")
        }
    }

    fun setNote(content: NoteViewData){
        val toShowNote = content.toShowNote
        setNoteContent(content)

        invisibleSubContents()
        showThreadButton.visibility = View.GONE //共通６
        subNote.visibility = View.GONE
        whoReactionUserLink.visibility = View.GONE  //非共通
        setRelationNoteListener(toShowNote.id, toShowNote, timelineItem, noteText)

    }

    fun setReNote(content: NoteViewData){
        val toShowNote = content.toShowNote
        setNoteContent(content)  //共通２

        invisibleSubContents()
        setWhoReactionUserLink(content.note.user, "リノート")
        showThreadButton.visibility = View.GONE
        setRelationNoteListener(toShowNote.id, toShowNote, timelineItem, noteText)


    }
    fun setQuoteReNote(content: NoteViewData){
        val toShowNote = content.toShowNote
        setNoteContent(content)

        showThreadButton.visibility = View.GONE
        setSubContent(toShowNote.renote!!)
        subNote.visibility = View.VISIBLE
        setWhoReactionUserLink(toShowNote.user, "引用リノート")

        //引用先とノートは別なので別でクリックリスナーを設定している
        setRelationNoteListener(toShowNote.id, toShowNote, timelineItem)
        setRelationNoteListener(toShowNote.renote.id, toShowNote.renote, subNoteText)

    }

    fun setReply(content: NoteViewData){
        val toShowNote = content.toShowNote //共通０
        setNoteContent(content)


        if(content.toShowNote.renote != null){
            setSubContent(content.toShowNote.renote)
            subNote.visibility = View.VISIBLE
        }else{
            subNote.visibility = View.GONE
        }

        showThreadButton.visibility = View.VISIBLE
        subNote.visibility = View.GONE
        setWhoReactionUserLink(toShowNote.user, "クソリプ")
        setRelationNoteListener(toShowNote.id, toShowNote, timelineItem, noteText, showThreadButton)    //非共通


    }


    fun invisibleReactionCount(){
        reactionView.visibility = View.GONE
    }

    fun addOnItemClickListener(listener: INoteClickListener?){
        contentClickListener = listener
    }
    fun addOnUserClickListener(listener: IUserClickListener?){
        this.userClickListener = listener
    }

    private fun setReactionCount(viewData: NoteViewData){
        val adapter = ReactionRecyclerAdapter(
            viewData.reactionCountPairList,
            viewData.toShowNote.myReaction
        )
        adapter.reactionItemClickListener = object : ItemClickListener<String> {
            override fun onClick(e: String) {
                Log.d("NoteViewHolder", "setReactionCountがクリックされた")
                contentClickListener?.onReactionClicked(viewData.toShowNote.id, viewData.toShowNote, viewData, e)
            }
        }
        reactionView.adapter = adapter
        reactionView.layoutManager = mLinearLayoutManager
        reactionView.visibility = View.VISIBLE
        if(viewData.toShowNote.myReaction == null){
            reactionButton.setImageResource(R.drawable.ic_plus)
        }else{
            reactionButton.setImageResource(R.drawable.ic_minus)
        }

    }

    private fun invisibleSubContents(){
        subUserIcon.visibility = View.GONE
        subUserName.visibility = View.GONE
        subUserId.visibility = View.GONE
        subNoteText.visibility =View.GONE
    }

    private fun setWhoReactionUserLink(user: User?, status: String){
        whoReactionUserLink.visibility = View.VISIBLE
        val text = "${user?.name?:user?.userName}さんが${status}しました"
        injectionText.injectionTextGoneWhenNull(text, whoReactionUserLink, user?.emojis)
        whoReactionUserLink.setOnClickListener{
            if(user != null){
                userClickListener?.onClickedUser(user)
            }
        }
    }


    private fun setNoteContent(content: NoteViewData){
        val note = content.toShowNote
        injectionName(note.user?.name, note.user?.userName, userName, note.user?.emojis)
        injectionId(note.user?.userName, note.user?.host, userId)
        injectionImage.roundInjectionImage(note.user?.avatarUrl?:"non", userIcon, 180)
        injectionText.injectionTextGoneWhenNull(note.text, noteText, note.emojis)
        setRelationUserListener(note.user!!, userName, userId, userIcon)
        setImage(filterImageData(note))
        injectionMediaPlayButton(note.files?.firstOrNull(), mediaPlayButton)
        if(note.renote != null){
            setSubContent(note.renote)
        }

        setReplyCount(note.replyCount)
        setReNoteCount(note.reNoteCount)
        setFourControlButtonListener(note, content)
        setReactionCount(content)
        setElapsedTime(content.note.createdAt)


    }

    private fun setSubContent(note: Note){
        injectionName(note.user?.name, note.user?.userName, subUserName, note.user?.emojis)
        injectionId(note.user?.userName, note.user?.host, subUserId)
        injectionImage.roundInjectionImage(note.user?.avatarUrl?:"non", subUserIcon, 180)
        injectionText.injectionTextGoneWhenNull(note.text, subNoteText)
        setRelationUserListener(note.user!!, subUserName, subUserId, subUserIcon)

    }


    private fun setImage(fileList: List<FileProperty>){

        val imageClickListener = View.OnClickListener { p0 ->
            var clickedImageIndex = 0

            for(n in 0 until imageViewList.size){
                if(imageViewList[n] == p0){
                    clickedImageIndex = n
                    break
                }
            }

            val urlList: List<String> = fileList.map{it.url}.filter{it != null && it.isNotBlank()}.map{it.toString()}
            contentClickListener?.onImageClicked(clickedImageIndex, urlList.toTypedArray())
        }

        imageViewList.forEach{
            it.visibility = View.GONE
            it.setOnClickListener(imageClickListener)
        }


        for(n in 0.until(fileList.size)){
            injectionImage.injectionImage(fileList[n].url!!, imageViewList[n], fileList[n].isSensitive)
        }

    }

    private fun setElapsedTime(date: Date){
        elapsedTime.text =  mTimeFormatter.formatTime(date)
    }

    //NP
    private fun setReplyCount(count: Int){
        injectionText.injectionTextInvisible(count.toString(), replyCount, "0")

    }

    //NP
    private fun setReNoteCount(count: Int){
        injectionText.injectionTextInvisible(count.toString(), reNoteCount, "0")
    }

    private fun setFourControlButtonListener(note: Note, viewData: NoteViewData){
        replyButton.setOnClickListener {
            contentClickListener?.onReplyButtonClicked(note.id, note)
        }
        reNoteButton.setOnClickListener {
            contentClickListener?.onReNoteButtonClicked(note.id, note)
        }
        reactionButton.setOnClickListener {
            contentClickListener?.onReactionClicked(note.id, note, viewData,null)
        }
        descriptionButton.setOnClickListener {
            contentClickListener?.onDescriptionButtonClicked(viewData.note.id, viewData.note)
        }
    }



    private fun injectionName(name: String?, id: String?, view: TextView, emojis: List<EmojiProperty>?){
        val tmpName = name?: id.toString()
        injectionText.injection(tmpName, view, emojis)
    }

    private fun injectionId(id: String?, host: String?, view: TextView){
        view.text = if(host == null) "@$id" else "@$id@$host"
        view.visibility = View.VISIBLE
    }

    private fun setRelationUserListener(user: User, vararg viewList: View){
        viewList.forEach {
            it.setOnClickListener{
                userClickListener?.onClickedUser(user)
            }
        }
    }

    private fun setRelationNoteListener(noteId: String, note: Note, vararg  view: View){
        view.forEach {
            it.setOnClickListener{
                contentClickListener?.onNoteClicked(noteId, note)
            }
        }
    }

    private fun filterImageData(data: Note): List<FileProperty>{

        val fileList = data.files ?: return emptyList()
        val nonNullUrlList = ArrayList<FileProperty>()
        for(n in fileList){
            val isImage = n?.type != null && n.type.startsWith("image")
            if(isImage && n?.url != null){
                nonNullUrlList.add(n)
            }
        }
        return nonNullUrlList
    }


    private fun injectionMediaPlayButton(fileProperty: FileProperty?, view: Button){
        val type = fileProperty?.type
        when {
            type == null -> {
                view.visibility = View.GONE
                return
            }
            type.startsWith("video") -> {
                view.visibility = View.VISIBLE
                view.text = "動画を再生"
            }
            type.startsWith("audio") -> {
                view.visibility = View.VISIBLE
                view.text = "音楽を再生"
            }
            else -> {
                view.visibility = View.GONE
                return
            }
        }
        view.setOnClickListener {
            contentClickListener?.onMediaPlayClicked(fileProperty)
        }
    }
}