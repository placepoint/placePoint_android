package com.phaseII.placepoint.AboutBusiness


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

class AboutBusinessPresenter(var view: AboutBusinessHelper) {
    fun askForGPSPermission() {
        view.askForGPS()
    }

    fun BusinessDetailService( busId: String) {
       // view.setAdapter(modelBusiness)
        val auth_code = view.getAuthCode()
       // val business_id = view.getBusId()

        var business_id = busId
        if (business_id==null||business_id.isEmpty()){
            business_id=view.getBusId()
        }
        val ifLoggedIn=view.getIfLoggedIn()
        webService(auth_code, business_id,ifLoggedIn)
    }

    private fun webService(auth_code: String, business_id: String, ifLoggedIn: String) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getSingleBusiness(auth_code, business_id,ifLoggedIn)
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
                            val posts = `object`.optJSONArray("posts")
                            view.setBusinessData(data.toString(),posts.toString())
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

    fun setSingleBusinessData() {
        view.setSingleBusinessData()
    }

    /*fun getBusinessData(model: ModelBusiness) {
       // val list = ConstantVal.getbusinessData(bdata)
        view.setData(model)
    }*/

}
