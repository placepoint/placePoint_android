package com.phaseII.placepoint.AboutBusiness

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.phaseII.placepoint.AboutBusiness.BusinessDetails.DetailFragment
import com.phaseII.placepoint.Home.BusinessListing.BusinessListingFragment
import com.phaseII.placepoint.Home.LiveFeeds.LiveFeedFragment

class DetailViewPagerAdapter (fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return DetailFragment()
            }
            1 -> return LiveFeedFragment()

        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }



}