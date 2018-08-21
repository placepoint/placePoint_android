package com.phaseII.placepoint.AboutBusiness

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.location.*
import com.phaseII.placepoint.AboutBusiness.BusinessDetails.DetailFragment
import com.phaseII.placepoint.BusinessDetailMap.BusinessDetailMapActivity
import com.phaseII.placepoint.ConstantClass.GpsTracker
import com.phaseII.placepoint.ConstantClass.Utils
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.BusinessListing.ModelBusiness
import com.phaseII.placepoint.Home.ViewPagerAdapter
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Town.ModelTown
import kotlinx.android.synthetic.main.about_business_scroll.*
import kotlinx.android.synthetic.main.activity_about_business.*
import kotlinx.android.synthetic.main.business_description.*
import kotlinx.android.synthetic.main.day_layout.*
import kotlinx.android.synthetic.main.location_text_layout.*
import org.json.JSONArray
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AboutBusinessActivity() : AppCompatActivity(), DetailFragment.setTitle {
    var busId: String = ""
    var showallpost: String = ""
    var from: String = ""
    var busName: String = ""
    var subscriptionType: String = ""
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var toolbar: Toolbar
    private lateinit var mText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_business)
        try {
            //modelBusiness = intent.extras!!.getParcelable<ModelBusiness>("model")
            busId = intent.extras.getString("busId")
            showallpost = intent.extras.getString("showallpost")
            from = intent.extras.getString("from")
            busName = intent.extras.getString("busName")

if(from=="businessListadapter"){
    Constants.getPrefs(this)!!.edit().putString("distance",intent.getStringExtra("distance")).apply()
    Constants.getPrefs(this)!!.edit().putString("mobNumber",intent.getStringExtra("mobNumber")).apply()
    Constants.getPrefs(this)!!.edit().putString("lati",intent.getStringExtra("lati")).apply()
    Constants.getPrefs(this)!!.edit().putString("longi",intent.getStringExtra("longi")).apply()
}
           // intent.putExtra("showallpost")
            /* val modelBusiness = intent.extras!!.getParcelable<ModelBusiness>("model")
             if (modelBusiness != null) {
                 mPresenter.getBusinessData(modelBusiness)
             }*/
            Constants.getPrefs(this)!!.edit().putString(Constants.BUSINESS_ID,busId).apply()
            Constants.getPrefs(this)!!.edit().putString(Constants.SHOW_ALL_POST,showallpost).apply()
            Constants.getPrefs(this)!!.edit().putString(Constants.FROMINTENT,from).apply()
            Constants.getPrefs(this)!!.edit().putString(Constants.STOPCLICK,"yes").apply()

            Constants.getPrefs(this)!!.edit().remove("BusinessSubscriptionType").apply()
            subscriptionType = intent.extras.getString("subscriptionType")
            Constants.getPrefs(this)!!.edit().putString("BusinessSubscriptionType",subscriptionType).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
setToolBar()
        init()

          }
    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
       // title = ""
        //supportActionBar!!.setDisplayShowTitleEnabled(false)
        mText = toolbar.findViewById(R.id.toolbar_title) as TextView
        val mArrow = toolbar.findViewById(R.id.arrow_down) as ImageView
        mArrow.visibility = View.GONE
       // mText.text = "Name"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun init() {
        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tabs) as TabLayout

        setupViewPager(viewPager)
    }


    private fun setupViewPager(viewPager: ViewPager) {

        viewPager.adapter = DetailViewPagerAdapter(supportFragmentManager!!)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)!!.text = "Business details"
        tabLayout.getTabAt(1)!!.text = busName+"'s"+" Posts"
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#34b0f2"))
        setCustomFont(tabLayout)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setTitleBusiness(business_name: String) {

        mText.text=business_name
    }
    fun setCustomFont(tabLayout: TabLayout) {
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
}
