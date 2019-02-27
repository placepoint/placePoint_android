package com.phaseII.placepoint.AboutBusiness

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.bus_horz_recycler_item.view.*
import java.util.ArrayList


class BusinessHorzRecyclerAdapter(val context: Context, val list: ArrayList<String>) :
        RecyclerView.Adapter<BusinessHorzRecyclerAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder(inflater.inflate(R.layout.bus_horz_recycler_item, parent, false))
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (context != null) {
            try {
                Glide.with(context)
                        .load(list[position])
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher))
                        .into(holder.itemView.mImage)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        holder.itemView.setOnClickListener {
            val intent=Intent(context,BusinessViewPager::class.java)
            val args = Bundle()
            args.putStringArrayList("list", list )
            intent.putExtra("BUN", args)
            intent.putExtra("Position", position)
            context.startActivity(intent)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
