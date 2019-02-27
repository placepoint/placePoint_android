package com.phaseII.placepoint.Business.MyPosts


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.ModelHome
import com.phaseII.placepoint.R
import com.phaseII.placepoint.SubscriptionPlan.SubscriptionActivity
import com.phaseII.placepoint.Town.ModelTown
import java.util.ArrayList
import java.util.HashSet











class MyPostsFragment : Fragment(), MyTimelineHelper, MyTimelineAdapter.BumpPost {

    private lateinit var mAdapter: MyTimelineAdapter
    lateinit var progressBar: ProgressBar
    lateinit var upgrade: Button
    private lateinit var mPresenter: MyTimelinePresenter
    private lateinit var noSubLay: ConstraintLayout

    private lateinit var mainLay: ConstraintLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var noPosts: TextView
    private val POSTCODE: Int = 111
    var firstPos=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            val v = inflater.inflate(R.layout.fragment_my_timeline, container, false)
            Constants.getSSlCertificate(activity!!)
            mPresenter = MyTimelinePresenter(this)
            mainLay = v.findViewById(R.id.mainLay)
            noSubLay = v.findViewById(R.id.noSubLay)
            upgrade = v.findViewById(R.id.upgrade)
            noPosts = v.findViewById(R.id.noPosts)
            recyclerView = v.findViewById(R.id.recyclerView)
            progressBar = v.findViewById(R.id.progressBar)
            recyclerView.stopNestedScroll()
            recyclerView.setHasFixedSize(true)
            mPresenter.prepareData()
            setHasOptionsMenu(true)
            upgrade.setOnClickListener {
                val intent = Intent(context, SubscriptionActivity::class.java)
                activity!!.startActivity(intent)
            }

            return v
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val optionBusiness = menu.findItem(R.id.openBusiness)
        val actionAdd = menu.findItem(R.id.action_add)
        optionBusiness.isVisible = false
        actionAdd.isVisible = true

    }

    override fun setDataToAdapter(data: String) {
        var list= ArrayList<ModelHome>()
        list = Constants.getHomeFeedData(data)
        var postList=ArrayList<ModelHome>()
        //postList=list
        if (list!!.size>0) {
            for (i in 0 until list.size){
                if (list[i].ftype=="0"){
                    postList!!.add(list[i])
                }
            }
                try {
                    if (postList != null && postList.size > 0) {
                        noPosts.visibility = View.GONE

                        recyclerView.layoutManager = LinearLayoutManager(activity)
                        mAdapter = MyTimelineAdapter(activity!!, postList)
                        recyclerView.adapter = mAdapter
                    } else {
                        noPosts.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {

                }
        }else{
            noPosts.visibility = View.VISIBLE
        }
    }

    override fun noPosts() {

        noPosts.visibility = View.VISIBLE
    }




    override fun getAuthCode(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.AUTH_CODE, "")!!
    }

    override fun getTownId(): String {

        // ****************Setting selected Town by user**************************
        val loc = Constants.getPrefs(activity!!)?.getString(Constants.LOCATION_LIST, "")!!
        val locList: ArrayList<ModelTown> = Constants.getTownData(loc)!!
        val hashSet2 = HashSet<ModelTown>()
        hashSet2.addAll(locList)
        locList.clear()
        locList.addAll(hashSet2)
        val townId = Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID2, "")
        for (i2 in 0 until locList.size) {
            if (locList[i2].id == townId) {
                return locList[i2].id
            }
        }
        return Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID2, "")!!
    }

    override fun getCatId(): String {
        val id = Constants.getPrefs(activity!!)?.getString(Constants.MAIN_CATEGORY, "")!!
        val result: List<String> = id.split(",").map { it.trim() }
        return if (result.size > 1) {
            result[1]
        } else {
            result[0]
        }
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun showNetworkError(resId: Int) {
        if (activity!=null) {
            try {
                Toast.makeText(activity, getString(resId), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == POSTCODE) {
                try {
                    mPresenter.prepareData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun saveCategories(categories: String) {

        if (activity != null) {
            Constants.getPrefs(activity!!)?.edit()!!.remove(Constants.CATEGORY_LIST).apply()
            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.CATEGORY_LIST, categories)?.apply()

        }
    }

    override fun saveLocation(location: String) {
        if (activity != null) {
            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.LOCATION_LIST, location)?.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE,"")=="3"){
            noSubLay.visibility=View.VISIBLE
            mainLay.visibility=View.GONE

        }else{
            noSubLay.visibility=View.GONE
            mainLay.visibility=View.VISIBLE
        }
    }

    override fun applyBumpPost(id: String) {
        mPresenter.apiForBump(id)
    }
}
