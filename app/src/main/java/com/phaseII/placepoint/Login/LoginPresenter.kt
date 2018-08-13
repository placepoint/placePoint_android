package com.phaseII.placepoint.Login

import android.text.TextUtils
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

class LoginPresenter(var view: LoginHelper) {
    fun openRegisterScreen() {
        view.callResgister()
    }

    fun openForgorPasswordScreen() {
        view.callForgotPassword()
    }

    fun attemptLogin() {
        val emailStr = view.getEmail()
        val passwordStr = view.getPassword()
        val auth_code: String? = view.getAuthCode()

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            view.setEmailError()
            return
        }
        if (passwordStr.isEmpty()) {
            view.setPasswordEmptyError()
            return
        }

        loginWebService(emailStr, passwordStr, auth_code)

    }

    private fun loginWebService(email: String, password: String, auth_code: String?) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.postLoginData(email,password,auth_code)
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

                            val business_id = `object`.optString("business_id")
                            val business_name = `object`.optString("business_name")
                            val msg = `object`.optString("msg")
                            val datobj=data.optJSONObject(0)
                            val cat_id=datobj.optString("category")
                            for (i in 0 until data.length()){
                                val dataJson=data.getJSONObject(i)
                                val email=dataJson.optString("email")
                                val townId=dataJson.optString("location")
                                val user_type=dataJson.optString("user_type")
                                view.saveEmail(email,townId,user_type)
                            }
                            view.showSuccessMessage(msg)
                            view.saveBusId(business_id,business_name,cat_id,password)

                         //   view.saveLoginDataToPrefs(setData(data))
                        }else{
                            val msg = `object`.optString("msg")
                            view.showErrorMessage(msg)
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
