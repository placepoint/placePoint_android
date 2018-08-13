package com.phaseII.placepoint.Business


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
import com.phaseII.placepoint.*
import com.phaseII.placepoint.BusEvents.*
import com.squareup.otto.Subscribe


class BusinessFragment : Fragment() {


    private lateinit var tabLayout: TabLayout
    public lateinit var viewPager: ViewPager
    private lateinit var myContext: FragmentActivity
    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_business, container, false)

        init(view)
        setToolBar(view)

        return view
    }

    private fun setToolBar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title)

        mTitle.text = "Business Profile"


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

    private fun hideKeyboard(activity: View) {

        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.windowToken, 0)
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

    @Subscribe
    fun getEventValue(event: PositionChangEvent) {
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

    override fun onPause() {
        super.onPause()
        Constants.getBus().unregister(this)
    }
}