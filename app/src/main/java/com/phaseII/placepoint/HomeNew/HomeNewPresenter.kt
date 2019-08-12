package com.phaseII.placepoint.HomeNew

import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Service
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeNewPresenter(var view: HomeNewContract.View ){
   // private var category_id: String = ""
    fun prepareBusinessData(s: String) {

        val auth_code: String = view.getAuthCode()
        //val town_id: String = view.getTownId()
        val limit: String = "100"
        val page: String = "0"

           // category_id = view.getCatId()

        var cat = 0

//        if (category_id.isEmpty()) {
//            cat = 1
//        }


        getBusinessDetailsService(auth_code, "9", limit, page, "29,30,42", cat,s)

    }

    lateinit var call: Call<ResponseBody>
    private fun getBusinessDetailsService(auth_code: String, town_id: String,
                                          limit: String, page: String,
                                          category_id: String?, cat: Int, relatedTo: String) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)

            call = service.getBusinessDetail(auth_code, town_id, limit, page, category_id)


        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        var data: JSONArray = JSONArray()
                        var parent_category_name = ""
                        if (status.equals("true", ignoreCase = true)) {



                                data = `object`.optJSONArray("featured_business")

                            view.setDataToAdapter(data.toString())
                        } else {
                            parent_category_name = `object`.optString("parent_category_name")
                            view.setDataToAdapter(data.toString())
                            view.showNodata()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.hideLoader()
                view.showNetworkError(R.string.network_error)
            }
        })
    }


}