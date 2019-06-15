package org.panta.misskeynest.view_presenter.note_description

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note_description.*
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.NoteDetailAdapter
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.listener.NoteClickListener
import org.panta.misskeynest.listener.UserClickListener
import org.panta.misskeynest.repository.PersonalRepository
import org.panta.misskeynest.storage.SharedPreferenceOperator
import org.panta.misskeynest.usecase.GetNoteDetail
import org.panta.misskeynest.view_data.NoteViewData
import org.panta.misskeynest.view_presenter.user_auth.AuthActivity

class NoteDescriptionActivity : AppCompatActivity() {

    companion object{
        const val NOTE_DESCRIPTION_NOTE_PROPERTY = "NOTE_DESCRIPTION_ACTIVITY_NOTE_PROPERTY"
    }

    private val mConnectionProperty: ConnectionProperty? by lazy {
        PersonalRepository(SharedPreferenceOperator(this))
            .getConnectionInfo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_description)

        setSupportActionBar(note_detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(mConnectionProperty == null){
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        val intent = intent
        val note: Note? = intent.getSerializableExtra(NOTE_DESCRIPTION_NOTE_PROPERTY) as Note

        Log.d("NoteDescriptionActivity", "対象のノート $note")

        if(note == null){
            finish()
            return
        }


        GetNoteDetail(mConnectionProperty!!).get(note){
            runOnUiThread {
                Log.d("NoteDescriptionActivity", "返ってきた $it")
                showNotes(it, note.id)
            }
        }

    }

    private fun showNotes(notes: List<NoteViewData>, currentNoteId: String){
        try{
            val layoutManager = LinearLayoutManager(applicationContext)
            note_description_view.layoutManager = layoutManager

            val adapter = NoteDetailAdapter(notes, currentNoteId)
            adapter.noteClickListener = NoteClickListener(this, this, mConnectionProperty!!).apply{
                this.onShowReactionDialog = {
                    it.show(supportFragmentManager, "reaction")
                }
            }
            adapter.userClickListener = UserClickListener(applicationContext)

            val index = notes.indexOf(notes.firstOrNull { it.id == currentNoteId })

            note_description_view.adapter = adapter
            note_description_view.scrollToPosition(index)
        }catch(e: Exception){
            Log.d("NoteDescriptionActivity", "error", e)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }
}
