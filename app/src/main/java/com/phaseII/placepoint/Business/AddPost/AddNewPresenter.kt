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
        var now_status = if (type.isEmpty()) {
            "1"
        } else {
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

            } else {
                if (time.isEmpty()) {
                    view.showError("You cannot left scheduling time empty")
                    return
                }
            }
        }

        //------------flashPost ------------------------------------------------------

        var flashSwitch = view.isFlashSwitchIsOn()
        var max_redemption = view.getPersonFlashValue()
        var per_person_redemption = view.getMaxFlashValue()
        var validity_date = view.getFlashDate()
        var validity_time = view.getFlashTime()
        var ftype = "0"
        if (flashSwitch) {
            ftype = "1"
            if (max_redemption.isEmpty()) {
                view.showError("Select Max number of offers to be redeemed.")
                return
            }
            if (per_person_redemption.isEmpty()) {
                view.showError("Select Max number Redeemed per person.")
                return
            }
            if (validity_date.isEmpty()) {
                view.showError("Select offer expires on.")
                return
            }
            if (validity_time.isEmpty()) {
                view.showError("Select offer expires Time")
                return
            }

        }
        //-----------------------------------------------------------------------------
        view.showLoader()
        val upload_video = view.uploadVideo()
//        if (upload_video.isEmpty()) {
//           return
//        }
        addPostService(auth_code, width, height, desc, video_link, image, image_status,
                title, serviceRunning, type, day, time, now_status, category, ftype, max_redemption, validity_date,
                validity_time, per_person_redemption, upload_video)
    }

    private fun addPostService(auth_code: String, width: String, height: String, desc: String, video_link: String,
                               img: String, image_status: String, title: String,
                               serviceRunning: Boolean, type1: String, day: String,
                               time: String, now_status2: String, category: String,
                               ftype: String, max_redemption: String, validity_date: String,
                               validity_time: String, per_person_redemption: String, upload_video: String) {

        if (!img.isEmpty()) {
            file = File(img)
            if (file != null) {
                requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            }
        }
        if (!upload_video.isEmpty()) {
            file = File(upload_video)
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
        val ftype1 = RequestBody.create(MediaType.parse("text/plain"), ftype)
        val max_redemption1 = RequestBody.create(MediaType.parse("text/plain"), max_redemption)
        val validity_date1 = RequestBody.create(MediaType.parse("text/plain"), validity_date)
        val validity_time1 = RequestBody.create(MediaType.parse("text/plain"), validity_time)
        val per_person_redemption1 = RequestBody.create(MediaType.parse("text/plain"), per_person_redemption)

        var images: MultipartBody.Part? = null
        if (requestFile != null) {
            images = MultipartBody.Part.createFormData("images", "images", requestFile)
        }

        var upload_video2: MultipartBody.Part? = null
        if (requestFile != null) {
            upload_video2 = MultipartBody.Part.createFormData("upload_video", upload_video, requestFile)
        }

        if (serviceRunning) {
            return
        }
        view.serviceIsRunning(true)
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.addPost(auth_code1, width, height, desc1, video_link1,
                images, image_status1, title1, type, day, time, now_status, category, ftype1,
                max_redemption1, validity_date1, validity_time1, per_person_redemption1, upload_video2)
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
                            view.clearPrefsall(type1, ftype)
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

    fun editPost(postID: String) {

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
        val imageStatus: String = view.getImageChanged()
        if (image.isEmpty()) {
            image_status = "false"
        } else {
            image_status = "true"
        }
        if (imageStatus == "true") {
            image_status = "true"
        }
        if (!type1.isEmpty()) {
            if (day1.isEmpty()) {
                view.showError("You cannot left scheduling option empty")
                return

            } else {
                if (time1.isEmpty()) {
                    view.showError("You cannot left scheduling time empty")
                    return
                }
            }
        }
        //------------flashPost ------------------------------------------------------

        var flashSwitch = view.isFlashSwitchIsOn()
        var max_redemption = view.getPersonFlashValue()
        var per_person_redemption = view.getMaxFlashValue()
        var validity_date = view.getFlashDate()
        var validity_time = view.getFlashTime()
        var ftype = "0"
        if (flashSwitch) {
            ftype = "1"
            if (max_redemption.isEmpty()) {
                view.showError("Select Max number of offers to be redeemed.")
                return
            }
            if (per_person_redemption.isEmpty()) {
                view.showError("Select Max number Redeemed per person.")
                return
            }
            if (validity_date.isEmpty()) {
                view.showError("Select offer expires on.")
                return
            }
            if (validity_time.isEmpty()) {
                view.showError("Select offer expires Time")
                return
            }
        }
        //-----------------------------------------------------------------------------


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
        val ftype1 = RequestBody.create(MediaType.parse("text/plain"), ftype)
        val max_redemption1 = RequestBody.create(MediaType.parse("text/plain"), max_redemption)
        val validity_date1 = RequestBody.create(MediaType.parse("text/plain"), validity_date)
        val validity_time1 = RequestBody.create(MediaType.parse("text/plain"), validity_time)
        val per_person_redemption1 = RequestBody.create(MediaType.parse("text/plain"), per_person_redemption)



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
        val call: Call<ResponseBody> = service.editSchedulePost(auth_code1, width,
                height, desc1, video_link1, images, image_status1, postID, type, day,
                time, category, title1, ftype1,
                max_redemption1, validity_date1, validity_time1, per_person_redemption1)
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
