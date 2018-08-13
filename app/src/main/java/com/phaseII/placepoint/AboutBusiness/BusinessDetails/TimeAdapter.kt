package com.phaseII.placepoint.AboutBusiness.BusinessDetails

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.time_check.view.*

class TimeAdapter(var context: Context, var list: ArrayList<ResultModel>) : RecyclerView.Adapter<TimeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.time_check, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var model = list[position]
        holder.itemView.day.text = model.day
        holder.itemView.time.text = model.time
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}