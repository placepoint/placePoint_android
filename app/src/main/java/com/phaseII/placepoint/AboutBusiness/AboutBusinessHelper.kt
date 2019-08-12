package com.phaseII.placepoint.AboutBusiness

import com.phaseII.placepoint.Home.BusinessListing.ModelBusiness


interface AboutBusinessHelper {
    fun askForGPS()
    fun setData(list: ModelBusiness)
    fun getAuthCode(): String
    fun getBusId(): String
    fun showLoader()
    fun hideLoader()
    fun showNetworkError(resId: Int)
    fun setBusinessData(data: String, toString: String)
    fun setSingleBusinessData()
    fun setAdapter(modelBusiness: ModelBusiness)
    fun getIfLoggedIn(): String

}
