package com.phaseII.placepoint.MultichoiceCategories

import org.json.JSONObject

interface CategoryHelper {
    fun setAdapterToList(list: ArrayList<ModelCategoryData>, highlightCat: ArrayList<String>)
    fun hideNoData()
    fun setSearchToView(mList: ArrayList<ModelCategoryData>, query: String)
    fun getCategoriesFromPrefs(): String
    fun getTownId(): String?
    fun getDeviceId(): String
    fun showLoader()
    fun hideLoader()
    fun saveAuthDataToPrefs(data: JSONObject)
    fun showNetworkError(resId: Int)
    fun getCategories()
    fun getMaincatIDS(): String?
    fun saveCategories(categories: String)
    fun saveTowns(towns: String)
    fun getSelectedList(): String

}
