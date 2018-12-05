package com.phaseII.placepoint.Business


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

import android.support.v4.app.FragmentStatePagerAdapter
import com.phaseII.placepoint.Business.AddPost.AddPostFragment
import com.phaseII.placepoint.Business.MyPosts.BothPostAndFlashPostFragment
import com.phaseII.placepoint.Business.Profile.ProfileFragment
import com.phaseII.placepoint.Business.ScheduledPost.ScheduledFragment

class ViewPagerAdapterBusiness(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return ProfileFragment()
            }
            1 -> return AddPostFragment()
            2 -> return ScheduledFragment()
            3 -> return BothPostAndFlashPostFragment()
        }
        return null
    }

    override fun getCount(): Int {
        return 4
    }


}