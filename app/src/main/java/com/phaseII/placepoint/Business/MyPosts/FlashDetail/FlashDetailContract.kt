package com.phaseII.placepoint.Business.MyPosts.FlashDetail

class FlashDetailContract {
    public interface View{
        fun showProgress()
        fun hideProgress()
        fun showNetworkError(server_error: Int)
        fun noData()
        fun setAdapter(list: ArrayList<ModelFDetail>)
        fun showToast(optString: String)

    }
}