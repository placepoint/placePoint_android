package com.phaseII.placepoint.ForgotPassword

interface ForgotPasswordView {
    fun showLoader()
    fun hideLoader()
    fun savePassWord(password: String)
    fun showError(optString: String?)
}