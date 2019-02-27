package com.phaseII.placepoint.Business.Profile

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.horz_recycler_item.view.*


class HorzRecyclerAdapter(var context: Context, val list: ArrayList<Uri>,
                          val croppedImages: java.util.ArrayList<Uri>, var preLoadImages: MutableList<String>,
                          val type: String) :
        RecyclerView.Adapter<HorzRecyclerAdapter.ViewHolder>() {
    var mCallback: OnHeadlineSelectedListener? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        try {
            mCallback = context as OnHeadlineSelectedListener
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ViewHolder(inflater.inflate(R.layout.horz_recycler_item, parent, false))
    }

    interface OnHeadlineSelectedListener {
        fun preloadListUpdate(position: Int)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (type == "old") {
            if (context != null) {
                try {
                    Glide.with(context)
                            .load(preLoadImages[position])
                            .apply(RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.mipmap.ic_launcher))
                            .into(holder.itemView.mImage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        } else {
            if (context != null) {
                try {
                    Glide.with(context)
                            .load(list[position])
                            .apply(RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.mipmap.placeholder))
                            .into(holder.itemView.mImage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
        holder.itemView.mCancel.setOnClickListener {
            if (type == "old") {
                try {
                    preLoadImages.removeAt(position)
                    type == "old"
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, preLoadImages.size)
                    list.remove(list[position])
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                try {
                    croppedImages.remove(list[position])
                    type == "new"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                list.remove(list[position])
                notifyDataSetChanged()
            }


        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface UpdateImageList {
        fun updateHorizontalImageList(position: Int)
    }
}
