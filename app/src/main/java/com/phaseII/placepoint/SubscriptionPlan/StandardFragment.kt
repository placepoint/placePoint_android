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

class StandardFragment : Fragment() {
    lateinit var recyclerView2: RecyclerView
    lateinit var adapter: PlanFeaturesAdapter
    lateinit var progressBar: ProgressBar
    lateinit var upgradeButton: Button

    var list: ArrayList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View=inflater!!.inflate(R.layout.standard,container,false)
        recyclerView2=view.findViewById(R.id.recyclerView2)
        progressBar=view.findViewById(R.id.progressBar4)
        upgradeButton=view.findViewById(R.id.upgradeButton)
        list.add("Features:")
        list.add(" - Limited to 1 flash deal per month")
        list.add(" - No Business of the week promo")
        list.add(" - Not a featured business on homepage")
        list.add(" - Below Pro Businesses on Listings")


        adapter = PlanFeaturesAdapter(activity!!, list)
        val linear = LinearLayoutManager(activity!!)
        recyclerView2.layoutManager = linear
        recyclerView2.adapter = adapter

        upgradeButton.setOnClickListener{
            var authcode=Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE,"")
            var userType=Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE,"")
            Constants.getPackageDetails(authcode,userType,"2",progressBar,activity!!)
        }
        return  view
    }

    override fun onResume() {
        super.onResume()
        Constants.getBus().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}