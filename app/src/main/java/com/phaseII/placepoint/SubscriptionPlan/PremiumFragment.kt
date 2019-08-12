package com.phaseII.placepoint.SubscriptionPlan


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R

class PremiumFragment: Fragment() {
    lateinit var recyclerView2: RecyclerView
    lateinit var upgradeButton: Button
    lateinit var progressBar: ProgressBar
    lateinit var adapter: PlanFeaturesAdapter
    var list: ArrayList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =inflater!!.inflate(R.layout.premium,container,false)
        upgradeButton=view.findViewById(R.id.upgradeButton)
        progressBar=view.findViewById(R.id.progressBar3)
        recyclerView2=view.findViewById(R.id.recyclerView2)
//        list.add(".  Allows you to add all business\n\t details including opening hours.")
//        list.add(".  Business Profile Page allowed.")
//        list.add(".  Unlimited Categories.")
//        list.add(".  Live Feed Allowed.")
//        list.add(".  Scheduled posts allowed.")
//        list.add(".  Positioned at top of listing.")
//        - Create Posters (Web Only)
//
//
//
list.add("Features:")
list.add(" - Unlimited Flash Deals")
list.add(" - Unlimited Competitions (coming soon)")
list.add(" - Unlimited Categories")
list.add(" - Create Poster (Web only)")
list.add(" - Feature Listing on homepage")
list.add(" - Prominent placement in category view")
list.add(" - Featured Business of the week promotion")
list.add(" - Unlimited Relevant Categories")
                adapter = PlanFeaturesAdapter(activity!!, list)
        val linear = LinearLayoutManager(activity!!)
        recyclerView2.layoutManager = linear
        recyclerView2.adapter = adapter
        upgradeButton.setOnClickListener{
            var authcode=Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE,"")
            var userType=Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE,"")
            Constants.getPackageDetails(authcode,userType,"1",progressBar,activity!!)
        }
        return  view
    }
}