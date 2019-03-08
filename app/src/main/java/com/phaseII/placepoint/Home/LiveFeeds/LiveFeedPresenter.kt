package com.phaseII.placepoint.Home.LiveFeeds


import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Service
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LiveFeedPresenter(val view: LiveFeedHelper) {
    fun prepareData(from: String) {
        val auth_code = view.getAuthCode()
        val townId = view.getTownId()
        val limit = "10000"
        val page = "0"
        val timeline = if (from == "profile") {
            "false"
        } else {
            "true"
        }
        var cat = 0
        val categoryId = if (from=="Taxi"){
            view.getTaxiID()
        }else{
            view.getCatId()
        }
        if (categoryId.isEmpty()) {
            cat = 1
        }

        feedWebService(auth_code, townId, limit, page, categoryId, timeline, cat)

    }

    lateinit var call: Call<ResponseBody>
    private fun feedWebService(auth_code: String, town_id: String, limit: String, page: String, category_id: String, timeline: String, cat: Int) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)

        call = if (cat == 1) {
            service.getALLFeedData(auth_code, town_id, limit, page, "android")
        } else {
            service.getHomeFeedData(auth_code, town_id, limit, page, category_id, timeline)
        }

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            val data = `object`.optJSONArray("data")
                            val category = `object`.optJSONArray("category")
                            val businessId = `object`.optString("business_id")
                            val msg = `object`.optString("msg")
                            view.saveBusId(businessId)
                            if (!msg.isNullOrEmpty()) {
                                view.showMsg(msg)
                            }
                            view.setDataToAdapter(data.toString(), category.toString())
                        } else {
                            val msg = `object`.optString("msg")
                            if (!msg.isNullOrEmpty()) {
                                view.showMsg(msg)
                            }
                            val category = `object`.optJSONArray("category")
                            view.setDataToAdapter("", category.toString())
                        }

                    } catch (e: IOException) {
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

    fun claimPostCall(auth_code: String, postId: String, name: String, phone_no: String,
                      email: String, position: String) {
        view.showLoader()

        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.claimPost(auth_code, postId, name,phone_no, email)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")

                        if (status == "true") {
                            val claimed = `object`.optString("claimed")
                            // Constants.getBus().post(ClaimRedeem(claimed))
                            view.updateModeldata(position,claimed)
                            view.showToast(`object`.optString("msg"))
                        } else {
                            view.showToast(`object`.optString("msg"))
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    view.showNetworkError(R.string.server_error)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.hideLoader()
                view.showNetworkError(R.string.network_error)
            }
        })
    }

}
