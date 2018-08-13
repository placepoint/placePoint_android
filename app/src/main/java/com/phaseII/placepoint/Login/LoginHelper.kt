package com.phaseII.placepoint.Login

interface LoginHelper {

        fun callResgister()
        fun callForgotPassword()
        fun setEmailError()
        fun setInvalidEmailError()
        fun getEmail(): String
        fun getPassword(): String
        fun setPasswordEmptyError()
        fun getAuthCode(): String?
        fun showLoader()
        fun hideLoader()
        fun showNetworkError(resId: Int)
        fun showSuccessMessage(msg: String)
        fun saveBusId(business_id: String, business_name: String, cat_id: String, password: String)
        fun showErrorMessage(msg: String?)
        fun saveEmail(email: String, townId: String, user_type: String)

}