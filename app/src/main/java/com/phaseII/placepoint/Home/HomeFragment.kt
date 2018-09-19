package com.phaseII.placepoint.Home


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
import com.phaseII.placepoint.BusEvents.*
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.Town.TownActivity
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.activity_home.*
import java.util.ArrayList


class HomeFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var toolbar: Toolbar
    private lateinit var mText: TextView
    private lateinit var dots: ImageView
    private lateinit var back: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_home, container, false)

        init(view)
        Constants.getSSlCertificate(activity!!)
        setToolBar(view)
        setHasOptionsMenu(true)
        setupViewPager(viewPager)
        setCustomFont(tabLayout)
        return view
    }


    override fun onResume() {
        super.onResume()



        Constants.getBus().register(this)
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.SHOW_ALL_POST, "yes").apply()
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.FROMINTENT, "home").apply()
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.STOPCLICK, "no").apply()
        if (Constants.getPrefs(activity!!)!!.getString("showTaxiAtHome", "no") == "yes") {
            // Constants.getPrefs(activity!!)!!.edit().putString("showTaxiAtHome", "no").apply()

            taxiWorking()
        }
    }

    private fun setToolBar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        mText = toolbar.findViewById(R.id.toolbar_title)
        dots = toolbar.findViewById(R.id.dots)
        back = toolbar.findViewById(R.id.back) as ImageView
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
        val showback = Constants.getPrefs(activity!!)!!.getString("showHomeBackButton", "no")
        if (showback == "yes") {
            back.visibility = View.VISIBLE
        } else {
            if (Constants.getPrefs(activity!!)!!.getString("showBackYesOrNo", "category") != "category") {
                back.visibility = View.GONE
            }
        }
        back.setOnClickListener {
            Constants.getBus().post(DoBackActionInDashBoard("value"))
        }
//        toolbar.inflateMenu(R.menu.my_home_menu)
//        toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
//            override fun onMenuItemClick(item: MenuItem): Boolean {
//                if (item.itemId==android.R.id.){
//                    val intent =Intent(activity,TownActivity::class.java)
//                    intent.putExtra("from","true")
//                    startActivity(intent)
//                }
//                return false
//            }
//        })
    }

    private fun init(view: View) {
        viewPager = view.findViewById(R.id.viewpager)
        tabLayout = view.findViewById(R.id.tabs) as TabLayout
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
                        tabViewChild.typeface = Typeface.createFromAsset(activity!!.getAssets(), "fonts/bold.ttf")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setupViewPager(viewPager: ViewPager) {

        var showFeed: String = "1"

        var counter = 2
        val cat = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_LIST, "")!!
        if (!cat.isEmpty()) {
            var cat_list: ArrayList<ModelCategoryData> = Constants.getCategoryData(cat)!!
            val selectedId = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_IDSUB, "")!!
            for (i in 0 until cat_list.size) {
                if (selectedId == cat_list[i].id) {
                    showFeed = cat_list[i].show_on_live
                }
            }
            var freeListing = Constants.getPrefs(activity!!)!!.getString("freeListing", "no")
            if (showFeed == "0" ) {
            //if (showFeed == "0" || freeListing.equals("yes")) {
                counter = 1
                viewPager.adapter = ViewPagerAdapter(fragmentManager!!, counter)
                tabLayout.setupWithViewPager(viewPager)
                tabLayout.getTabAt(0)!!.text = "Business listings"
                // tabLayout.getTabAt(1)!!.text = "Live feed"
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

//        var firsttime=Constants.getPrefs(activity!!)!!.getString("firstTime","no")
//        if (firsttime == "yes"){
//            Constants.getPrefs(activity!!)!!.edit().putString("firstTime","no").apply()
//            viewPager.currentItem = 1
//        }


    }


    override fun onPause() {
        super.onPause()
        Constants.getBus().unregister(this)
    }

    @Subscribe
    fun getEventValue(event: TAXI_EVENT) {
        taxiWorking()
        back.visibility = View.VISIBLE

    }

    @Subscribe
    fun getEventValue(event: HideBackButton) {

        if (Constants.getPrefs(activity!!)!!.getString("showBackYesOrNo", "category") != "category") {
            back.visibility = View.GONE
        }

    }

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


    @Subscribe
    fun getEventValue(event: SeTaxitTitleEvent) {

        mText.text = "Taxi" +
                " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")
        back.visibility = View.VISIBLE
        Constants.getPrefs(activity!!)!!.edit().putString("backPress", "1").apply()
        Constants.getPrefs(activity!!)!!.edit().putString("Title", mText.text.toString()).apply()
//        if()
//        Constants.getBus().post(BusiniessListingTaxiEvent("taxi"))
//        Constants.getBus().post(LiveFeedTaxiEvent("LiveTaxi"))

    }

    @Subscribe
    fun getEventValue(event: SetTitleEvent) {

        if (event.value.status == "1") {
            mText.text = "All " + event.value.name +
                    " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")
        } else {
            mText.text = Constants.getPrefs(activity!!)!!.getString(Constants.CATEGORY_NAMEO, "") +
                    " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")

        }


    }
}
