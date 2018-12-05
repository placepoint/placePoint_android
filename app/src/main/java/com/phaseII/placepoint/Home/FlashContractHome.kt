package com.phaseII.placepoint.Home

class FlashContractHome {
    public interface View{
        fun showProgress()
        fun hideProgress()
        fun noData()
        fun showNetworkError(server_error: Int)
        fun setAdapter(data: String)
        fun showToast(optString: String?)
        fun updateModeldata(position: String)


    }
}