package com.phaseII.placepoint.Business.Profile

import android.net.Uri
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Service
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException




class BusinessProfilePresenter(var view: BusinessProfileHelper) {
    private var file: File? = null
    private var requestFile: RequestBody? = null

    fun prepareBusinessData() {

        val auth_code: String? = view.getAuthCodeConstant()
        val town_id: String? = view.getTownIdConstant()
        val limit: String? = "10"
        val page: String? = "0"
        val category_id: String? = view.getCatId()
        getBusinessDetailsService(auth_code, town_id, limit, page, category_id)
    }

    private fun getBusinessDetailsService(auth_code: String?, town_id: String?, limit: String?, page: String?, category_id: String?) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getBusinessDetail(auth_code, town_id, limit, page, category_id)
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

    fun openStartTimePicker(day: String) {
        return view.showOpenStartTimePicker(day)
    }

    fun closeStartTimePicker() {
        return view.showCloseStartTimePicker()
    }

    fun closeEndTimePicker() {
        return view.showCloseEndTimePicker()
    }


    fun setWeekRadioGroup() {
        view.setRadioGroupListener()
    }

    fun setAdapterForCroppedImages(list: ArrayList<Uri>?, croppedImages: java.util.ArrayList<Uri>, preLoadImages: MutableList<String>, type: String) {
        view.setAdapterForCroppedImages(list, croppedImages, preLoadImages)
    }

    fun askForGPSPermission() {
        view.askForGPS()
    }

    fun openEndTimePicker() {
        view.showOpenEndTimePicker()
    }

    fun openCamera() {
        view.openCamera()
    }

    fun openGallery() {
        view.openGallery()
    }

    fun openCropper(list: java.util.ArrayList<Uri>, s: String) {
        view.openCropper(list, s)
    }

    fun requestAllPermissions() {
        view.requestAllPermissions()
    }

    fun setInitialTimeData(arr: JSONArray) {
        view.setInitialData(getTimeList(arr))
    }

    private fun getTimeList(arr: JSONArray): ArrayList<ModelTime> {
        val list = arrayListOf<ModelTime>()
        // mon
        var model = ModelTime()
        try {
            val objMon = arr.getJSONObject(0)
            model.startFrom = objMon.optString("startFrom")
            model.closeFrom = objMon.optString("closeFrom")
            model.startTo = objMon.optString("startTo")
            model.closeTo = objMon.optString("closeTo")
            if (objMon.optString("startFrom") == "closed") {
                Constants.closedList[0] = true
                //model.close = "true"
            }
        } catch (e: Exception) {
            model.startFrom = "12:00 AM"
            model.startTo = "12:00 AM"
            model.closeFrom = "12:00 AM"
            model.closeTo = "12:00 AM"
            Constants.closedList[0] = false
            // model.close = "false"
        }
        list.add(model)
        // tues
        model = ModelTime()
        try {
            val obj = arr.getJSONObject(1)
            model.startFrom = obj.optString("startFrom")
            model.closeFrom = obj.optString("closeFrom")
            model.startTo = obj.optString("startTo")
            model.closeTo = obj.optString("closeTo")
            if (obj.optString("startFrom") == "closed") {
                Constants.closedList[1] = true
            }
        } catch (e: Exception) {
            model.startFrom = "12:00 AM"
            model.startTo = "12:00 AM"
            model.closeFrom = "12:00 AM"
            model.closeTo = "12:00 AM"
            Constants.closedList[1] = false
        }
        list.add(model)
        //wed
        model = ModelTime()
        try {
            val obj = arr.getJSONObject(2)
            model.startFrom = obj.optString("startFrom")
            model.closeFrom = obj.optString("closeFrom")
            model.startTo = obj.optString("startTo")
            model.closeTo = obj.optString("closeTo")
            if (obj.optString("startFrom") == "closed") {
                Constants.closedList[2] = true
            }
        } catch (e: Exception) {
            model.startFrom = "12:00 AM"
            model.startTo = "12:00 AM"
            model.closeFrom = "12:00 AM"
            model.closeTo = "12:00 AM"
            Constants.closedList[2] = false
        }
        list.add(model)
        // thu
        model = ModelTime()
        try {
            val obj = arr.getJSONObject(3)
            model.startFrom = obj.optString("startFrom")
            model.closeFrom = obj.optString("closeFrom")
            model.startTo = obj.optString("startTo")
            model.closeTo = obj.optString("closeTo")
            if (obj.optString("startFrom") == "closed") {
                Constants.closedList[3] = true
            }
        } catch (e: Exception) {
            model.startFrom = "12:00 AM"
            model.startTo = "12:00 AM"
            model.closeFrom = "12:00 AM"
            model.closeTo = "12:00 AM"
            Constants.closedList[3] = false
        }
        list.add(model)
        //fri
        model = ModelTime()
        try {
            val obj = arr.getJSONObject(4)
            model.startFrom = obj.optString("startFrom")
            model.closeFrom = obj.optString("closeFrom")
            model.startTo = obj.optString("startTo")
            model.closeTo = obj.optString("closeTo")
            if (obj.optString("startFrom") == "closed") {
                Constants.closedList[4] = true
            }
        } catch (e: Exception) {
            model.startFrom = "12:00 AM"
            model.startTo = "12:00 AM"
            model.closeFrom = "12:00 AM"
            model.closeTo = "12:00 AM"
            Constants.closedList[4] = false
        }
        list.add(model)
        //sat
        model = ModelTime()
        try {
            val obj = arr.getJSONObject(5)
            model.startFrom = obj.optString("startFrom")
            model.closeFrom = obj.optString("closeFrom")
            model.startTo = obj.optString("startTo")
            model.closeTo = obj.optString("closeTo")
            if (obj.optString("startFrom") == "closed") {
                Constants.closedList[5] = true
            }
        } catch (e: Exception) {
            model.startFrom = "12:00 AM"
            model.startTo = "12:00 AM"
            model.closeFrom = "12:00 AM"
            model.closeTo = "12:00 AM"
            Constants.closedList[5] = false
        }
        list.add(model)
        //sun
        model = ModelTime()
        try {
            val obj = arr.getJSONObject(6)
            model.startFrom = obj.optString("startFrom")
            model.closeFrom = obj.optString("closeFrom")
            model.startTo = obj.optString("startTo")
            model.closeTo = obj.optString("closeTo")
            if (obj.optString("startFrom") == "closed") {
                Constants.closedList[6] = true
            }
        } catch (e: Exception) {
            model.startFrom = "12:00 AM"
            model.startTo = "12:00 AM"
            model.closeFrom = "12:00 AM"
            model.closeTo = "12:00 AM"
            Constants.closedList[6] = false
        }
        list.add(model)
        return list
    }

    fun clickHandler() {
        view.handleClicks()
    }

    fun showSelectTownPopup() {
        view.selectTownPopUp()
    }

    fun showSelectCatPopup() {
        view.selectCatPopUp()
    }

    private val stringArray: ArrayList<String> = arrayListOf()

    fun onSaveButtonClick() {
        val auth_code = view.getAuthCode()
        var town_id = view.getTownId()
        val bus_name = view.getBusName()
        val bus_desc = view.getBusDesc()
        val email=view.getEmail()
        val business_email=view.getEmail()
        val userType=view.getUserType()
        if (bus_name == "") {
            view.setTownError("Business Name is missing")

            return
        }
        val coverImage = view.getCoverImage()
        var coverImageString = view.getCoverImageString()
if (userType!="3") {
    if (coverImage == null) {
        val modelImagae: String = view.getFromModel()
        if (modelImagae.isEmpty()) {
            view.coverImageIssue("Select Cover Image")

            return
        }
    }
    if (bus_desc == "") {
        view.setDescError("Business Description is missing")

        return
    }
    val checkAllDays = view.checkingAllDaysValidation();
    if (checkAllDays) {
        return
    }
}
        val address = view.getAddress()
        if (address == null || address.equals("")) {
            view.setAdressError("Address is missing")

            return
        }
        val contact_no = view.getContactNo()
        if (contact_no == null || contact_no.equals("")) {
            view.setContactError("Contact is missing")

            return
        }
        var category_id = view.getCategoryId()
        if (userType!="3") {
            if (business_email == null || business_email.equals("")) {
                view.setContactError("Email is missing")

                return
            }
            if (town_id == null || town_id.equals("")) {
                val pretown = view.getPrefillTownId()
                if (pretown == "") {
                    view.setTownError("Town is missing")

                    return
                } else {
                    town_id = pretown
                }
            }

            if (category_id == null || category_id.equals("")) {
                val preCat = view.getPrefillCatId()
                if (preCat == "") {
                    view.setCatError("Category is missing")

                    return
                } else {
                    category_id = preCat
                }
            }
        }

        val images = view.getMultiPartFiles()
        val oldimages = view.getOldMultiPartFiles()
        var image_status = "true"
        if (images.size > 0) {
            image_status = "true"
        }

        val s = images.size
        val image_count = s.toString()
        val video_link = view.getVideoLink()
        val time = view.getTimeArray()
        if (userType!="3") {
            for (i in 0 until time.size) {
                if (!time[i].status) {
                    if (time[i].startFrom == "12:00AM") {
                        view.showMessageErr("Please select time for all days")

                        return
                    }
                }
            }
        }
        val opening_hours = view.getOpeningHours()
        //val closing_hours = view.getClosingHours()
        val lat = view.getLatitude()
        val long = view.getLongitude()
        view.saveBusinessName()


        view.setclickTrue()

      if (coverImage == null) {
//        if (coverImageString == null||coverImageString.isEmpty()) {
            updateBusinessPageService2(bus_name,auth_code, town_id, email,category_id, image_status, video_link
                    , opening_hours, image_count, address, contact_no, images, lat, long, oldimages, bus_desc, coverImageString,business_email)

        } else {
            updateBusinessPageService(bus_name,auth_code, town_id,email, category_id, image_status, video_link
                    , opening_hours, image_count, address, contact_no, images, lat, long, oldimages, bus_desc, coverImage!!, coverImageString,business_email)

        }
    }

    private fun updateBusinessPageService2(bus_name: String,auth_code: String, town_id: String, email: String, category_id: String, image_status: String, video_link: String, opening_hours: String, image_count: String, address: String, contact_no: String, images: ArrayList<Uri>, lat: String, long: String, oldimages: String, bus_desc: String, coverImageString: String
                                           , business_email: String) {
        view.showLoader()
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        if (images.size == 0) {
            // call without 5 images
            call = service.updateBusinessPage3(bus_name,auth_code, town_id,email
                    , category_id, image_status, video_link, opening_hours, image_count,
                    address, contact_no, lat, long, bus_desc, oldimages,business_email)
        } else {
            val parts:ArrayList<MultipartBody.Part> = ArrayList()
            val ss=images.size
            for (i in 0 until   images.size) {
                val file = File(images[i].path)
                val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
                parts.add(MultipartBody.Part.createFormData("images_$i", file.name, surveyBody))
            }
            val business_name = RequestBody.create(okhttp3.MultipartBody.FORM, bus_name)
            val auth_code = RequestBody.create(okhttp3.MultipartBody.FORM, auth_code)
            val town_id = RequestBody.create(okhttp3.MultipartBody.FORM, town_id)
            val email = RequestBody.create(okhttp3.MultipartBody.FORM, email)
            val category_id = RequestBody.create(okhttp3.MultipartBody.FORM, category_id)
            val image_status = RequestBody.create(okhttp3.MultipartBody.FORM, image_status)
            val video_link = RequestBody.create(okhttp3.MultipartBody.FORM, video_link)
            val opening_hours = RequestBody.create(okhttp3.MultipartBody.FORM, opening_hours)
            val image_count = RequestBody.create(okhttp3.MultipartBody.FORM, image_count)
            val address = RequestBody.create(okhttp3.MultipartBody.FORM, address)
            val contact_no = RequestBody.create(okhttp3.MultipartBody.FORM, contact_no)
            val lat = RequestBody.create(okhttp3.MultipartBody.FORM, lat)
            val long = RequestBody.create(okhttp3.MultipartBody.FORM, long)
            val bus_desc = RequestBody.create(okhttp3.MultipartBody.FORM, bus_desc)
            val oldimages = RequestBody.create(okhttp3.MultipartBody.FORM, oldimages)
            val business_email = RequestBody.create(okhttp3.MultipartBody.FORM, business_email)

            call = service.updateBusinessPage(business_name,auth_code, town_id,email
                    , category_id, parts, image_status, video_link, opening_hours, image_count,
                    address, contact_no, lat, long, oldimages, bus_desc,business_email)
        }
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            //  val data = `object`.optJSONArray("data")
                            val msg = `object`.optString("msg")
                            val busId = `object`.optString("business_id")
                            view.showMessage(msg,busId)
                            view.saveBusId(busId)
                            view.saveMainCat(category_id)
                        }else if (status.equals("false", ignoreCase = true)){
                            if (res.contains("action")){
                                view.logOut()
                            }
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



    private var requestBody: MultipartBody? = null
    lateinit var call: Call<ResponseBody>
    private var cover_image: MultipartBody.Part? = null
    private var allimage: MultipartBody.Part? = null

    private fun updateBusinessPageService(bus_name: String,auth_code: String, town_id: String,email:String,
                                          category_id: String,
                                          image_status: String, video_link: String, opening_hours: String,
                                          image_count: String, address: String, contact_no: String,
                                          images: ArrayList<Uri>, lat: String, long: String, oldimages: String,
                                          bus_desc: String,
                                          coverImage: Uri,
                                          coverImageString: String,
                                          business_email: String) {
        view.showLoader()
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        if (images.size == 0) {
            // call without image
            if (coverImage != null) {


                val bus_name1 = RequestBody.create(MediaType.parse("text/plain"), bus_name)
                val auth_code1 = RequestBody.create(MediaType.parse("text/plain"), auth_code)
                val town_id1 = RequestBody.create(MediaType.parse("text/plain"), town_id)
                val email1 = RequestBody.create(MediaType.parse("text/plain"), email)
                val category_id1 = RequestBody.create(MediaType.parse("text/plain"), category_id)
                val image_status1 = RequestBody.create(MediaType.parse("text/plain"), image_status)
                val video_link1 = RequestBody.create(MediaType.parse("text/plain"), video_link)
                val opening_hours1 = RequestBody.create(MediaType.parse("text/plain"), opening_hours)
                val image_count1 = RequestBody.create(MediaType.parse("text/plain"), image_count)
                val contact_no1 = RequestBody.create(MediaType.parse("text/plain"), contact_no)
                val lat1 = RequestBody.create(MediaType.parse("text/plain"), lat)
                val long1 = RequestBody.create(MediaType.parse("text/plain"), long)
                val bus_desc1 = RequestBody.create(MediaType.parse("text/plain"), bus_desc)
                val oldimages1 = RequestBody.create(MediaType.parse("text/plain"), oldimages)
                val business_email1 = RequestBody.create(MediaType.parse("text/plain"), business_email)
                //val cover_image = RequestBody.create(MediaType.parse("text/plain"), cover_image)
                val address1 = RequestBody.create(MediaType.parse("text/plain"), address)



                file = File(coverImageString)
                if (file != null) {
                    requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                }

                if (requestFile != null) {
                    cover_image = MultipartBody.Part.createFormData("cover_image", "cover_image", requestFile)

                }


                call = service.updateBusinessPage2(bus_name1,auth_code1, town_id1,email1
                        , category_id1, image_status1, video_link1, opening_hours1, image_count1,
                        address1, contact_no1, lat1, long1, bus_desc1, oldimages1,business_email1, cover_image)

            } else {



                call = service.updateBusinessPage3(bus_name,auth_code, town_id,email
                        , category_id, image_status, video_link, opening_hours, image_count,
                        address, contact_no, lat, long, bus_desc, oldimages,business_email)
            }


        } else {
            val parts:ArrayList<MultipartBody.Part> = ArrayList()
            val ss=images.size
            for (i in 0 until   images.size) {
                val file = File(images[i].path)
                val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
                parts.add(MultipartBody.Part.createFormData("images_$i", file.name, surveyBody))
            }
            val business_name1 = RequestBody.create(okhttp3.MultipartBody.FORM, bus_name)
            val auth_code1 = RequestBody.create(okhttp3.MultipartBody.FORM, auth_code)
            val town_id1 = RequestBody.create(okhttp3.MultipartBody.FORM, town_id)
            val email1 = RequestBody.create(okhttp3.MultipartBody.FORM, email)
            val category_id1 = RequestBody.create(okhttp3.MultipartBody.FORM, category_id)
            val image_status1 = RequestBody.create(okhttp3.MultipartBody.FORM, image_status)
            val video_link1 = RequestBody.create(okhttp3.MultipartBody.FORM, video_link)
            val opening_hours1 = RequestBody.create(okhttp3.MultipartBody.FORM, opening_hours)
            val image_count1 = RequestBody.create(okhttp3.MultipartBody.FORM, image_count)
            val address1 = RequestBody.create(okhttp3.MultipartBody.FORM, address)
            val contact_no1 = RequestBody.create(okhttp3.MultipartBody.FORM, contact_no)
            val lat1 = RequestBody.create(okhttp3.MultipartBody.FORM, lat)
            val long1 = RequestBody.create(okhttp3.MultipartBody.FORM, long)
            val bus_desc1 = RequestBody.create(okhttp3.MultipartBody.FORM, bus_desc)
            val oldimages1 = RequestBody.create(okhttp3.MultipartBody.FORM, oldimages)
            val business_email1 = RequestBody.create(okhttp3.MultipartBody.FORM, business_email)

            call = service.updateBusinessPage(business_name1,auth_code1, town_id1,email1
                    , category_id1, parts, image_status1, video_link1, opening_hours1, image_count1,
                    address1, contact_no1, lat1, long1, oldimages1, bus_desc1,business_email1)
        }
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            //  val data = `object`.optJSONArray("data")
                            val msg = `object`.optString("msg")
                            val busId = `object`.optString("business_id")
                            view.showMessage(msg, busId)
                            view.saveBusId(busId)
                            view.saveMainCat(category_id)
                        }else if (status.equals("false", ignoreCase = true)){
                            if (res.contains("action")){
                                view.logOut()
                            }
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

    fun setCatAndLocData() {
        view.getCatAndLocData()
    }

    fun setPrefilledData() {
        val auth_code = view.getAuthCode()
        val business_id = view.getBusId()
        webService(auth_code, business_id)
        //view.setPreFilledData()
    }


    private var data: JSONArray? = null

    private fun webService(auth_code: String, business_id: String) {
        view.showLoader()
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getSingleBusiness(auth_code, business_id,"true")
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                view.hideLoader()
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            data = `object`.optJSONArray("data")
                            val  end_time=`object`.optString("end_time")
                            view.setBusinessPrefilledData(data.toString(),end_time)
                        } else if(status=="false") {

                            if (res.contains("action")){
                                view.logOut()
                            }
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

    fun setSingleBusinessPrefilledData() {
        view.setSingleBusinessPrefilledData()
    }

    fun uploadCoverImage() {
        view.setCoverImage()
    }



}
