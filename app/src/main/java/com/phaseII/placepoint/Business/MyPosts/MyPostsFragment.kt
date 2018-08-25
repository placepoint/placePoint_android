package com.phaseII.placepoint.Business.MyPosts


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.AboutBusiness.SingleBusinessModel
import com.phaseII.placepoint.Constants

import com.phaseII.placepoint.R
import com.phaseII.placepoint.SubscriptionPlan.SubscriptionActivity
import com.phaseII.placepoint.Town.ModelTown
import kotlinx.android.synthetic.main.fragment_my_timeline.*

import java.util.ArrayList
import java.util.HashSet


class MyPostsFragment : Fragment(), MyTimelineHelper {

    private lateinit var mAdapter: MyTimelineAdapter
    lateinit var progressBar: ProgressBar
    lateinit var upgrade: Button
    private lateinit var mPresenter: MyTimelinePresenter

    private lateinit var recyclerView: RecyclerView
    private lateinit var noPosts: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_my_timeline, container, false)
        Constants.getSSlCertificate(activity!!)
        mPresenter = MyTimelinePresenter(this)
        upgrade = v.findViewById(R.id.upgrade)
        noPosts = v.findViewById(R.id.noPosts)
        recyclerView = v.findViewById(R.id.recyclerView)
        progressBar = v.findViewById(R.id.progressBar)
        recyclerView.stopNestedScroll()
        recyclerView.setHasFixedSize(true)
        mPresenter.PrepareData()
        setHasOptionsMenu(true)
        upgrade.setOnClickListener {
            val intent = Intent(context, SubscriptionActivity::class.java)
            activity!!.startActivity(intent)
        }
        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val optionBusiness = menu.findItem(R.id.openBusiness)
        val actionAdd = menu.findItem(R.id.action_add)
        optionBusiness.isVisible = false
        actionAdd.isVisible = true

    }

    override fun setDataToAdapter(data: String) {
        val list = Constants.getHomeFeedData(data)
        try {
            if (list != null && list.size > 0) {
                noPosts.visibility = View.GONE

                recyclerView.layoutManager = LinearLayoutManager(activity)
                mAdapter = MyTimelineAdapter(this.context!!, list!!)
                recyclerView.adapter = mAdapter
            } else {
                noPosts.visibility = View.VISIBLE
            }
        } catch (e: Exception) {

        }

    }

    override fun noPosts() {

        noPosts.visibility = View.VISIBLE
    }

    private val POSTCODE: Int = 111

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        if (item != null) {
//            if (item.itemId == R.id.action_add) {
//                val intent = Intent(activity!!, AddNewActivity::class.java)
//                startActivityForResult(intent, POSTCODE)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//
//
//    }

    override fun getAuthCode(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.AUTH_CODE, "")!!
    }

    override fun getTownId(): String {

        // ****************Setting selected Town by user**************************
        var model: SingleBusinessModel = SingleBusinessModel()
        var list: java.util.ArrayList<SingleBusinessModel>? = arrayListOf()
        val data = Constants.getPrefs(activity!!)?.getString(Constants.SINGLE_BUSINESS_LIST, "")!!
        list = Constants.getSingleBusinessData(data)
        val loc = Constants.getPrefs(activity!!)?.getString(Constants.LOCATION_LIST, "")!!
        var loc_list: ArrayList<ModelTown> = arrayListOf()
        loc_list = Constants.getTownData(loc)!!
        val hashSet2 = HashSet<ModelTown>()
        hashSet2.addAll(loc_list)
        loc_list.clear()
        loc_list.addAll(hashSet2)
//        if (list != null) {
//            for(i in 0 until list.size){
//                model=list[i]
//                val townid=model.town_id
//                for (i2 in 0 until loc_list.size) {
//                    if (loc_list[i2].id == townid) {
//                       // towns.text = loc_list[i2].townname
//                        return loc_list[i2].id
//                    }
//                }
//
//            }
//            //val townName = Constants.getPrefs(this)!!.getString(Constants.TOWN_NAME, "")
//
//        }
        val townid = Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID2, "")
        for (i2 in 0 until loc_list.size) {
            if (loc_list[i2].id == townid) {
                return loc_list[i2].id
            }
        }
        return Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID2, "")!!
    }

    override fun getCatId(): String {
        val id = Constants.getPrefs(activity!!)?.getString(Constants.MAIN_CATEGORY, "")!!
        var result: List<String> = id.split(",").map { it.trim() }
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
                    mPresenter.PrepareData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    override fun saveCategories(catagories: String) {

        if (activity != null) {
            Constants.getPrefs(activity!!)?.edit()!!.remove(Constants.CATEGORY_LIST).apply()
            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.CATEGORY_LIST, catagories)?.apply()

        }
    }

    override fun saveLocaton(location: String) {
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
}
