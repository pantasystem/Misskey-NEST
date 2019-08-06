package org.panta.misskeynest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting.*
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.local.SharedPreferenceOperator

class SettingActivity : AppCompatActivity() {

    private lateinit var settingRepository: PersonalRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        settingRepository = PersonalRepository(SharedPreferenceOperator(this))

        setSimpleEdit()

    }

    private fun setSimpleEdit(){
        is_visible_simple_edit.isChecked = settingRepository.isVisibleSimpleEdit
        is_visible_simple_edit.setOnCheckedChangeListener { _, isChecked ->
            settingRepository.isVisibleSimpleEdit = isChecked
        }
    }

}
