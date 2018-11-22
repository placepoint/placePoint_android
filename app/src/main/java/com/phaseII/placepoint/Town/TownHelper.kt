package com.phaseII.placepoint.Town

import org.json.JSONObject

interface TownHelper {
    fun setDataToAdapter(data: String)
    fun showNetworkError(resId: Int)
    fun hideLoader()
    fun showLoader()
    fun saveCategories(toString: String)



}