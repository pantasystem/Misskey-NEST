package org.panta.misskeynest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_drive.*
import org.panta.misskeynest.pager.DrivePagerAdapter


class DriveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drive)

        setSupportActionBar(drive_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pagerAdapter = DrivePagerAdapter(supportFragmentManager, null)
        drive_pager.adapter = pagerAdapter
        drive_pager.offscreenPageLimit = 2
        drive_tab.setupWithViewPager(drive_pager)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> finish()
        }
        return true
    }
}
