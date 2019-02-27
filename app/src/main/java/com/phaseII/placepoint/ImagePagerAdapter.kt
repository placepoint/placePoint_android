package com.phaseII.placepoint

import android.content.Context

import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.support.constraint.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions



class ImagePagerAdapter(var fm: FragmentManager?, var list: List<String>, val context: FragmentActivity?) : PagerAdapter() {
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
                            .centerCrop()
                            .placeholder(R.mipmap.placeholder))
                    .into(imageView)
        }

        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // super.destroyItem(container, position, `object`)
        (container as ViewPager).removeView(`object` as View)
    }

}
