package com.phaseII.placepoint.Home.BusinessListing

interface BusinessHelper {
    fun setDataToAdapter(data: String, cat: Int, parent_category_name: String, relatedTo: String)
    fun showLoader()
    fun hideLoader()
    fun showNetworkError(resId: Int)
    fun getAuthCode(): String
    fun getTownId(): String
    fun getCatId(): String
    fun showNodata()
    fun getTaxiCatId(): String

}
