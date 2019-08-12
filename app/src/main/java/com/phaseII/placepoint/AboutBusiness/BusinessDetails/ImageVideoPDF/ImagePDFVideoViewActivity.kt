package com.phaseII.placepoint.AboutBusiness.BusinessDetails.ImageVideoPDF

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.phaseII.placepoint.R




class ImagePDFVideoViewActivity : AppCompatActivity() ,ImageFragment.GetArrayImage,VideoFragment.GetArrayVideo,PDFFragment.GetArrayPDF{

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView
    lateinit var back: ImageView
     var array:ArrayList<String> = ArrayList()
     var imageArray:ArrayList<String> = ArrayList()
     var videoArray:ArrayList<String> = ArrayList()
     var pdfArray:ArrayList<String> = ArrayList()
    private lateinit var adapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pdfvideo_view)

        setToolBar()
        if (intent.hasExtra("array")){
            array =intent.getStringArrayListExtra("array")
            for (i in 0 until array.size){
                if (array[i].contains("mp4")||array[i].contains("mp3")
                        ||array[i].contains("wma")||array[i].contains(".mov")){
                    videoArray.add(array[i])
                }else if (array[i].contains("pdf")){
                    pdfArray.add(array[i])
                } else{
                    imageArray.add(array[i])
                }

            }
        }
        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tabs)

        setupViewPager(viewPager)
       //setFragment(ImageFragment())
    }

    private fun setCustomFont(tabLayout: TabLayout) {
        try {
            val vg = tabLayout.getChildAt(0) as ViewGroup
            val tabsCount = vg.childCount

            for (j in 0 until tabsCount) {
                val vgTab = vg.getChildAt(j) as ViewGroup

                val tabChildsCount = vgTab.childCount

                for (i in 0 until tabChildsCount) {
                    val tabViewChild = vgTab.getChildAt(i)
                    if (tabViewChild is TextView) {
                        //Put your font in assests folder
                        //assign name of the font here (Must be case sensitive)
                        tabViewChild.typeface = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun setupViewPager(viewPager: ViewPager?) {

        try {

            adapter= ViewPagerAdapter(this.supportFragmentManager!!)
            viewPager!!.adapter=adapter
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.getTabAt(0)!!.text = "Images"
            tabLayout.getTabAt(1)!!.text = "Videos"
            tabLayout.getTabAt(2)!!.text = "PDF"
            val mTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/bold.ttf")

            setCustomFont(tabLayout)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position

            }
        })

        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {

            }
        })
    }

    private fun setClickHandler() {

//        imageView.setOnClickListener{
//            setFragment(ImageFragment())
//        }
//        videoView.setOnClickListener{
//            setFragment(VideoFragment())
//        }
//        pdfView.setOnClickListener{
//            setFragment(PDFFragment())
//        }
    }

    private fun setFragment(fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    override fun getArrayImage(): ArrayList<String> {
        return imageArray
    }

    override fun getArrayVideo(): ArrayList<String> {
        return videoArray
    }

    override fun getArrayPDF(): ArrayList<String> {
        return pdfArray
    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        back = toolbar.findViewById(R.id.back) as ImageView
        back.visibility = View.GONE
        mTitle.text = "Files & Videos"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
