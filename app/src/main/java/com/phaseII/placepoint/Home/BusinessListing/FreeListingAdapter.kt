package com.phaseII.placepoint.Home.BusinessListing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.R

import kotlinx.android.synthetic.main.freelist_item.view.*
import java.util.ArrayList

class FreeListingAdapter(var context:Context, val list: ArrayList<ModelBusiness>): RecyclerView.Adapter<FreeListingAdapter.ViewHolder>() {
    val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.freelist_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
holder.itemView.title.text=list[position].business_name
holder.itemView.address.text=list[position].address
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView)
}