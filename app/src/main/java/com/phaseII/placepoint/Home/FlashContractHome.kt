package com.phaseII.placepoint.Home

class FlashContractHome {
    public interface View{
        fun showProgress()
        fun hideProgress()
        fun noData()
        fun showNetworkError(server_error: Int)
        fun setAdapter(data: String, position: Int)
        fun showToast(optString: String?)
        fun updateModeldata(position: String, claimed: String)
        fun getMultiTowns(): String
        fun getMultiCategories(): String
        fun showAlert(optString: String?)
        fun showNextDialog(position: Int, model: ModelHome)


    }
}