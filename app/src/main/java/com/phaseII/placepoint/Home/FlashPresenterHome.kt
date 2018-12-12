package com.phaseII.placepoint.Home

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

class FlashPresenterHome(var view: FlashContractHome.View) {
    fun getFlashPost(auth_code: String, town_id: String, category_id: String, limit: String, page: String) {
        view.showProgress()

        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getFlashDetail(auth_code, town_id, category_id, limit, page)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideProgress()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            val data = `object`.optJSONArray("data")
                            val category = `object`.optJSONArray("category")
                            val businessId = `object`.optString("business_id")

                            view.setAdapter(data.toString())
                        } else {
                            view.noData()
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
                view.hideProgress()
                view.showNetworkError(R.string.network_error)
            }
        })
    }

    fun claimPostCall(auth_code: String, postId: String, name: String,
                      email: String, position: String) {
        view.showProgress()

        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.claimPost(auth_code, postId, name, email)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideProgress()
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
                view.hideProgress()
                view.showNetworkError(R.string.network_error)
            }
        })
    }
}