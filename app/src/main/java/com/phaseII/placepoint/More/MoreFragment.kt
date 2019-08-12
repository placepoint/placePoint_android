package com.phaseII.placepoint.More


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.BusEvents.LogoutEvent
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.DashBoard.DashBoardActivity

import com.phaseII.placepoint.R
import com.phaseII.placepoint.Service
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_more.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException


class MoreFragment : Fragment() {

    lateinit var list: RecyclerView
    lateinit var toolbar: Toolbar
    private lateinit var mTitle: TextView
    private lateinit var progressBar: ProgressBar
    //  lateinit var goToHome:HomeTab
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_more, container, false)
        Constants.getSSlCertificate(activity!!)
        init(view)
        setAdApter()
        setToolBar(view)
        return view
    }

    private fun setToolBar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        mTitle.text = "More"

    }

    private fun setAdApter() {
        val items: ArrayList<String> = ArrayList()
        if (Constants.getPrefs(activity!!)!!.getBoolean(Constants.LOGGED, false)) {
            items.add("Choose Town")
            items.add("Reset Password")
            items.add("Terms and conditions")
            items.add("Privacy Policy")
            items.add("Business")
            items.add("Chat Support")
            items.add("Manage Notifications")
            items.add("Logout")
        } else {
            items.add("Choose Town")
            items.add("Terms and conditions")
            items.add("Privacy Policy")
            items.add("Business")
            items.add("Chat Support")
            items.add("Manage Notifications")
        }

        var adapter = MoreAdapter(this.activity!!, items)
        val linear = LinearLayoutManager(activity)
        list.layoutManager = linear
        list.adapter = adapter
    }

    private fun init(view: View) {
        list = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)

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

    @Subscribe
    fun getMessage(event: LogoutEvent) {
        progressBar.visibility = View.VISIBLE
        var retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .build()
        val service = retrofit.create(Service::class.java)
        val call: Call<ResponseBody> = service.logoutApp(event.value)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        // if (status.equals("true", ignoreCase = true)) {
                        val intent = Intent(context, DashBoardActivity::class.java)
//                        intent.putExtra("from", "false")
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        (context as Activity).finish()

                        // }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Unable to Logout.", Toast.LENGTH_LONG).show()
            }
        })
    }
//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        goToHome= context as HomeTab
//    }

//    override fun onAttach(activity: Activity?) {
//        super.onAttach(activity)
//        goToHome= activity as HomeTab
//    }
//    override fun comingToHome() {
//        goToHome.showHome()
//    }
//    interface HomeTab{
//        fun showHome()
//    }
}
