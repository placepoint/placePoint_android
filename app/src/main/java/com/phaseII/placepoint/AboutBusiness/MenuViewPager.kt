package com.phaseII.placepoint.AboutBusiness

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.github.vivchar.viewpagerindicator.ViewPagerIndicator
import com.phaseII.placepoint.ImagePagerAdapter
import com.phaseII.placepoint.MenuImagePagerAdapter
import com.phaseII.placepoint.R

class MenuViewPager : AppCompatActivity() {
    lateinit var Pager: ViewPager
    lateinit var view_pager_indicator: ViewPagerIndicator
    lateinit var toolbar: Toolbar
    lateinit var toolbarArrow: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_view_pager)
        Pager = findViewById(R.id.pager)
        view_pager_indicator = findViewById(R.id.view_pager_indicator)
        setToolBar()

        val args = intent.getBundleExtra("BUN")
        val list = (args.getStringArrayList("list") as ArrayList<String>?)
        val adapterViewPager = MenuImagePagerAdapter(supportFragmentManager, list!!, this@MenuViewPager)
        Pager.adapter = adapterViewPager
        view_pager_indicator.setupWithViewPager(Pager)
        val pos=intent.getIntExtra("Position",0)

        Pager.currentItem = pos
    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        toolbarArrow = toolbar.findViewById(R.id.arrow_down) as ImageView
        toolbarArrow.visibility = View.GONE
        setSupportActionBar(toolbar)
        title = ""
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        mTitle.text = "Photos"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
