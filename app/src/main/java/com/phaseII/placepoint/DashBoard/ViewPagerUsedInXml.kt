package com.phaseII.placepoint.DashBoard


import android.annotation.SuppressLint
import android.content.Context


import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


class ViewPagerUsedInXml(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //---Add this line to make viewPager swipe-------
        //return super.onInterceptTouchEvent(ev)

        //---Adding this line viewPager stop swiping-----
       return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        //---Add this line to make viewPager swipe-------
        //return super.onTouchEvent(ev)

        //---Adding this line viewPager stop swiping-----
        return false
    }

}