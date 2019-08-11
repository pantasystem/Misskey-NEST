package org.panta.misskeynest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_drive.*
import org.panta.misskeynest.fragment.DriveFragment
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.viewdata.DriveViewData


class DriveActivity : AppCompatActivity(), ItemClickListener<DriveViewData> {

    private lateinit var mDriveFragment: DriveFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drive)

        setSupportActionBar(drive_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ft = supportFragmentManager.beginTransaction()
        mDriveFragment = DriveFragment()
        ft.replace(R.id.fragment_base, mDriveFragment)
        ft.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    override fun toString(): String {
        return "DriveActivity"
    }

    override fun onClick(item: DriveViewData) {
        Log.d("DriveActivity", "アイテムがクリックされました: $item")
        when (item) {
            is DriveViewData.FolderViewData -> {
                changeFragment(item)
            }
            is DriveViewData.FileViewData -> {
                Log.d("DriveActivity", "プレビュー用のActivityが開始されます")
            }

        }
    }

    override fun onBackPressed() {
        val backedResult = mDriveFragment.goBack()
        if(backedResult){
            //ok
        }else{
            finish()
        }
    }
    private fun changeFragment(item: DriveViewData.FolderViewData){
        /*val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_base, DriveFragment.newInstance(item))
        ft.commit()*/
        mDriveFragment.reset(item)
    }
}
