package com.phaseII.placepoint.SubscriptionPlan

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.phaseII.placepoint.R
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.phaseII.placepoint.Constants


class SubscriptionActivity : AppCompatActivity() {
    lateinit var pager: ViewPager
    lateinit var tabLayout: TabLayout

    private lateinit var toolbar: Toolbar
    private lateinit var mText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)
        Constants.getSSlCertificate(this)
        setViewPager()
        setToolBar()

    }

    private fun setToolBar() {

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        mText = toolbar.findViewById(R.id.toolbar_title)
        mText.text = "Subscription Plans"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setViewPager() {
        var userType = Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "")
        var count = 2
//        if (userType == "2") {
//            count = 1
//        }
        pager = findViewById(R.id.photos_viewpager)
        tabLayout = findViewById(R.id.tabLayout)
        val adapter = ViewPagerAdapter(supportFragmentManager!!, count)
        pager.adapter = adapter
        tabLayout.setupWithViewPager(pager, true)
    }
}
