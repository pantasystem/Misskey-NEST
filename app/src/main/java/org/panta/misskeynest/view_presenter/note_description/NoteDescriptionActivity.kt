package org.panta.misskeynest.view_presenter.note_description

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note_description.*
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.TimelineAdapter
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.repository.Description
import org.panta.misskeynest.view_data.NoteViewData
import java.io.File

class NoteDescriptionActivity : AppCompatActivity() {

    companion object{
        const val NOTE_DESCRIPTION_NOTE_PROPERTY = "NOTE_DESCRIPTION_ACTIVITY_NOTE_PROPERTY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_description)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        val note: Note? = intent.getSerializableExtra(NOTE_DESCRIPTION_NOTE_PROPERTY) as Note

        if(note == null){
            finish()
            return
        }

        val testDescription = Description()
        testDescription.getOriginNotes(note){
            if(it == null){

            }else{
                showNotes(it)
            }
        }

    }

    private fun showNotes(notes: List<NoteViewData>){

        val layoutManager = LinearLayoutManager(applicationContext)
        note_description_view.layoutManager = layoutManager

        note_description_view.adapter = org.panta.misskeynest.adapter.TimelineAdapter(applicationContext, notes)
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
