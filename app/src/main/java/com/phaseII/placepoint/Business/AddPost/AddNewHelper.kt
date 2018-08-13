package com.phaseII.placepoint.Business.AddPost

import android.net.Uri
import org.json.JSONObject

/**
 * Created by Loveleen on 7/6/18.
 */
interface AddNewHelper {
    fun openCropper(list: ArrayList<Uri>)
    fun openCamera()
    fun openGallery()
    fun getAuthCode(): String
    fun getDesc(): String
    fun getVideoLink(): String
    fun getImage(): String
    fun showLoader()
    fun hideLoader()
    fun showNetworkError(resId: Int)
    fun showMessage(msg: String?)
    fun getPostText(): String
    fun showEmptyPostMsg()
    fun getBusinessName(): String
    fun saveLocAndCat(data: JSONObject)
    fun getImageWidth(): String
    fun getImageHeight(): String
    fun serviceIsRunning(b: Boolean)
    fun getType(): String
    fun getDay(): String
    fun getTime(): String
    abstract fun getNowStatus(): String
    abstract fun getCategory(): String
    fun clearPrefsall(now_status: String)
    fun showError(s: String)
    fun showScheduleScreen()
    fun getEditTime(): String
    fun getEditDay(): String
    fun getEditType(): String
}