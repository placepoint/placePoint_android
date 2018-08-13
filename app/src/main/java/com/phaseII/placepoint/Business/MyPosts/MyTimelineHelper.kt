package com.phaseII.placepoint.Business.MyPosts

interface MyTimelineHelper {
    fun setDataToAdapter(data: String)
    fun getAuthCode(): String
    fun getTownId(): String
    fun getCatId(): String
    fun showLoader()
    fun hideLoader()
    fun showNetworkError(resId: Int)
    fun noPosts()
    fun saveCategories(catagories: String)
    fun saveLocaton(location: String)

}
