package com.phaseII.placepoint.Business.Profile

import android.net.Uri

interface BusinessProfileHelper {
    fun setRadioGroupListener()
    fun showOpenStartTimePicker(day: String)
    fun setAdapterForCroppedImages(list: ArrayList<Uri>?, croppedImages: java.util.ArrayList<Uri>, preLoadImages: MutableList<String>)
    fun askForGPS()
    fun showOpenEndTimePicker()
    fun showCloseEndTimePicker()
    fun showCloseStartTimePicker()
    fun openCropper(list: java.util.ArrayList<Uri>, s: String)
    fun openCamera()
    fun openGallery()
    fun requestAllPermissions()
    fun setInitialData(timeList1: ArrayList<ModelTime>)
    fun handleClicks()
    fun selectTownPopUp()
    fun selectCatPopUp()
    fun getAuthCode(): String
    fun getTownId(): String
    fun getCategoryId(): String
    fun getVideoLink(): String
    fun getAddress(): String
    fun getContactNo(): String
    fun getOpeningHours(): String
    fun getClosingHours(): String
    fun showLoader()
    fun hideLoader()
    fun showNetworkError(resId: Int)
    fun getMultiPartFiles(): ArrayList<Uri>
    fun showMessage(msg: String?, busId: String)
    fun getCatAndLocData()
    fun showNoImageMessage(s: String)
    fun setPreFilledData()
    fun getLatitude(): String
    fun getLongitude(): String
    fun saveBusId(busId: String?)
    fun setTownError(s: String)
    fun setCatError(s: String)
    fun setContactError(s: String)
    fun setAdressError(s: String)
    fun getAuthCodeConstant(): String?
    fun getTownIdConstant(): String?
    fun getCatId(): String?
    fun setBusinessPrefilledData(data: String, end_time: String, user_type: String)
    fun setSingleBusinessPrefilledData()
    fun getBusId(): String
    fun getPrefillTownId():String
    fun getPrefillCatId():String
    fun getOldMultiPartFiles(): String
    fun saveBusinessName()
    fun getBusName(): String
    fun getTimeArray(): ArrayList<ModelTime>
    fun showMessageErr(s: String)
    fun setDescError(s: String)
    fun getBusDesc(): String
    fun setCoverImage()
    fun getCoverImage(): Uri?
    fun getCoverImageString(): String
    fun getOldCoverImage(): String
    fun saveMainCat(category_id: String)
    fun getEmail(): String
    fun coverImageIssue(s: String)
    fun getFromModel(): String
    fun logOut()
    fun setclickFalse()
    fun setclickTrue()
    fun checkingAllDaysValidation(): Boolean
    fun getUserType(): String

}
