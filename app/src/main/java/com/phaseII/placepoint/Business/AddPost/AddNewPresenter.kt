package com.phaseII.placepoint.Business.AddPost

import android.net.Uri
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Service

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class AddNewPresenter(val view: AddNewHelper) {
    private var file: File? = null
    private var requestFile: RequestBody? = null
    var image_status = "false"
    fun openCamera() {
        view.openCamera()
    }

    fun openCropper(list: ArrayList<Uri>) {
        view.openCropper(list)
    }

    fun openGallery() {
        view.openGallery()
    }

    fun addPost(serviceRunning: Boolean) {
        val postText = view.getPostText()
        if (postText.isEmpty()) {
            view.showEmptyPostMsg()
            return
        }

        val auth_code = view.getAuthCode()
        val desc = view.getDesc()
        val video_link = view.getVideoLink()
        val image = view.getImage()
        val title = view.getBusinessName()
        val height = view.getImageHeight()
        val width = view.getImageWidth()
        val type = view.getType()
        val day = view.getDay()
        val time = view.getTime()
        var now_status = if (type.isEmpty()){
            "1"
        }else{
            view.getNowStatus()
        }

        val category = view.getCategory()


        if (image.isEmpty()) {
            image_status = "false"
        } else {
            image_status = "true"
        }
        if (category.isEmpty()) {
            view.showError("Select atleast one catagory")
            return
        }
        if (!type.isEmpty()) {
            if (day.isEmpty()) {
                view.showError("You cannot left scheduling option empty")
                return

            }else{
                if (time.isEmpty()) {
                    view.showError("You cannot left scheduling time empty")
                    return
                }
            }
        }



        addPostService(auth_code, width, height, desc, video_link, image, image_status,
                title, serviceRunning, type, day, time, now_status, category)
    }

    private fun addPostService(auth_code: String, width: String, height: String, desc: String, video_link: String,
                               img: String, image_status: String, title: String,
                               serviceRunning: Boolean, type1: String, day: String,
                               time: String, now_status2: String, category: String) {
        view.showLoader()
        if (!img.isEmpty()) {
            file = File(img)
            if (file != null) {
                requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            }
        }
        val auth_code1 = RequestBody.create(MediaType.parse("text/plain"), auth_code)
        val desc1 = RequestBody.create(MediaType.parse("text/plain"), desc)
        val video_link1 = RequestBody.create(MediaType.parse("text/plain"), video_link)
        val image_status1 = RequestBody.create(MediaType.parse("text/plain"), image_status)
        val title1 = RequestBody.create(MediaType.parse("text/plain"), title)
        val width = RequestBody.create(MediaType.parse("text/plain"), width)
        val height = RequestBody.create(MediaType.parse("text/plain"), height)
        val type = RequestBody.create(MediaType.parse("text/plain"), type1)
        val day = RequestBody.create(MediaType.parse("text/plain"), day)
        val time = RequestBody.create(MediaType.parse("text/plain"), time)
        val now_status = RequestBody.create(MediaType.parse("text/plain"), now_status2)
        val category = RequestBody.create(MediaType.parse("text/plain"), category)

        var images: MultipartBody.Part? = null
        if (requestFile != null) {
            images = MultipartBody.Part.createFormData("images", "images", requestFile)
        }

        if (serviceRunning) {
            return
        }
        view.serviceIsRunning(true)
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.addPost(auth_code1, width, height, desc1, video_link1, images, image_status1, title1, type, day, time, now_status, category)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                view.serviceIsRunning(false)
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            val msg = `object`.optString("msg")
                            view.showMessage(msg)
                            val data = `object`.optJSONObject("data")
                            view.saveLocAndCat(data)
                            view.clearPrefsall(type1)
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
                view.hideLoader()
                view.serviceIsRunning(false)
                view.showNetworkError(R.string.network_error)
            }
        })


    }

    fun editPost(postID:String) {

         val auth_code = view.getAuthCode()
        val desc = view.getDesc()
        val video_link = view.getVideoLink()
        val image = view.getImage()
        val title = view.getBusinessName()
        val height1 = view.getImageHeight()
        val width1 = view.getImageWidth()
        val type1 = view.getEditType()
        val day1 = view.getEditDay()
        val time1 = view.getEditTime()
        val category1 = view.getCategory()
        if (image.isEmpty()) {
            image_status = "false"
        } else {
            image_status = "true"
        }
        val auth_code1 = RequestBody.create(MediaType.parse("text/plain"), auth_code)
        val title1 = RequestBody.create(MediaType.parse("text/plain"), title)
        val desc1 = RequestBody.create(MediaType.parse("text/plain"), desc)
        val video_link1 = RequestBody.create(MediaType.parse("text/plain"), video_link)
        val image_status1 = RequestBody.create(MediaType.parse("text/plain"), image_status)
        val postID = RequestBody.create(MediaType.parse("text/plain"), postID)
        val width = RequestBody.create(MediaType.parse("text/plain"), width1)
        val height = RequestBody.create(MediaType.parse("text/plain"), height1)
        val type = RequestBody.create(MediaType.parse("text/plain"), type1)
        val day = RequestBody.create(MediaType.parse("text/plain"), day1)
        val time = RequestBody.create(MediaType.parse("text/plain"), time1)
        val category = RequestBody.create(MediaType.parse("text/plain"), category1)
        view.showLoader()
        if (!image.isEmpty()) {
            file = File(image)
            if (file != null) {
                requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            }
        }
        var images: MultipartBody.Part? = null
        if (requestFile != null) {
            images = MultipartBody.Part.createFormData("images", "images", requestFile)
        }


        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.editSchedulePost(auth_code1, width, height, desc1, video_link1,images, image_status1, postID, type, day, time,  category,title1)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                view.serviceIsRunning(false)
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                       // if (status.equals("true", ignoreCase = true)) {
                            view.showScheduleScreen()
                        //}

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
                view.hideLoader()
                view.serviceIsRunning(false)
                view.showNetworkError(R.string.network_error)
            }
        })
    }

}
