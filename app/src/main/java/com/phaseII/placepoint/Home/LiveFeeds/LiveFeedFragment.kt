package com.phaseII.placepoint.Home.LiveFeeds


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.BusEvents.BusiniessListingBackEvent
import com.phaseII.placepoint.BusEvents.LiveFeedTaxiEvent
import com.phaseII.placepoint.BusEvents.LiveListingBackEvent
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.ModelHome
import com.phaseII.placepoint.R
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.business_listing_fragment.*
import kotlinx.android.synthetic.main.fragment_business_profile.*

class LiveFeedFragment : Fragment(), HomeHelper {
    private lateinit var mPresenter: HomePresenter
    lateinit var recyclerView: RecyclerView
    lateinit var noPosts: TextView
    lateinit var progressBar: ProgressBar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        mPresenter = HomePresenter(this)
        Constants.getSSlCertificate(activity!!)
        noPosts = v.findViewById(R.id.noPosts)
        recyclerView = v.findViewById(R.id.recyclerView)
        progressBar = v.findViewById(R.id.progressBar)
        recyclerView.stopNestedScroll()
        recyclerView.setHasFixedSize(true)
        val from= Constants.getPrefs(activity!!)!!.getString(Constants.FROMINTENT,"")
        mPresenter.PrepareData(from)
        return v
    }

    override fun setDataToAdapter(data: String, category: String) {
        try {
            Constants.getPrefs(activity!!)!!.edit().putString(Constants.CATEGORY_LIST, category).apply()
            var list2 = Constants.getCategoryData(category)

            //Getting those town which shows taxi facility in Business Detail
            for (i in 0 until list2!!.size) {
                if (list2[i].name == "Taxi" && list2[i].parent_category == "0") {
                    Constants.getPrefs(activity!!)!!.edit().putString(Constants.TAXI_TOWNID, list2[i].town_id).apply()
                }
                if (list2[i].name=="Taxi"&&list2[i].parent_category!="0"){
                    Constants.getPrefs(activity!!)!!.edit().putString(Constants.TAXI_SUB_ID,list2[i].id).apply()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

        var list = Constants.getHomeFeedData(data)
        val listTemp= arrayListOf<ModelHome>()
        if (list != null && list.size > 0) {

            var business_id = "0"
            try {
                val showAllPosts = Constants.getPrefs(activity!!)!!.getString(Constants.SHOW_ALL_POST, "")
                if (showAllPosts == "no") {
                    business_id = Constants.getPrefs(activity!!)!!.getString(Constants.BUSINESS_ID, "")
                for(i in 0 until list.size){
                    if (list[i].bussness_id==business_id){
                        listTemp.add(list[i])
                    }
                }
                    list=listTemp

                }
            }catch (e:Exception){
                e.printStackTrace()
            }
            if (list != null && list.size > 0) {
                try {
                    noPosts.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.layoutManager = LinearLayoutManager(activity!!)
                    recyclerView.adapter = HomeAdapter(activity!!, list!!)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }else{
                recyclerView
                noPosts.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        } else {
            recyclerView.visibility = View.GONE
            noPosts.visibility = View.VISIBLE
        }

    }

    override fun getAuthCode(): String {
        if (activity != null && isAdded) {
            return Constants.getPrefs(activity!!)?.getString(Constants.AUTH_CODE, "")!!
        }
        return ""
    }

    override fun getTownId(): String {
        if (activity != null && isAdded) {
            if (Constants.getPrefs(activity!!)?.getString(Constants.FROMINTENT, "")=="profile")
            {
                return Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID2, "")!!
            }

            return Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID, "")!!
        }
        return ""
    }

    override fun getCatId(): String {
        if (activity != null && isAdded) {
            if (Constants.getPrefs(activity!!)?.getString(Constants.FROMINTENT, "")=="profile")
            {
                val id = Constants.getPrefs(activity!!)?.getString(Constants.MAIN_CATEGORY, "")!!
                var result: List<String> = id.split(",").map { it.trim() }
                return if (result.size > 1) {
                    result[1]
                } else {
                    result[0]
                }

            }
            return Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_IDS, "")!!
        }
        return ""
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun showNetworkError(resId: Int) {
        if (activity != null && isAdded) {
            Toast.makeText(activity, getString(resId), Toast.LENGTH_SHORT).show()
        }
    }

    override fun showMsg(msg: String?) {
        if (activity != null && isAdded) {
            noPosts.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
          //  Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun saveBusId(business_id: String?) {
        if (activity != null && isAdded) {
            //Constants.getPrefs(activity!!)?.edit()?.putString(Constants.BUSINESS_ID, business_id)?.apply()
        }
    }

    override fun getTaxiID(): String {
        return Constants.getPrefs(activity!!)!!.getString(Constants.TAXI_SUB_ID,"")
    }

    override fun onResume() {
        super.onResume()

//        if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE,"")=="3"
//        ||Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE,"")=="2"){
//            noSubLay.visibility=View.VISIBLE
//            mainLay.visibility=View.GONE
//
//        }else{
//            noSubLay.visibility=View.GONE
//            mainLay.visibility=View.VISIBLE
//        }
        Constants.getBus().register(this)
    }

    override fun onPause() {
        super.onPause()
        Constants.getBus().unregister(this)
    }
    @Subscribe
    fun getEventValue(event: LiveFeedTaxiEvent) {

        mPresenter.PrepareData("Taxi")

    }
 @Subscribe
    fun getEventValue(event: LiveListingBackEvent) {

        mPresenter.PrepareData("home")

    }
}
