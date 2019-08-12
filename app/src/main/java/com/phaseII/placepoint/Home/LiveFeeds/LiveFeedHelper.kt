package com.phaseII.placepoint.Home.LiveFeeds

interface LiveFeedHelper {
    fun setDataToAdapter(data: String, category: String)
    fun getAuthCode(): String
    fun getTownId(): String
    fun getCatId(): String
    fun showLoader()
    fun hideLoader()
    fun showNetworkError(resId: Int)
    fun showMsg(msg: String?)
    fun saveBusId(business_id: String?)
    fun getTaxiID(): String
    fun updateModeldata(position: String, claimed: String)
    fun showToast(optString: String?)
    fun getBusId(): String
    fun getIfLoggedIn(): String
    fun setBusinessData(toString: String, toString1: String)

}
