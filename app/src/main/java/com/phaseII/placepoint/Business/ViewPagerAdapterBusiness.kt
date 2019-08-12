package com.phaseII.placepoint.Business


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

import android.support.v4.app.FragmentStatePagerAdapter
import com.phaseII.placepoint.Business.AddPost.AddPostFragment
import com.phaseII.placepoint.Business.MyPosts.BothPostAndFlashPostFragment
import com.phaseII.placepoint.Business.Profile.ProfileFragment
import com.phaseII.placepoint.Business.ScheduledPost.ScheduledFragment
import android.util.SparseArray
import android.view.ViewGroup





class ViewPagerAdapterBusiness(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
lateinit var fragment:Fragment
    var registeredFragments = SparseArray<Fragment>()
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                fragment=ProfileFragment()
                return fragment
            }
            1 -> {
                fragment=AddPostFragment()
                return fragment
            }
            2 ->{
                fragment=ScheduledFragment()
                return fragment
            }
            3 ->{
            fragment=BothPostAndFlashPostFragment()
            return fragment
        }
        }
        return null
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment {
        return registeredFragments.get(position)
    }
    override fun getCount(): Int {
        return 4
    }


}