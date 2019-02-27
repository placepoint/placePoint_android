package com.phaseII.placepoint.SubscriptionPlan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.plan_item.view.*

class PlanFeaturesAdapter(var context: Context, var list: ArrayList<String>) : RecyclerView.Adapter<PlanFeaturesAdapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(LayoutInflater.from(context).inflate(R.layout.plan_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.itemView.featureText.text = list[position]
    }


    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView)
}