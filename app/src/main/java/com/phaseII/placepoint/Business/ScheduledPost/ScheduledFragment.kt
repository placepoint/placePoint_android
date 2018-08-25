package com.phaseII.placepoint.Business.ScheduledPost


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.phaseII.placepoint.BusEvents.DeleteScheduleEvent
import com.phaseII.placepoint.Constants

import com.phaseII.placepoint.R
import com.phaseII.placepoint.BusEvents.SchedulePostEvent
import com.phaseII.placepoint.SubscriptionPlan.SubscriptionActivity
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_scheduled.*


class ScheduledFragment : Fragment(), ScheduleContract {
    var postion: Int = -1
    lateinit var list2: ArrayList<ModelSchdule>
    lateinit var scheduleList: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var upgrade: Button
    lateinit var adapter: ScheduleAdapter
    var list: ArrayList<ModelSchdule> = arrayListOf()
    private lateinit var mPresenter: SchedulePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_scheduled, container, false)
        scheduleList = view.findViewById(R.id.scheduleList)
        progressBar = view.findViewById(R.id.progressBar)
        upgrade = view.findViewById(R.id.upgrade)

        upgrade.setOnClickListener {
            val intent = Intent(context, SubscriptionActivity::class.java)
            activity!!.startActivity(intent)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        Constants.getSSlCertificate(activity!!)
        Constants.getBus().register(this)
        mPresenter = SchedulePresenter(this)

        if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3"
                || Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "2") {
            noSubLay.visibility = View.VISIBLE
            mainLay.visibility = View.GONE

        } else {
            mPresenter.getSchedulePosts(Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE, ""))
            noSubLay.visibility = View.GONE
            mainLay.visibility = View.VISIBLE
        }
    }

    @Subscribe
    fun getEventValue(event: SchedulePostEvent) {
        mPresenter.getSchedulePosts(Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE, ""))
    }

    override fun onPause() {
        super.onPause()
        Constants.getBus().unregister(this)
    }

    override fun showLoader() {

        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun showNetworkError(resId: Int) {
        Toast.makeText(activity, getString(resId), Toast.LENGTH_LONG).show()
    }

    override fun getAuthCode(): String {
        return Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE, "")
    }

    override fun NoData() {
        try {
            noData.visibility = View.VISIBLE
        }catch (e:Exception){
         e.printStackTrace()
        }

    }

    override fun refreshAdapter(pos: Int) {
        //  mPresenter.getSchedulePosts(Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE, ""))

        list2.removeAt(pos)
        if (list2.size > 0) {
            noData.visibility = View.GONE
        } else {
            noData.visibility = View.VISIBLE
        }
        adapter.notifyDataSetChanged()
    }


    override fun setAdapter(data: String) {
        list2 = Constants.getScheduleList(data)!!
        if (list2 != null && list2.size > 0) {
            try {
                if (activity != null) {
                    list2.reverse()
                    noData.visibility = View.GONE
                    adapter = ScheduleAdapter(activity!!, list2!!)
                    val linear = LinearLayoutManager(activity)
                    scheduleList.layoutManager = linear
                    scheduleList.adapter = adapter
                }
            } catch (e: Exception) {

            }
        } else {
            noData.visibility = View.VISIBLE
        }

    }

    @Subscribe
    fun getEventValue(event: DeleteScheduleEvent) {

        mPresenter.deleteSchedule(event.value.id, event.position)

    }
}
