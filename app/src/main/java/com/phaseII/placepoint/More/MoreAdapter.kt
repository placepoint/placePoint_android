package com.phaseII.placepoint.More

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Town.TownActivity
import kotlinx.android.synthetic.main.more_item.view.*
import android.app.Activity
import android.content.SharedPreferences
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.BusEvents.ClaimPostLiveFeed
import com.phaseII.placepoint.BusEvents.LogoutEvent
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.Home.ModelClainService
import com.phaseII.placepoint.PrivacyPolicy.PrivacyPolicyActivity
import com.phaseII.placepoint.Service
import com.phaseII.placepoint.Town.MultipleTown.MultipleTownActivity
import com.phaseII.placepoint.UpdatePassword.UpdatePasswordActivity
import com.phaseII.placepoint.sdk.JivoActivity
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException


class MoreAdapter(var context: Context, var items: ArrayList<String>) : RecyclerView.Adapter<MoreAdapter.ViewHolder>() {
    val businessFragment = context as OpenBusinessFragment
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.more_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.item_name.text = items[position]
        holder.itemView.setOnClickListener {
            if (Constants.getPrefs(context!!)!!.getBoolean(Constants.LOGGED, false)) {
                if (position == 0) {
                    Constants.getPrefs(context)!!.edit().putString("showHomeBackButton", "yes").apply()
                    Constants.getPrefs(context)!!.edit().putString("comingFrom", "more").apply()
                    val intent = Intent(context, TownActivity::class.java)
                    intent.putExtra("from", "true")
                    context.startActivity(intent)
                }
                if (position == 1) {
                    val intent = Intent(context, UpdatePasswordActivity::class.java)
                    context.startActivity(intent)
                }
                if (position == 2) {

                }
                if (position == 3) {
                    val intent = Intent(context, PrivacyPolicyActivity::class.java)
                    context.startActivity(intent)
                }
                if (position == 4) {

                    Constants.getPrefs(context)?.edit()?.putString("showbb", "yes")?.apply()
                    businessFragment.openBusinessFragment()
                }
                if (position == 5) {
                    val intent = Intent(context, JivoActivity::class.java)
                    context.startActivity(intent)

                }
                if (position == 6) {
                    Constants.getPrefs(context)?.edit()?.putString("changeMultiTown", "Town")?.apply()

                    val intent = Intent(context, MultipleTownActivity::class.java)
                    context.startActivity(intent)

                }
                if (position == 7) {

                    val cat = Constants.getPrefs(context)!!.getString(Constants.CATEGORY_IDS, "")
                    val townId = Constants.getPrefs(context)!!.getString(Constants.TOWN_ID, "")
                    val townName = Constants.getPrefs(context)!!.getString(Constants.TOWN_NAME, "")
//             val townOpen=Constants.getPrefs(context)!!.getString(Constants.OPEN_TOWN,"")
                    val townlist = Constants.getPrefs(context)!!.getString(Constants.LOCATION_LIST, "")
                    val authcode = Constants.getPrefs(context)!!.getString(Constants.AUTH_CODE, "")
                    val getauth = Constants.getPrefs(context)?.getString("getAuthCode", "no")!!
                    val catList = Constants.getPrefs(context)?.getString(Constants.CATEGORY_LIST, "")!!
                    val taxitownid = Constants.getPrefs(context)?.getString(Constants.TAXI_TOWNID, "")!!
                    val taxiSubid = Constants.getPrefs(context)?.getString(Constants.TAXI_SUB_ID, "")!!
//
                    Constants.getPrefs(context)?.edit()?.putString("firstTime", "Town")?.apply()

                    Constants.getPrefs(context)!!.edit().clear().apply()
                    Constants.getPrefs(context)?.edit()?.putString(Constants.OPEN_TOWN, "no")?.apply()
                    Constants.getPrefs(context)?.edit()?.putString(Constants.TOWN_ID, townId)?.apply()
                    Constants.getPrefs(context)?.edit()?.putString(Constants.TOWN_NAME, townName)?.apply()
                    Constants.getPrefs(context)?.edit()?.putString(Constants.AUTH_CODE, authcode)?.apply()
                    Constants.getPrefs(context)?.edit()?.putString("getAuthCode", getauth)?.apply()
                    Constants.getPrefs(context)?.edit()?.putString("firstTime", "Town")?.apply()
                    //Constants.getPrefs(context)?.edit()?.putString(Constants.CATEGORY_IDS, cat)?.apply()
                    Constants.getPrefs(context)?.edit()?.putString(Constants.LOCATION_LIST, townlist)?.apply()
                    Constants.getPrefs(context)?.edit()?.putString(Constants.CATEGORY_LIST, catList)?.apply()
                    Constants.getPrefs(context)?.edit()?.putString(Constants.TAXI_TOWNID, taxitownid)?.apply()
                    Constants.getPrefs(context)?.edit()?.putString(Constants.TAXI_SUB_ID, taxiSubid)?.apply()
                    Constants.getPrefs(context!!)!!.edit().putString("showHLiveYes", "yes").apply()
                    Constants.getPrefs(context!!)!!.edit().putString("comingFrom", "").apply()

                    Constants.getBus().post(LogoutEvent(getauth))
                    //logoutApi(getauth)

                }
            } else {
                if (position == 0) {
                    Constants.getPrefs(context)!!.edit().putString("showHomeBackButton", "yes").apply()
                    Constants.getPrefs(context)!!.edit().putString("comingFrom", "more").apply()
                    val intent = Intent(context, TownActivity::class.java)
                    intent.putExtra("from", "true")
                    context.startActivity(intent)

                }
                if (position == 1) {

                }
                if (position == 2) {
                    val intent = Intent(context, PrivacyPolicyActivity::class.java)
                    context.startActivity(intent)
                }
                if (position == 3) {
                    Constants.getPrefs(context)?.edit()?.putString("showbb", "yes")?.apply()
                    businessFragment.openBusinessFragment()
                }
                if (position == 4) {

                    val intent = Intent(context, JivoActivity::class.java)
                    context.startActivity(intent)
                }
                if (position == 5) {
                    Constants.getPrefs(context)?.edit()?.putString("changeMultiTown", "Town")?.apply()
                    val intent = Intent(context, MultipleTownActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }


    private fun logoutApi(auth_code: String) {

        var retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .build()
        val service = retrofit.create(Service::class.java)
        val call: Call<ResponseBody> = service.logoutApp(auth_code)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        // if (status.equals("true", ignoreCase = true)) {
                        val intent = Intent(context, DashBoardActivity::class.java)
                        intent.putExtra("from", "false")
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        (context as Activity).finish()

                        // }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Unable to Logout.", Toast.LENGTH_LONG).show()
            }
        })
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface OpenBusinessFragment {
        fun openBusinessFragment()
    }
}

