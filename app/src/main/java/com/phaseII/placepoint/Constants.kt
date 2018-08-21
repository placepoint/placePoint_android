package com.phaseII.placepoint

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.location.Location
import android.net.Uri
import android.os.StrictMode
import android.provider.Settings
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.phaseII.placepoint.AboutBusiness.SingleBusinessModel
import com.phaseII.placepoint.Business.ScheduledPost.ModelSchdule
import com.phaseII.placepoint.Home.BusinessListing.ModelBusiness
import com.phaseII.placepoint.Home.ModelHome
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.Payment.PaymentActivity
import com.phaseII.placepoint.SubscriptionPlan.SubscriptionActivity
import com.phaseII.placepoint.Town.ModelTown
import com.squareup.otto.Bus
import com.squareup.otto.ThreadEnforcer
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.jetbrains.anko.layoutInflater
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Constants {

    companion object {
        //Live----------------------------

        const val BASE_URL = "http://34.254.213.227/webservices/data_v1/"
        const val STRIPE_KEY = "pk_live_kt0mTsSnlapCVN44Ilfy7snQ"


        //=================================

        //Test---------------------------
//       const val BASE_URL = "http://cloudart.com.au/projects/Placepoint/index.php/webservices/data_v1/"
//       const val STRIPE_KEY = "pk_test_IWmxeaTtErjZDGj3Dcu2oJw0"


        const val TOKEN = "token"
        const val DEVICE_TYPE = "Android"
        val closedList: ArrayList<Boolean> = arrayListOf()


        const val PRIVACY_URL = "http://www.placepoint.ie/privacypolicy.html"

        const val OPEN_TOWN: String = "open_town"
        const val OPEN_CAT: String = "open_cat"
        const val TOWN_NAME: String = "town_name"
        const val PREFILL_TOWN: String = "prefill_town"
        const val PREFILL_CAT: String = "prefill_cat"
        const val CATEGORY_LIST: String = "category_list"
        const val LOCATION_LIST: String = "location_list"
        const val BUSINESS_LIST: String = "business_list"
        const val SINGLE_BUSINESS_LIST: String = "single_business_list"
        val LOCATION_ACCESS: String = "access"
        private var retrofit: Retrofit? = null

        private var bus: Bus? = null
        val LOGIN: String = "login"
        const val ID = "id"
        const val AUTH_CODE = "auth_code"
        const val DEVICE_ID = "device_id"
        const val BUSINESS_ID = "business_id"
        const val CATEGORY_NAME = "category_name"
        const val BUSINESS_NAME = "business_name"
        const val CATEGORY = "category"
        const val CATEGORY_IDS = "category_id"
        const val MAIN_CATEGORY = "main_category"
        const val TOWN_ID = "town_id"
        const val TOWN_ID2 = "town_id2"
        const val EMAIL: String = "email"
        const val LOGGED_IN: String = "loggedIn"
        const val PASSWORD: String = "password"
        const val YOUTUBE_API_KEY = "AIzaSyApStcfBPK4d_PGwDYJIaQ47niGcRE9c3Q"

        const val LOGGED: String = "LoggedInTheApp"
        const val CATEGORY_NAMES: String = "catnames"
        const val CATEGORY_NAMEO: String = "catnameso"
        const val ADDPOST_TYPE: String = "addposttype"
        const val ADDPOST_DAY: String = "addpostday"
        const val ADDPOST_TIME: String = "addposttime"
        const val ADDPOST_NOW_STATUS: String = "addpostnowstatus"
        const val SHOW_ALL_POST: String = "showallposts"
        const val ADDPOST_CATEGORY: String = "addpostcategory"
        const val CATEGORY_NAMES_ADDPOST: String = "addpostcategoryname"
        const val FROMINTENT: String = "fromtoaboutbusinessactivity"
        const val STOPCLICK: String = "stopclick"
        const val BUSINESS_ID_MAIN: String = "business_id_main"
        const val TOWN_NAMEPROFILE: String = "townnameprofile"
        const val MY_BUSINESS_NAME: String = "mybusinessName"
        const val TAXI_TOWNID: String = "taxiTownId"
        const val TAXI_SUB_ID: String = "taxisubId"
        const val USERTYPE: String = "usertype"


        fun getBus(): Bus {
            if (bus == null) {
                bus = Bus(ThreadEnforcer.ANY)
            }
            return bus as Bus
        }

        fun getWebClient(): Retrofit? {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
            var client = OkHttpClient()
            client = builder.build()
            if (retrofit == null) {
                retrofit = Retrofit.Builder().client(client).baseUrl(Constants.BASE_URL).build()
            }
            return retrofit
        }

        fun getPrefs(context: Context): SharedPreferences? {
            try {
                return context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        //-----Disable Dashboard bottom navigation scroll----------------
        @SuppressLint("RestrictedApi")
        fun disableShiftMode(view: BottomNavigationView) {
            val menuView = view.getChildAt(0) as BottomNavigationMenuView
            try {
                val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
                shiftingMode.isAccessible = true
                shiftingMode.setBoolean(menuView, false)
                shiftingMode.isAccessible = false
                if (menuView.childCount < 6) {
                    for (i in 0 until menuView.childCount) {
                        val item = menuView.getChildAt(i) as BottomNavigationItemView

                        item.setShiftingMode(false)
                        // set once again checked value, so view will be updated

                        item.setChecked(item.itemData.isChecked)
                    }
                }
            } catch (e: NoSuchFieldException) {
                Log.e("BNVHelper", "Unable to get shift mode field", e)
            } catch (e: IllegalAccessException) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e)
            }

        }

        //-------------------------------------
        fun getTownData(data: String): java.util.ArrayList<ModelTown>? {
            val listType = object : TypeToken<List<ModelTown>>() {
            }.type
            return getGsonObject().fromJson(data, listType)
        }

        //---------------------------------------------------------------------
        fun getGsonObject(): Gson {
            return Gson()
        }

        //---------------------------------------------------------------------
        fun getCategoryData(data: String): java.util.ArrayList<ModelCategoryData>? {
            val listType = object : TypeToken<List<ModelCategoryData>>() {
            }.type
            return getGsonObject().fromJson(data, listType)
        }

        //-------------------------------------------------------------------------
        @SuppressLint("HardwareIds")
        fun getDeviceId(context: Context): String {
            val deviceId = Settings.Secure.getString(context.contentResolver,
                    Settings.Secure.ANDROID_ID)
            return deviceId
        }

        //--------------------------------------------------------------------------
        fun getbusinessData(data: String): java.util.ArrayList<ModelBusiness>? {
            val listType = object : TypeToken<List<ModelBusiness>>() {
            }.type
            return getGsonObject().fromJson(data, listType)
        }

        //------------Service to Get Updated Town and Category from BackEnd------------------
        fun getAppDataService(context: Context) {
            val retrofit = Constants.getWebClient()
            val service = retrofit!!.create(Service::class.java)
            val call: Call<ResponseBody> = service.getAppData()
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        try {
                            val res = response.body()!!.string()
                            val `object` = JSONObject(res)
                            val status = `object`.optString("status")
                            if (status.equals("true", ignoreCase = true)) {
                                val category = `object`.optJSONArray("category")

                                val location = `object`.optJSONArray("location")
                                Constants.getPrefs(context)?.edit()?.putString(Constants.CATEGORY_LIST, category.toString())?.apply()
                                Constants.getPrefs(context)?.edit()?.putString(Constants.LOCATION_LIST, location.toString())?.apply()

                            }

                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {


                }
            })
        }
//-----------------------------------------------------------------------------

        fun getSingleBusinessData(data: String): java.util.ArrayList<SingleBusinessModel>? {
            try {
                val listType = object : TypeToken<List<SingleBusinessModel>>() {
                }.type
                return getGsonObject().fromJson(data, listType)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        //-------------------------------------------------------------------------
        fun getHomeFeedData(data: String): java.util.ArrayList<ModelHome>? {
            val listType = object : TypeToken<List<ModelHome>>() {
            }.type
            return getGsonObject().fromJson(data, listType)
        }

        //-------------------------------------------------------------------------
        @SuppressLint("SimpleDateFormat")
        fun getDate(updated_at: String): String {
            val inputPattern = "yyyy-MM-dd h:mm:ss"
            val outputPattern = "MMM, dd yyyy h:mm a"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String = ""

            try {
                date = inputFormat.parse(updated_at)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }
//-----------------------------------------------------------------------------

        fun getScheduleList(data: String): java.util.ArrayList<ModelSchdule>? {
            try {
                val listType = object : TypeToken<List<ModelSchdule>>() {
                }.type
                return getGsonObject().fromJson(data, listType)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun sharePost(text: String, link: String, context: Context) {

            val myUri = Uri.parse(link)
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

            val shareIntent = Intent(android.content.Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, myUri)
            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
            context.startActivity(Intent.createChooser(shareIntent, "Share Post"))
        }

        fun ShareOnFaceBookk(text: String, link: String, context: Context) {
//            val myUri = Uri.parse(link)
//            val builder = StrictMode.VmPolicy.Builder()
//            StrictMode.setVmPolicy(builder.build())
//
//            val shareIntent = Intent(android.content.Intent.ACTION_SEND)
//            shareIntent.type = "image/*"
//            shareIntent.putExtra(Intent.EXTRA_STREAM, myUri)
//            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
//            context.startActivity(Intent.createChooser(shareIntent, "Share Post"))


            var intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain");
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
            intent.putExtra(Intent.EXTRA_TEXT, link)

// See if official Facebook app is found
            var facebookAppFound = false;
            var matches = context.getPackageManager().queryIntentActivities(intent, 0);
            for (info in matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                    intent.setPackage(info.activityInfo.packageName);
                    facebookAppFound = true;
                    break;
                }
            }

// As fallback, launch sharer.php in a browser
            if (!facebookAppFound) {
//    var  sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
//    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                Toast.makeText(context, "Facebook app not found.", Toast.LENGTH_LONG).show()
            }

            context.startActivity(intent)
        }

        fun hideKeyBoard(context: Context, view: View) {
            val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        }

        fun subscriptionDialog(context: Context) {

            val dialog = AlertDialog.Builder(context)
            dialog.setCancelable(false)
            dialog.setTitle("Alert")
            dialog.setMessage("You are a free user and can only choose one category. Please upgrade your plan to choose more categories.")
            dialog.setPositiveButton("Upgrade", DialogInterface.OnClickListener { dialog, id ->
                showUpgradePopup(context)
            })
                    .setNegativeButton("Cancel ", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })

            val alert = dialog.create()
            alert.show()
        }

        fun showUpgradePopup(context: Context) {
            val dialogBuilder = AlertDialog.Builder(context)
// ...Irrelevant code for customizing the buttons and title
            dialogBuilder.setTitle("Choose Subscription Plan")
            val inflater = context.layoutInflater

            val dialogView = inflater.inflate(R.layout.user_type, null)
            dialogBuilder.setView(dialogView)
            dialogBuilder.setCancelable(false)
            var value = 0
            val radioGroup = dialogView.findViewById(R.id.radioGroup) as RadioGroup
            val checkedIdIs = Constants.getPrefs(context)!!.getString(Constants.USERTYPE, "1")
            if (checkedIdIs == "3") {
                radioGroup.check(R.id.free)
            } else if (checkedIdIs == "2") {
                radioGroup.check(R.id.standard)
            } else {
                radioGroup.check(R.id.premium)
            }


            radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->

                Constants.getPrefs(context)?.edit()?.remove(Constants.MAIN_CATEGORY)?.apply()
                if (checkedId == R.id.free) {
                    value = 3
                } else if (checkedId == R.id.standard) {
                    value = 2
                } else {
                    value = 1
                }
                Constants.getPrefs(context)!!.edit().putString(Constants.USERTYPE, value.toString()).apply()
            })
            dialogBuilder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->

                dialog.dismiss()
            })

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

        }

        fun showPopup(context: Context) {
            var builder = AlertDialog.Builder(context);
            var inflater = context.layoutInflater
            var dialogView = inflater.inflate(R.layout.subscription_popup, null)
            builder.setView(dialogView)
            var closeBtn = dialogView.findViewById(R.id.upgrade) as Button
            var dialog = builder.create()

            closeBtn.setOnClickListener {
                val intent = Intent(context, SubscriptionActivity::class.java)
                context.startActivity(intent)
                dialog.dismiss()
            }

            dialog.show()
        }

        fun getPackageDetails(auth_code: String, currenttype: String, upgrade_type: String, progress: ProgressBar, context: Context) {

            progress.visibility = View.VISIBLE

            val retrofit = Constants.getWebClient()
            val service = retrofit!!.create(Service::class.java)
            val call: Call<ResponseBody> = service.getPackageDetails(auth_code, currenttype, upgrade_type)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        progress.visibility = View.GONE
                        try {
                            val res = response.body()!!.string()
                            val `object` = JSONObject(res)
                            val status = `object`.optString("status")
                            if (status.equals("true", ignoreCase = true)) {
                                // {"status":"true","amount_payable":730}
                                val amount_payable = `object`.optString("amount_payable")
                                val intent = Intent(context, PaymentActivity::class.java)
                                intent.putExtra("amount_payable", amount_payable)
                                intent.putExtra("currenttype", currenttype)
                                intent.putExtra("upgrade_type", upgrade_type)
                                intent.putExtra("type", upgrade_type)

                                intent.putExtra("from", "payment")
                                context.startActivity(intent)

                            }

                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progress.visibility = View.GONE

                }
            })
        }

        fun findingOpenWhen(modelBusiness: ModelBusiness, dayValue: Int): String {
            var weekDay=-1
            var checkday=dayValue
            var opeiningDayTime = ArrayList<String>()
            val array = modelBusiness.opening_time
            val arr = JSONArray(array)
            for (dd in 0 until 7) {
                if (dd <= arr.length()) {
                    var jsonObject = arr.getJSONObject(dd)
                    var startFrom = jsonObject.optString("startFrom")
                    opeiningDayTime.add(startFrom)
                }
            }

            try {
                var currentTime = getCurrentTime()
                var todayOpeningTime = arr.getJSONObject(dayValue)
                var todayStartTime = todayOpeningTime.optString("startFrom")
                if (todayStartTime.contains("AM")) {
                    todayStartTime.replace("AM", "am")
                } else if (todayStartTime.contains("PM")) {
                    todayStartTime.replace("PM", "pm")
                }
                val sdf = SimpleDateFormat("hh:mm a")
                val start = sdf.parse(currentTime)
                val end = sdf.parse(todayStartTime)

                if (start.after(end)) {
                    checkday = dayValue + 1
                } else {
                    checkday = dayValue
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

            var openingtime = ""
            outer@ for (i in 0 until 7) {
                if (checkday > 6) {
                    checkday = 0
                }
                if (checkday <= 6) {
                    var jsonObject = arr.getJSONObject(checkday)
                    var startFrom = jsonObject.optString("startFrom")
                    var startTo = jsonObject.optString("startTo")
                    if (startFrom == "12:00 AM"&&startTo == "12:00 AM") {
                        openingtime = ""
//                        break@outer
                    }else{
                        if (checkday==0){
                            openingtime = "$startFrom Monday"
                        }
                        if (checkday==1){
                            openingtime = "$startFrom Tuesday"
                        }
                        if (checkday==2){
                            openingtime = "$startFrom Wednesday"
                        }
                        if (checkday==3){
                            openingtime = "$startFrom Thursday"
                        }
                        if (checkday==4){
                            openingtime = "$startFrom Friday"
                        }
                        if (checkday==5){
                            openingtime = "$startFrom Saturday"
                        }
                        if (checkday==6){
                            openingtime = "$startFrom Sunday"
                        }
                        break@outer
                    }
                    checkday++
                }

            }
            if (openingtime.isEmpty()) {
                return ""
            } else {
                return openingtime
            }
            return ""

        }

         fun getCurrentTime(): String {
            val sdf = SimpleDateFormat("hh:mm a")
            return sdf.format(Date())
        }

        fun findingOpenWhen2(modelBusiness: SingleBusinessModel, dayValue1: Int): String {

            var checkday=dayValue1
            var weekDay=-1
            var opeiningDayTime = ArrayList<String>()
            val array = modelBusiness.opening_time
            val arr = JSONArray(array)
            for (dd in 0 until 7) {
                if (dd <= arr.length()) {
                    var jsonObject = arr.getJSONObject(dd)
                    var startFrom = jsonObject.optString("startFrom")
                    opeiningDayTime.add(startFrom)
                }
            }
            try {
                var currentTime = getCurrentTime()
                var todayOpeningTime = arr.getJSONObject(dayValue1)
                var todayStartTime = todayOpeningTime.optString("startFrom")
                if (todayStartTime.contains("AM")) {
                    todayStartTime.replace("AM", "am")
                } else if (todayStartTime.contains("PM")) {
                    todayStartTime.replace("PM", "pm")
                }
                val sdf = SimpleDateFormat("hh:mm a")
                val start = sdf.parse(currentTime)
                val end = sdf.parse(todayStartTime)

                if (start.after(end)) {
                    checkday = dayValue1 + 1
                } else {
                    checkday = dayValue1
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
            var openingtime = ""
            outer@ for (i in 0 until 7) {
                if (checkday > 6) {
                    checkday = 0
                }
                if (checkday <= 6) {
                    var jsonObject = arr.getJSONObject(checkday)
                    var startFrom = jsonObject.optString("startFrom")
                    var startTo = jsonObject.optString("startTo")
                    if (startFrom == "12:00 AM"&&startTo == "12:00 AM") {
                        openingtime = ""
//                        break@outer
                    }else{
                        weekDay=checkday
                        if (checkday==0){
                            openingtime = "$startFrom Monday"
                        }
                       if (checkday==1){
                            openingtime = "$startFrom Tuesday"
                        }
                       if (checkday==2){
                            openingtime = "$startFrom Wednesday"
                        }
                       if (checkday==3){
                            openingtime = "$startFrom Thursday"
                        }
                       if (checkday==4){
                            openingtime = "$startFrom Friday"
                        }
                       if (checkday==5){
                            openingtime = "$startFrom Saturday"
                        }
                       if (checkday==6){
                            openingtime = "$startFrom Sunday"
                        }
                        break@outer
                    }
                    checkday++
                }

            }
            if (openingtime.isEmpty()) {
                return ""
            } else {
                return openingtime
            }
            return ""
        }

        fun findDistanceFromCurrentPosition(currentLatitude: Double, currentLongitude: Double, toDouble: Double, toDouble1: Double): Double {

           return  findDistance(currentLatitude, currentLongitude
                        ,toDouble, toDouble1)
        }

        private fun findDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val loc1 = Location("")
            loc1.latitude = lat1
            loc1.longitude = lon1

            val loc2 = Location("")
            loc2.latitude = lat2
            loc2.longitude = lon2

            val distanceInMeters = loc1.distanceTo(loc2)
            return (distanceInMeters.toDouble() / 1000)
        }
    }
}