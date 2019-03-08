package com.phaseII.placepoint.Business


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import android.support.v4.view.ViewPager.OnPageChangeListener
import android.support.v7.widget.Toolbar
import android.widget.TextView
import android.graphics.Typeface
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.github.florent37.tutoshowcase.TutoShowcase
import com.phaseII.placepoint.*
import com.phaseII.placepoint.BusEvents.*
import com.squareup.otto.Subscribe


class BusinessFragment : Fragment() {


    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView
    lateinit var back: ImageView
    lateinit var showMoreFrag: BackToMoreFragment


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_business, container, false)
        Constants.getSSlCertificate(activity!!)
        init(view)
        setToolBar(view)

        return view
    }

    private fun setToolBar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title)



            back = toolbar.findViewById(R.id.back) as ImageView
        back.visibility=View.VISIBLE
            mTitle.text = "Categories " + " (" + Constants.getPrefs(activity!!)?.getString(Constants.TOWN_NAME, "") + ")"
            back.setOnClickListener {
                showMoreFrag.showMoreFragment()
            }


        mTitle.text = "Business Profile"


    }
interface BackToMoreFragment{
    fun showMoreFragment()
}

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        showMoreFrag=context as BackToMoreFragment
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        showMoreFrag=activity as BackToMoreFragment
    }
    private fun init(view: View) {

        viewPager = view.findViewById(R.id.viewpager)
        tabLayout = view.findViewById(R.id.tabs)

        setupViewPager(viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {

        try {
            viewPager.adapter = ViewPagerAdapterBusiness(this.fragmentManager!!)
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.getTabAt(0)!!.text = "Profile"
            tabLayout.getTabAt(1)!!.text = "Add post"
            tabLayout.getTabAt(2)!!.text = "Scheduled"
            tabLayout.getTabAt(3)!!.text = "My posts"
            val mTypeface = Typeface.createFromAsset(getContext()!!.getAssets(), "fonts/bold.ttf")

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
                if (position == 1) {
                    Constants.getBus().post(EmptyFields("empty"))
                    if(Constants.getPrefs(activity!!)!!.getString("showALiveYes","no")=="no") {
                        Constants.getPrefs(activity!!)!!.edit().putString("showALiveYes", "yes").apply()
                       if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE,"") != "3"){
                           showCase()
                        }

                    }

                }
                hideKeyboard(mTitle)
            }
        })

        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                hideKeyboard(mTitle)
                if (position == 0) {
                    mTitle.text = "Business Profile"

                }
                if (position == 1) {
                    mTitle.text = "Add new posts"
                    Constants.getBus().post(EmptyFields("empty"))

                }
                if (position == 2) {
                    mTitle.text = "Scheduled posts"

                }
                if (position == 3) {
                    mTitle.text = "My posts"
                }
            }
        })
    }
    private fun showCase() {
        TutoShowcase.from(activity!!)
                .setContentView(R.layout.tutorialtwo)
//                .on(R.id.tapToExitLay)
//                .onClickContentView(R.id.tapToExitLay, View.OnClickListener {
//
//                })
                .showOnce("yes")
                .show()
    }
    private fun hideKeyboard(activity: View) {

        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.windowToken, 0)
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
                        tabViewChild.typeface = Typeface.createFromAsset(activity!!.getAssets(), "fonts/bold.ttf")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Subscribe
    fun getEventValue(event: PositionChangEvent) {
        val setto=event.value.ftype
        Constants.getPrefs(activity!!)!!.edit().putString("ftype",setto).apply()
        viewPager.currentItem = 3
    }

    @Subscribe
    fun getEventValue(event: EditScheduleEvent) {
        viewPager.currentItem = 1
        mTitle.text="Edit Post"
        Constants.getBus().post(SetwhileEditScheduleEvent(event.value))
    }

    @Subscribe
    fun getEventValueSchedule(event: ScheduleChangEvent) {
        viewPager.currentItem = 2
        Constants.getBus().post(SchedulePostEvent("hitservice"))
    }

    override fun onResume() {
        super.onResume()
        Constants.getBus().register(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        try {
            Constants.getBus().unregister(this)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}