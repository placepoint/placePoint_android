package com.phaseII.placepoint.MultichoiceCategories

import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Service
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class CategoryPresenter(val view: CategoryHelper) {
    fun prepareCategory(ids: String?, from: String) {


        val data = view.getCategoriesFromPrefs()
        var list = Constants.getCategoryData(data)
        val highlightCat = arrayListOf<String>()
        if (from == "addpost") {
          //  val ids2 = view.getMaincatIDS()
                val selectedlist=view.getSelectedList()

                val ids2 = ids
                var idList = ids2!!.split(",")
                val list2 = arrayListOf<ModelCategoryData>()
                var list3 = arrayListOf<ModelCategoryData>()
                var list4 = arrayListOf<ModelCategoryData>()

                for (l in 0 until list!!.size) {
                    for (m in 0 until idList.size) {
                        if (list[l].id == idList[m]) {
                            list2.add(list[l])
                        }
                    }
                }
                for (l in 0 until list.size) {
                    for (m in 0 until list2.size) {
                        if (list[l].id == list2[m].parent_category) {
                            list2.add(list[l])
                        }
                    }
                }

                list.clear()
                list3 = list2.distinctBy { it.id } as ArrayList<ModelCategoryData>
                //list2.distinctBy { Pair(it.id, it.id) }
                for (p in 0 until list3!!.size) {
                    if (list3[p].show_on_live != "0") {
                        list4.add(list3[p])
                    }
                }
                list = list4
                var mainCat = arrayListOf<ModelCategoryData>()
                var sub = arrayListOf<ModelCategoryData>()
                var mainCat1 = arrayListOf<ModelCategoryData>()
                for (h in 0 until list.size) {
                    if (list[h].parent_category == "0") {
                        mainCat.add(list[h])
                    }else {
                        if (!selectedlist.isEmpty()) {
                            var selectedidList = selectedlist!!.split(",")
                            for (k in 0 until selectedidList.size) {
                                if (selectedidList[k] == list[h].id) {
                                    list[h].checked = true
                                }
                            }

                        }
                        sub.add(list[h])
                    }
                }

                for (g in 0 until mainCat.size) {

                    inn@ for (m in 0 until list!!.size) {
                        if (mainCat[g].id == list[m].parent_category) {

                            mainCat1.add(mainCat[g])
                            break@inn
                        }
                    }

                }
                list.clear()
                list=sub
                for (j in 0 until mainCat1.size ){
                    list.add(mainCat1[j])
                }

        } else {
            if (ids != null) {
                if (!ids.isEmpty()) {
                    if (list != null && list.size > 0) {
                        //val idList = asList(ids.split(","))

                        val idList = ids.split(",")

                        for (j in 0 until idList.size) {
                            for (k in 0 until list.size) {

                                if (idList[j] == list[k].id) {
                                    list[k].checked = true
                                    highlightCat.add(list[k].parent_category)
                                }
                            }

                        }
                    }
                }

            }
        }
        view.hideNoData()
        if (list != null) {
            view.setAdapterToList(list, highlightCat)
        }
    }

    fun setDataToSearch(mList: ArrayList<ModelCategoryData>, query: String) {
        view.setSearchToView(mList, query)
    }

    fun runAppService() {
        val town_id = view.getTownId()
        val device_id = view.getDeviceId()
        val device_type = Constants.DEVICE_TYPE
        appDataService(device_id, town_id, device_type)
    }

    private fun appDataService(device_id: String, town_id: String?, device_type: String) {
       // view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getAppData()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    view.hideLoader()
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            val location = `object`.optJSONArray("location")
                            val category     = `object`.optJSONArray("category")
                            view.saveCategories(category.toString())
                            view.saveTowns(location.toString())
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.hideLoader()
               // view.showNetworkError(R.string.network_error)
            }
        })
    }


    fun runAppAuthService() {
        val town_id = view.getTownId()
        val device_id = view.getDeviceId()
        val device_type = Constants.DEVICE_TYPE
        appAuthService(device_id, town_id, device_type)
    }

    private fun appAuthService(device_id: String, town_id: String?, device_type: String) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getAppAuth(device_id, town_id, device_type)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            val data = `object`.optJSONArray("data")
                            view.saveAuthDataToPrefs(setData(data))
                          //  view.getCategories()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.hideLoader()
                view.showNetworkError(R.string.network_error)
            }
        })
    }
    private fun setData(data: JSONArray?): JSONObject {
        var obj: JSONObject? = null
        if (data != null) {
            for (i in 0 until data.length()) {
                obj = data.optJSONObject(i)
            }
        }
        return obj!!
    }

    fun setDataFromPrefs() {

        val data = view.getCategoriesFromPrefs()
        val list = Constants.getCategoryData(data)
        val highlightCat = arrayListOf<String>()
        view.hideNoData()
        if (list != null) {
            view.setAdapterToList(list, highlightCat)
        }
    }
}
