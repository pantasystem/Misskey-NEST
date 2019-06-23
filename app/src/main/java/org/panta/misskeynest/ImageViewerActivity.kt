package org.panta.misskeynest

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_image_viewer.*
import org.panta.misskeynest.pager.ImagePageAdapter

class ImageViewerActivity : AppCompatActivity() {

    companion object{
        private const val IMAGE_URL_LIST = "IMAGE_VIEW_ACTIVITY_IMAGE_URL_LIST"
        private const val CLICKED_IMAGE_URL = "CLICKED_IMAGE_URL_IMAGE_VIEWER_ACTIVITY"

        fun startActivity(context: Context?, imageUrlList: Array<String>, showImagePage: Int){
            val intent = Intent(context, ImageViewerActivity::class.java)
            intent.putExtra(IMAGE_URL_LIST, imageUrlList)
            intent.putExtra(CLICKED_IMAGE_URL, showImagePage)
            context?.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        val imageArray = intent.getStringArrayExtra(IMAGE_URL_LIST)
        val clickedImageIndex = intent.getIntExtra(CLICKED_IMAGE_URL, 0)

        val imageList = imageArray.toList()

        Log.d("ImageViewerActivity", imageList.toString())
        Log.d("ImageViewerActivity", imageList.size.toString())
        val pagerAdapter = ImagePageAdapter(supportFragmentManager, imageList)
        image_view_pager.offscreenPageLimit = imageList.size
        image_view_pager.adapter = pagerAdapter

        image_view_pager.currentItem = clickedImageIndex

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
