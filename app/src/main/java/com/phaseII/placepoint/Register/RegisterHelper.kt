package com.phaseII.placepoint.Register

import org.json.JSONObject

/**
 * Created by Loveleen on 5/6/18.
 */
interface RegisterHelper {
    fun getEmail():String
    fun getPassword(): String
    fun getBusinessName(): String
    fun getBusinessLocation(): String
    fun getBusinessCategory(): String
    fun setEmailError()
    fun setInvalidEmailError()
    fun setPasswordError()
    fun setBNameError()
    fun setBLocError()
    fun setBCatError()
    fun showLoginScreen()
    fun getAuthCode(): String?
    fun showLoader()
    fun hideLoader()
    fun showNetworkError(resId: Int)
    fun saveRegisterDataToPrefs(data: JSONObject, pass: String, category: String)
    fun openBusinessProfile()
    fun showMessage(msg: String?)
    fun saveEmail(email: String)
    fun getUserType(): String
    fun openPayment(email: String, pass: String, bName: String, bLoc: String, bCat: String, auth_code: String, type: String)
}