package com.phaseII.placepoint.Payment

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

class PaymentPresenter(var view: PaymentContract) {
    fun registerWebService(email: String, pass: String, bName: String,
                           bLoc: String,
                           bCat: String, auth_code: String,
                           type: String, token: String, coupon: String) {


        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.postRegisterData(email, pass, bName, bLoc, bCat, auth_code, type, token,coupon)
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
                            var category = ""
                            for (i in 0 until data.length()) {
                                val dataJson = data.getJSONObject(i)
                                val email = dataJson.optString("email")
                                category = dataJson.optString("category")
                                view.saveEmail(email)
                            }
                            view.saveRegisterDataToPrefs(setData(data), pass, category)

                        } else {
                            val msg = `object`.optString("msg")
                            view.showMessage(msg)
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

    private fun setData(data: JSONArray?): JSONObject {
        var obj: JSONObject? = null
        if (data != null) {
            for (i in 0 until data.length()) {
                obj = data.optJSONObject(i)
            }
        }
        return obj!!
    }

    fun openBusinessProfileFragment() {


    }

    fun paymentService(auth_code: String, currenttype: String, upgrade_type: String, token: String, amount: String) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.upgrade(auth_code, currenttype, upgrade_type, token, amount)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            view.showPaymentDone(upgrade_type)
                        } else {
                            val msg = `object`.optString("msg")
                            view.showMessage(msg)
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
}