package com.phaseII.placepoint.Business.MyPosts


import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Service
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MyTimelinePresenter(val view: MyTimelineHelper) {
    fun prepareData() {
        val auth_code = view.getAuthCode()
        val townId = view.getTownId()
        val limit = "10000"
        val page = "0"
        val timeline = "false"
        val categoryId = view.getCatId()
        feedWebService(auth_code, townId, limit, page, categoryId,timeline)
    }

    private fun feedWebService(auth_code: String, town_id: String, limit: String, page: String, category_id: String, timeline: String) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getHomeFeedData(auth_code, town_id, limit, page, category_id, timeline)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            val location = `object`.optJSONArray("location")
                            val category     = `object`.optJSONArray("category")
                            view.saveCategories(category.toString())
                            view.saveLocation(location.toString())

                            val data = `object`.optJSONArray("data")
                            view.setDataToAdapter(data.toString())

                        }else{
                            view.noPosts()
                            val location = `object`.optJSONArray("location")
                            val category     = `object`.optJSONArray("category")
                            view.saveCategories(category.toString())
                            view.saveLocation(location.toString())

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

}
