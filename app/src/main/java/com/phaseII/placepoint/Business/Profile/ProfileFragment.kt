package com.phaseII.placepoint.Business.Profile


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AlertDialog.Builder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.phaseII.placepoint.AboutBusiness.AboutBusinessActivity
import com.phaseII.placepoint.AboutBusiness.SingleBusinessModel
import com.phaseII.placepoint.BusEvents.SetPagerToHome
import com.phaseII.placepoint.Business.Profile.BusinessProfileMap.ProfileMapActivity
import com.phaseII.placepoint.ConstantClass.GpsTracker
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Cropper.CropperActivity
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.MultichoiceCategories.MultipleCategories

import com.phaseII.placepoint.R
import com.phaseII.placepoint.Register.MultipleCatagoryAdapter
import com.phaseII.placepoint.SubscriptionPlan.SubscriptionActivity
import com.phaseII.placepoint.Town.ModelTown
import com.phaseII.placepoint.Town.TownActivity
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment(), BusinessProfileHelper, HorzRecyclerAdapter.updateImageList {
    protected val REQUEST_ID_MULTIPLE_PERMISSIONS: Int = 110
    private var cat_id: String = ""
    private var town_id: String = ""
    lateinit var openHoursStart: TextView
    lateinit var openHoursEnd: TextView
    lateinit var closeStartHours: TextView
    private var filter: InputFilter? = null
    lateinit var addresss: TextView
    lateinit var coverlay: TextView
    private var model: SingleBusinessModel = SingleBusinessModel()
    lateinit var number: EditText
    lateinit var progressBar: ProgressBar
    lateinit var progressBar2: ProgressBar
    lateinit var closeEndHours: TextView

    lateinit var coverTitle: TextView
    lateinit var header: TextView
    lateinit var add_label: TextView
    lateinit var open_hour_label: TextView
    lateinit var openingLabel: TextView
    lateinit var emailText: TextView
    lateinit var townText: TextView
    lateinit var catText: TextView




    lateinit var select_town: TextView
    lateinit var emailIdText: EditText
    lateinit var select_category: TextView
    lateinit var bus_desc: EditText
    lateinit var bus_name: EditText
    lateinit var word_count: TextView
    lateinit var change: TextView
    lateinit var expirePlanOn: TextView
    lateinit var packageName: TextView
    lateinit var tallDays: LinearLayout
    lateinit var closeTimeLayout: ConstraintLayout
    lateinit var cross_btn: ImageView
    lateinit var editImage: ImageView
    lateinit var save: Button
    lateinit var camera: ImageView
    lateinit var allTime: ImageView
    lateinit var youtube: ImageView
    lateinit var Ivcover: ImageView
    lateinit var youtubeLink: EditText
    lateinit var gallery: ImageView
    lateinit var closeBox: CheckBox
    lateinit var radioGroup: RadioGroup
    lateinit var transLayout: RelativeLayout
    lateinit var desc_layout: RelativeLayout
    lateinit var horz_Image_recycler: RecyclerView
    private var openStartTime: String = "12:00 AM"
    private var openEndTime: String = "12:00 AM"
    private var closeStartTime: String = "12:00 AM"
    private var closeEndTime: String = "12:00 AM"
    private var type: String = ""
    private lateinit var mPresenter: BusinessProfilePresenter
    var checkopen: String = ""
    var timeList: ArrayList<ModelTime> = arrayListOf()

    // var closedList: ArrayList<Boolean> = arrayListOf()
    private lateinit var pickAddress: RelativeLayout
    lateinit var client: GoogleApiClient
    protected val REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101
    protected val REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102
    protected val REQUEST_STORAGE_WRITE_ACCESS = 112
    protected val FINE_LOC = 120
    protected val REQUEST_CAMERA = 100
    protected val REQUEST_CAMERA_COVER = 103
    protected val SELECT_PICTURES = 110
    protected val SELECT_COVER = 111
    protected var GALLERY: Boolean? = false
    private var mAlertDialog: AlertDialog? = null
    var imageUri: Uri? = null
    var imageUri2: Uri? = null
    private var listFromCropper: java.util.ArrayList<Uri>? = arrayListOf()
    private var CoverFromCropper: java.util.ArrayList<Uri>? = arrayListOf()
    private var CroppedImages: java.util.ArrayList<Uri>? = arrayListOf()
    private var AllImages: java.util.ArrayList<Uri>? = arrayListOf()
    private var OldImages: java.util.ArrayList<Uri>? = arrayListOf()
    var recyclerItems: java.util.ArrayList<Uri> = arrayListOf()
    var CoverItems: java.util.ArrayList<Uri> = arrayListOf()
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var result1: PendingResult<LocationSettingsResult>
    var day: String = "Monday"
    var modelTime = ModelTime()
    private val requestCode: Int = 3
    var jsonContact: String = ""
    var cat_list: ArrayList<ModelCategoryData> = arrayListOf()
    var loc_list: ArrayList<ModelTown> = arrayListOf()
    var cat_name: ArrayList<String> = arrayListOf()
    var loc_names: ArrayList<String> = arrayListOf()
    var preLoadImages: MutableList<String> = arrayListOf()
    private var long: String? = ""
    private var lat: String? = ""
    private var array: String = ""
    private var arr = JSONArray()
    private var close: Boolean = false
    var radioButtonClick: Int = 0
    var from = ""
    private var isServiceRunning: Boolean = false
    // lateinit var updateToolbarOption:hideToolBarOption
    var value: String = ""
    var open = 0
    var open1 = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_business_profile, container, false)
        header = v.findViewById(R.id.header)
        coverTitle = v.findViewById(R.id.coverTitle)
        add_label = v.findViewById(R.id.add_label)
        open_hour_label = v.findViewById(R.id.open_hour_label)
        openingLabel = v.findViewById(R.id.openingLabel)
        emailText = v.findViewById(R.id.emailText)
        townText = v.findViewById(R.id.townText)
        catText = v.findViewById(R.id.catText)
        openHoursStart = v.findViewById(R.id.openHoursStart)
        openHoursEnd = v.findViewById(R.id.openHoursEnd)
        closeStartHours = v.findViewById(R.id.closeHoursStart)
        closeEndHours = v.findViewById(R.id.closeHoursEnd)
        allTime = v.findViewById(R.id.allTime)
        tallDays = v.findViewById(R.id.tallDays)
        Ivcover = v.findViewById(R.id.image)
        addresss = v.findViewById(R.id.addresss)
        number = v.findViewById(R.id.number)
        coverlay = v.findViewById(R.id.coverlay)
        progressBar = v.findViewById(R.id.progressBar)
        progressBar2 = v.findViewById(R.id.progressBar2)
        closeBox = v.findViewById(R.id.closeBox)
        transLayout = v.findViewById(R.id.transLayout)
        closeTimeLayout = v.findViewById(R.id.closeTimeLayout)
        cross_btn = v.findViewById(R.id.cross_btn)
        horz_Image_recycler = v.findViewById(R.id.horz_Image_recycler)
        desc_layout = v.findViewById(R.id.desc_layout)
        radioGroup = v.findViewById(R.id.group)
        camera = v.findViewById(R.id.camera)
        save = v.findViewById(R.id.save)

        bus_desc = v.findViewById(R.id.bus_desc)
        word_count = v.findViewById(R.id.word_count)
        change = v.findViewById(R.id.change)
        expirePlanOn = v.findViewById(R.id.expireOn)
        packageName = v.findViewById(R.id.packageName)
        select_town = v.findViewById(R.id.select_town)
        select_category = v.findViewById(R.id.select_cate)
        editImage = v.findViewById(R.id.editImage)
        pickAddress = v.findViewById(R.id.pickAddressLayout)
        gallery = v.findViewById(R.id.gallery)
        youtube = v.findViewById(R.id.youtube)
        youtubeLink = v.findViewById(R.id.youtubeLink)
        bus_name = v.findViewById(R.id.chooser)
        emailIdText = v.findViewById(R.id.emailIdText)

        bus_desc.addTextChangedListener(mTextEditorWatcher)
        emailIdText.addTextChangedListener(mTextEditorWatcher2)
        Constants.closedList.add(false)
        Constants.closedList.add(false)
        Constants.closedList.add(false)
        Constants.closedList.add(false)
        Constants.closedList.add(false)
        Constants.closedList.add(false)
        Constants.closedList.add(false)
        bus_desc.imeOptions = EditorInfo.IME_ACTION_DONE
        bus_desc.setRawInputType(InputType.TYPE_CLASS_TEXT)
        mPresenter = BusinessProfilePresenter(this)

        mPresenter.setPrefilledData()
        mPresenter.clickHandler()
        mPresenter.setWeekRadioGroup()
        client = GoogleApiClient.Builder(activity!!).addApi(LocationServices.API).build()
        mPresenter.requestAllPermissions()
        mPresenter.setCatAndLocData()
        mPresenter.prepareBusinessData()
        // mPresenter.setInitialTimeData(arr)
        Constants.getAppDataService(activity!!)
        return v
    }

    override fun onPause() {
        super.onPause()
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {

        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Constants.getBus().register(this)
        } catch (e: Exception) {

        }
        if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
            packageName.text = "Free Package"
            desc_layout.isClickable = true
            bus_desc.isFocusableInTouchMode=false
            emailIdText.isFocusableInTouchMode=false
            disablePickers()
            disableTitles()
        } else if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "2") {
            packageName.text = "Standard Package"
            desc_layout.isClickable = false
            bus_desc.isFocusableInTouchMode=true
            emailIdText.isFocusableInTouchMode=true
            enablePickers()
            enableTitles()
        } else if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "1") {
            packageName.text = "Premium Package"
            desc_layout.isClickable = false
            bus_desc.isFocusableInTouchMode=true
            emailIdText.isFocusableInTouchMode=true
            change.visibility = View.GONE
            enablePickers()
            enableTitles()
        }else if(Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "4") {
            packageName.text = "Admin"
            desc_layout.isClickable = false
            bus_desc.isFocusableInTouchMode=true
            emailIdText.isFocusableInTouchMode=true
            change.visibility = View.GONE
            enablePickers()
            enableTitles()
        }
//        if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE,"")=="3"){
//               noSubLay.visibility=View.VISIBLE
//               mainLay.visibility=View.GONE
//
//        }else{
//            noSubLay.visibility=View.GONE
//            mainLay.visibility=View.VISIBLE
//        }
        value = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_NAMES, "")!!
        if (value != null && !value.isEmpty()) {
            select_category.text = value
        }
        // setCategory()

        emailIdText.setText(Constants.getPrefs(activity!!)?.getString(Constants.EMAIL, ""))
        // updateToolbarOption.hideRightToolbarIcon()
    }

    private fun setCategory() {
        var savedCatergory = ""
        val mainCatValue = Constants.getPrefs(activity!!)!!.getString(Constants.MAIN_CATEGORY, "")
        if (!mainCatValue.isEmpty()) {
            val mainCat = mainCatValue.split(",")
            val arrayname = arrayListOf<String>()
            out@ for (p in 0 until mainCat.size) {
                inn@ for (q in 0 until cat_list.size) {
                    if (mainCat[p] == cat_list[q].id) {
                        arrayname.add(cat_list[q].name)
                        if (savedCatergory.isEmpty()) {
                            savedCatergory = cat_list[q].name
                        } else {
                            savedCatergory = savedCatergory + ", " + cat_list[q].name
                        }

//                        break@inn
//                        break@out
                    }
                }
            }
        }
        select_category.text = savedCatergory
    }

    override fun getCatAndLocData() {
        val cat = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_LIST, "")!!
        val loc = Constants.getPrefs(activity!!)?.getString(Constants.LOCATION_LIST, "")!!

        try {
            cat_list = Constants.getCategoryData(cat)!!
            val hashSet = HashSet<ModelCategoryData>()
            hashSet.addAll(cat_list)
            cat_list.clear()
            cat_list.addAll(hashSet)
            val sortedList = cat_list.sortedWith(compareBy({ it.name }))
            val array = arrayOfNulls<ModelCategoryData>(sortedList.size)
            cat_list.toArray(array)
            loc_list = Constants.getTownData(loc)!!
            val hashSet2 = HashSet<ModelTown>()
            hashSet2.addAll(loc_list)
            loc_list.clear()
            loc_list.addAll(hashSet2)
            val sortedList1 = loc_list.sortedWith(compareBy({ it.townname }))
            val array1 = arrayOfNulls<ModelTown>(sortedList1.size)
            loc_list.toArray(array1)
            for (i in 0 until cat_list.size) {
                cat_name.add(cat_list[i].name)
            }
            for (i in 0 until loc_list.size) {
                loc_names.add(loc_list[i].townname)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun handleClicks() {
        desc_layout.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }

        }
        coverlay.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }

        }
        Ivcover.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }

        }
        tallDays.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            showDialogToSetSameTime()
        }

        youtube.setOnClickListener {

            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            youtubeLink.visibility = View.VISIBLE
        }
        select_town.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            mPresenter.showSelectTownPopup()
        }
        select_category.setOnClickListener {

            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            //mPresenter.showSelectCatPopup()
            val intent = Intent(activity, MultipleCategories::class.java)
            intent.putExtra("from", "profile")
            startActivity(intent)
        }
        openHoursStart.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            checkopen = "open"
            mPresenter.openStartTimePicker(day)
        }
        openHoursEnd.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            checkopen = "open"
            mPresenter.openEndTimePicker()
        }
        closeStartHours.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            checkopen = "close"
            mPresenter.closeStartTimePicker()
        }
        closeEndHours.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            checkopen = "close"
            mPresenter.closeEndTimePicker()
        }

        camera.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            if (AllImages != null) {
//                if (AllImages!!.size < 5) {
                mPresenter.openCamera()
//                } else {
//                    showImageLimitPopUp()
//                }
            }
        }
        gallery.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            if (AllImages != null) {
//                if (AllImages!!.size < 5) {
                mPresenter.openGallery()
//                } else {
//                    showImageLimitPopUp()
//                }
            }
        }
        emailIdText.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                emailIdText.isFocusableInTouchMode = false
                emailIdText.setInputType(InputType.TYPE_NULL);
                return@setOnClickListener
            }
            emailIdText.isFocusableInTouchMode = true
        }
        bus_desc.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                Constants.hideKeyBoard(activity!!, bus_desc)
                bus_desc.isFocusableInTouchMode = false
                return@setOnClickListener
            }
            bus_desc.isFocusableInTouchMode = true
        }
        pickAddress.setOnClickListener {
            val tracker = GpsTracker(activity!!)
            if (tracker.canGetLocation()) {
                val prefs = Constants.getPrefs(activity!!)
                if (prefs != null) {
                    if (prefs.getBoolean(Constants.LOCATION_ACCESS, false)) {
                        startActivityForResult(Intent(activity!!, ProfileMapActivity::class.java), FINE_LOC)
                    } else {
                        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            } else {
                locationOnOFF(activity!!)
            }
        }


        editImage.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            mPresenter.uploadCoverImage()
        }
        change.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3" ||
                    Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "2") {
                val intent = Intent(activity, SubscriptionActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(activity!!, "You are already a Premium User.", Toast.LENGTH_LONG).show()
            }
        }
        save.setOnClickListener {

            if (!isServiceRunning) {

                mPresenter.onSaveButtonClick()
            }
        }


        cross_btn.setOnClickListener {
            // closeTimeLayout.visibility = View.GONE
            if (closeTimeLayout.isEnabled) {
                closeTimeLayout.isEnabled = false
                closeTimeLayout.isClickable = false
                closeEndHours.isClickable = false
                closeStartHours.isClickable = false
                closeEndHours.text = "12:00 AM"
                closeStartHours.text = "12:00 AM"
                closeEndHours.setBackgroundColor(resources.getColor(R.color.light_grey))
                closeStartHours.setBackgroundColor(resources.getColor(R.color.light_grey))
                // setDataAccDays("")
                callCrossButton(true)
            } else {
                //  setDataAccDays("")
                callCrossButton(false)
                closeEndHours.text = "12:00 AM"
                closeStartHours.text = "12:00 AM"
                closeTimeLayout.isEnabled = true
                closeTimeLayout.isClickable = true
                closeEndHours.isClickable = true
                closeStartHours.isClickable = true
                closeStartHours.setBackgroundDrawable(resources.getDrawable(R.drawable.timer_value_bg))
                closeEndHours.setBackgroundDrawable(resources.getDrawable(R.drawable.timer_value_bg))
            }
        }
//        closeBox.setOnClickListener {
//            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
//                Constants.showPopup(activity!!)
//                closeBox.isChecked=false
//                return@setOnClickListener
//            }
//            from = "click"
//        }
        closeBox.setOnCheckedChangeListener { buttonView, isChecked ->
            // if (radioButtonClick!=1) {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                closeBox.isChecked=false
                return@setOnCheckedChangeListener
            }
            if (from != "set") {
                if (isChecked) {

                    close = true
                    transLayout.visibility = View.VISIBLE
                    setClosedAccToDays()
                    disablePickers()
                    setData(true)

                    // setDataAccDays(checks)

                } else {
                    close = false
                    transLayout.visibility = View.GONE
                    setOpenAccToDays()
                    enablePickers()
                    setData(false)
                    // setDataAccDays(checks)
                }
            }
        }

    }

    private fun locationOnOFF(context: Context) {

        var lm: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (q: Exception) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Alert")
            dialog.setMessage(("Your loction is turned Off"))
            dialog.setPositiveButton(("Turn On"), DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                var myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(myIntent)

            });

            dialog.show()
        }
    }


    override fun checkingAllDaysValidation(): Boolean {
        var execute = 0
        outer@ for (i in 0 until 7) {
            var inDay: String = ""
            when (i) {
                0 -> inDay = "Monday"
                1 -> inDay = "Tuesday"
                2 -> inDay = "Wednesday"
                3 -> inDay = "Thursday"
                4 -> inDay = "Friday"
                5 -> inDay = "Saturday"
                6 -> inDay = "Sunday"
            }
            if (timeList.size > 0) {


                if (timeList[i].startFrom == timeList[i].startTo) {
                    if (!timeList[i].status) {
                        Toast.makeText(activity, "Open from and Open To cannot be same on $inDay ", Toast.LENGTH_LONG).show()
                        execute = 1
                        break@outer
                    }

                }
//                    if (timeList[i].startFrom == timeList[i].closeFrom) {
//
//                        if (!timeList[i].status) {
//                            if (!timeList[i].closeStatus) {
//                                Toast.makeText(activity, "Opening and Closing Time Cannot be same on $inDay ", Toast.LENGTH_LONG).show()
//                                execute = 1
//                                break@outer
//                            }
//                        }
//
//                    }
//                    if (timeList[i].closeFrom == timeList[i].closeTo) {
//                        if (!timeList[i].status) {
//                            if (!timeList[i].closeStatus) {
//                                Toast.makeText(activity, "Closing Time Cannot be same on $inDay ", Toast.LENGTH_LONG).show()
//                                execute = 1
//                                break@outer
//                            }
//                        }
//
//                    }


            }

        }
        if (execute == 1) {
            return true
        }
        return false
    }

    private fun showDialogToSetSameTime() {
        val dialog = Builder(activity!!)
        dialog.setCancelable(false)
        dialog.setTitle("Alert!")
        dialog.setMessage("Are you sure you want to apply this time to all days?")
        dialog.setPositiveButton("Yes") { dialog, id ->
            var value = 0
            if (day == "Monday") {
                value = 0
            }
            if (day == "Tuesday") {
                value = 1
            }
            if (day == "Wednesday") {
                value = 2
            }
            if (day == "Thursday") {
                value = 3
            }
            if (day == "Friday") {
                value = 4
            }
            if (day == "Saturday") {
                value = 5
            }
            if (day == "Sunday") {
                value = 6
            }
            for (i in 0 until 7) {
                timeList[i].startFrom = timeList[value].startFrom
                timeList[i].startTo = timeList[value].startTo
                timeList[i].closeFrom = timeList[value].closeFrom
                timeList[i].closeTo = timeList[value].closeTo
                timeList[i].status = timeList[value].status
                timeList[i].closeStatus = timeList[value].closeStatus

            }
            dialog.dismiss()
        }
                .setNegativeButton("Cancel ") { dialog, which ->
                    dialog.dismiss()
                }

        val alert = dialog.create()
        alert.show()

    }

    private fun callCrossButton(b: Boolean) {
        if (day == "Monday") {

            timeList[0].closeFrom = "12:00 AM"
            timeList[0].closeTo = "12:00 AM"
//                timeList[0].startFrom = "12:00 AM"
//                timeList[0].startTo = "12:00 AM"

            //  timeList[0].closeStatus=b
            timeList[0].closeStatus = b
        }
        if (day == "Tuesday") {
            timeList[1].closeFrom = "12:00 AM"
            timeList[1].closeTo = "12:00 AM"
//            timeList[1].startFrom = "12:00 AM"
//            timeList[1].startTo = "12:00 AM"
            timeList[1].closeStatus = b
        }
        if (day == "Wednesday") {
            timeList[2].closeFrom = "12:00 AM"
            timeList[2].closeTo = "12:00 AM"
//            timeList[2].startFrom = "12:00 AM"
//            timeList[2].startTo = "12:00 AM"
            timeList[2].closeStatus = b
        }
        if (day == "Thursday") {
            timeList[3].closeFrom = "12:00 AM"
            timeList[3].closeTo = "12:00 AM"
//            timeList[3].startFrom = "12:00 AM"
//            timeList[3].startTo = "12:00 AM"
            timeList[3].closeStatus = b
        }
        if (day == "Friday") {
            timeList[4].closeFrom = "12:00 AM"
            timeList[4].closeTo = "12:00 AM"
//            timeList[4].startFrom = "12:00 AM"
//            timeList[4].startTo = "12:00 AM"
            timeList[4].closeStatus = b
        }
        if (day == "Saturday") {
            timeList[5].closeFrom = "12:00 AM"
            timeList[5].closeTo = "12:00 AM"
//            timeList[5].startFrom = "12:00 AM"
//            timeList[5].startTo = "12:00 AM"
            timeList[5].closeStatus = b
        }
        if (day == "Sunday") {
            timeList[6].closeFrom = "12:00 AM"
            timeList[6].closeTo = "12:00 AM"
//            timeList[6].startFrom = "12:00 AM"
//            timeList[6].startTo = "12:00 AM"
            timeList[6].closeStatus = b
        }
    }


    private fun setData(b: Boolean) {
        try {
            if (day == "Monday") {
//                editTimeList(day, timeList[0].startFrom, "startFrom")
//                editTimeList(day, timeList[0].startTo, "startTo")
//                editTimeList(day, timeList[0].closeFrom, "closeFrom")
//                editTimeList(day, timeList[0].closeTo, "closeTo")
//                timeList[0].status=b
                // Constants.closedList[0] = b
//                if (b) {
//                    timeList[0].startFrom = "closed"
//                    // setDataAccDays(checks)
//                } else {
//                    timeList[0].startFrom = "12:00 AM"
//                    //  setDataAccDays(checks)
//                }
                if (b) {
                    timeList[0].closeFrom = "12:00 AM"
                    timeList[0].closeTo = "12:00 AM"
                    timeList[0].startFrom = "12:00 AM"
                    timeList[0].startTo = "12:00 AM"
                    timeList[0].status = b

                } else {
                    timeList[0].status = b

                }

            }
            if (day == "Tuesday") {
//                editTimeList(day, timeList[1].startFrom, "startFrom")
//                editTimeList(day, timeList[1].startTo, "startTo")
//                editTimeList(day, timeList[1].closeFrom, "closeFrom")
//                editTimeList(day, timeList[1].closeTo, "closeTo")
                //timeList[1].status=b
                //  Constants.closedList[1]=b
                if (b) {
                    timeList[1].closeFrom = "12:00 AM"
                    timeList[1].closeTo = "12:00 AM"
                    timeList[1].startFrom = "12:00 AM"
                    timeList[1].startTo = "12:00 AM"
                    timeList[1].status = b

                } else {
                    timeList[1].status = b

                }

            }
            if (day == "Wednesday") {
//                editTimeList(day, timeList[2].startFrom, "startFrom")
//                editTimeList(day, timeList[2].startTo, "startTo")
//                editTimeList(day, timeList[2].closeFrom, "closeFrom")
//                editTimeList(day, timeList[2].closeTo, "closeTo")
                // timeList[2].status=b
                // Constants.closedList[2]=b
                if (b) {
                    timeList[2].closeFrom = "12:00 AM"
                    timeList[2].closeTo = "12:00 AM"
                    timeList[2].startFrom = "12:00 AM"
                    timeList[2].startTo = "12:00 AM"
                    timeList[2].status = b

                } else {
                    timeList[2].status = b


                }


            }
            if (day == "Thursday") {
//                editTimeList(day, timeList[3].startFrom, "startFrom")
//                editTimeList(day, timeList[3].startTo, "startTo")
//                editTimeList(day, timeList[3].closeFrom, "closeFrom")
//                editTimeList(day, timeList[3].closeTo, "closeTo")
                //timeList[3].status=b
                //  Constants.closedList[3]=b
                if (b) {
                    timeList[3].closeFrom = "12:00 AM"
                    timeList[3].closeTo = "12:00 AM"
                    timeList[3].startFrom = "12:00 AM"
                    timeList[3].startTo = "12:00 AM"
                    timeList[3].status = b

                } else {
                    timeList[3].status = b

                }


            }
            if (day == "Friday") {
//                editTimeList(day, timeList[4].startFrom, "startFrom")
//                editTimeList(day, timeList[4].startTo, "startTo")
//                editTimeList(day, timeList[4].closeFrom, "closeFrom")
//                editTimeList(day, timeList[4].closeTo, "closeTo")
                //timeList[4].status=b
                //  Constants.closedList[4]=b
                if (b) {
                    timeList[4].closeFrom = "12:00 AM"
                    timeList[4].closeTo = "12:00 AM"
                    timeList[4].startFrom = "12:00 AM"
                    timeList[4].startTo = "12:00 AM"
                    timeList[4].status = b

                } else {
                    timeList[4].status = b

                }

            }
            if (day == "Saturday") {
//                editTimeList(day, timeList[5].startFrom, "startFrom")
//                editTimeList(day, timeList[5].startTo, "startTo")
//                editTimeList(day, timeList[5].closeFrom, "closeFrom")
//                editTimeList(day, timeList[5].closeTo, "closeTo")
                // timeList[5].status=b
                // Constants.closedList[5]=b
                if (b) {
                    timeList[5].closeFrom = "12:00 AM"
                    timeList[5].closeTo = "12:00 AM"
                    timeList[5].startFrom = "12:00 AM"
                    timeList[5].startTo = "12:00 AM"
                    timeList[5].status = b

                } else {
                    timeList[5].status = b
                }

            }
            if (day == "Sunday") {
//                editTimeList(day, timeList[6].startFrom, "startFrom")
//                editTimeList(day, timeList[6].startTo, "startTo")
//                editTimeList(day, timeList[6].closeFrom, "closeFrom")
//                editTimeList(day, timeList[6].closeTo, "closeTo")
                // timeList[6].status=b
                //  Constants.closedList[6]=b
                if (b) {
                    timeList[6].closeFrom = "12:00 AM"
                    timeList[6].closeTo = "12:00 AM"
                    timeList[6].startFrom = "12:00 AM"
                    timeList[6].startTo = "12:00 AM"
                    timeList[6].status = b

                } else {
                    timeList[6].status = b

                }


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun askForPermission(permission: String) {
        if (ContextCompat.checkSelfPermission(activity!!, permission) != PackageManager.PERMISSION_GRANTED) {
            /* val utils = Utils()
             utils.getPermission(permission, activity!!,requestCode)*/

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(permission), FINE_LOC)
            } else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(permission), FINE_LOC)
            }
        } else {
            startActivityForResult(Intent(activity!!, ProfileMapActivity::class.java), FINE_LOC)
        }
    }

    private var checks: String = ""

    @SuppressLint("SetTextI18n")
    override fun showOpenStartTimePicker(day: String) {


        val values = arrayOf("AM", "PM")
        var am_pm = 0
        var hours = 0
        var minutes = 0
        val d = Builder(activity!!)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.number_picker_dialog, null)
        d.setTitle("Select Time")
        d.setView(dialogView)
        val numberPicker = dialogView.findViewById(R.id.dialog_number_picker) as NumberPicker
        val numberPickerMin = dialogView.findViewById(R.id.dialog_number_picker_min) as NumberPicker
        val numberPicker2 = dialogView.findViewById(R.id.dialog_am_pm) as NumberPicker
        numberPicker.maxValue = 12
        numberPicker.minValue = 1
        numberPicker.wrapSelectorWheel = true
        numberPickerMin.maxValue = 59
        numberPickerMin.minValue = 0
        numberPickerMin.wrapSelectorWheel = true
        numberPicker2.displayedValues = values
        numberPicker2.wrapSelectorWheel = false
        numberPicker2.maxValue = 1
        numberPicker2.minValue = 0

        numberPicker.setOnValueChangedListener { numberPicker, i, i1 ->
        }
        //  numberPicker.setFormatter { i -> String.format("%02d", i) }
        d.setPositiveButton("Done") { dialogInterface, i ->
            hours = numberPicker.value
            minutes = numberPickerMin.value
            am_pm = numberPicker2.value
            val am = values[am_pm]
            // openStartTime = String.format("%02d", hours) + " " + am
            val form = DecimalFormat("00")
            openStartTime = hours.toString() + ":" + form.format(minutes) + " " + am
            openHoursStart.text = openStartTime
            checks = "openstart"
            editTimeList(day, openStartTime, "startFrom")
            //  setDataAccDays(checks)
            dialogInterface.dismiss()
        }
        d.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        val alertDialog = d.create()
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun showOpenEndTimePicker() {
        val values = arrayOf("AM", "PM")
        var am_pm = 0
        var hours = 0
        var minutes = 0
        val d = Builder(activity!!)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.number_picker_dialog, null)
        d.setTitle("Select Time")
        d.setView(dialogView)
        val numberPicker = dialogView.findViewById(R.id.dialog_number_picker) as NumberPicker
        val numberPicker2 = dialogView.findViewById(R.id.dialog_am_pm) as NumberPicker
        val numberPickerMin = dialogView.findViewById(R.id.dialog_number_picker_min) as NumberPicker

        numberPickerMin.maxValue = 59
        numberPickerMin.minValue = 0
        numberPickerMin.wrapSelectorWheel = true

        numberPicker2.displayedValues = null
        numberPicker.maxValue = 12
        numberPicker.minValue = 1
        numberPicker.wrapSelectorWheel = true

        numberPicker2.displayedValues = values
        numberPicker2.wrapSelectorWheel = false
        numberPicker2.maxValue = 1
        numberPicker2.minValue = 0

        numberPicker.setOnValueChangedListener { numberPicker, i, i1 ->
        }
        //  numberPicker.setFormatter { i -> String.format("%02d", i) }
        d.setPositiveButton("Done") { dialogInterface, i ->
            hours = numberPicker.value
            minutes = numberPickerMin.value
            am_pm = numberPicker2.value
            val am = values[am_pm]
            // openEndTime = String.format("%02d", hours) + " " + am
            val form = DecimalFormat("00")
            openEndTime = hours.toString() + ":" + form.format(minutes) + " " + am
            openHoursEnd.text = openEndTime
            checks = "openend"
            editTimeList(day, openEndTime, "startTo")
            //setDataAccDays(checks)
            dialogInterface.dismiss()
        }
        d.setNegativeButton("Cancel", { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        val alertDialog = d.create()
        alertDialog.show()
    }

    private fun editTimeList(day: String, selectedTime: String, to: String) {
        var newTime = selectedTime
        if (newTime == "closed") {
            newTime = "12:00 AM"
        }
        if (day == "Monday") {
            if (to == "startTo") {
                timeList[0].startTo = newTime
            }
            if (to == "startFrom") {
                timeList[0].startFrom = newTime
            }
            if (to == "closeFrom") {
                timeList[0].closeFrom = newTime
            }
            if (to == "closeTo") {
                timeList[0].closeTo = newTime
            }
            if (Constants.closedList[0]) {
                try {
                    closeBox.isChecked = true
                    disablePickers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                closeBox.isChecked = false
                enablePickers()
            }

        }
        if (day == "Tuesday") {
            if (to == "startTo") {
                timeList[1].startTo = newTime
            }
            if (to == "startFrom") {
                timeList[1].startFrom = newTime
            }
            if (to == "closeFrom") {
                timeList[1].closeFrom = newTime
            }
            if (to == "closeTo") {
                timeList[1].closeTo = newTime
            }

            if (Constants.closedList[1]) {
                try {
                    closeBox.isChecked = true
                    disablePickers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                closeBox.isChecked = false
                enablePickers()
            }

        }
        if (day == "Wednesday") {
            if (to == "startTo") {
                timeList[2].startTo = newTime
            }
            if (to == "startFrom") {
                timeList[2].startFrom = newTime
            }
            if (to == "closeFrom") {
                timeList[2].closeFrom = newTime
            }
            if (to == "closeTo") {
                timeList[2].closeTo = newTime
            }
            if (Constants.closedList[2]) {
                try {
                    closeBox.isChecked = true
                    disablePickers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                closeBox.isChecked = false
                enablePickers()
            }

        }
        if (day == "Thursday") {
            if (to == "startTo") {
                timeList[3].startTo = newTime
            }
            if (to == "startFrom") {
                timeList[3].startFrom = newTime
            }
            if (to == "closeFrom") {
                timeList[3].closeFrom = newTime
            }
            if (to == "closeTo") {
                timeList[3].closeTo = newTime
            }
            if (Constants.closedList[3]) {
                try {
                    closeBox.isChecked = true
                    disablePickers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                closeBox.isChecked = false
                enablePickers()
            }

        }
        if (day == "Friday") {
            if (to == "startTo") {
                timeList[4].startTo = newTime
            }
            if (to == "startFrom") {
                timeList[4].startFrom = newTime
            }
            if (to == "closeFrom") {
                timeList[4].closeFrom = newTime
            }
            if (to == "closeTo") {
                timeList[4].closeTo = newTime
            }
            if (Constants.closedList[4]) {
                try {
                    closeBox.isChecked = true
                    disablePickers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                closeBox.isChecked = false
                enablePickers()
            }

        }
        if (day == "Saturday") {
            if (to == "startTo") {
                timeList[5].startTo = newTime
            }
            if (to == "startFrom") {
                timeList[5].startFrom = newTime
            }
            if (to == "closeFrom") {
                timeList[5].closeFrom = newTime
            }
            if (to == "closeTo") {
                timeList[5].closeTo = newTime
            }
            if (Constants.closedList[5]) {
                try {
                    closeBox.isChecked = true
                    disablePickers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                closeBox.isChecked = false
                enablePickers()
            }

        }
        if (day == "Sunday") {
            if (to == "startTo") {
                timeList[6].startTo = newTime
            }
            if (to == "startFrom") {
                timeList[6].startFrom = newTime
            }
            if (to == "closeFrom") {
                timeList[6].closeFrom = newTime
            }
            if (to == "closeTo") {
                timeList[6].closeTo = newTime
            }
            if (Constants.closedList[6]) {
                try {
                    closeBox.isChecked = true
                    disablePickers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                closeBox.isChecked = false
                enablePickers()
            }

        }
    }

    @SuppressLint("SetTextI18n")
    override fun showCloseStartTimePicker() {
        val values = arrayOf("AM", "PM")
        var am_pm = 0
        var hours = 0
        var minutes = 0
        val d = Builder(activity!!)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.number_picker_dialog, null)
        d.setTitle("Select Time")
        d.setView(dialogView)
        val numberPicker = dialogView.findViewById(R.id.dialog_number_picker) as NumberPicker
        val numberPicker2 = dialogView.findViewById(R.id.dialog_am_pm) as NumberPicker
        val numberPickerMin = dialogView.findViewById(R.id.dialog_number_picker_min) as NumberPicker

        numberPickerMin.maxValue = 59
        numberPickerMin.minValue = 0
        numberPickerMin.wrapSelectorWheel = true

        numberPicker2.displayedValues = null
        numberPicker.maxValue = 12
        numberPicker.minValue = 1
        numberPicker.wrapSelectorWheel = true

        numberPicker2.displayedValues = values
        numberPicker2.wrapSelectorWheel = false
        numberPicker2.maxValue = 1
        numberPicker2.minValue = 0

        numberPicker.setOnValueChangedListener { numberPicker, i, i1 ->
        }
        // numberPicker.setFormatter { i -> String.format("%02d", i) }
        d.setPositiveButton("Done") { dialogInterface, i ->
            hours = numberPicker.value
            minutes = numberPickerMin.value
            am_pm = numberPicker2.value
            val am: String = values[am_pm]
            //closeStartTime = String.format("%02d", hours) + " " + am
            val form = DecimalFormat("00")
            closeStartTime = hours.toString() + ":" + form.format(minutes) + " " + am
            closeStartHours.text = closeStartTime
            checks = "closestart"
            //setDataAccDays(checks)
            editTimeList(day, closeStartTime, "closeFrom")
            dialogInterface.dismiss()
        }
        d.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        val alertDialog = d.create()
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun showCloseEndTimePicker() {
        var am_pm = 0
        var hours = 0
        var minutes = 0
        val d = Builder(activity!!)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.number_picker_dialog, null)
        d.setTitle("Select Time")
        d.setView(dialogView)
        val numberPicker = dialogView.findViewById(R.id.dialog_number_picker) as NumberPicker
        val numberPicker2 = dialogView.findViewById(R.id.dialog_am_pm) as NumberPicker

        val numberPickerMin = dialogView.findViewById(R.id.dialog_number_picker_min) as NumberPicker

        numberPickerMin.maxValue = 59
        numberPickerMin.minValue = 0
        numberPickerMin.wrapSelectorWheel = true

        numberPicker2.displayedValues = null
        numberPicker.maxValue = 12
        numberPicker.minValue = 1
        numberPicker2.displayedValues = arrayOf("AM", "PM")
        numberPicker.wrapSelectorWheel = true

        //  numberPicker2.displayedValues = values
        numberPicker2.wrapSelectorWheel = false
        numberPicker2.maxValue = 1
        numberPicker2.minValue = 0

        numberPicker.setOnValueChangedListener { numberPicker, i, i1 ->
        }
        // numberPicker.setFormatter { i -> String.format("%02d", i) }
        d.setPositiveButton("Done") { dialogInterface, i ->
            hours = numberPicker.value
            minutes = numberPickerMin.value
            am_pm = numberPicker2.value
            val am = numberPicker2.displayedValues[am_pm]
            // closeEndTime = String.format("%02d", hours) + " " + am
            val form = DecimalFormat("00")
            closeEndTime = hours.toString() + ":" + form.format(minutes) + " " + am
            closeEndHours.text = closeEndTime
            checks = "closeend"
            editTimeList(day, closeEndTime, "closeTo")
            // setDataAccDays(checks)
            dialogInterface.dismiss()
        }
        d.setNegativeButton("Cancel", { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        val alertDialog = d.create()
        alertDialog.show()
    }

    override fun setAdapterForCroppedImages(list: ArrayList<Uri>?, croppedImages: java.util.ArrayList<Uri>, preLoadImages: MutableList<String>) {
        if (list != null) {
            if (list.size > 0) {
                val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                horz_Image_recycler.layoutManager = linearLayoutManager
                horz_Image_recycler.adapter = HorzRecyclerAdapter(this!!.activity!!, list, croppedImages, preLoadImages, type)
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE_READ_ACCESS_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (GALLERY!!)
                    pickFromGallery("multiple")
            }
            REQUEST_STORAGE_WRITE_ACCESS_PERMISSION ->

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        val packageManager = activity!!.packageManager
                        // if device support camera?
                        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                            //yes
                            cameraIntent("")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            REQUEST_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent("")
                } else {
                    Toast.makeText(activity!!, "camera permission denied", Toast.LENGTH_LONG).show()
                    val editor = Constants.getPrefs(activity!!)?.edit()
                    editor?.putBoolean(Constants.LOCATION_ACCESS, false)
                    editor?.apply()
                    Toast.makeText(activity!!, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            FINE_LOC -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val editor = Constants.getPrefs(activity!!)?.edit()
                    editor?.putBoolean(Constants.LOCATION_ACCESS, true)
                    editor?.apply()
                } else {
                    Toast.makeText(activity!!, "Location permission denied", Toast.LENGTH_LONG).show()
                    val editor = Constants.getPrefs(activity!!)?.edit()
                    editor?.putBoolean(Constants.LOCATION_ACCESS, false)
                    editor?.apply()
                    Toast.makeText(activity!!, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_STORAGE_WRITE_ACCESS -> {
                if (GALLERY!!)
                    pickFromGallery("multiple")
            }
        }


        /* if (requestCode == 3) {

             if (ActivityCompat.checkSelfPermission(activity!!, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                 mPresenter.askForGPSPermission()
                 val editor = Constants.getPrefs(activity!!)?.edit()
                 editor?.putBoolean(Constants.LOCATION_ACCESS, true)
                 editor?.apply()
                 startActivityForResult(Intent(activity!!, ProfileMapActivity::class.java), FINE_LOC)
             }


         }*/
    }

    override fun openCropper(list: java.util.ArrayList<Uri>, s: String) {
        if (s == "cover") {
            val intent = Intent(activity!!, CropperActivity::class.java)
            val args = Bundle()
            args.putSerializable("LIST", CoverItems)
            intent.putExtra("BUN", args)
            intent.putExtra("from", "bus")
            startActivityForResult(intent, 7)
        } else {
            val intent = Intent(activity!!, CropperActivity::class.java)
            val args = Bundle()
            args.putSerializable("LIST", list)
            intent.putExtra("BUN", args)
            intent.putExtra("from", "bus")
            startActivityForResult(intent, 2)
        }
    }

    override fun openCamera() {
        GALLERY = false
        cameraIntent("")
    }

    private fun cameraIntent(s: String) {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
        } else {

            if (s == "cover") {

                if (ContextCompat.checkSelfPermission(activity!!,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //do your stuff
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.TITLE, "New Picture")
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                    imageUri2 = activity!!.contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2)
                    startActivityForResult(intent, REQUEST_CAMERA_COVER)

                } else {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            getString(R.string.permission_write_storage_rationale),
                            REQUEST_STORAGE_WRITE_ACCESS_PERMISSION)
                }

            } else {
                if (ContextCompat.checkSelfPermission(activity!!,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //do your stuff
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.TITLE, "New Picture")
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                    imageUri = activity!!.contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(intent, REQUEST_CAMERA)

                } else {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            getString(R.string.permission_write_storage_rationale),
                            REQUEST_STORAGE_WRITE_ACCESS_PERMISSION)
                }

            }


        }
    }


    private fun pickFromGallery(s: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION)
        } else if (ContextCompat.checkSelfPermission(activity!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS)
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
                    .setType("image/*")
                    .addCategory(Intent.CATEGORY_OPENABLE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }

            if (s == "one") {
                try {
                    val intent1 = Intent()
                    intent1.type = "image/*"
                    intent1.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Intent.createChooser(intent1, "Select Picture"), SELECT_COVER)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                try {
                    val intent1 = Intent()
                    intent1.type = "image/*"
                    intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    intent1.action = Intent.ACTION_GET_CONTENT
                    intent1.putExtra("crop", "true")
                    intent1.putExtra("scale", true)
                    intent1.putExtra("outputX", 256)
                    intent1.putExtra("outputY", 256)
                    intent1.putExtra("aspectX", 1)
                    intent1.putExtra("aspectY", 1)
                    intent1.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
                   // intent1.putExtra(MediaStore.EXTRA_OUTPUT, getBitmapFromUri())
                    startActivityForResult(Intent.createChooser(intent1, "Select Picture"), SELECT_PICTURES)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }




    protected fun requestPermission(permission: String, rationale: String, requestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(activity!!,
                                arrayOf(permission), requestCode)
                    }, getString(R.string.ok), null, getString(R.string.cancel))
        } else {
            ActivityCompat.requestPermissions(activity!!, arrayOf(permission), requestCode)
        }
    }


    protected fun showAlertDialog(title: String?, message: String?,
                                  onPositiveButtonClickListener: DialogInterface.OnClickListener?,
                                  positiveText: String,
                                  onNegativeButtonClickListener: DialogInterface.OnClickListener?,
                                  negativeText: String) {
        val builder = Builder(activity!!)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener)
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener)
        mAlertDialog = builder.show()
    }


    private var coverImage: Uri? = null
    private var coverImageString: String? = ""


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 9) {
            if (data != null) {
                value = data.getStringExtra("result")

                //select_cate.text=data.getStringExtra("result")
            }
        }


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    recyclerItems.clear()
                    recyclerItems.add(imageUri!!)
                    mPresenter.openCropper(recyclerItems, "")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (requestCode == REQUEST_CAMERA_COVER) {
                try {
                    CoverItems.clear()
                    CoverItems.add(imageUri2!!)
                    mPresenter.openCropper(CoverItems, "cover")
                    coverImage = imageUri2!!
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == 2) {
            try {
                val args = data!!.getBundleExtra("BUNDLE")
                listFromCropper = (args.getSerializable("ARRAYLIST") as java.util.ArrayList<Uri>?)
                CroppedImages!!.addAll(listFromCropper!!)
                AllImages!!.addAll(OldImages!!)
                AllImages!!.addAll(listFromCropper!!)


//                if (AllImages!!.size > 5) {
//                    showImageLimitPopUp()
//                    AllImages!!.subList(5, AllImages!!.size).clear()
//                }
                type = "new"
                mPresenter.setAdapterForCroppedImages(AllImages, CroppedImages!!, preLoadImages, type)

                /* if (listFromCropper!!.size > 0) {
                     //   croppedImage.visibility = View.VISIBLE
                     for (i in listFromCropper!!) {
                     }
                 }*/

            } catch (e: Exception) {
                e.printStackTrace()
            }


        } else if (requestCode == 7) {
            try {
                val args = data!!.getBundleExtra("BUNDLE")
                CoverFromCropper = (args.getSerializable("ARRAYLIST") as java.util.ArrayList<Uri>?)
                val b = CoverFromCropper!![0]
                //val b = resultUri

                Glide.with(activity!!)
                        .load(b)
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.placeholder))
                        .into(Ivcover)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (resultCode == 5) {
            val location = data!!.getStringExtra("location")
            lat = data.getStringExtra("lat")
            long = data.getStringExtra("long")
            addresss.text = location
        }

        if (requestCode === SELECT_PICTURES) {
            recyclerItems.clear()
            if (null != data) {
                if (null != data.clipData) {
                    for (i in 0 until data.clipData.itemCount) {
                        val item = data.clipData.getItemAt(i)
                        val ur = item.uri
                        recyclerItems.add(ur)
                    }
                    mPresenter.openCropper(recyclerItems, "")
                } else {
                    val uri = data.data
                    recyclerItems.add(uri)
                    mPresenter.openCropper(recyclerItems, "")
                }
            }
        }
        if (requestCode === SELECT_COVER) {
            if (null != data) {
                CoverItems.clear()
                val uri = data.data
                CoverItems.add(uri)
                mPresenter.openCropper(CoverItems, "cover")
                coverImage = uri
            }
        }
    }

    private fun SaveImage(b: Bitmap): File {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/Android/data/com.example.user24.placepoint/Asaved_images")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        if (file.exists())
            file.delete()
        try {
            val out = FileOutputStream(file)
            b.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }


    private fun showImageLimitPopUp() {
        Builder(activity!!)
                .setMessage("You cannot select more than 5 images.")
                .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                }).create().show()
    }

    override fun askForGPS() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = (30 * 1000).toLong()
        mLocationRequest.fastestInterval = (5 * 1000).toLong()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        builder.setAlwaysShow(true)
        result1 = LocationServices.SettingsApi.checkLocationSettings(client, builder.build())
        result1.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(activity!!, 0x7)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }

    override fun openGallery() {
        GALLERY = true
        pickFromGallery("multiple")
    }

    override fun requestAllPermissions() {
        val read = ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        val write = ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val camera = ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.CAMERA)

        val location = ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION)

        val listPermissionsNeeded = ArrayList<String>()

        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        val ar = listPermissionsNeeded.toTypedArray()
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(ar, REQUEST_ID_MULTIPLE_PERMISSIONS)
        }
    }

    override fun setInitialData(timeList1: ArrayList<ModelTime>) {
        timeList = timeList1
    }

    override fun selectTownPopUp() {
        val items1 = loc_names.toTypedArray()
        val items = items1.distinct().toTypedArray()
        var sortedList = items.sortedArray()
        val value = arrayOfNulls<String>(1)
        val builder = Builder(activity!!)
                .setSingleChoiceItems(sortedList, -1) { dialog, which -> value[0] = sortedList[which] }
                .setPositiveButton("Ok") { dialog, which ->
                    try {
                        val lw = (dialog as AlertDialog).listView
                        val checkedItem = lw.adapter.getItem(lw.checkedItemPosition)
                        val pos = lw.checkedItemPosition
                        val selectedTown = checkedItem.toString()
                        select_town.text = selectedTown
                        var sortedList = loc_list.sortedWith(compareBy({ it.townname }))
                        town_id = sortedList[pos].id
                        Constants.getPrefs(activity!!)?.edit()?.putString(Constants.TOWN_NAMEPROFILE, selectedTown)?.apply()
                        Constants.getPrefs(activity!!)?.edit()?.putString(Constants.TOWN_ID2, town_id)?.apply()
                        dialog.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    // dialog.dismiss()
                }
                .setNegativeButton("Cancel"
                ) { dialog, which -> dialog.dismiss() }.create()
        builder.show()
    }

    private fun getAllUniqueValues(list: ArrayList<ModelCategoryData>): ArrayList<ModelCategoryData> {
        val uniqueList = ArrayList<ModelCategoryData>()
        val enemyIds = ArrayList<String>()
        for (entry in list) {
            if (!enemyIds.contains(entry.id)) {
                enemyIds.add(entry.id)
                uniqueList.add(entry)
            }
        }
        return uniqueList
    }

    override fun selectCatPopUp() {
        val mRecyclerView: RecyclerView

        val builder = Builder(activity!!)
        val optionDialog = builder.create()
        val inflater = LayoutInflater.from(activity!!)
        val content = inflater.inflate(R.layout.custom_dialog, null)
        builder.setView(content)
        mRecyclerView = content.findViewById<View>(R.id.my_recycler_view) as RecyclerView
        val array = getAllUniqueValues(cat_list)

        val mainCatValue = Constants.getPrefs(activity!!)!!.getString(Constants.MAIN_CATEGORY, "")
        val mainCat = mainCatValue.split(",")
        for (i in 0 until array.size) {
            var exist = false
            for (h in 0 until mainCat.size) {
                if (array[i].id == mainCat[h]) {
                    exist = true
                }
            }
            array[i].checked = exist

        }
        var sortedList = array.sortedWith(compareBy { it.name })
        val adapter = MultipleCatagoryAdapter(activity!!, sortedList, "BusinessProfileFragment")
        val linear = LinearLayoutManager(activity!!)
        mRecyclerView.layoutManager = linear
        mRecyclerView.adapter = adapter
        builder.setNegativeButton("CANCEL", DialogInterface.OnClickListener { d, arg1 ->
            d.dismiss()
        })
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { d, arg1 ->
            cat_id = adapter.getSelected()
            select_category.text = adapter.getFirstSelectedName()
            if (select_category.text.toString().isEmpty()) {
                setCategory()
            }
            d.dismiss()
        })

//        cancel.setOnClickListener {
//            optionDialog.dismiss()
//        }
//        ok.setOnClickListener {
//            cat_id = adapter.getSelected()
//            bus_cat.text = adapter.getFirstSelectedName()
//            optionDialog.dismiss()
//        }

        builder.show()
//        val items1 = cat_name.toTypedArray()
//        val items = items1.distinct().toTypedArray()
//        val len = items.size
//        val arrayChecked = BooleanArray(len)
//        val mainCatValue = Constants.getPrefs(activity!!)!!.getString(Constants.MAIN_CATEGORY, "")
//
//        val mainCat = mainCatValue.split(",")
//        val arrayname = arrayListOf<String>()
//        for (p in 0 until mainCat.size) {
//            for (q in 0 until cat_list.size) {
//                if (mainCat[p] == cat_list[q].id) {
//                    arrayname.add(cat_list[q].name)
//                }
//            }
//        }
//
//        for (t in 0 until items.size) {
//            var exist = false
//            for (g in 0 until arrayname.size) {
//                if (items[t] == arrayname[g]) {
//                    exist = true
//                }
//            }
////            var exist = false
////            inner@ for (j in 0 until items.size) {
////                if (cat_list[j].id == mainCat[i]) {
////                    exist = true
////
////                    break@inner
////                }
////            }
//            arrayChecked[t] = exist
//        }
//        var dialog: AlertDialog
//        val builder = AlertDialog.Builder(activity!!)
//        builder.setMultiChoiceItems(items, arrayChecked) { dialog, which, isChecked ->
//            // Update the clicked item checked status
//            arrayChecked[which] = isChecked
//
//            // Get the clicked item
//            val color = items[which]
//
//        }
//
//
//        // Set the positive/yes button click listener
//        builder.setPositiveButton("OK") { _, _ ->
//            // Do something when click positive button
//            arrayname.clear()
//            cat_id=""
//            for (h in 0 until arrayChecked.size) {
//                if (arrayChecked[h]) {
//                    arrayname.add(items[h])
//                }
//            }
//            for (a in 0 until arrayname.size) {
//                for (k in 0 until cat_list.size) {
//
//                    if (arrayname[a] == cat_list[k].name) {
//                        if (cat_id.isEmpty()) {
//                            cat_id = cat_list[k].id
//
//                        } else {
//                            cat_id = cat_id + "," + cat_list[k].id
//                        }
//                    }
//                }
//            }
//
//            for (i in 0 until items.size) {
//
//
//                val checked = arrayChecked[i]
//                if (checked) {
//                    val name = items[i]
//                    for (l in 0 until cat_list.size) {
//                        if (name == cat_list[l].name) {
//                            if (cat_id.isEmpty()) {
//                                cat_id = cat_list[l].id
//                                select_category.text = cat_list[l].name
//                            } else {
//                                select_category.text = cat_list[i].name
//                                cat_id = cat_id + "," + cat_list[l].id
//                            }
//                        }
//                    }
////                    cat_id = if (cat_id.isEmpty()) {
////                        cat_list[i].id
////                        select_category.setText(cat_list[i].name).toString()
////                    } else {
////                        cat_id + "," + cat_list[i].id
////                    }
//                }
//            }
//
//        }
//        builder.setNegativeButton("Cancel") { _, _ ->
//            // Do something when click positive button
//
//        }
//
//        // Initialize the AlertDialog using builder object
//        dialog = builder.create()
//
//        // Finally, display the alert dialog
//        dialog.show()


//        val builder = AlertDialog.Builder(activity!!)
//                .setSingleChoiceItems(items, -1) { dialog, which -> value[0] = items[which] }
//                .setPositiveButton("Ok") { dialog, which ->
//                    try {
//                        val lw = (dialog as AlertDialog).listView
//                        val checkedItem = lw.adapter.getItem(lw.checkedItemPosition)
//                        val selectedCat = checkedItem.toString()
//                        val pos = lw.checkedItemPosition
//                        select_category.text = selectedCat
//                        cat_id = cat_list[pos].id
//                        Constants.getPrefs(activity!!)?.edit()?.putString(Constants.CATEGORY_NAME, selectedCat)?.apply()
//                        Constants.getPrefs(activity!!)?.edit()?.putString(Constants.CATEGORY, cat_id)?.apply()
//                        dialog.dismiss()
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                    // dialog.dismiss()
//                }
//                .setNegativeButton("Cancel"
//                ) { dialog, which -> dialog.dismiss() }.create()
//        builder.show()


    }

    override fun getAuthCode(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.AUTH_CODE, "")!!
    }

    override fun getTownId(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID2, "")!!
    }

    override fun getCategoryId(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.MAIN_CATEGORY, "")!!
    }

    override fun getPrefillTownId(): String {
        return model.town_id
    }

    override fun getPrefillCatId(): String {
        return model.category_id
    }

    override fun getVideoLink(): String {
        return youtubeLink.text.toString().trim()
    }

    override fun getAddress(): String {
        return addresss.text.toString().trim()
    }

    override fun getContactNo(): String {
        return number.text.toString().trim()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getOpeningHours(): String {
        val gson = Gson()
        var timeList1: ArrayList<ModelTimeString> = arrayListOf()
        for (i in 0 until timeList.size) {
            var model = ModelTimeString()
            model.closeFrom = timeList[i].closeFrom
            model.closeTo = timeList[i].closeTo
            model.startFrom = timeList[i].startFrom
            model.startTo = timeList[i].startTo
            model.status = timeList[i].status.toString()
            model.closeStatus = timeList[i].closeStatus.toString()
            timeList1.add(model)

        }
        jsonContact = gson.toJson(timeList1)
        Log.e("List", jsonContact)
        // val jsonArray = JSONArray(jsonContact)
        return jsonContact
    }

    override fun getClosingHours(): String {
        var timeList1: ArrayList<ModelTimeString> = arrayListOf()
        for (i in 0 until timeList.size) {
            var model = ModelTimeString()
            model.closeFrom = timeList[i].closeFrom
            model.closeTo = timeList[i].closeTo
            model.startFrom = timeList[i].startFrom
            model.startTo = timeList[i].startTo
            model.status = timeList[i].status.toString()
            model.closeStatus = timeList[i].closeStatus.toString()
            timeList1.add(model)

        }
        val gson = Gson()
        jsonContact = gson.toJson(timeList1)
        Log.e("List", jsonContact)
        return jsonContact
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
        progressBar2.visibility = View.VISIBLE
        save.text = "saving to the server..."
    }

    override fun setclickFalse() {
        isServiceRunning = false
    }

    override fun setclickTrue() {
        isServiceRunning = true
    }

    override fun hideLoader() {
        isServiceRunning = false
        save.text = "save"
        progressBar.visibility = View.GONE
        progressBar2.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun showNetworkError(resId: Int) {
        isServiceRunning = false
        if (activity != null && isAdded) {
            Toast.makeText(activity, getString(resId), Toast.LENGTH_SHORT).show()
        }
        save.text = "save"
    }

    override fun getMultiPartFiles(): ArrayList<Uri> {
        return CroppedImages!!
    }

    override fun getOldMultiPartFiles(): String {
        val gsonBuilder = GsonBuilder().create()
        val jsonFromJavaArrayList = gsonBuilder.toJson(preLoadImages)
        return jsonFromJavaArrayList
    }

    override fun showMessage(msg: String?, busId: String) {
        try {
            Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
            openBusinessPage(busId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openBusinessPage(busId: String) {
        if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
            Constants.getBus().post(SetPagerToHome("home"))
        } else {
            val intent = Intent(activity!!, AboutBusinessActivity::class.java)
            intent.putExtra("busId", busId)
            intent.putExtra("showallpost", "no")
            intent.putExtra("from", "profile")
            intent.putExtra("busName", bus_name.text.toString())
            intent.putExtra("subscriptionType",Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE,""))
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        isServiceRunning = false
    }

    override fun showNoImageMessage(s: String) {
        Toast.makeText(activity!!, s, Toast.LENGTH_SHORT).show()
    }

    override fun setPreFilledData() {
        val busname = Constants.getPrefs(activity!!)?.getString(Constants.MY_BUSINESS_NAME, "")
        bus_name.setText(busname)
    }

    override fun getLatitude(): String {
        return lat!!
    }

    override fun getLongitude(): String {
        return long!!
    }

    override fun saveBusId(busId: String?) {
        Constants.getPrefs(activity!!)?.edit()?.putString(Constants.BUSINESS_ID, busId)?.apply()
    }

    override fun setTownError(s: String) {
        Toast.makeText(activity!!, "No Town selected", Toast.LENGTH_SHORT).show()
    }

    override fun setCatError(s: String) {
        Toast.makeText(activity!!, "No Category selected", Toast.LENGTH_SHORT).show()
    }

    override fun setContactError(s: String) {
        number.error = s
    }

    override fun setAdressError(s: String) {
        Toast.makeText(activity!!, "No Address selected", Toast.LENGTH_SHORT).show()
    }


    override fun getAuthCodeConstant(): String? {
        return Constants.getPrefs(activity!!)?.getString(Constants.AUTH_CODE, "")!!
    }

    override fun getTownIdConstant(): String? {
        if (activity != null && isAdded) {
            return Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID, "")!!
        }
        return ""
    }

    override fun getCatId(): String? {
        if (activity != null && isAdded) {
            return Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY, "")!!
        }
        return ""
    }

    override fun getBusId(): String {
        if (activity != null && isAdded) {
            return Constants.getPrefs(activity!!)?.getString(Constants.BUSINESS_ID_MAIN, "")!!
        }
        return ""
    }

    override fun setBusinessPrefilledData(data: String, end_time: String, user_type: String) {
        if (activity != null && isAdded) {
            if (end_time != "N/A") {

                try {
                    val currentTime = getCurrentTime()

                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val start = sdf.parse(currentTime)
                    val end = sdf.parse(end_time)
                    if (start.after(end)) {
                        expirePlanOn.text = "Expired on- "+ parseDateToddMMyyyy(end_time)
                    } else {
                        expirePlanOn.text = "Expires on- " + parseDateToddMMyyyy(end_time)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.SINGLE_BUSINESS_LIST, data)?.apply()
            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.USERTYPE, user_type)?.apply()
            mPresenter.setSingleBusinessPrefilledData()
        }
    }

    private fun getCurrentTime(): String {

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date())
    }


    private var intitalSetting: Boolean = false

    private var list: java.util.ArrayList<SingleBusinessModel>? = arrayListOf()
    private fun changeTimeFormat(time: String): String {
        val inputPattern = "hha"
        val outputPattern = "hh:mm aa"
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

    fun parseDateToddMMyyyy(time: String): String? {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd-MMM-yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }
    override fun saveBusinessName() {
        Constants.getPrefs(activity!!)?.edit()?.putString(Constants.BUSINESS_NAME, bus_name.text.toString())?.apply()
    }

    override fun getBusName(): String {
        return bus_name.text.toString()
    }

    private fun enablePickers() {
        openHoursStart.setBackgroundDrawable(resources.getDrawable(R.drawable.timer_value_bg))
        openHoursEnd.setBackgroundDrawable(resources.getDrawable(R.drawable.timer_value_bg))
        closeStartHours.setBackgroundDrawable(resources.getDrawable(R.drawable.timer_value_bg))
        closeEndHours.setBackgroundDrawable(resources.getDrawable(R.drawable.timer_value_bg))
        openHoursStart.isEnabled = true
        openHoursEnd.isEnabled = true
        closeStartHours.isEnabled = true
        closeEndHours.isEnabled = true
    }

    private fun disablePickers() {
        /*openHoursStart.text = "0am"
        closeStartHours.text = "0am"
        openHoursEnd.text = "0PM"
        closeEndHours.text = "0pm"*/
        openHoursStart.setBackgroundColor(resources.getColor(R.color.light_grey))
        openHoursEnd.setBackgroundColor(resources.getColor(R.color.light_grey))
        closeStartHours.setBackgroundColor(resources.getColor(R.color.light_grey))
        closeEndHours.setBackgroundColor(resources.getColor(R.color.light_grey))
        openHoursStart.isEnabled = false
        openHoursEnd.isEnabled = false
        closeStartHours.isEnabled = false
        closeEndHours.isEnabled = false
    }
private fun disableTitles() {
        /*openHoursStart.text = "0am"
        closeStartHours.text = "0am"
        openHoursEnd.text = "0PM"
        closeEndHours.text = "0pm"*/
        coverTitle.setTextColor(resources.getColor(R.color.light_grey))
        header.setTextColor(resources.getColor(R.color.light_grey))
        add_label.setTextColor(resources.getColor(R.color.light_grey))
        open_hour_label.setTextColor(resources.getColor(R.color.light_grey))
        openingLabel.setTextColor(resources.getColor(R.color.light_grey))
        emailText.setTextColor(resources.getColor(R.color.light_grey))
        townText.setTextColor(resources.getColor(R.color.light_grey))
        catText.setTextColor(resources.getColor(R.color.light_grey))
    emailIdText.setTextColor(resources.getColor(R.color.light_grey))
    select_category.setTextColor(resources.getColor(R.color.light_grey))
    select_town.setTextColor(resources.getColor(R.color.light_grey))
   // coverlay.visibility=View.VISIBLE



    }
    private fun enableTitles() {

        coverTitle.setTextColor(resources.getColor(R.color.black))
        header.setTextColor(resources.getColor(R.color.black))
        add_label.setTextColor(resources.getColor(R.color.black))
        open_hour_label.setTextColor(resources.getColor(R.color.black))
        openingLabel.setTextColor(resources.getColor(R.color.black))
        emailText.setTextColor(resources.getColor(R.color.black))
        townText.setTextColor(resources.getColor(R.color.black))
        catText.setTextColor(resources.getColor(R.color.black))
        emailIdText.setTextColor(resources.getColor(R.color.black))
        select_category.setTextColor(resources.getColor(R.color.black))
        select_town.setTextColor(resources.getColor(R.color.black))
      //  coverlay.visibility=View.GONE

    }
    private fun setDataAccDays(checks: String) {
        if (day == "Monday") {
            modelTime = ModelTime()
            if (Constants.closedList[0]) {
                try {
                    modelTime.startFrom = "closed"
                    modelTime.startTo = "closed"
                    modelTime.closeFrom = "closed"
                    modelTime.closeTo = "closed"
                    timeList[0] = modelTime
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                modelTime = ModelTime()
                if (intitalSetting) {
                    modelTime.startFrom = openStartTime
                    modelTime.startTo = openEndTime
                    modelTime.closeFrom = closeStartTime
                    modelTime.closeTo = closeEndTime
                    timeList[0] = modelTime
                } else {
                    if (checks == "openstart") {
                        modelTime.startFrom = timeList[0].startFrom
                        modelTime.startTo = timeList[0].startTo
                        modelTime.closeFrom = timeList[0].closeFrom
                        modelTime.closeTo = timeList[0].closeTo
                    } else if (checks == "openend") {
                        modelTime.startTo = timeList[0].startTo
                        modelTime.startFrom = timeList[0].startFrom
                        modelTime.closeFrom = timeList[0].closeFrom
                        modelTime.closeTo = timeList[0].closeTo
                    } else if (checks == "closestart") {
                        modelTime.closeFrom = timeList[0].closeFrom
                        modelTime.startTo = timeList[0].startTo
                        modelTime.startFrom = timeList[0].startFrom
                        modelTime.closeTo = timeList[0].closeTo
                    } else if (checks == "closeend") {
                        modelTime.closeTo = timeList[0].closeTo
                        modelTime.startTo = timeList[0].startTo
                        modelTime.closeFrom = timeList[0].closeFrom
                        modelTime.startFrom = timeList[0].startFrom
                    } else {
                        modelTime.closeTo = timeList[0].closeTo
                        modelTime.startTo = timeList[0].startTo
                        modelTime.closeFrom = timeList[0].closeFrom
                        modelTime.startFrom = timeList[0].startFrom
                    }
                    timeList[0] = modelTime

                }
            }

        } else if (day == "Tuesday") {
            modelTime = ModelTime()
            if (Constants.closedList[1]) {
                try {
                    modelTime.startFrom = "closed"
                    modelTime.startTo = "closed"
                    modelTime.closeFrom = "closed"
                    modelTime.closeTo = "closed"
                    timeList[1] = modelTime
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                if (intitalSetting) {
                    modelTime.startFrom = openStartTime
                    modelTime.startTo = openEndTime
                    modelTime.closeFrom = closeStartTime
                    modelTime.closeTo = closeEndTime
                    timeList[1] = modelTime
                } else {
                    if (checks == "openstart") {
                        modelTime.startFrom = timeList[1].startFrom
                        modelTime.startTo = timeList[1].startTo
                        modelTime.closeFrom = timeList[1].closeFrom
                        modelTime.closeTo = timeList[1].closeTo
                    } else if (checks == "openend") {
                        modelTime.startTo = timeList[1].startTo
                        modelTime.startFrom = timeList[1].startFrom
                        modelTime.closeFrom = timeList[1].closeFrom
                        modelTime.closeTo = timeList[1].closeTo
                    } else if (checks == "closestart") {
                        modelTime.closeFrom = timeList[1].closeFrom
                        modelTime.startTo = timeList[1].startTo
                        modelTime.startFrom = timeList[1].startFrom
                        modelTime.closeTo = timeList[1].closeTo
                    } else if (checks == "closeend") {
                        modelTime.closeTo = timeList[1].closeTo
                        modelTime.startTo = timeList[1].startTo
                        modelTime.closeFrom = timeList[1].closeFrom
                        modelTime.startFrom = timeList[1].startFrom
                    } else {
                        modelTime.closeTo = timeList[1].closeTo
                        modelTime.startTo = timeList[1].startTo
                        modelTime.closeFrom = timeList[1].closeFrom
                        modelTime.startFrom = timeList[1].startFrom
                    }
                    timeList[1] = modelTime

                }
            }

        } else if (day == "Wednesday") {
            modelTime = ModelTime()
            if (Constants.closedList[2]) {
                try {
                    modelTime.startFrom = "closed"
                    modelTime.startTo = "closed"
                    modelTime.closeFrom = "closed"
                    modelTime.closeTo = "closed"
                    timeList[2] = modelTime
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                if (intitalSetting) {
                    modelTime.startFrom = openStartTime
                    modelTime.startTo = openEndTime
                    modelTime.closeFrom = closeStartTime
                    modelTime.closeTo = closeEndTime
                    timeList[2] = modelTime
                } else {
                    if (checks == "openstart") {
                        modelTime.startFrom = timeList[2].startFrom
                        modelTime.startTo = timeList[2].startTo
                        modelTime.closeFrom = timeList[2].closeFrom
                        modelTime.closeTo = timeList[2].closeTo
                    } else if (checks == "openend") {
                        modelTime.startTo = timeList[2].startTo
                        modelTime.startFrom = timeList[2].startFrom
                        modelTime.closeFrom = timeList[2].closeFrom
                        modelTime.closeTo = timeList[2].closeTo
                    } else if (checks == "closestart") {
                        modelTime.closeFrom = timeList[2].closeFrom
                        modelTime.startTo = timeList[2].startTo
                        modelTime.startFrom = timeList[2].startFrom
                        modelTime.closeTo = timeList[2].closeTo
                    } else if (checks == "closeend") {
                        modelTime.closeTo = timeList[2].closeTo
                        modelTime.startTo = timeList[2].startTo
                        modelTime.closeFrom = timeList[2].closeFrom
                        modelTime.startFrom = timeList[2].startFrom
                    } else {
                        modelTime.closeTo = timeList[2].closeTo
                        modelTime.startTo = timeList[2].startTo
                        modelTime.closeFrom = timeList[2].closeFrom
                        modelTime.startFrom = timeList[2].startFrom
                    }
                    timeList[2] = modelTime

                }
            }


        } else if (day == "Thursday") {
            modelTime = ModelTime()
            if (Constants.closedList[3]) {
                try {
                    modelTime.startFrom = "closed"
                    modelTime.startTo = "closed"
                    modelTime.closeFrom = "closed"
                    modelTime.closeTo = "closed"
                    timeList[3] = modelTime
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                if (intitalSetting) {
                    modelTime.startFrom = openStartTime
                    modelTime.startTo = openEndTime
                    modelTime.closeFrom = closeStartTime
                    modelTime.closeTo = closeEndTime
                    timeList[3] = modelTime
                } else {
                    if (checks == "openstart") {
                        modelTime.startFrom = timeList[3].startFrom
                        modelTime.startTo = timeList[3].startTo
                        modelTime.closeFrom = timeList[3].closeFrom
                        modelTime.closeTo = timeList[3].closeTo
                    } else if (checks == "openend") {
                        modelTime.startTo = timeList[3].startTo
                        modelTime.startFrom = timeList[3].startFrom
                        modelTime.closeFrom = timeList[3].closeFrom
                        modelTime.closeTo = timeList[3].closeTo
                    } else if (checks == "closestart") {
                        modelTime.closeFrom = timeList[3].closeFrom
                        modelTime.startTo = timeList[3].startTo
                        modelTime.startFrom = timeList[3].startFrom
                        modelTime.closeTo = timeList[3].closeTo
                    } else if (checks == "closeend") {
                        modelTime.closeTo = timeList[3].closeTo
                        modelTime.startTo = timeList[3].startTo
                        modelTime.closeFrom = timeList[3].closeFrom
                        modelTime.startFrom = timeList[3].startFrom
                    } else {
                        modelTime.closeTo = timeList[3].closeTo
                        modelTime.startTo = timeList[3].startTo
                        modelTime.closeFrom = timeList[3].closeFrom
                        modelTime.startFrom = timeList[3].startFrom
                    }
                    timeList[3] = modelTime

                }
            }
        } else if (day == "Friday") {
            modelTime = ModelTime()
            if (Constants.closedList[4]) {
                try {
                    modelTime.startFrom = "closed"
                    modelTime.startTo = "closed"
                    modelTime.closeFrom = "closed"
                    modelTime.closeTo = "closed"
                    timeList[4] = modelTime
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                if (intitalSetting) {
                    modelTime.startFrom = openStartTime
                    modelTime.startTo = openEndTime
                    modelTime.closeFrom = closeStartTime
                    modelTime.closeTo = closeEndTime
                    timeList[4] = modelTime
                } else {
                    if (checks == "openstart") {
                        modelTime.startFrom = timeList[4].startFrom
                        modelTime.startTo = timeList[4].startTo
                        modelTime.closeFrom = timeList[4].closeFrom
                        modelTime.closeTo = timeList[4].closeTo
                    } else if (checks == "openend") {
                        modelTime.startTo = timeList[4].startTo
                        modelTime.startFrom = timeList[4].startFrom
                        modelTime.closeFrom = timeList[4].closeFrom
                        modelTime.closeTo = timeList[4].closeTo
                    } else if (checks == "closestart") {
                        modelTime.closeFrom = timeList[4].closeFrom
                        modelTime.startTo = timeList[4].startTo
                        modelTime.startFrom = timeList[4].startFrom
                        modelTime.closeTo = timeList[4].closeTo
                    } else if (checks == "closeend") {
                        modelTime.closeTo = timeList[4].closeTo
                        modelTime.startTo = timeList[4].startTo
                        modelTime.closeFrom = timeList[4].closeFrom
                        modelTime.startFrom = timeList[4].startFrom
                    } else {
                        modelTime.closeTo = timeList[4].closeTo
                        modelTime.startTo = timeList[4].startTo
                        modelTime.closeFrom = timeList[4].closeFrom
                        modelTime.startFrom = timeList[4].startFrom
                    }
                    timeList[4] = modelTime

                }
            }
        } else if (day == "Saturday") {
            modelTime = ModelTime()
            if (Constants.closedList[5]) {
                try {
                    modelTime.startFrom = "closed"
                    modelTime.startTo = "closed"
                    modelTime.closeFrom = "closed"
                    modelTime.closeTo = "closed"
                    timeList[5] = modelTime
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                if (intitalSetting) {
                    modelTime.startFrom = openStartTime
                    modelTime.startTo = openEndTime
                    modelTime.closeFrom = closeStartTime
                    modelTime.closeTo = closeEndTime
                    timeList[5] = modelTime
                } else {
                    if (checks == "openstart") {
                        modelTime.startFrom = timeList[5].startFrom
                        modelTime.startTo = timeList[5].startTo
                        modelTime.closeFrom = timeList[5].closeFrom
                        modelTime.closeTo = timeList[5].closeTo
                    } else if (checks == "openend") {
                        modelTime.startTo = timeList[5].startTo
                        modelTime.startFrom = timeList[5].startFrom
                        modelTime.closeFrom = timeList[5].closeFrom
                        modelTime.closeTo = timeList[5].closeTo
                    } else if (checks == "closestart") {
                        modelTime.closeFrom = timeList[5].closeFrom
                        modelTime.startTo = timeList[5].startTo
                        modelTime.startFrom = timeList[5].startFrom
                        modelTime.closeTo = timeList[5].closeTo
                    } else if (checks == "closeend") {
                        modelTime.closeTo = timeList[5].closeTo
                        modelTime.startTo = timeList[5].startTo
                        modelTime.closeFrom = timeList[5].closeFrom
                        modelTime.startFrom = timeList[5].startFrom
                    } else {
                        modelTime.closeTo = timeList[5].closeTo
                        modelTime.startTo = timeList[5].startTo
                        modelTime.closeFrom = timeList[5].closeFrom
                        modelTime.startFrom = timeList[5].startFrom
                    }
                    timeList[5] = modelTime

                }
            }
        } else if (day == "Sunday") {
            modelTime = ModelTime()
            if (Constants.closedList[6]) {
                try {
                    modelTime.startFrom = "closed"
                    modelTime.startTo = "closed"
                    modelTime.closeFrom = "closed"
                    modelTime.closeTo = "closed"
                    timeList[6] = modelTime
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                if (intitalSetting) {
                    modelTime.startFrom = openStartTime
                    modelTime.startTo = openEndTime
                    modelTime.closeFrom = closeStartTime
                    modelTime.closeTo = closeEndTime
                    timeList[6] = modelTime
                } else {
                    when (checks) {
                        "openstart" -> {
                            modelTime.startFrom = timeList[6].startFrom
                            modelTime.startTo = timeList[6].startTo
                            modelTime.closeFrom = timeList[6].closeFrom
                            modelTime.closeTo = timeList[6].closeTo
                        }
                        "openend" -> {
                            modelTime.startTo = timeList[6].startTo
                            modelTime.startFrom = timeList[6].startFrom
                            modelTime.closeFrom = timeList[6].closeFrom
                            modelTime.closeTo = timeList[6].closeTo
                        }
                        "closestart" -> {
                            modelTime.closeFrom = timeList[6].closeFrom
                            modelTime.startTo = timeList[6].startTo
                            modelTime.startFrom = timeList[6].startFrom
                            modelTime.closeTo = timeList[6].closeTo
                        }
                        "closeend" -> {
                            modelTime.closeTo = timeList[6].closeTo
                            modelTime.startTo = timeList[6].startTo
                            modelTime.closeFrom = timeList[6].closeFrom
                            modelTime.startFrom = timeList[6].startFrom
                        }
                        else -> {
                            modelTime.closeTo = timeList[6].closeTo
                            modelTime.startTo = timeList[6].startTo
                            modelTime.closeFrom = timeList[6].closeFrom
                            modelTime.startFrom = timeList[6].startFrom
                        }
                    }
                    timeList[6] = modelTime

                }
            }
        }

    }

    private fun setClosedAccToDays() {
        if (day.equals("Monday")) {
            //  timeList[0].close = "true"
            Constants.closedList[0] = close
        } else if (day == "Tuesday") {
            //   timeList[1].close = "true"
            Constants.closedList[1] = close
        } else if (day == "Wednesday") {
            //  timeList[2].close = "true"
            Constants.closedList[2] = close
        } else if (day == "Thursday") {
            // timeList[3].close = "true"
            Constants.closedList[3] = close
        } else if (day == "Friday") {
            // timeList[4].close = "true"
            Constants.closedList[4] = close
        } else if (day == "Saturday") {
            //  timeList[5].close = "true"
            Constants.closedList[5] = close
        } else if (day == "Sunday") {
            //  timeList[6].close = "true"
            Constants.closedList[6] = close
        }
    }

    private fun setOpenAccToDays() {
        if (day.equals("Monday")) {
            //timeList[0].close = "false"
            Constants.closedList[0] = close
        } else if (day == "Tuesday") {
            //  timeList[1].close = "false"
            Constants.closedList[1] = close
        } else if (day == "Wednesday") {
            // timeList[2].close = "false"
            Constants.closedList[2] = close
        } else if (day == "Thursday") {
            //  timeList[3].close = "false"
            Constants.closedList[3] = close
        } else if (day == "Friday") {
            // timeList[4].close = "false"
            Constants.closedList[4] = close
        } else if (day == "Saturday") {
            //  timeList[5].close = "false"
            Constants.closedList[5] = close
        } else if (day == "Sunday") {
            // timeList[6].close = "false"
            Constants.closedList[6] = close
        }
    }

    override fun getTimeArray(): ArrayList<ModelTime> {
        return timeList
    }

    val MAX_WORDS: Int = 1000
    override fun showMessageErr(s: String) {
        Toast.makeText(activity!!, s, Toast.LENGTH_SHORT).show()
    }

    val mTextEditorWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            if (open==1) {
//                if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
//                    Constants.showPopup(activity!!)
//                    return
//                }
            word_count.text = s.length.toString() + "/" + MAX_WORDS
//            }
//            open=1
        }

        override fun afterTextChanged(s: Editable) {}
    }
    //For contact Number
    val mTextEditorWatcher2 = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            if (open1==1) {
//                if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
//                    Constants.showPopup(activity!!)
//                    return
//                }
//            }
//            open1=1
        }

        override fun afterTextChanged(s: Editable) {}
    }


    override fun setDescError(s: String) {
        bus_desc.error = s
        Toast.makeText(activity!!, s, Toast.LENGTH_SHORT).show()
    }

    override fun getBusDesc(): String {
        return bus_desc.text.toString().trim()
    }

    override fun setCoverImage() {
        val items = arrayOf("Open Camera", "Choose From Gallery")
        val value = arrayOfNulls<String>(1)
        val builder = Builder(activity!!)
                .setSingleChoiceItems(items, -1) { dialog, which -> value[0] = items[which] }
                .setPositiveButton("Ok") { dialog, which ->
                    try {
                        val lw = (dialog as AlertDialog).listView
                        val checkedItem = lw.adapter.getItem(lw.checkedItemPosition)
                        val pos = lw.checkedItemPosition
                        if (pos == 0) {
                            cameraIntent("cover")
                        } else {
                            pickFromGallery("one")
                        }
                        dialog.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    // dialog.dismiss()
                }
                .setNegativeButton("Cancel"
                ) { dialog, which -> dialog.dismiss() }.create()
        builder.show()

    }

//    private fun cameraIntentCoverImage() {
//        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
//        } else {
//            if (ContextCompat.checkSelfPermission(activity!!,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                //do your stuff
//                val values = ContentValues()
//                values.put(MediaStore.Images.Media.TITLE, "New Picture")
//                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
//                imageUri2 = activity!!.contentResolver.insert(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2)
//                startActivityForResult(intent, REQUEST_CAMERA_COVER)
//
//            } else {
//                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        getString(R.string.permission_write_storage_rationale),
//                        REQUEST_STORAGE_WRITE_ACCESS_PERMISSION)
//            }
//        }
//    }

    private fun pickFromGalleryCoverImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION)
        } else if (ContextCompat.checkSelfPermission(activity!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS)
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
                    .setType("image/*")
                    .addCategory(Intent.CATEGORY_OPENABLE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }

            try {
                val intent1 = Intent()
                intent1.type = "image/*"
                intent1.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent1, "Select Picture"), SELECT_COVER)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    override fun getCoverImage(): Uri? {

        try {
            return coverImage!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun getCoverImageString(): String {
        try {
            val uri = CoverItems[0]
            val ur = getRealPathFromURI(uri)
            return ur
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
        //return model.cover_image+"jpg"
    }

    private fun getRealPathFromURI(uri: Uri?): String {
        var cursor = activity!!.contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = activity!!.contentResolver.query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null)
        cursor!!.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }

    override fun getOldCoverImage(): String {
        return list!!.get(0).cover_image
    }

    override fun saveMainCat(category_id: String) {

        Constants.getPrefs(activity!!)!!.edit().putString(Constants.MAIN_CATEGORY, category_id).apply()
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.EMAIL, emailIdText.text.toString()).apply()

    }

    override fun getEmail(): String {
        // return Constants.getPrefs(activity!!)!!.getString(Constants.EMAIL, "")
        return emailIdText.text.toString()

    }

    override fun getFromModel(): String {
        return model.cover_image
    }

    override fun coverImageIssue(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("CommitPrefEdits")
    override fun logOut() {
        if (activity != null) {
            val builder: Builder
            builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Builder(activity!!, android.R.style.Theme_Material_Dialog_Alert)
            } else {
                Builder(activity!!)
            }
            builder.setTitle("Alert!")
                    .setMessage("User Inactive")
                    .setPositiveButton("Ok") { dialog, which ->
                        Constants.getPrefs(activity!!)?.edit()!!.putBoolean(Constants.LOGIN, false)!!.apply()
                        Constants.getPrefs(activity!!)!!.edit().remove(Constants.LOGGED).apply()
                        //  setDrawerItems()
                        // mPresenter.logOut(Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE, ""))
                        val intent = Intent(activity!!, TownActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }
    }


    //****************************************RadioButtons**************************************************
    override fun setRadioGroupListener() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->

           // if (open == 1) {
                if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                    Constants.showPopup(activity!!)
                    disablePickers()
                    return@setOnCheckedChangeListener
                }
            //}
            open = 1
            radioButtonClick = 1
            from = "set"
            when (checkedId) {

                R.id.mon -> {
                    day = "Monday"
                    /* openStartTime = "0AM"
                     closeEndTime = "0PM"
                     openEndTime = "0PM"
                     closeStartTime = "0PM"*/
                    try {

                        if (timeList[0].status) {
                            setLayout(true)
                            closeBox.isChecked = true
                            timeList[0].startFrom = "12:00 AM"
                            timeList[0].startTo = "12:00 AM"
                            timeList[0].closeFrom = "12:00 AM"
                            timeList[0].closeTo = "12:00 AM"
                        } else {
                            setLayout(false)
                            closeBox.isChecked = false
                            if (timeList[0].closeStatus) {
                                setCloseLayout(true)
                                timeList[0].closeFrom = "12:00 AM"
                                timeList[0].closeTo = "12:00 AM"
                            } else {
                                setCloseLayout(false)
                            }

                        }
                        openHoursStart.text = timeList[0].startFrom
                        closeStartHours.text = timeList[0].closeFrom
                        openHoursEnd.text = timeList[0].startTo
                        closeEndHours.text = timeList[0].closeTo
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
//                    closeTimeLayout.visibility = View.VISIBLE
//                    closeBox.isChecked = Constants.closedList[0]
//
//                    try {
//                        if (timeList[0].startFrom == "closed") {
//                            closeBox.isChecked = true
//                            disablePickers()
//                            openStartTime = "12:00 AM"
//                            closeEndTime = "12:00 AM"
//                            openEndTime = "12:00 AM"
//                            closeStartTime = "12:00 AM"
//
//                            openHoursStart.text = "12:00 AM"
//                            closeStartHours.text = "12:00 AM"
//                            openHoursEnd.text = "12:00 AM"
//                            closeEndHours.text = "12:00 AM"
//                        } else {
//                            closeBox.isChecked = false
//                            enablePickers()
//                            openHoursStart.text = timeList[0].startFrom
//                            closeStartHours.text = timeList[0].closeFrom
//                            openHoursEnd.text = timeList[0].startTo
//                            closeEndHours.text = timeList[0].closeTo
//
//
//                        }
//                    } catch (e: Exception) {
//                        openHoursStart.text = "12:00 AM"
//                        closeStartHours.text = "12:00 AM"
//                        openHoursEnd.text = "12:00 AM"
//                        closeEndHours.text = "12:00 AM"
//                    }

                }
                R.id.tues -> {
                    day = "Tuesday"
//                    openStartTime = "12:00 AM"
//                    closeEndTime = "12:00 AM"
//                    openEndTime = "12:00 AM"
//                    closeStartTime = "12:00 AM"
//                    closeTimeLayout.visibility = View.VISIBLE
//                    closeBox.isChecked = Constants.closedList[1]
//                    try {
//                        if (timeList[1].startFrom == "closed") {
//                            closeBox.isChecked = true
//                            disablePickers()
//                            openStartTime = "12:00 AM"
//                            closeEndTime = "12:00 AM"
//                            openEndTime = "12:00 AM"
//                            closeStartTime = "12:00 AM"
//
//                            openHoursStart.text = "12:00 AM"
//                            closeStartHours.text = "12:00 AM"
//                            openHoursEnd.text = "12:00 AM"
//                            closeEndHours.text = "12:00 AM"
//                        } else {
//                            closeBox.isChecked = false
//                            openHoursStart.text = timeList[1].startFrom
//                            closeStartHours.text = timeList[1].closeFrom
//                            openHoursEnd.text = timeList[1].startTo
//                            closeEndHours.text = timeList[1].closeTo
//                            enablePickers()
//                        }
//                    } catch (e: Exception) {
//                        openHoursStart.text = "12:00 AM"
//                        closeStartHours.text = "12:00 AM"
//                        openHoursEnd.text = "12:00 AM"
//                        closeEndHours.text = "12:00 AM"
//                    }
//
                    try {

                        if (timeList[1].status) {
                            setLayout(true)
                            closeBox.isChecked = true
                            timeList[1].startFrom = "12:00 AM"
                            timeList[1].startTo = "12:00 AM"
                            timeList[1].closeFrom = "12:00 AM"
                            timeList[1].closeTo = "12:00 AM"
                        } else {
                            setLayout(false)
                            closeBox.isChecked = false
                            if (timeList[1].closeStatus) {
                                setCloseLayout(true)
                                timeList[1].closeFrom = "12:00 AM"
                                timeList[1].closeTo = "12:00 AM"
                            } else {
                                setCloseLayout(false)
                            }

                        }
                        openHoursStart.text = timeList[1].startFrom
                        closeStartHours.text = timeList[1].closeFrom
                        openHoursEnd.text = timeList[1].startTo
                        closeEndHours.text = timeList[1].closeTo
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                R.id.wed -> {
                    day = "Wednesday"
//                    openStartTime = "12:00 AM"
//                    closeEndTime = "12:00 AM"
//                    openEndTime = "12:00 AM"
//                    closeStartTime = "12:00 AM"
//                    closeTimeLayout.visibility = View.VISIBLE
//                    closeBox.isChecked = Constants.closedList[2]
//
//                    try {
//                        if (timeList[2].startFrom == "closed") {
//                            closeBox.isChecked = true
//                            disablePickers()
//                            openStartTime = "12:00 AM"
//                            closeEndTime = "12:00 AM"
//                            openEndTime = "12:00 AM"
//                            closeStartTime = "12:00 AM"
//
//                            openHoursStart.text = "12:00 AM"
//                            closeStartHours.text = "12:00 AM"
//                            openHoursEnd.text = "12:00 AM"
//                            closeEndHours.text = "12:00 AM"
//                        } else {
//                            closeBox.isChecked = false
//                            openHoursStart.text = timeList[2].startFrom
//                            closeStartHours.text = timeList[2].closeFrom
//                            openHoursEnd.text = timeList[2].startTo
//                            closeEndHours.text = timeList[2].closeTo
//
//                            enablePickers()
//                        }
//                    } catch (e: Exception) {
//                        openHoursStart.text = "12:00 AM"
//                        closeStartHours.text = "12:00 AM"
//                        openHoursEnd.text = "12:00 AM"
//                        closeEndHours.text = "12:00 AM"
//                    }
                    try {

                        if (timeList[2].status) {
                            setLayout(true)
                            closeBox.isChecked = true
                            timeList[2].startFrom = "12:00 AM"
                            timeList[2].startTo = "12:00 AM"
                            timeList[2].closeFrom = "12:00 AM"
                            timeList[2].closeTo = "12:00 AM"
                        } else {
                            setLayout(false)
                            closeBox.isChecked = false
                            if (timeList[2].closeStatus) {
                                setCloseLayout(true)
                                timeList[2].closeFrom = "12:00 AM"
                                timeList[2].closeTo = "12:00 AM"
                            } else {
                                setCloseLayout(false)
                            }

                        }
                        openHoursStart.text = timeList[2].startFrom
                        closeStartHours.text = timeList[2].closeFrom
                        openHoursEnd.text = timeList[2].startTo
                        closeEndHours.text = timeList[2].closeTo
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                R.id.thu -> {
                    day = "Thursday"
//                    openStartTime = "12:00 AM"
//                    closeEndTime = "12:00 AM"
//                    openEndTime = "12:00 AM"
//                    closeStartTime = "12:00 AM"
//                    closeTimeLayout.visibility = View.VISIBLE
//                    closeBox.isChecked = Constants.closedList[3]
//                    try {
//                        if (timeList[3].startFrom == "closed") {
//                            closeBox.isChecked = true
//                            disablePickers()
//                            openStartTime = "12:00 AM"
//                            closeEndTime = "12:00 AM"
//                            openEndTime = "12:00 AM"
//                            closeStartTime = "12:00 AM"
//
//                            openHoursStart.text = "12:00 AM"
//                            closeStartHours.text = "12:00 AM"
//                            openHoursEnd.text = "12:00 AM"
//                            closeEndHours.text = "12:00 AM"
//                        } else {
//                            closeBox.isChecked = false
//                            openHoursStart.text = timeList[3].startFrom
//                            closeStartHours.text = timeList[3].closeFrom
//                            openHoursEnd.text = timeList[3].startTo
//                            closeEndHours.text = timeList[3].closeTo
//
//                            enablePickers()
//                        }
//                    } catch (e: Exception) {
//                        closeBox.isChecked = false
//                        openHoursStart.text = "12:00 AM"
//                        closeStartHours.text = "12:00 AM"
//                        openHoursEnd.text = "12:00 AM"
//                        closeEndHours.text = "12:00 AM"
//                    }
                    try {
                        if (timeList[3].status) {
                            setLayout(true)
                            closeBox.isChecked = true

                            timeList[3].startFrom = "12:00 AM"
                            timeList[3].startTo = "12:00 AM"
                            timeList[3].closeFrom = "12:00 AM"
                            timeList[3].closeTo = "12:00 AM"
                        } else {
                            closeBox.isChecked = false
                            setLayout(false)
                            if (timeList[3].closeStatus) {
                                setCloseLayout(true)
                                timeList[3].closeFrom = "12:00 AM"
                                timeList[3].closeTo = "12:00 AM"
                            } else {
                                setCloseLayout(false)

                            }

                        }

                        openHoursStart.text = timeList[3].startFrom
                        openHoursEnd.text = timeList[3].startTo
                        closeStartHours.text = timeList[3].closeFrom
                        closeEndHours.text = timeList[3].closeTo
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                R.id.fri -> {
                    day = "Friday"
//                    openStartTime = "12:00 AM"
//                    closeEndTime = "12:00 AM"
//                    openEndTime = "12:00 AM"
//                    closeStartTime = "12:00 AM"
//                    closeTimeLayout.visibility = View.VISIBLE
//                    closeBox.isChecked = Constants.closedList[4]
//
//                    try {
//                        if (timeList[4].startFrom == "closed") {
//                            closeBox.isChecked = true
//                            disablePickers()
//                            openStartTime = "12:00 AM"
//                            closeEndTime = "12:00 AM"
//                            openEndTime = "12:00 AM"
//                            closeStartTime = "12:00 AM"
//
//                            openHoursStart.text = "12:00 AM"
//                            closeStartHours.text = "12:00 AM"
//                            openHoursEnd.text = "12:00 AM"
//                            closeEndHours.text = "12:00 AM"
//                        } else {
//                            closeBox.isChecked = false
//                            openHoursStart.text = timeList[4].startFrom
//                            closeStartHours.text = timeList[4].closeFrom
//                            openHoursEnd.text = timeList[4].startTo
//                            closeEndHours.text = timeList[4].closeTo
//
//                            enablePickers()
//                        }
//                    } catch (e: Exception) {
//                        openHoursStart.text = "12:00 AM"
//                        closeStartHours.text = "12:00 AM"
//                        openHoursEnd.text = "12:00 AM"
//                        closeEndHours.text = "12:00 AM"
//                    }
                    try {

                        if (timeList[4].status) {
                            setLayout(true)
                            closeBox.isChecked = true
                            timeList[4].startFrom = "12:00 AM"
                            timeList[4].startTo = "12:00 AM"
                            timeList[4].closeFrom = "12:00 AM"
                            timeList[4].closeTo = "12:00 AM"
                        } else {
                            closeBox.isChecked = false
                            setLayout(false)
                            if (timeList[4].closeStatus) {
                                setCloseLayout(true)
                                timeList[4].closeFrom = "12:00 AM"
                                timeList[4].closeTo = "12:00 AM"
                            } else {
                                setCloseLayout(false)
                            }
                        }
                        openHoursStart.text = timeList[4].startFrom
                        closeStartHours.text = timeList[4].closeFrom
                        openHoursEnd.text = timeList[4].startTo
                        closeEndHours.text = timeList[4].closeTo
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    // }
                }
                R.id.sat -> {
                    day = "Saturday"
//                    openStartTime = "12:00 AM"
//                    closeEndTime = "12:00 AM"
//                    openEndTime = "12:00 AM"
//                    closeStartTime = "12:00 AM"
//                    closeTimeLayout.visibility = View.VISIBLE
//                    closeBox.isChecked = Constants.closedList[5]
//
//                    try {
//                        if (timeList[5].startFrom == "closed") {
//                            closeBox.isChecked = true
//                            disablePickers()
//                            openStartTime = "12:00 AM"
//                            closeEndTime = "12:00 AM"
//                            openEndTime = "12:00 AM"
//                            closeStartTime = "12:00 AM"
//
//                            openHoursStart.text = "12:00 AM"
//                            closeStartHours.text = "12:00 AM"
//                            openHoursEnd.text = "12:00 AM"
//                            closeEndHours.text = "12:00 AM"
//                        } else {
//                            closeBox.isChecked = false
//                            openHoursStart.text = timeList[5].startFrom
//                            closeStartHours.text = timeList[5].closeFrom
//                            openHoursEnd.text = timeList[5].startTo
//                            closeEndHours.text = timeList[5].closeTo
//
//                            enablePickers()
//                        }
//                    } catch (e: Exception) {
//                        openHoursStart.text = "12:00 AM"
//                        closeStartHours.text = "12:00 AM"
//                        openHoursEnd.text = "12:00 AM"
//                        closeEndHours.text = "12:00 AM"
//                    }
                    try {

                        if (timeList[5].status) {
                            setLayout(true)
                            closeBox.isChecked = true
                            timeList[5].startFrom = "12:00 AM"
                            timeList[5].startTo = "12:00 AM"
                            timeList[5].closeFrom = "12:00 AM"
                            timeList[5].closeTo = "12:00 AM"
                        } else {
                            closeBox.isChecked = false
                            setLayout(false)
                            if (timeList[5].closeStatus) {
                                setCloseLayout(true)
                                timeList[5].closeFrom = "12:00 AM"
                                timeList[5].closeTo = "12:00 AM"
                            } else {
                                setCloseLayout(false)
                            }

                        }
                        openHoursStart.text = timeList[5].startFrom
                        closeStartHours.text = timeList[5].closeFrom
                        openHoursEnd.text = timeList[5].startTo
                        closeEndHours.text = timeList[5].closeTo
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    // }
                }
                R.id.sun -> {
                    day = "Sunday"
//                    openStartTime = "12:00 AM"
//                    closeEndTime = "12:00 AM"
//                    openEndTime = "12:00 AM"
//                    closeStartTime = "12:00 AM"
//                    closeTimeLayout.visibility = View.VISIBLE
//                    closeBox.isChecked = Constants.closedList[6]
//                    try {
//                        if (timeList[6].startFrom == "closed") {
//                            closeBox.isChecked = true
//                            disablePickers()
//                            openStartTime = "12:00 AM"
//                            closeEndTime = "12:00 AM"
//                            openEndTime = "12:00 AM"
//                            closeStartTime = "12:00 AM"
//
//                            openHoursStart.text = "12:00 AM"
//                            closeStartHours.text = "12:00 AM"
//                            openHoursEnd.text = "12:00 AM"
//                            closeEndHours.text = "12:00 AM"
//                        } else {
//                            closeBox.isChecked = false
//                            openHoursStart.text = timeList[6].startFrom
//                            closeStartHours.text = timeList[6].closeFrom
//                            openHoursEnd.text = timeList[6].startTo
//                            closeEndHours.text = timeList[6].closeTo
//
//                            enablePickers()
//                        }
//                    } catch (e: Exception) {
//                        openHoursStart.text = "12:00 AM"
//                        closeStartHours.text = "12:00 AM"
//                        openHoursEnd.text = "12:00 AM"
//                        closeEndHours.text = "12:00 AM"
//                    }
                    try {

                        if (timeList[6].status) {
                            setLayout(true)
                            closeBox.isChecked = true
                            timeList[6].startFrom = "12:00 AM"
                            timeList[6].startTo = "12:00 AM"
                            timeList[6].closeFrom = "12:00 AM"
                            timeList[6].closeTo = "12:00 AM"
                        } else {
                            closeBox.isChecked = false
                            setLayout(false)
                            if (timeList[6].closeStatus) {
                                setCloseLayout(true)
                                timeList[6].closeFrom = "12:00 AM"
                                timeList[6].closeTo = "12:00 AM"
                            } else {
                                setCloseLayout(false)
                            }

                        }
                        openHoursStart.text = timeList[6].startFrom
                        closeStartHours.text = timeList[6].closeFrom
                        openHoursEnd.text = timeList[6].startTo
                        closeEndHours.text = timeList[6].closeTo
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            from = "click"
        }
    }

    private fun setCloseLayout(b: Boolean) {
        if (b) {
            closeTimeLayout.isEnabled = false
            closeTimeLayout.isClickable = false
            closeEndHours.isClickable = false
            closeStartHours.isClickable = false
            closeEndHours.setBackgroundColor(resources.getColor(R.color.light_grey))
            closeStartHours.setBackgroundColor(resources.getColor(R.color.light_grey))
            // setDataAccDays("")
//            callCrossButton(false)
        } else {
            //  setDataAccDays("")
//            callCrossButton(true)

            closeTimeLayout.isEnabled = true
            closeTimeLayout.isClickable = true
            closeEndHours.isClickable = true
            closeStartHours.isClickable = true
            closeStartHours.setBackgroundDrawable(resources.getDrawable(R.drawable.timer_value_bg))
            closeEndHours.setBackgroundDrawable(resources.getDrawable(R.drawable.timer_value_bg))
        }

    }

    private fun setLayout(b: Boolean) {
        from = "set"
        if (b) {
            close = true
            transLayout.visibility = View.VISIBLE
            setClosedAccToDays()
            disablePickers()
            // setData(true)

            // setDataAccDays(checks)

        } else {
            close = false
            transLayout.visibility = View.GONE
            setOpenAccToDays()
            enablePickers()
            //setData(false)
            // setDataAccDays(checks)
        }
        from = "click"
    }


    //******************************************************************************************

    override fun setSingleBusinessPrefilledData() {
        from = "set"
        val data = Constants.getPrefs(activity!!)?.getString(Constants.SINGLE_BUSINESS_LIST, "")!!
        list = Constants.getSingleBusinessData(data)
        try {
            if (list != null) {
                intitalSetting = false
                mPresenter.setInitialTimeData(arr)
                for (i in 0 until list!!.size) {
                    model = list!![i]
                    addresss.text = model.address
                    bus_name.setText(model.business_name)
                    Constants.getPrefs(activity!!)?.edit()!!.putString(Constants.MY_BUSINESS_NAME, model.business_name).apply()
                    number.setText(model.contact_no)
                    lat = model.lat
                    long = model.long
                    if (model.description!="[]") {
                        bus_desc.setText(model.description)
                    }else{
                        bus_desc.setText("")
                    }

                    try {
                        Glide.with(activity!!)
                                .load(model.cover_image)
                                .apply(RequestOptions()
                                        .centerCrop()
                                        .placeholder(R.mipmap.placeholder))
                                .into(Ivcover)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                    // val town = Constants.getPrefs(activity!!)?.getString(Constants.TOWN_NAME, "")!!
                    ///   val cat = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_NAME, "")!!
                    getCatAndLocData()
                    try {
                        val town_id = model.town_id
                        val cat_id = model.category_id
                        var savedCatergory = ""
                        val mainCat = cat_id.split(",")
                        for (i1 in 0 until cat_list.size) {
                            for (j in 0 until mainCat.size)
                                if (cat_list[i1].id == mainCat[j]) {
                                    if (savedCatergory.isEmpty()) {
                                        savedCatergory = cat_list[i1].name
                                    } else {
                                        savedCatergory = savedCatergory + ", " + cat_list[i1].name
                                    }


                                }
                        }
                        select_category.text = savedCatergory
                        for (i2 in 0 until loc_list.size) {
                            if (loc_list[i2].id == town_id) {
                                select_town.text = loc_list[i2].townname
                                Constants.getPrefs(activity!!)?.edit()?.putString(Constants.TOWN_ID2, loc_list[i2].id)?.apply()
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    val business_user_id = model.business_user_id

                    try {

                        preLoadImages = model.image_url.toMutableList()
                        for (str in preLoadImages) {
                            val uriStr = Uri.parse(str)
                            OldImages!!.add(uriStr)
                        }
                        if (OldImages!!.size > 0) {
                            type = "old"
                            mPresenter.setAdapterForCroppedImages(OldImages, CroppedImages!!, preLoadImages, type)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                    try {
                        array = model.opening_time
                        arr = JSONArray(array)
                        try {
                            val objMon = arr.getJSONObject(0)
                            if (objMon!!.optString("status") == "true") {
                                closeBox.isChecked = true
                                disablePickers()
                            } else {
                                enablePickers()
                                if (objMon.optString("closeStatus") == "true") {
//                                    val date = changeTimeFormat(objMon.optString("startFrom"))
//                                    if (date != null && !date.isEmpty()) {
//                                        openHoursStart.text = date
//                                    } else {
//                                        openHoursStart.text = objMon.optString("startFrom")
//                                    }

                                    openHoursStart.text = objMon.optString("startFrom")
                                    openHoursEnd.text = objMon.optString("startTo")
                                    closeStartHours.text = "12:00 AM"
                                    closeEndHours.text = "12:00 AM"


                                    closeTimeLayout.isEnabled = false
                                    closeTimeLayout.isClickable = false
                                    closeEndHours.isClickable = false
                                    closeStartHours.isClickable = false
                                    closeEndHours.setBackgroundColor(resources.getColor(R.color.light_grey))
                                    closeStartHours.setBackgroundColor(resources.getColor(R.color.light_grey))

                                } else {
                                    openHoursStart.text = objMon.optString("startFrom")
                                    openHoursEnd.text = objMon.optString("startTo")
                                    closeStartHours.text = objMon.optString("closeFrom")
                                    closeEndHours.text = objMon.optString("closeTo")
                                }
//                                if (objMon.optString("closeFrom") != "closed") {
//                                    val date = changeTimeFormat(objMon.optString("closeFrom"))
//                                    if (date != null && !date.isEmpty()) {
//                                        closeStartHours.text = date
//                                    } else {
//                                        closeStartHours.text = objMon.optString("closeFrom")
//                                    }
//                                } else {
//                                    closeStartHours.text = "12:00 AM"
//                                    closeTimeLayout.isEnabled = false
//                                    closeTimeLayout.isClickable = false
//                                    closeEndHours.isClickable = false
//                                    closeStartHours.isClickable = false
//                                    closeEndHours.text = "12:00 AM"
//                                    closeStartHours.text = "12:00 AM"
//                                    closeEndHours.setBackgroundColor(resources.getColor(R.color.light_grey))
//                                    closeStartHours.setBackgroundColor(resources.getColor(R.color.light_grey))
//                                }


                            }

                            try {
                                for (i1 in 0 until arr.length()) {
                                    val obj = arr.getJSONObject(i1)
                                    timeList[i1].startFrom = obj.optString("startFrom")
                                    timeList[i1].startTo = obj.optString("startTo")
                                    timeList[i1].closeFrom = obj.optString("closeFrom")
                                    timeList[i1].closeTo = obj.optString("closeTo")
                                    val status = obj.optString("status")
                                    val closeStatus = obj.optString("closeStatus")
                                    if (status == "true") {
                                        // timeList[i1].close = "true"
                                        Constants.closedList[i1] = true
                                        timeList[i1].status = true
                                    } else {
                                        Constants.closedList[i1] = false
                                        timeList[i1].status = false
                                    }
                                    if (closeStatus == "true") {
                                        // timeList[i1].close = "true"
                                        Constants.closedList[i1] = true
                                        timeList[i1].closeStatus = true
                                    } else {
                                        Constants.closedList[i1] = false
                                        timeList[i1].closeStatus = false
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                from = "click"
            } else {
                intitalSetting = true
                mPresenter.setInitialTimeData(arr)
                from = "click"
            }
        } catch (E: Exception) {
            E.printStackTrace()
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            //  updateToolbarOption=context as hideToolBarOption
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            //    updateToolbarOption=activity as hideToolBarOption
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun updateHorizontalImageList(position: Int) {
//        if (AllImages!=null) {
//            AllImages!!.removeAt(position)
//        }
    }

    interface hideToolBarOption {
        fun hideRightToolbarIcon()
    }

    override fun getUserType(): String {
        return Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "")
    }
}
