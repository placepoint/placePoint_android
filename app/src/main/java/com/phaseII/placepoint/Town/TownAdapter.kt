package com.phaseII.placepoint.Town

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phaseII.placepoint.Categories.CategoryActivity
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Service
import kotlinx.android.synthetic.main.town_item.view.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class TownAdapter(val context: TownActivity, var list: List<ModelTown>,
                  var p: String,var from: String) : RecyclerView.Adapter<TownAdapter.ViewHolder>() {

    val inflator = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflator.inflate(R.layout.town_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list.get(position)
        holder.itemView.title_text.text = model.townname
        holder.itemView.main.setOnClickListener {
//            if (from=="false") {
//
//                Constants.getPrefs(context)?.edit()?.putString(Constants.OPEN_TOWN, "no")?.apply()
//                Constants.getPrefs(context)?.edit()?.putString(Constants.TOWN_ID, model.id)?.apply()
//                Constants.getPrefs(context)?.edit()?.putString(Constants.TOWN_NAME, model.townname)?.apply()
//
//                val intent = Intent(context, CategoryActivity::class.java)
//                intent.putExtra("from","cat")
//                intent.putExtra("from1",from)
//                context.startActivity(intent)
//                context.finish()
//            }else{
                //Constants.getPrefs(context)?.edit()?.putString("firstTime", "Town")?.apply()
//            if (from=="false") {
//                Constants.getPrefs(context)?.edit()?.putString("getAuthCode", "yes")?.apply()
//            }else{
//                Constants.getPrefs(context)?.edit()?.putString("getAuthCode", "no")?.apply()
//            }

            if (Constants.getPrefs(context)!!.getString("getAuthCode", "") == "") {
                Constants.getPrefs(context)?.edit()?.putString(Constants.OPEN_TOWN, "no")?.apply()
                //Constants.getPrefs(context)?.edit()?.putString(Constants.TOWN_ID, model.id)?.apply()
               // Constants.getPrefs(context)?.edit()?.putString(Constants.TOWN_NAME, model.townname)?.apply()

                runAppAuthService(model.id, model.townname)


            }else {
                //if (Constants.getPrefs(context)!!.getString(Constants.CATEGORY_IDS,"").isEmpty()){
                    Constants.getPrefs(context)?.edit()?.putString(Constants.OPEN_TOWN, "no")?.apply()
               // Constants.getPrefs(context)?.edit()?.putString(Constants.TOWN_ID, model.id)?.apply()
               // Constants.getPrefs(context)?.edit()?.putString(Constants.TOWN_NAME, model.townname)?.apply()

                val intent = Intent(context, CategoryActivity::class.java)
                intent.putExtra("from","cat")
                intent.putExtra("from1",from)
                intent.putExtra("townId",model.id)
                intent.putExtra("townName", model.townname)
                context.startActivity(intent)
               // context.finish()
//                }else {
//                    val intent = Intent(context, DashBoardActivity::class.java)
//                    context.startActivity(intent)
//                    Constants.getPrefs(context)?.edit()?.putString(Constants.OPEN_TOWN, "no")?.apply()
//                    Constants.getPrefs(context)?.edit()?.putString(Constants.TOWN_ID, model.id)?.apply()
//                    Constants.getPrefs(context)?.edit()?.putString(Constants.TOWN_NAME, model.townname)?.apply()
//                    context.finish()
//                }
            }
//            }
        }
    }

    private fun runAppAuthService(townId: String, townname: String) {
        val town_id = townId
       // val town_id = Constants.getPrefs(context)?.getString(Constants.TOWN_ID, "")
        val device_id = Constants.getDeviceId(context)
        val device_type = Constants.DEVICE_TYPE
        appAuthService(device_id, town_id, device_type,townname)
    }
    private fun appAuthService(device_id: String, town_id: String?, device_type: String, townname: String) {

        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getAppAuth(device_id, town_id, device_type)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            val data = `object`.optJSONArray("data")
                            saveAuthDataToPrefs(setData(data),town_id,townname)
                            //  view.getCategories()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    private fun saveAuthDataToPrefs(data: JSONObject, town_id: String?, townname: String) {
        try {
            if (data != null) {
                Constants.getPrefs(context)?.edit()?.putString(Constants.ID, data.optString("id"))?.apply()
                Constants.getPrefs(context)?.edit()?.putString(Constants.AUTH_CODE, data.optString("auth_code"))?.apply()
                Constants.getPrefs(context)?.edit()?.putString(Constants.DEVICE_ID, data.optString("device_id"))?.apply()
                Constants.getPrefs(context)?.edit()?.putString(Constants.DEVICE_TYPE, data.optString("device_type"))?.apply()
                //Constants.getPrefs(this)?.edit()?.putString(Constants.TOWN_ID, data.optString("town_id"))?.apply()
                Constants.getPrefs(context)?.edit()?.putString(Constants.BUSINESS_ID, data.optString("business_id"))?.apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Constants.getPrefs(context)?.edit()?.putString("getAuthCode", "no")?.apply()
       // if (Constants.getPrefs(context)!!.getString(Constants.CATEGORY_IDS,"").isEmpty()){

            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("from","cat")
            intent.putExtra("from1",from)
            intent.putExtra("townId",town_id)
            intent.putExtra("townName",townname)

            context.startActivity(intent)
           // context.finish()
//        }else {
//            val intent = Intent(context, DashBoardActivity::class.java)
//            context.startActivity(intent)
//            context.finish()
//        }
//        val intent = Intent(context, DashBoardActivity::class.java)
//        context.startActivity(intent)
//        context.finish()
    }

    private fun setData(data: JSONArray?): JSONObject {
        var obj: JSONObject? = null
        if (data != null) {
            for (i in 0 until data.length()) {
                obj = data.optJSONObject(i)
            }
        }
        return obj!!
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

//    fun filterList( filterTown: ArrayList<ModelTown>) {
//        this.list=filterTown
//        notifyDataSetChanged()
//    }

}
