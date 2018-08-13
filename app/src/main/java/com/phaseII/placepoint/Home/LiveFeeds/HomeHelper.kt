package com.phaseII.placepoint.Home.LiveFeeds

interface HomeHelper {
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

}
