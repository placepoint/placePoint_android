package com.phaseII.placepoint.Home

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent






class ViewPagerHomeInXml(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
        // return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return super.onTouchEvent(ev)
        //return false
    }


}