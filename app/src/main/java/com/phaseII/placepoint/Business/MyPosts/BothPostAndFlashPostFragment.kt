package com.phaseII.placepoint.Business.MyPosts

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.R.id.tabLayout




class BothPostAndFlashPostFragment: Fragment() {

    private lateinit var tabLayout: TabLayout

//lateinit var postClick:TextView
//lateinit var flashPostClick:TextView
lateinit var frameId: FrameLayout
//    var clicked:Int=0
//    var clicked2:Int=0
    var sdk = android.os.Build.VERSION.SDK_INT
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View=inflater.inflate(R.layout.flash_and_mypost, container, false)
        initial(view)
//        postClick=view.findViewById(R.id.postClick)
//        flashPostClick=view.findViewById(R.id.flashPostClick)
       frameId=view.findViewById(R.id.frameLayout)
       val ftype= Constants.getPrefs(activity!!)!!.getString("ftype","0")
        if (ftype=="0") {
            val manager = activity!!.supportFragmentManager
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frameLayout, MyPostsFragment(), "frag1")
            transaction.commit()
            val tab = tabLayout.getTabAt(0)
            tab!!.select()
        }else{
            val manager = activity!!.supportFragmentManager
            val transaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frameLayout, FlashPostFragment(), "frag1")
            transaction.commit()
            val tab = tabLayout.getTabAt(1)
            tab!!.select()
        }
        Constants.getPrefs(activity!!)!!.edit().putString("ftype","0").apply()
//        postClick.setOnClickListener{
//            if (clicked==0) {
//                clicked=1
//                clicked2=0
//                val manager = activity!!.supportFragmentManager
//                val transaction = fragmentManager!!.beginTransaction()
//                transaction.replace(R.id.frameLayout, MyPostsFragment(), "frag1")
//                transaction.commit()
//                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    postClick.setTextColor(activity!!.resources.getColor(R.color.white))
//                    flashPostClick.setTextColor(activity!!.resources.getColor(R.color.black))
//                    postClick.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.left_round_corner_blue))
//                    flashPostClick.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.right_corner_round_white))
//                } else {
//                    postClick.setTextColor(activity!!.resources.getColor(R.color.white))
//                    flashPostClick.setTextColor(activity!!.resources.getColor(R.color.black))
//                    postClick.background = ContextCompat.getDrawable(activity!!, R.drawable.left_round_corner_blue)
//                    flashPostClick.background = ContextCompat.getDrawable(activity!!, R.drawable.right_corner_round_white)
//                }
//            }

//        }
//        flashPostClick.setOnClickListener{
//            if (clicked2==0) {
//                clicked2 = 1
//                clicked = 0
//                val manager = activity!!.getSupportFragmentManager()
//                val transaction = fragmentManager!!.beginTransaction()
//                transaction.replace(R.id.frameLayout, FlashPostFragment(), "frag1")
//                transaction.commit()
//                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    flashPostClick.setTextColor(activity!!.resources.getColor(R.color.white))
//                    postClick.setTextColor(activity!!.resources.getColor(R.color.black))
//                    postClick.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.left_corner_round_white))
//                    flashPostClick.setBackgroundDrawable(ContextCompat.getDrawable(activity!!, R.drawable.right_corner_round_blue))
//                } else {
//                    flashPostClick.setTextColor(activity!!.resources.getColor(R.color.white))
//                    postClick.setTextColor(activity!!.resources.getColor(R.color.black))
//                    postClick.setBackground(ContextCompat.getDrawable(activity!!, R.drawable.left_corner_round_white))
//                    flashPostClick.setBackground(ContextCompat.getDrawable(activity!!, R.drawable.right_corner_round_blue))
//                }
//            }
//        }
        return view
    }

    private fun initial(view: View) {
        tabLayout = view.findViewById(R.id.tabs)
        setupViewPager()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position==0){
                    val manager = activity!!.supportFragmentManager
                val transaction = fragmentManager!!.beginTransaction()
                transaction.replace(R.id.frameLayout, MyPostsFragment(), "frag1")
                transaction.commit()
                }else{
                    val manager = activity!!.supportFragmentManager
                val transaction = fragmentManager!!.beginTransaction()
                transaction.replace(R.id.frameLayout, FlashPostFragment(), "frag1")
                transaction.commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun setupViewPager() {
        try {
            tabLayout.addTab(tabLayout.newTab().setText("Post"))
            tabLayout.addTab(tabLayout.newTab().setText("Flash post"))
            val mTypeface = Typeface.createFromAsset(getContext()!!.getAssets(), "fonts/bold.ttf")

            setCustomFont(tabLayout)


        } catch (e: Exception) {
            e.printStackTrace()
        }
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
}