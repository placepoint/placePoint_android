package com.phaseII.placepoint.AboutBusiness.BusinessDetails


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.location.*
import com.phaseII.placepoint.AboutBusiness.*
import com.phaseII.placepoint.BusEvents.TAXI_EVENT
import com.phaseII.placepoint.BusinessDetailMap.BusinessDetailMapActivity
import com.phaseII.placepoint.ConstantClass.GpsTracker
import com.phaseII.placepoint.ConstantClass.Utils
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.BusinessListing.ModelBusiness
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData

import com.phaseII.placepoint.R
import com.phaseII.placepoint.Town.ModelTown
import kotlinx.android.synthetic.main.about_business_scroll.*
import kotlinx.android.synthetic.main.business_description.*
import kotlinx.android.synthetic.main.day_layout.*
import kotlinx.android.synthetic.main.location_text_layout.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailFragment() : Fragment(), AboutBusinessHelper, Parcelable {

    private lateinit var mPresenter: AboutBusinessPresenter
    lateinit var client: GoogleApiClient
    //lateinit var toolbar: Toolbar
    lateinit var Pager: ViewPager
    private lateinit var model: SingleBusinessModel
    private lateinit var selected: String
    private lateinit var lat: String
    private lateinit var long: String
    lateinit var view_pager_indicator: ViewPagerIndicator
    lateinit var horz_recycler: RecyclerView
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var result: PendingResult<LocationSettingsResult>
    private val requestCode: Int = 1
    //private lateinit var mTitle: TextView
    private lateinit var towns: TextView
    private lateinit var subscriptionType: TextView
    private lateinit var openMap: TextView
    private lateinit var categoryText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var emailText: TextView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var noTime: TextView
    private lateinit var distance: TextView
    private lateinit var subscriptionHeading: TextView
    private lateinit var subscripLay: RelativeLayout
    private lateinit var navigationImage: ImageView
    private lateinit var taxiImage: ImageView
    private lateinit var callImage: ImageView
    private lateinit var descLayout: RelativeLayout
    private lateinit var label1: TextView
    private lateinit var label33: TextView
    private lateinit var constraintLayout2: ConstraintLayout
    private lateinit var model1: ModelBusiness
    //  var busId: String = ""
    var cat_list: ArrayList<ModelCategoryData> = arrayListOf()
    private var modelBusiness: ModelBusiness = ModelBusiness()
    lateinit var updateTitle: setTitle

    //List for Days concatenation logic
    private val mainList = ArrayList<ModelMain>()
    private val resultList = ArrayList<ResultModel>()
    lateinit var showHide: String

    @SuppressLint("ValidFragment")
    constructor(parcel: Parcel) : this() {
        selected = parcel.readString()
        lat = parcel.readString()
        long = parcel.readString()
        mLocationRequest = parcel.readParcelable(LocationRequest::class.java.classLoader)
        model1 = parcel.readParcelable(ModelBusiness::class.java.classLoader)
        modelBusiness = parcel.readParcelable(ModelBusiness::class.java.classLoader)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_detail, container, false)
        mPresenter = AboutBusinessPresenter(this)
        showHide = Constants.getPrefs(activity!!)!!.getString("HideContents", "no")
        initialise(view, showHide)

        setClickHandler(view)
        client = GoogleApiClient.Builder(activity!!).addApi(LocationServices.API).build()

        mPresenter.BusinessDetailService(Constants.getPrefs(activity!!)!!.getString(Constants.BUSINESS_ID, ""))
        val email = Constants.getPrefs(activity!!)!!.getString(Constants.EMAIL, "")

        //--------------------------------------------------------------------------
        var usertye = Constants.getPrefs(activity!!)!!.getString("BusinessSubscriptionType", "")
        var utype = ""
        if (usertye == "1") {
            utype = "Premium Package"
        } else if (usertye == "4") {
            utype = "Admin"
        } else {
            utype = "Standard Package"
        }
        subscriptionType.text = utype
        return view
    }

    private fun initialise(view: View, showHide: String) {
        label1 = view.findViewById(R.id.label1)
        noTime = view.findViewById(R.id.noTime)
        descLayout = view.findViewById(R.id.descLayout)
        label33 = view.findViewById(R.id.label33)
        horz_recycler = view.findViewById(R.id.horz_recycler)
        title = view.findViewById(R.id.title)
        description = view.findViewById(R.id.description)
        navigationImage = view.findViewById(R.id.navigationImage)
        taxiImage = view.findViewById(R.id.taxiImage)
        callImage = view.findViewById(R.id.callImage)
        distance = view.findViewById(R.id.distance)
        emailText = view.findViewById(R.id.emailText)
        subscriptionType = view.findViewById(R.id.subscriptionType)
        towns = view.findViewById(R.id.towns)
        openMap = view.findViewById(R.id.openMap)
        progressBar = view.findViewById(R.id.progressBar)
        categoryText = view.findViewById(R.id.categoryText)
        constraintLayout2 = view.findViewById(R.id.constraintLayout2)
        subscriptionHeading = view.findViewById(R.id.subscriptionHeading)
        subscripLay = view.findViewById(R.id.subscripLay)

        if (showHide == "yes") {
            label1.visibility = View.GONE
            descLayout.visibility = View.GONE
            label33.visibility = View.GONE
            horz_recycler.visibility = View.GONE
            subscriptionHeading.visibility = View.GONE
            subscripLay.visibility = View.GONE

        } else {
            label1.visibility = View.VISIBLE
            descLayout.visibility = View.VISIBLE
            label33.visibility = View.VISIBLE
            horz_recycler.visibility = View.VISIBLE
            subscriptionHeading.visibility = View.VISIBLE
            subscripLay.visibility = View.VISIBLE
        }

        var catTaxi = Constants.getPrefs(activity!!)!!.getString("nameCat", "vv")
        if (catTaxi.equals("TaxiRelatedData") || catTaxi.equals("Taxi")) {
            taxiImage.visibility=View.GONE
            navigationImage.visibility=View.GONE
        }else{
            taxiImage.visibility=View.VISIBLE
            navigationImage.visibility=View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        Constants.getSSlCertificate(activity!!)
        try {

            Constants.getBus().register(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (ActivityCompat.checkSelfPermission(activity!!,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!! as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setClickHandler(view: View) {

        callImage.setOnClickListener {
            showDialog(Constants.getPrefs(activity!!)!!.getString("mobNumber", ""))
        }
        taxiImage.setOnClickListener {
            //Constants.getBus().post(TAXI_EVENT("taxi"))
            Constants.getPrefs(activity!!)!!.edit().putString("showTaxiAtHome", "yes").apply()
            activity!!.finish()
        }
        navigationImage.setOnClickListener {
            var gmmIntentUri = Uri.parse("google.navigation:q=${Constants.getPrefs(activity!!)!!.getString("lati", "")},${Constants.getPrefs(activity!!)!!.getString("longi", "")}")
            var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            activity!!.startActivity(mapIntent)
        }
        openMap.setOnClickListener {
            val tracker = GpsTracker(activity)
            if (tracker.canGetLocation()) {
                if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    val utils = Utils()
                    utils.getPermission(Manifest.permission.ACCESS_FINE_LOCATION, activity!!, requestCode)
                } else {
                    val intent = Intent(activity, BusinessDetailMapActivity::class.java)
                    intent.putExtra("lat", lat)
                    intent.putExtra("long", long)
                    startActivityForResult(intent, requestCode)
                }
            }
        }
    }

    private fun showDialog(phoneNo: String?) {
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setCancelable(false)
        dialog.setTitle("Make a call")
        dialog.setMessage(phoneNo)
        dialog.setPositiveButton("Call", DialogInterface.OnClickListener { dialog, id ->

            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNo")
            if (ActivityCompat.checkSelfPermission(activity!!,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return@OnClickListener
            }

            activity!!.startActivity(callIntent)
        })
                .setNegativeButton("Cancel ", DialogInterface.OnClickListener { dialog, which ->
                    //Action for "Cancel".
                })

        val alert = dialog.create()
        alert.show()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            try {
                if (ActivityCompat.checkSelfPermission(activity!!, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.askForGPSPermission()
                    val editor = Constants.getPrefs(activity!!)?.edit()
                    editor?.putBoolean(Constants.LOCATION_ACCESS, true)
                    editor?.apply()
                    startActivityForResult(Intent(activity, BusinessDetailMapActivity::class.java), requestCode)
                } else {
                    val editor = Constants.getPrefs(activity!!)?.edit()
                    editor?.putBoolean(Constants.LOCATION_ACCESS, false)
                    editor?.apply()
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            } catch (ignored: Exception) {
            }
        }
    }


    override fun askForGPS() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = (30 * 1000).toLong()
        mLocationRequest.fastestInterval = (5 * 1000).toLong()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        builder.setAlwaysShow(true)
        result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(activity, 0x7)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }

    override fun setData(list: ModelBusiness) {
        /* model1 = list
         setPager(model1)
         mTitle.text = model1.business_name
         addresss.text = model1.address*/

    }

    private fun setPager(model: SingleBusinessModel) {
        val arraylist = model.image_url.toMutableList()

        if (arraylist != null) {
            if (arraylist.size > 0) {
                val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                horz_recycler.layoutManager = linearLayoutManager
                horz_recycler.adapter = BusinessHorzRecyclerAdapter(activity!!, arraylist as ArrayList<String>)
            }
        }

    }

    override fun getAuthCode(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.AUTH_CODE, "")!!
    }

    override fun getBusId(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.BUSINESS_ID, "")!!

    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun showNetworkError(resId: Int) {
        Toast.makeText(activity, getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun setBusinessData(data: String) {
        Constants.getPrefs(activity!!)?.edit()?.putString(Constants.SINGLE_BUSINESS_LIST, data)?.apply()
        mPresenter.setSingleBusinessData()
    }

    override fun setAdapter(modelBusiness: ModelBusiness) {
        mPresenter.setSingleBusinessData()
    }

    override fun getIfLoggedIn(): String {
        return Constants.getPrefs(activity!!)!!.getString(Constants.LOGGED_IN, "false")
    }

    override fun setSingleBusinessData() {
        val data = Constants.getPrefs(activity!!)?.getString(Constants.SINGLE_BUSINESS_LIST, "")!!
        val list = Constants.getSingleBusinessData(data)

        try {

            if (list != null) {
                for (i in 0 until list.size) {
                    model = list[i]
                    constraintLayout2.visibility = View.VISIBLE

                    Constants.getPrefs(activity!!)!!.edit().putString("mobNumber", model.contact_no).apply()
                    Constants.getPrefs(activity!!)!!.edit().putString("lati", model.lat).apply()
                    Constants.getPrefs(activity!!)!!.edit().putString("longi", model.long).apply()
                    try {
                        var gps = GpsTracker(this.activity)
                        var distance2 = Constants.findDistanceFromCurrentPosition(gps.getLatitude(), gps.getLongitude()
                                , model.lat.toDouble(), model.long.toDouble())
                        var roundDis = String.format("%.2f", distance2)
                        title.text = "" + roundDis.toString() + " Km away"
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    setPager(model)
                    if (!model.email.isEmpty()) {
                        emailText.visibility = View.VISIBLE
                        emailText.text = model.email
                    } else {
                        emailText.visibility = View.GONE
                    }
                    // ****************Setting selected Town by user**************************
                    try {
                        var list: java.util.ArrayList<SingleBusinessModel>? = arrayListOf()
                        val data = Constants.getPrefs(activity!!)?.getString(Constants.SINGLE_BUSINESS_LIST, "")!!
                        list = Constants.getSingleBusinessData(data)
                        val loc = Constants.getPrefs(activity!!)?.getString(Constants.LOCATION_LIST, "")!!
                        var loc_list: ArrayList<ModelTown> = arrayListOf()
                        loc_list = Constants.getTownData(loc)!!
                        val hashSet2 = HashSet<ModelTown>()
                        hashSet2.addAll(loc_list)
                        loc_list.clear()
                        loc_list.addAll(hashSet2)
                        val townid = model.town_id
                        for (i2 in 0 until loc_list.size) {
                            if (loc_list[i2].id == townid) {
                                towns.text = loc_list[i2].townname
                            }
                        }

                        //Show taxi------------------------
                        try {
                            val taxiTownId = Constants.getPrefs(activity!!)!!.getString(Constants.TAXI_TOWNID, "");
                            val choosenTownId = townid
                            var idList = taxiTownId!!.split(",")

                            if (idList.contains(choosenTownId)) {

                                taxiImage.visibility = View.VISIBLE
                                var catTaxi = Constants.getPrefs(activity!!)!!.getString("nameCat", "vv")
                                if (catTaxi == "TaxiRelatedData" || catTaxi == "Taxi") {
                                    taxiImage.visibility=View.GONE
                                    navigationImage.visibility=View.GONE
                                }
                            } else {
                                taxiImage.visibility = View.GONE
                            }


                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        // ********************Setting selected Categories by user****************

                        val mainCatValue = model.category_id
                        val mainCat = mainCatValue.split(",")
                        val cat = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_LIST, "")!!
                        cat_list = Constants.getCategoryData(cat)!!
                        val arrayname = arrayListOf<String>()
                        if (!mainCatValue.isEmpty()) {
                            val mainCat = mainCatValue.split(",")

                            out@ for (p in 0 until mainCat.size) {
                                inn@ for (q in 0 until cat_list.size) {
                                    if (mainCat[p] == cat_list[q].id) {
                                        arrayname.add(cat_list[q].name)
                                        ///select_category.text = cat_list[q].name
                                        break@inn
                                        break@out
                                    }
                                }
                            }
                        }
                        selected = ""
                        for (a in 0 until arrayname.size) {
                            selected = if (selected.isEmpty()) {
                                arrayname[a]
                            } else {
                                selected + "," + arrayname[a]
                            }
                        }
                        categoryText.text = selected
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    // mTitle.text = model.business_name
                    updateTitle.setTitleBusiness(model.business_name)
                    Constants.getPrefs(activity!!)?.edit()!!.putString(Constants.BUSINESS_NAME, model.business_name).apply()

                    addresss.text = model.address
                    if (!model.contact_no.isEmpty()) {
                        contact.visibility = View.VISIBLE
                        contact.text = model.contact_no
                    } else {
                        contact.visibility = View.GONE
                    }

                    lat = model.lat
                    long = model.long
                    val town_id = model.town_id
                    val bus_desc = model.description
                    desc1.text = bus_desc
                    val cat_id = model.category_id
                    val business_user_id = model.business_user_id
                    try {
                        Glide.with(activity!!)
                                .load(model.cover_image)

                                .apply(RequestOptions()
                                        .centerCrop()
                                        .placeholder(R.mipmap.placeholder))
                                .into(cover)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                    val array = model.opening_time
                    if (array.isEmpty()) {
                        noTime.visibility = View.VISIBLE
                    } else {
                        noTime.visibility = View.GONE
                    }
                    val arr = JSONArray(array)

                    /**
                     * Weekday concatenation logic
                     */
                    val array2 = arrayListOf<DayModel>()
                    for (i in 0 until arr.length()) {
                        val objSat = arr.getJSONObject(i)

                        var model = DayModel()
                        if (i == 0) {
                            model.day = "Mon"
                        } else if (i == 1) {
                            model.day = "Tue"
                        } else if (i == 2) {
                            model.day = "Wed"
                        } else if (i == 3) {
                            model.day = "Thu"
                        } else if (i == 4) {
                            model.day = "Fri"
                        } else if (i == 5) {
                            model.day = "Sat"
                        } else if (i == 6) {
                            model.day = "Sun"
                        }

                        val startTime: String = objSat.optString("startFrom")
                        val endTime: String = objSat.optString("startTo")
                        if (startTime == endTime) {
                            model.time = "closed"
                        } else {
                            model.time = "$startTime to $endTime"
                        }
                        array2.add(model)
                    }


                    for (modelDay in array2) {
                        if (!checkIfAlreadyExist(modelDay.day)) {
                            val subList = getDayTimeString(modelDay, array2)
                            mainList.add(subList)
                        }
                    }
                    val existdays = ArrayList<ResultModel>()
                    for (index in mainList.indices) {
                        val resultString = mainList[index].resultString
                        Log.i("$index : ", resultString.day)
                        existdays.add(resultString)

                    }

                    val modelDummy = ResultModel()
                    modelDummy.time = "Open"
                    modelDummy.day = "Days"
                    existdays.add(0, modelDummy)

                    daysListView.layoutManager = LinearLayoutManager(activity)
                    daysListView.adapter = TimeAdapter(activity!!, existdays)
                    logicForOpenCloseButton(model)
                    val myid = Constants.getPrefs(activity!!)!!.getString(Constants.MYBUSINESS_ID, "-128")
                    if (business_user_id == myid) {
                        subscripLay.visibility = View.VISIBLE
                        subscriptionHeading.visibility = View.VISIBLE
                    } else {
                        subscripLay.visibility = View.GONE
                        subscriptionHeading.visibility = View.GONE
                    }

                }
            }
        } catch (E: Exception) {
            E.printStackTrace()
        }


    }

    private fun logicForOpenCloseButton(model: SingleBusinessModel) {
        val currentDay = getCurrentTimeAndDate()
        val currentTime = getCurrentTime()
        if (currentDay == "Mon") {
            setOpenClosed(0, openStatus, model, currentTime)
        } else if (currentDay == "Tue") {
            setOpenClosed(1, openStatus, model, currentTime)
        } else if (currentDay == "Wed") {
            setOpenClosed(2, openStatus, model, currentTime)
        } else if (currentDay == "Thu") {
            setOpenClosed(3, openStatus, model, currentTime)
        } else if (currentDay == "Fri") {
            setOpenClosed(4, openStatus, model, currentTime)
        } else if (currentDay == "Sat") {
            setOpenClosed(5, openStatus, model, currentTime)
        } else if (currentDay == "Sun") {
            setOpenClosed(6, openStatus, model, currentTime)
        }
    }

    private fun setOpenClosed(dayValue1: Int, timing: TextView, modelBusiness: SingleBusinessModel, currentTime: String) {
        val array = modelBusiness.opening_time
        val arr = JSONArray(array)
        if (dayValue1 == 0) {
            checkPreviousDayClosingHours(arr.getJSONObject(6), currentTime, dayValue1, timing, modelBusiness)
        } else {
            checkPreviousDayClosingHours(arr.getJSONObject(dayValue1 - 1), currentTime, dayValue1, timing, modelBusiness)
        }


    }


    private fun checkPreviousDayClosingHours(jsonObject: JSONObject, currentTime: String, dayValue1: Int, timing: TextView, modelBusiness: SingleBusinessModel) {
        var startTo = jsonObject.optString("startTo")
        var startFrom = jsonObject.optString("startFrom")
        var curr: String = ""
        val inFormat = SimpleDateFormat("hh:mm aa")
        val inFormat1 = SimpleDateFormat("hh:mm:ss aa")
        val outFormat = SimpleDateFormat("HH:mm")

        val time24 = outFormat.format(inFormat.parse(startTo))
        val time245 = outFormat.format(inFormat.parse(startFrom))
        try {
            curr = outFormat.format(inFormat1.parse(currentTime))
        } catch (e: Exception) {
            e.printStackTrace()
        }


        try {
            val start = outFormat.parse(time245)
            val end = outFormat.parse(time24)
            val curr2 = outFormat.parse(curr)
            if (start.after(end)) {

                if (curr2.before(end)) {
                    timing.text = "OPEN NOW"
                    openCloseLay.visibility = View.VISIBLE
                    openCloseLay.setBackgroundResource(R.drawable.background_timing_green)
                } else {
                    checkInTodayTiming(dayValue1, timing, modelBusiness, currentTime)
                }
            }

            else {
                checkInTodayTiming(dayValue1, timing, modelBusiness, currentTime)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    private fun checkInTodayTiming(dayValue1: Int, timing: TextView, modelBusiness: SingleBusinessModel, currentTime: String) {

        val array = modelBusiness.opening_time
        val arr = JSONArray(array)
        val objMon = arr.getJSONObject(dayValue1)
        if (objMon!!.optString("startFrom").equals("closed")) {
            timing.text = "CLOSED"
            val timeForOpening = Constants.findingOpenWhen2(modelBusiness, dayValue1)
            if (!timeForOpening.isEmpty()) {
                openStatusAt.visibility = View.VISIBLE
                openStatusAt.text = "Open $timeForOpening"
            } else {
                openStatusAt.visibility = View.GONE
            }
            openCloseLay.visibility = View.VISIBLE
            openCloseLay.setBackgroundResource(R.drawable.background_timing_red)
        } else {

            if (objMon!!.optString("startFrom").equals("12:00 AM") && objMon!!.optString("startTo").equals("12:00 AM")) {
                timing.text = "CLOSED"
                val timeForOpening = Constants.findingOpenWhen2(modelBusiness, dayValue1)
                if (!timeForOpening.isEmpty()) {
                    openStatusAt.visibility = View.VISIBLE
                    openStatusAt.text = "Open $timeForOpening"
                } else {
                    openStatusAt.visibility = View.GONE
                }
                openCloseLay.visibility = View.VISIBLE
                openCloseLay.setBackgroundResource(R.drawable.background_timing_red)
            } else {
                val exist = checkIfCurrentTimeLiesInGivenInterVal22(currentTime, changeTimeFormat(objMon.optString("startFrom")), "startTime")
                val exist2 = checkIfCurrentTimeLiesInGivenInterVal23(currentTime, changeTimeFormat(objMon.optString("startFrom")), changeTimeFormat(objMon.optString("startTo")), "EndTime")
                val existInClosedTimings = checkIfCurrentTimeLiesInGivenInterVal(currentTime, changeTimeFormat(objMon.optString("closeFrom")), changeTimeFormat(objMon.optString("closeTo")), "closeTime")
                if (exist && exist2) {
                    timing.text = "OPEN NOW"
                    openCloseLay.visibility = View.VISIBLE
                    openCloseLay.setBackgroundResource(R.drawable.background_timing_green)
              } else {
                    timing.text = "CLOSED"

                    openCloseLay.visibility = View.VISIBLE
                    val timeForOpening = Constants.findingOpenWhen2(modelBusiness, dayValue1)
                    if (!timeForOpening.isEmpty()) {
                        openStatusAt.visibility = View.VISIBLE
                        openStatusAt.text = "Open $timeForOpening"
                    } else {
                        openStatusAt.visibility = View.GONE
                    }
                    openCloseLay.setBackgroundResource(R.drawable.background_timing_red)
                }

            }


        }
    }

    private fun changeTimeFormat(time: String): String {
        val inputPattern = "hh:mm a"
        val outputPattern = "hh:mm:ss aa"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String = ""

        try {
            date = inputFormat.parse(time.trim())
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }


    private fun checkIfCurrentTimeLiesInGivenInterVal(currentTime: String, start: String, end: String, from: String): Boolean {

        if (end.contains("AM")) {
            if (checkStartTimings(currentTime, "23:59:59 PM", "end")) {
                return true
            }
        }
        if (checkStartTimings(currentTime, end, "end")) {
            return true
        }
        return false

    }

    private fun checkIfCurrentTimeLiesInGivenInterVal22(currentTime: String, start: String, from: String): Boolean {

        return checkStartTimings(currentTime, start, "start")


    }

    private fun checkIfCurrentTimeLiesInGivenInterVal23(currentTime: String, start: String, end: String, from: String): Boolean {

        if (start.contains("AM") && end.contains("AM") ||
                start.contains("am") && end.contains("am")) {
            if (checkStartTimings(start, end, "start")) {
                return checkStartTimings(currentTime, "11:59:59 PM", "end")
            } else {
                return checkStartTimings(currentTime, end, "end")
            }


        }
        if (start.contains("PM") && end.contains("PM") ||
                start.contains("pm") && end.contains("pm")) {
            val inFormat = SimpleDateFormat("hh:mm:ss aa")
            val outFormat = SimpleDateFormat("HH:mm")

            val time24 = outFormat.format(inFormat.parse(start))
            val time245 = outFormat.format(inFormat.parse(end))
            try {
                val start = outFormat.parse(time24)
                val end = outFormat.parse(time245)
                if (start.after(end)) {
                    return checkStartTimings(currentTime, "11:59:59 PM", "end")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return checkStartTimings(currentTime, end, "end")

        }
        if (start.contains("PM") && end.contains("AM") ||
                start.contains("pm") && end.contains("am")) {

            return checkStartTimings(currentTime, "11:59:59 PM", "end")


        }
        if (start.contains("AM") && end.contains("PM") ||
                start.contains("am") && end.contains("pm")) {

            return checkStartTimings(currentTime, end, "end")


        }
        return checkStartTimings(currentTime, start, "end")


    }



    private fun checkStartTimings(time: String, endtime: String, from: String): Boolean {

        val pattern = "hh:mm:ss aa"
        val sdf = SimpleDateFormat(pattern)

        try {
            val date1 = sdf.parse(time)
            val date2 = sdf.parse(endtime)
            if (from == "start") {
                return date1.after(date2)
            } else if (from == "end") {
                return date1.before(date2)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }

    private fun getCurrentTime(): String {

        val sdf = SimpleDateFormat("hh:mm:ss aa")
        return sdf.format(Date())
    }

    private fun getCurrentTimeAndDate(): String {
        val sdf = SimpleDateFormat("EEE")
        return sdf.format(Date())

    }


    override fun describeContents(): Int {
        return 0
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(selected)
        parcel.writeString(lat)
        parcel.writeString(long)
        parcel.writeParcelable(mLocationRequest, flags)
        parcel.writeParcelable(model1, flags)
        parcel.writeParcelable(modelBusiness, flags)
    }

    companion object CREATOR : Parcelable.Creator<DetailFragment> {
        override fun createFromParcel(parcel: Parcel): DetailFragment {
            return DetailFragment(parcel)
        }

        override fun newArray(size: Int): Array<DetailFragment?> {
            return arrayOfNulls(size)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        updateTitle = context as setTitle
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        updateTitle = activity as setTitle
    }

    interface setTitle {
        fun setTitleBusiness(business_name: String)
    }

    private fun getDayTimeString(timeToFind: DayModel, list: ArrayList<DayModel>): ModelMain {
        val modelMain = ModelMain()
        val subList = ArrayList<DayModel>()
        var timeToReturn = ""
        var lastIndex = -1
        for (i in list.indices) {
            val model = list[i]
            if (timeToFind.time == model.time) {

                subList.add(model)

                if (lastIndex != -1 && i - lastIndex == 1) {

                    if (timeToReturn.contains("-") && !timeToReturn.contains(",")) {
                        timeToReturn = "${timeToFind.day}-${model.day}"
                    } else {
                        timeToReturn = "$timeToReturn-${model.day}"
                    }

                } else {
                    if (timeToReturn.isEmpty()) {
                        timeToReturn = model.day
                    } else {
                        timeToReturn = "$timeToReturn, ${model.day}"
                    }

                }

                lastIndex = i
            }
        }

        modelMain.list = subList
        modelMain.resultString.day = timeToReturn
        modelMain.resultString.time = timeToFind.time

        return modelMain
    }

    fun checkIfAlreadyExist(day: String): Boolean {
        if (mainList.isNotEmpty()) {
            for (value in mainList) {
                val list = value.list
                for (data in list) {
                    if (data.day == day) {
                        return true
                    }
                }
            }
        }

        return false
    }
}
