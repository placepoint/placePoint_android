package com.phaseII.placepoint.AboutBusiness.MyLiveFeed


import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.BusEvents.ClaimPostLiveFeed
import com.phaseII.placepoint.BusEvents.LiveFeedTaxiEvent
import com.phaseII.placepoint.BusEvents.LiveListingBackEvent
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.LiveFeeds.LiveFeedAdapter
import com.phaseII.placepoint.Home.LiveFeeds.LiveFeedHelper
import com.phaseII.placepoint.Home.LiveFeeds.LiveFeedPresenter
import com.phaseII.placepoint.Home.ModelHome
import com.phaseII.placepoint.R
import com.squareup.otto.Subscribe


class MyLiveFeedFragment : Fragment(), LiveFeedHelper, MyLiveFeedAdapter.ShowViewMoreLess {
    private lateinit var mPresenter: LiveFeedPresenter
    lateinit var recyclerView: RecyclerView
    lateinit var noPosts: TextView
    lateinit var pullToRefresh: SwipeRefreshLayout
    lateinit var progressBar: ProgressBar
    lateinit var refreshLay: RelativeLayout
    var c = 0
    var list= ArrayList<ModelHome>()
    lateinit var adapter: MyLiveFeedAdapter

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        mPresenter = LiveFeedPresenter(this)
        Constants.getSSlCertificate(activity!!)
        noPosts = v.findViewById(R.id.noPosts)
        recyclerView = v.findViewById(R.id.recyclerView)
        progressBar = v.findViewById(R.id.progressBar)
        pullToRefresh = v.findViewById(R.id.pullToRefresh)
        refreshLay = v.findViewById(R.id.refreshLay)
        recyclerView.stopNestedScroll()
        recyclerView.setHasFixedSize(true)
        val from = Constants.getPrefs(activity!!)!!.getString(Constants.FROMINTENT, "")
      //  mPresenter.prepareData(from,"false")
        mPresenter.BusinessDetailService(Constants.getPrefs(activity!!)!!.getString(Constants.BUSINESS_ID, ""))
        pullToRefresh.setOnRefreshListener {
            mPresenter.BusinessDetailService(Constants.getPrefs(activity!!)!!.getString(Constants.BUSINESS_ID, ""))

           // mPresenter.prepareData(from, "false")
            pullToRefresh.isRefreshing = false
        }

        return v
    }

    override fun setDataToAdapter(data: String, category: String) {
        try {
            Constants.getPrefs(activity!!)!!.edit().putString(Constants.CATEGORY_LIST, category).apply()
            val list2 = Constants.getCategoryData(category)
            for (i in 0 until list2!!.size) {
                if (list2[i].name == "Taxi" && list2[i].parent_category == "0") {
                    Constants.getPrefs(activity!!)!!.edit().putString(Constants.TAXI_TOWNID, list2[i].town_id).apply()
                }
                if (list2[i].name == "Taxi" && list2[i].parent_category != "0") {
                    Constants.getPrefs(activity!!)!!.edit().putString(Constants.TAXI_SUB_ID, list2[i].id).apply()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!data.isEmpty()) {
            list = Constants.getHomeFeedData(data)
            val listTemp = arrayListOf<ModelHome>()
            if (list != null && list.size > 0) {

                var businessID = "0"
                try {
                    val showAllPosts = Constants.getPrefs(activity!!)!!.getString(Constants.SHOW_ALL_POST, "")
                    if (showAllPosts == "no") {
                        businessID = Constants.getPrefs(activity!!)!!.getString(Constants.BUSINESS_ID, "")
                        for (i in 0 until list.size) {
                            if (list[i].bussness_id == businessID) {
                                listTemp.add(list[i])
                            }
                        }
                        list = listTemp

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (list != null && list.size > 0) {
                    try {
                        noPosts.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.layoutManager = LinearLayoutManager(activity!!)
                        adapter = MyLiveFeedAdapter(activity!!, list,this)
                        recyclerView.adapter=adapter
                        Constants.getPrefs(activity!!)!!.edit().putString("showTLive","yes").apply()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Constants.getPrefs(activity!!)!!.edit().putString("showTLive","no").apply()

                    recyclerView
                    noPosts.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            } else {
                Constants.getPrefs(activity!!)!!.edit().putString("showTLive","no").apply()
                recyclerView.visibility = View.GONE
                noPosts.visibility = View.VISIBLE
            }
        } else {
            try {
                recyclerView.visibility = View.GONE
                noPosts.visibility = View.VISIBLE
                Constants.getPrefs(activity!!)!!.edit().putString("showTLive", "no").apply()
            }catch (e:Exception){
                e.printStackTrace()
            }
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
            if (Constants.getPrefs(activity!!)?.getString(Constants.FROMINTENT, "") == "profile") {
                return Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID2, "")!!
            }

            return Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID, "")!!
        }
        return ""
    }


    override fun getCatId(): String {
        if (activity != null && isAdded) {
            if (Constants.getPrefs(activity!!)?.getString(Constants.FROMINTENT, "") == "profile") {
                val id = Constants.getPrefs(activity!!)?.getString(Constants.MAIN_CATEGORY, "")!!
                val result: List<String> = id.split(",").map { it.trim() }
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
        }
    }

    override fun saveBusId(business_id: String?) {

    }

    override fun getTaxiID(): String {
        return Constants.getPrefs(activity!!)!!.getString(Constants.TAXI_SUB_ID, "")
    }

    override fun onResume() {
        super.onResume()

        Constants.getBus().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Constants.getBus().unregister(this)
    }

    @Subscribe
    fun getEventValue(event: LiveFeedTaxiEvent) {

        mPresenter.prepareData("Taxi", "false")

    }
    @Subscribe
    fun getEventValue(event: ClaimPostLiveFeed) {

        val auth_code = Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE, "")
        mPresenter.claimPostCall(auth_code, event.model.postId, event.model.name, event.model.phoneNo, event.model.email, event.model.position,event.model.perPerson)
    }
    @Subscribe
    fun getEventValue(event: LiveListingBackEvent) {

        mPresenter.prepareData("home", "false")

    }

    override fun updateModelData(position: String, claimed: String) {

        try {
            val rr = list[position.toInt()]
            val countRe = rr.redeemed.toInt() + 1
            rr.redeemed = countRe.toString()
            rr.personRedeem = claimed
            list.set(position.toInt(), rr)
            adapter.notifyDataSetChanged()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun showToast(optString: String?) {
        Toast.makeText(activity, optString, Toast.LENGTH_LONG).show()
    }

    override fun showMoreLess(postion: Int) {
        recyclerView.scrollToPosition(postion)
    }

    override fun getBusId(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.BUSINESS_ID, "")!!

    }

    override fun getIfLoggedIn(): String {
        return Constants.getPrefs(activity!!)!!.getString(Constants.LOGGED_IN, "false")
    }

    override fun setBusinessData(data2: String, post: String) {

        if (!post.isEmpty()) {
            list = Constants.getHomeFeedData(post)
            val listTemp = arrayListOf<ModelHome>()
            if (list != null && list.size > 0) {

                var businessID = "0"
                try {
                    val showAllPosts = Constants.getPrefs(activity!!)!!.getString(Constants.SHOW_ALL_POST, "")
                    if (showAllPosts == "no") {
                        businessID = Constants.getPrefs(activity!!)!!.getString(Constants.BUSINESS_ID, "")
                        for (i in 0 until list.size) {
                            if (list[i].bussness_id == businessID) {
                                listTemp.add(list[i])
                            }
                        }
                        list = listTemp

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (list != null && list.size > 0) {
                    val listTemp2 = arrayListOf<ModelHome>()
                    for (i in 0 until list.size) {
                        if (list[i].ftype == "0") {
                            listTemp2.add(list[i])
                        }
                    }
                    list=listTemp2
                    try {
                        noPosts.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.layoutManager = LinearLayoutManager(activity!!)
                        adapter = MyLiveFeedAdapter(activity!!, list,this)
                        recyclerView.adapter=adapter
                        Constants.getPrefs(activity!!)!!.edit().putString("showTLive","yes").apply()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Constants.getPrefs(activity!!)!!.edit().putString("showTLive","no").apply()

                    recyclerView
                    noPosts.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            } else {
                Constants.getPrefs(activity!!)!!.edit().putString("showTLive","no").apply()
                recyclerView.visibility = View.GONE
                noPosts.visibility = View.VISIBLE
            }
        } else {
            try {
                recyclerView.visibility = View.GONE
                noPosts.visibility = View.VISIBLE
                Constants.getPrefs(activity!!)!!.edit().putString("showTLive", "no").apply()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

    }
}
