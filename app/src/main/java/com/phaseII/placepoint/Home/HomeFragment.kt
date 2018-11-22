package com.phaseII.placepoint.Home


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import com.phaseII.placepoint.R
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.TextView
import com.phaseII.placepoint.Constants
import android.widget.ImageView
import android.widget.LinearLayout
import com.github.florent37.tutoshowcase.TutoShowcase
import com.phaseII.placepoint.BusEvents.*
import com.phaseII.placepoint.Home.BusinessListing.BusinessListingFragment
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.Town.TownActivity
import com.squareup.otto.Subscribe
import java.util.ArrayList


class HomeFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var toolbar: Toolbar
    private lateinit var mText: TextView
    private lateinit var dots: ImageView
    private lateinit var back: ImageView
    private lateinit var filter: ImageView
    private lateinit var layContainViewPager: LinearLayout
    private lateinit var staticLay: LinearLayout
    private lateinit var popbl: PopupShow


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_home, container, false)
        init(view)
        Constants.getSSlCertificate(activity!!)
        setToolBar(view)
        setHasOptionsMenu(true)
        setupViewPager(viewPager)
        setCustomFont(tabLayout)
        val cc = Constants.getPrefs(activity!!)!!.getString("cog", "0").toInt()
        val dd = cc + 1
        Constants.getPrefs(activity!!)!!.edit().putString("cog", dd.toString()).apply()
        Constants.getPrefs(activity!!)!!.edit().putString("cob", "0").apply()

        return view
    }


    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        Constants.getBus().register(this)
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.SHOW_ALL_POST, "yes").apply()
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.FROMINTENT, "home").apply()
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.STOPCLICK, "no").apply()
        if (Constants.getPrefs(activity!!)!!.getString("showTaxiAtHome", "no") == "yes") {
            taxiWorking()
        }

        val prefs = Constants.getPrefs(activity!!)
        val showLay = prefs!!.getString("showLay", "static")
        if (showLay == "static") {
            filter.visibility = View.GONE
            layContainViewPager.visibility = View.GONE
            staticLay.visibility = View.VISIBLE
            mText.text = "HomePage"
            if (Constants.getPrefs(activity!!)!!.getString("showHLiveYes", "no") == "no") {
                Constants.getPrefs(activity!!)!!.edit().putString("showHLiveYes", "yes").apply()
                showCase()
            }

        } else {

            filter.visibility = View.VISIBLE
            layContainViewPager.visibility = View.VISIBLE
            staticLay.visibility = View.GONE
        }

    }

    private fun showCase() {
        TutoShowcase.from(activity!!)
                .setContentView(R.layout.tutorial_businesslist)
                .showOnce("yes")
                .show()
    }

    @SuppressLint("SetTextI18n")
    private fun setToolBar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        mText = toolbar.findViewById(R.id.toolbar_title)
        dots = toolbar.findViewById(R.id.dots)
        back = toolbar.findViewById(R.id.back) as ImageView
        filter = toolbar.findViewById(R.id.filter) as ImageView
        dots.visibility = View.GONE
        val cats = Constants.getPrefs(activity!!)!!.getString(Constants.CATEGORY_IDS, "")
        if (!cats.isEmpty()) {
            mText.text = Constants.getPrefs(activity!!)!!.getString(Constants.CATEGORY_NAMEO, "") +
                    " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")
        }
        dots.setOnClickListener {
            val intent = Intent(activity, TownActivity::class.java)
            intent.putExtra("from", "true")
            startActivity(intent)
        }
        val showBack = Constants.getPrefs(activity!!)!!.getString("showHomeBackButton", "no")
        if (showBack == "yes") {
            back.visibility = View.VISIBLE
        } else {
            if (Constants.getPrefs(activity!!)!!.getString("showBackYesOrNo", "category") != "category") {
                back.visibility = View.GONE
            }
        }
        back.setOnClickListener {
            Constants.getBus().post(DoBackActionInDashBoard("value"))
        }

    }

    private fun init(view: View) {
        viewPager = view.findViewById(R.id.viewpager)
        tabLayout = view.findViewById(R.id.tabs) as TabLayout
        layContainViewPager = view.findViewById(R.id.layContainViewPager)
        staticLay = view.findViewById(R.id.staticLay)
    }

    private fun setCustomFont(tabLayout: TabLayout) {
        try {
            val vg = tabLayout.getChildAt(0) as ViewGroup
            val tabsCount = vg.childCount

            for (j in 0 until tabsCount) {
                val vgTab = vg.getChildAt(j) as ViewGroup

                val tabChildCount = vgTab.childCount

                for (i in 0 until tabChildCount) {
                    val tabViewChild = vgTab.getChildAt(i)
                    if (tabViewChild is TextView) {
                        tabViewChild.typeface = Typeface.createFromAsset(activity!!.assets, "fonts/bold.ttf")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setupViewPager(viewPager: ViewPager) {

        var showFeed = "1"

        var counter = 2
        val cat = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_LIST, "")!!
        if (!cat.isEmpty()) {
            val catList: ArrayList<ModelCategoryData> = Constants.getCategoryData(cat)!!
            val selectedId = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_IDSUB, "")!!
            for (i in 0 until catList.size) {
                if (selectedId == catList[i].id) {
                    showFeed = catList[i].show_on_live
                }
            }
            if (showFeed == "0") {
                counter = 1
                viewPager.adapter = ViewPagerAdapter(fragmentManager!!, counter)
                tabLayout.setupWithViewPager(viewPager)
                tabLayout.getTabAt(0)!!.text = "Business listings"
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4F5D81"))
            } else {

                viewPager.adapter = ViewPagerAdapter(fragmentManager!!, counter)
                tabLayout.setupWithViewPager(viewPager)
                tabLayout.getTabAt(0)!!.text = "Business listings"
                tabLayout.getTabAt(1)!!.text = "Live feed"
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#34b0f2"))


            }
        } else {
            viewPager.adapter = ViewPagerAdapter(fragmentManager!!, counter)
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.getTabAt(0)!!.text = "Business listings"
            tabLayout.getTabAt(1)!!.text = "Live feed"
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#34b0f2"))
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    filter.visibility = View.VISIBLE


                } else {
                    if (Constants.getPrefs(activity!!)!!.getString("showTLiveYes", "no") == "no") {
                        if (Constants.getPrefs(activity!!)!!.getString("showTLive", "no") == "yes") {
                            Constants.getPrefs(activity!!)!!.edit().putString("showTLiveYes", "yes").apply()
                            showCaseLiveFeed()
                        }
                    }

                    filter.visibility = View.GONE
                }

            }

        })

        filter.setOnClickListener {

            val fragment = viewPager.adapter!!.instantiateItem(viewPager, 0) as BusinessListingFragment
            if (fragment is BusinessListingFragment) {
                fragment.showData()
            }


        }
    }

    private fun showCaseLiveFeed() {
        TutoShowcase.from(activity!!)
                .setContentView(R.layout.tutorialthree)
                .showOnce("yes")
                .show()
    }


    override fun onPause() {
        super.onPause()
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @Subscribe
    fun getEventValue(event: TAXI_EVENT) {
        taxiWorking()
        back.visibility = View.VISIBLE
    }

    @Subscribe
    fun getEventValue(event: SetInitialValue) {
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Subscribe
    fun getEventValue(event: HideBackButton) {

        if (Constants.getPrefs(activity!!)!!.getString("showBackYesOrNo", "category") != "category") {
            back.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun taxiWorking() {
        back.setOnClickListener {

            if (Constants.getPrefs(activity!!)!!.getString("showBackYesOrNo", "category") != "category") {
                back.visibility = View.GONE
            }
            Constants.getBus().post(DoBackActionInDashBoard("value"))
        }
        back.visibility = View.VISIBLE
        Constants.getPrefs(activity!!)!!.edit().putString("backPress", "1").apply()
        Constants.getPrefs(activity!!)!!.edit().putString("Title", mText.text.toString()).apply()
        Constants.getBus().post(BusiniessListingTaxiEvent("taxi"))
        Constants.getBus().post(LiveFeedTaxiEvent("LiveTaxi"))

        mText.text = "Taxi" +
                " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")


    }


    @SuppressLint("SetTextI18n")
    @Subscribe
    fun getTaxiEventValue(event: SeTaxitTitleEvent) {

        mText.text = "Taxi" +
                " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")
        back.visibility = View.VISIBLE
        Constants.getPrefs(activity!!)!!.edit().putString("backPress", "1").apply()
        Constants.getPrefs(activity!!)!!.edit().putString("Title", mText.text.toString()).apply()
    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    fun getTitleEventValue(event: SetTitleEvent) {

        if (event.value.status == "1") {
            mText.text = "All " + event.value.name +
                    " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")
        } else {
            mText.text = Constants.getPrefs(activity!!)!!.getString(Constants.CATEGORY_NAMEO, "") +
                    " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            popbl = context as PopupShow
        } catch (e: Exception) {

        }
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            popbl = activity as PopupShow
        } catch (e: Exception) {

        }
    }

    interface PopupShow {
        fun showPopUpBL()
    }
}
