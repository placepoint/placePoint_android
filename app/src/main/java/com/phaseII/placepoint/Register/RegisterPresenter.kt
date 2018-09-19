package com.phaseII.placepoint.Register

import android.text.TextUtils
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

class RegisterPresenter(var view: RegisterHelper) {
    fun getRegisterData(userType: String, coupon: String) {
        val email: String = view.getEmail()
        val pass: String = view.getPassword()
        val getBName: String = view.getBusinessName()
        val getBLoc: String = view.getBusinessLocation()
        val getBCat: String = view.getBusinessCategory()
        val auth_code: String = view.getAuthCode()

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            view.setEmailError()
            return
        } else if (!isEmailValid(email)) {
            view.setInvalidEmailError()
            return
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            view.setPasswordError()
            return
        }

        if (getBName.isEmpty()) {
            view.setBNameError()
            return
        }
        if (getBLoc.isEmpty()) {
            view.setBLocError()
            return
        }
        if (getBCat.isEmpty()) {
            view.setBCatError()
            return
        }
        var type = view.getUserType()
//        if (type != "3") {
//            view.openPayment(email, pass, getBName, getBLoc, getBCat, auth_code!!, type)
//        }else{
//            registerWebService(email, pass, getBName, getBLoc, getBCat, auth_code!!,type)
//        }
//   if (type == "3"||type=="4") {
//       registerWebService(email, pass, getBName, getBLoc, getBCat, auth_code,type,coupon)
//
//   }else{
       view.openPayment(email, pass, getBName, getBLoc, getBCat, auth_code, type,coupon)
//
//        }
    }
    fun getRegisterService(userType: String?, couponID: String) {
        val email: String = view.getEmail()
        val pass: String = view.getPassword()
        val getBName: String = view.getBusinessName()
        val getBLoc: String = view.getBusinessLocation()
        val getBCat: String = view.getBusinessCategory()
        val auth_code: String = view.getAuthCode()

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            view.setEmailError()
            return
        } else if (!isEmailValid(email)) {
            view.setInvalidEmailError()
            return
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            view.setPasswordError()
            return
        }

        if (getBName.isEmpty()) {
            view.setBNameError()
            return
        }
        if (getBLoc.isEmpty()) {
            view.setBLocError()
            return
        }
        if (getBCat.isEmpty()) {
            view.setBCatError()
            return
        }
        var type = view.getUserType()
        registerWebService(email, pass, getBName, getBLoc, getBCat, auth_code,type,"")
    }

    private fun registerWebService(email: String, pass: String, bName: String, bLoc: String, bCat: String, auth_code: String, type: String, coupon: String) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.postRegisterData(email, pass, bName, bLoc, bCat, auth_code, type, "",coupon)
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
                            var category=""

                            for (i in 0 until data.length()) {
                                val dataJson = data.getJSONObject(i)
                                val email = dataJson.optString("email")
                                category = dataJson.optString("category")
                                view.saveEmail(email)
                            }
                            view.saveRegisterDataToPrefs(setData(data), pass,category)
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

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    fun getLoginScreen() {
        view.showLoginScreen()
    }

    fun openBusinessProfileFragment() {
        view.openBusinessProfile()
    }

    fun  CheckCouponValidity(couponID: String, userType: String, from: String) {
        val email: String = view.getEmail()
        val pass: String = view.getPassword()
        val getBName: String = view.getBusinessName()
        val getBLoc: String = view.getBusinessLocation()
        val getBCat: String = view.getBusinessCategory()
        val auth_code: String = view.getAuthCode()

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            view.setEmailError()
            return
        } else if (!isEmailValid(email)) {
            view.setInvalidEmailError()
            return
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            view.setPasswordError()
            return
        }

        if (getBName.isEmpty()) {
            view.setBNameError()
            return
        }
        if (getBLoc.isEmpty()) {
            view.setBLocError()
            return
        }
        if (getBCat.isEmpty()) {
            view.setBCatError()
            return
        }
        var type = view.getUserType()

        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.CheckCouponValidity(auth_code,couponID,userType)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        var amount=`object`.optString("price")
                        if(status=="true"){

                            view.hitRegisterApi(email,pass,getBName,getBCat,getBLoc,type,from,amount,auth_code)
                        }else{
                            view.showInvalidMessage(email,pass,getBName,getBCat,getBLoc,type,from,amount,auth_code)
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
