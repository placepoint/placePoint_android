package com.phaseII.placepoint

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

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


class MenuImagePagerAdapter(var fm: FragmentManager?, var list: List<String>, val context: FragmentActivity?) : PagerAdapter() {
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

            if (list[position].contains(".pdf")) {
                Glide.with(context).load(R.drawable.ic_pdf_large)
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.placeholder))
                        .into(imageView)

            } else {
                Glide.with(context).load(list[position])
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.placeholder))
                        .into(imageView)

            }
            itemView.setOnClickListener {
                if (list[position].contains(".pdf")) {
                    val target = Intent(Intent.ACTION_VIEW)
                    target.setDataAndType(Uri.parse(list[position]), "application/pdf")
                    target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

                    val intent = Intent.createChooser(target, "Open File")
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        // Instruct the user to install a PDF reader here, or something
                    }
                }
            }
        }

        container.addView(itemView)

        return itemView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // super.destroyItem(container, position, `object`)
        (container as ViewPager).removeView(`object` as View)
    }

}
