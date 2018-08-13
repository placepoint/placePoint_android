package com.phaseII.placepoint.DashBoard

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class DashBoardViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private  var mFragmentList: ArrayList<Fragment> = arrayListOf()
    // private lateinit var mFragmentTitleList: ArrayList<String>
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        //mFragmentTitleList.add(title)
    }
}