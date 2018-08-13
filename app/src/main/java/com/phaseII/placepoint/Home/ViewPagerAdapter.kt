package com.phaseII.placepoint.Home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.phaseII.placepoint.Home.BusinessListing.BusinessListingFragment
import com.phaseII.placepoint.Home.LiveFeeds.LiveFeedFragment

class ViewPagerAdapter(fm: FragmentManager, var counter: Int) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return BusinessListingFragment()
            }
            1 -> return LiveFeedFragment()

        }
        return null
    }

    override fun getCount(): Int {
        return counter
    }



}