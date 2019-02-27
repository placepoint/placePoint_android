package com.phaseII.placepoint.Business.MyPosts

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import android.support.v4.view.ViewPager
import android.support.constraint.ConstraintLayout
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.R


class MyTimelinePagerAdapter( var list: ArrayList<Int>, val context: Context?) : PagerAdapter() {
    private var layoutInflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.home_viewpager_layout, container, false)
        val imageView = itemView.findViewById(R.id.viewPagerItem) as ImageView

        if (context != null) {
            Glide.with(context).load(list[position])
                    .apply(RequestOptions()
                            .placeholder(R.mipmap.placeholder))
                    .into(imageView)
        }

        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

}
