package com.phaseII.placepoint.Payment

import org.json.JSONObject

interface PaymentContract {
    fun showLoader()
    fun hideLoader()
    fun saveRegisterDataToPrefs(data: JSONObject, pass: String, category: String)
    fun saveEmail(email: String?)
    fun showMessage(msg: String?)
    fun showNetworkError(network_error: Int)
    fun showPaymentDone(upgrade_type: String)
}