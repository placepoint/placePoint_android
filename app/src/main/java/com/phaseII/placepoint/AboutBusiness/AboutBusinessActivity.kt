package com.phaseII.placepoint.AboutBusiness

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.phaseII.placepoint.AboutBusiness.BusinessDetails.DetailFragment
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import android.support.v4.view.ViewPager.OnPageChangeListener
import kotlinx.android.synthetic.main.toolbar_color_primary.*


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
    private lateinit var clainIcon: TextView

    var fromFreeListing="no"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_business)
        Constants.getSSlCertificate(this)
        try {
            //modelBusiness = intent.extras!!.getParcelable<ModelBusiness>("model")
            busId = intent.extras.getString("busId")
            showallpost = intent.extras.getString("showallpost")
            from = intent.extras.getString("from")
            busName = intent.extras.getString("busName")
            if (intent.hasExtra("show")){
                Constants.getPrefs(this)!!.edit().putString("showv", "yes").apply()
            }else{
                Constants.getPrefs(this)!!.edit().putString("showv", "no").apply()
            }

            if (from == "businessListadapter") {
                Constants.getPrefs(this)!!.edit().putString("distance", intent.getStringExtra("distance")).apply()
                Constants.getPrefs(this)!!.edit().putString("mobNumber", intent.getStringExtra("mobNumber")).apply()
                Constants.getPrefs(this)!!.edit().putString("lati", intent.getStringExtra("lati")).apply()
                Constants.getPrefs(this)!!.edit().putString("longi", intent.getStringExtra("longi")).apply()
                Constants.getPrefs(this)!!.edit().putString("freeListing", intent.getStringExtra("freeListing")).apply()
                Constants.getPrefs(this)!!.edit().putString("nameCat", intent.getStringExtra("nameCat")).apply()
               fromFreeListing=intent.getStringExtra("freeListing")
                if ( intent.getStringExtra("freeListing")=="yes"){
                    Constants.getPrefs(this)!!.edit().putString("HideContents", "yes").apply()
                }else{
                    Constants.getPrefs(this)!!.edit().putString("HideContents", "no").apply()
                }
            }
            // intent.putExtra("showallpost")
            /* val modelBusiness = intent.extras!!.getParcelable<ModelBusiness>("model")
             if (modelBusiness != null) {
                 mPresenter.getBusinessData(modelBusiness)
             }*/
            Constants.getPrefs(this)!!.edit().putString(Constants.BUSINESS_ID, busId).apply()
            Constants.getPrefs(this)!!.edit().putString(Constants.SHOW_ALL_POST, showallpost).apply()
            Constants.getPrefs(this)!!.edit().putString(Constants.FROMINTENT, from).apply()
            Constants.getPrefs(this)!!.edit().putString(Constants.STOPCLICK, "yes").apply()

            Constants.getPrefs(this)!!.edit().remove("BusinessSubscriptionType").apply()
            subscriptionType = intent.extras.getString("subscriptionType")
            Constants.getPrefs(this)!!.edit().putString("BusinessSubscriptionType", subscriptionType).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setToolBar()
        init()
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
               if(position==0){
                   clainIcon.visibility = View.VISIBLE
               }else{
                   clainIcon.visibility = View.GONE
               }
            }
        })
    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // title = ""
        //supportActionBar!!.setDisplayShowTitleEnabled(false)
        mText = toolbar.findViewById(R.id.toolbar_title) as TextView

         clainIcon = toolbar.findViewById(R.id.claim) as TextView


        // mText.text = "Name"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        clainIcon.setOnClickListener{
            showAlertForFaceBook()
        }
    }

    private fun showAlertForFaceBook() {
        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(false)
        dialog.setTitle("Alert")
        dialog.setMessage("To claim this business, click the claim button below and you will be taken to the PlacePoint Facebook page where you can PM us to request ownership. Please specify the email account you want to claim ownership with.")
        dialog.setPositiveButton("Claim", DialogInterface.OnClickListener { dialog, id ->
          //  Constants.shareOnFaceBook("text","https://www.facebook.com/placepoint.ireland",this);
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://www.facebook.com/placepoint.ireland")
            startActivity(i)
        })
                .setNegativeButton("Cancel ", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })

        val alert = dialog.create()
        alert.show()
    }

    private fun init() {
        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tabs) as TabLayout

        setupViewPager(viewPager)
    }


    private fun setupViewPager(viewPager: ViewPager) {

        viewPager.adapter = DetailViewPagerAdapter(supportFragmentManager!!,fromFreeListing)
        tabLayout.setupWithViewPager(viewPager)
        if (fromFreeListing.equals("yes")){
            tabLayout.getTabAt(0)!!.text = "Business details"
        }else{
            tabLayout.getTabAt(0)!!.text = "Business details"
            tabLayout.getTabAt(1)!!.text = busName + "'s" + " Posts"
        }

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

        mText.text = business_name
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

    override fun showHideClaim(value: Int) {
        if (value==1){
            claim.visibility= View.VISIBLE
        }else{
            claim.visibility= View.GONE
        }
    }
}
