package com.phaseII.placepoint.SubscriptionPlan

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.R

class FreeFragment : Fragment() {
    lateinit var recyclerView2: RecyclerView
    lateinit var adapter: PlanFeaturesAdapter
    var list: ArrayList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =inflater!!.inflate(R.layout.free,container,false)
        recyclerView2=view.findViewById(R.id.recyclerView2)
        list.add(".  Listing limited to name,\n\t address and phone")
        list.add(".  No business profile page")
        list.add(".  1 Category max allowed")
        list.add(".  No live feed.")
        list.add(".  No scheduled posts allowed.")
        list.add(".  Positioned at bottom of listing.")
        adapter = PlanFeaturesAdapter(activity!!, list)
        val linear = LinearLayoutManager(activity!!)
        recyclerView2.layoutManager = linear
        recyclerView2.adapter = adapter
        return  view
    }
}