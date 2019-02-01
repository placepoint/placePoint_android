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
import com.google.android.gms.security.ProviderInstaller
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
import javax.net.ssl.SSLContext

class Constants {

    companion object {
        //Live----------------------------

        // const val BASE_URL = "http://34.254.213.227/webservices/data_v1/"
//       const val BASE_URL = "https://www.placepoint.ie/webservices/data_v1/"
//        const val STRIPE_KEY = "pk_live_kt0mTsSnlapCVN44Ilfy7snQ"

        //"https://www.placepoint.ie/webservices/data_v1/"
        //=================================

        //Test---------------------------
        //  const val BASE_URL = "http://cloudart.com.au/projects/Placepoint/index.php/webservices/data_v1/"
        const val BASE_URL = "https://www.placepoint.ie/webservices/data_v1/"
        const val STRIPE_KEY = "pk_test_IWmxeaTtErjZDGj3Dcu2oJw0"


        var isAppOpenedFirstTime = false
        const val TOKEN = "token"
        const val DEVICE_TYPE = "Android"
        val closedList: ArrayList<Boolean> = arrayListOf()
        const val PRIVACY_URL = "https://www.placepoint.ie/privacypolicy"
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
        const val CATEGORY_IDSUB: String = "busListCat"
        const val MYBUSINESS_ID: String = "mybusId"


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

                        // item.setShiftingMode(false)
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
            return Settings.Secure.getString(context.contentResolver,
                    Settings.Secure.ANDROID_ID)
        }

        //--------------------------------------------------------------------------
        fun getBusinessData(data: String): java.util.ArrayList<ModelBusiness>? {
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
        fun getHomeFeedData(data: String): java.util.ArrayList<ModelHome> {
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
            var str = ""

            try {
                date = inputFormat.parse(updated_at)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }

        //-----------------------------------------------------------------------------
        //-------------------------------------------------------------------------
        @SuppressLint("SimpleDateFormat")
        fun getDate2(updated_at: String): String {
            val inputPattern = "yyyy-MM-dd h:mm:ss"
            val outputPattern = "MMM dd, h:mm a"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str = ""

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

        fun shareOnFaceBook(text: String, link: String, context: Context) {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, link)
            var facebookAppFound = false
            val matches = context.packageManager.queryIntentActivities(intent, 0)
            for (info in matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                    //  if (info.activityInfo.packageName.toLowerCase().equals("com.facebook.katana")) {
                    intent.setPackage(info.activityInfo.packageName)
                    facebookAppFound = true
                    break
                }
            }
            if (!facebookAppFound) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(link)
                context.startActivity(i)
                return
                // Toast.makeText(context, "Facebook app not found.", Toast.LENGTH_LONG).show()
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
                showUpGradePopup(context)
            })
                    .setNegativeButton("Cancel ", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })

            val alert = dialog.create()
            alert.show()
        }

        private fun showUpGradePopup(context: Context) {
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
            when (checkedIdIs) {
                "3" -> radioGroup.check(R.id.free)
                "2" -> radioGroup.check(R.id.standard)
                else -> radioGroup.check(R.id.premium)
            }


            radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->

                Constants.getPrefs(context)?.edit()?.remove(Constants.MAIN_CATEGORY)?.apply()
                value = when (checkedId) {
                    R.id.free -> 3
                    R.id.standard -> 2
                    else -> 1
                }
                Constants.getPrefs(context)!!.edit().putString(Constants.USERTYPE, value.toString()).apply()
            })
            dialogBuilder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->

                dialog.dismiss()
            })

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

        }

        @SuppressLint("InflateParams")
        fun showPopup(context: Context) {
            val builder = AlertDialog.Builder(context);
            val inflater = context.layoutInflater
            val dialogView = inflater.inflate(R.layout.subscription_popup, null)
            builder.setView(dialogView)
            val closeBtn = dialogView.findViewById(R.id.upgrade) as Button
            val dialog = builder.create()

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

        @SuppressLint("SimpleDateFormat")
        fun findingOpenWhen(modelBusiness: ModelBusiness, dayValue: Int): String {
            var weekDay = -1
            var checkday = dayValue
            val openingDayTime = ArrayList<String>()
            val array = modelBusiness.opening_time
            val arr = JSONArray(array)
            for (dd in 0 until 7) {
                if (dd <= arr.length()) {
                    val jsonObject = arr.getJSONObject(dd)
                    val startFrom = jsonObject.optString("startFrom")
                    openingDayTime.add(startFrom)
                }
            }

            try {
                val currentTime = getCurrentTime()
                val todayOpeningTime = arr.getJSONObject(dayValue)
                val todayStartTime = todayOpeningTime.optString("startFrom")
                if (todayStartTime.contains("AM")) {
                    todayStartTime.replace("AM", "am")
                } else if (todayStartTime.contains("PM")) {
                    todayStartTime.replace("PM", "pm")
                }
                val sdf = SimpleDateFormat("hh:mm a")
                val start = sdf.parse(currentTime)
                val end = sdf.parse(todayStartTime)

                checkday = if (start.after(end)) {
                    dayValue + 1
                } else {
                    dayValue
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var openingtime = ""
            outer@ for (i in 0 until 7) {
                if (checkday > 6) {
                    checkday = 0
                }
                if (checkday <= 6) {
                    val jsonObject = arr.getJSONObject(checkday)
                    val startFrom = jsonObject.optString("startFrom")
                    val startTo = jsonObject.optString("startTo")
                    if (startFrom == "12:00 AM" && startTo == "12:00 AM") {
                        openingtime = ""
                    } else {
                        if (checkday == 0) {
                            openingtime = "$startFrom Monday"
                        }
                        if (checkday == 1) {
                            openingtime = "$startFrom Tuesday"
                        }
                        if (checkday == 2) {
                            openingtime = "$startFrom Wednesday"
                        }
                        if (checkday == 3) {
                            openingtime = "$startFrom Thursday"
                        }
                        if (checkday == 4) {
                            openingtime = "$startFrom Friday"
                        }
                        if (checkday == 5) {
                            openingtime = "$startFrom Saturday"
                        }
                        if (checkday == 6) {
                            openingtime = "$startFrom Sunday"
                        }
                        break@outer
                    }
                    checkday++
                }

            }
            return if (openingtime.isEmpty()) {
                ""
            } else {
                openingtime
            }
            return ""

        }

        @SuppressLint("SimpleDateFormat")
        fun getCurrentTime(): String {
            val sdf = SimpleDateFormat("hh:mm a")
            return sdf.format(Date())
        }

        @SuppressLint("SimpleDateFormat")
        fun findingOpenWhen2(modelBusiness: SingleBusinessModel, dayValue1: Int): String {

            var checkday = dayValue1
            var weekDay = -1
            val openingDayTime = ArrayList<String>()
            val array = modelBusiness.opening_time
            val arr = JSONArray(array)
            for (dd in 0 until 7) {
                if (dd <= arr.length()) {
                    val jsonObject = arr.getJSONObject(dd)
                    val startFrom = jsonObject.optString("startFrom")
                    openingDayTime.add(startFrom)
                }
            }
            try {
                val currentTime = getCurrentTime()
                val todayOpeningTime = arr.getJSONObject(dayValue1)
                val todayStartTime = todayOpeningTime.optString("startFrom")
                if (todayStartTime.contains("AM")) {
                    todayStartTime.replace("AM", "am")
                } else if (todayStartTime.contains("PM")) {
                    todayStartTime.replace("PM", "pm")
                }
                val sdf = SimpleDateFormat("hh:mm a")
                val start = sdf.parse(currentTime)
                val end = sdf.parse(todayStartTime)

                checkday = if (start.after(end)) {
                    dayValue1 + 1
                } else {
                    dayValue1
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            var openingtime = ""
            outer@ for (i in 0 until 7) {
                if (checkday > 6) {
                    checkday = 0
                }
                if (checkday <= 6) {
                    val jsonObject = arr.getJSONObject(checkday)
                    val startFrom = jsonObject.optString("startFrom")
                    val startTo = jsonObject.optString("startTo")
                    if (startFrom == "12:00 AM" && startTo == "12:00 AM") {
                        openingtime = ""
                    } else {
                        weekDay = checkday
                        if (checkday == 0) {
                            openingtime = "$startFrom Monday"
                        }
                        if (checkday == 1) {
                            openingtime = "$startFrom Tuesday"
                        }
                        if (checkday == 2) {
                            openingtime = "$startFrom Wednesday"
                        }
                        if (checkday == 3) {
                            openingtime = "$startFrom Thursday"
                        }
                        if (checkday == 4) {
                            openingtime = "$startFrom Friday"
                        }
                        if (checkday == 5) {
                            openingtime = "$startFrom Saturday"
                        }
                        if (checkday == 6) {
                            openingtime = "$startFrom Sunday"
                        }
                        break@outer
                    }
                    checkday++
                }

            }
            return if (openingtime.isEmpty()) {
                ""
            } else {
                openingtime
            }
            return ""
        }

        fun findDistanceFromCurrentPosition(currentLatitude: Double, currentLongitude: Double, toDouble: Double, toDouble1: Double): Double {

            return findDistance(currentLatitude, currentLongitude
                    , toDouble, toDouble1)
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

        fun getSSlCertificate(context: Context) {
            try {
                ProviderInstaller.installIfNeeded(context)
                val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
                sslContext.init(null, null, null)
                sslContext.createSSLEngine()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}