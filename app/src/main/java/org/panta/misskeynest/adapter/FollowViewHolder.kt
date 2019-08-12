package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.vanniktech.emoji.EmojiTextView
import kotlinx.android.synthetic.main.item_follow_follower.view.*
import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.util.InjectionImage
import org.panta.misskeynest.viewdata.FollowViewData

class FollowViewHolder(itemView: View, var clickListener: org.panta.misskeynest.adapter.FollowsAdapter.FollowAdapterListener?) : RecyclerView.ViewHolder(itemView){

    private val statusText: EmojiTextView = itemView.status_text
    private val userIcon: ImageView = itemView.user_icon
    private val userName: TextView = itemView.user_name
    private val userIdView: TextView = itemView.user_name
    private val descriptionView: TextView = itemView.user_description
    private val followUnFollowButton: Button = itemView.follow_un_follow_button

    fun bindItem(data: FollowViewData, type: FollowFollowerType){
        val user = if(type == FollowFollowerType.FOLLOWING){
            statusText.text = if(data.follower == null){
                followUnFollowButton.text = "解除"
                "片思い悲しいなぁ・・"
            }else{
                followUnFollowButton.text = "解除"
                "フォローされています"
            }

            data.following
        }else{
            statusText.text = if(data.following == null){
                followUnFollowButton.text = "フォロー"
                "フォローしていません"
            }else{
                followUnFollowButton.text = "解除"
                "フォローしています"
            }
            data.follower
        }
        if(user != null){
            setUserInfo(user)
        }

        followUnFollowButton.setOnClickListener {
            clickListener?.onFollowButtonClicked(data)
        }
    }

    private fun setUserInfo(data: User){
        //setImage(userIcon, data.avatarUrl.toString())
        InjectionImage().injectionImage(data.avatarUrl.toString(), userIcon, false)
        userName.text = data.name
        userIdView.text = data.userName
        descriptionView.text = data.description
    }


}