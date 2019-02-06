package com.phaseII.placepoint.Business.MyPosts.FlashDetail

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

class FlashDetailPresenter(var view: FlashDetailContract.View) {
    fun getDetails(auth_code1: String, postId: String) {
        view.showProgress()

        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getClaimedFlashPostList(auth_code1, postId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideProgress()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status == "true") {
                            view.setAdapter(parseData(`object`.optJSONArray("data")))
                        } else {
                            view.noData()
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    view.showNetworkError(R.string.server_error)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.hideProgress()
                view.showNetworkError(R.string.network_error)
            }
        })
    }

    private fun parseData(optJSONArray: JSONArray?): ArrayList<ModelFDetail> {
        var list = ArrayList<ModelFDetail>()
        if (optJSONArray != null && optJSONArray.length() > 0) {
            for (i in 0 until optJSONArray.length()) {
                val dataObject = optJSONArray.optJSONObject(i)

                val model = ModelFDetail()
                model.id = dataObject.optString("id")
                model.post_id = dataObject.optString("post_id")
                model.name = dataObject.optString("name")
                model.email = dataObject.optString("email")
                model.status = dataObject.optString("status")
                model.created_at = dataObject.optString("created_at")
                model.updated_at = dataObject.optString("updated_at")
               // if (model.status == "0") {
                    list.add(model)
                //}
            }
            //return
        }
        return list
    }

    fun deleteEmails(auth_code1: String, s: String, postId: String) {

        view.showProgress()

        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.changeFlashPostStatus(auth_code1, s, postId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideProgress()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status == "true") {
                            getDetails(auth_code1, postId)
                        } else {
                            view.noData()
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    view.showNetworkError(R.string.server_error)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.hideProgress()
                view.showNetworkError(R.string.network_error)
            }
        })
    }

    fun sendEmail(auth_code1: String, postId: String) {

        view.showProgress()

        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.sendEmail(auth_code1, postId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideProgress()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        //if (status=="true") {
                        view.showToast(`object`.optString("msg"))
//                        }else{
//
//                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    view.showNetworkError(R.string.server_error)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.hideProgress()
                view.showNetworkError(R.string.network_error)
            }
        })
    }
}