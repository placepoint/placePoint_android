package com.phaseII.placepoint.HomeNew

class HomeNewContract {
    public interface View{
        fun getAuthCode(): String
        fun showLoader()
        fun hideLoader()
        fun setDataToAdapter(toString: String)
        fun showNodata()
        fun showNetworkError(network_error: Int)

    }
}