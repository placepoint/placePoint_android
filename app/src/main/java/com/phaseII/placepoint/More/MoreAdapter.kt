package com.phaseII.placepoint.More

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Town.TownActivity
import kotlinx.android.synthetic.main.more_item.view.*
import android.app.Activity
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.PrivacyPolicy.PrivacyPolicyActivity


class MoreAdapter(var context: Context, var items: ArrayList<String>) : RecyclerView.Adapter<MoreAdapter.ViewHolder>() {
    //  val home=context as SwitchToHome
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
            if (position == 0) {
                val intent = Intent(context, PrivacyPolicyActivity::class.java)
                context.startActivity(intent)
            }
            if (position == 1) {

            }
            if (position == 2) {

                val intent = Intent(context, TownActivity::class.java)
                intent.putExtra("from", "true")
                context.startActivity(intent)
            }
            if (position == 3) {
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


                val intent = Intent(context, DashBoardActivity::class.java)
                intent.putExtra("from", "false")
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}