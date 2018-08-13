package com.phaseII.placepoint.ForgotPassword


import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Service
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ForgotPasswordPresenter(var view: ForgotPasswordView) {
   fun sendPassword(emailId: String, authcode: String) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
       val call: Call<ResponseBody> = service.forgotPassword(authcode,emailId )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json=JSONObject(res)
                        val status=json.optString("status")
                        if (status=="true") {
                            view.savePassWord("12345")
                        }else{
                            view.showError(json.optString("msg"))
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
            }
        })
    }



}