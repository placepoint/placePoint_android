package com.phaseII.placepoint.Business.ScheduledPost



interface ScheduleContract {
    fun showLoader()
    fun hideLoader()
    abstract fun showNetworkError(resId: Int)
    fun setAdapter(data: String)
    fun getAuthCode(): String
    fun refreshAdapter(id: Int)
    fun NoData()
}