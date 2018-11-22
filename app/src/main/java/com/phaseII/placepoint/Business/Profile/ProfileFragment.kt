package com.phaseII.placepoint.Business.Profile


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


open class ProfileFragment : Fragment(), BusinessProfileHelper, HorzRecyclerAdapter.UpdateImageList {
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
        Constants.getSSlCertificate(activity!!)
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

    override fun onDestroy() {
        super.onDestroy()
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
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
            bus_desc.isFocusableInTouchMode = false
            emailIdText.isFocusableInTouchMode = false
            disablePickers()
            disableTitles()
        } else if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "2") {
            packageName.text = "Standard Package"
            desc_layout.isClickable = false
            bus_desc.isFocusableInTouchMode = true
            emailIdText.isFocusableInTouchMode = true
            enablePickers()
            enableTitles()
        } else if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "1") {
            packageName.text = "Premium Package"
            desc_layout.isClickable = false
            bus_desc.isFocusableInTouchMode = true
            emailIdText.isFocusableInTouchMode = true
            change.visibility = View.GONE
            enablePickers()
            enableTitles()
        } else if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "4") {
            packageName.text = "Admin"
            desc_layout.isClickable = false
            bus_desc.isFocusableInTouchMode = true
            emailIdText.isFocusableInTouchMode = true
            change.visibility = View.GONE
            enablePickers()
            enableTitles()
        }

        value = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_NAMES, "")!!
        if (value != null && !value.isEmpty()) {
            select_category.text = value
        }

        emailIdText.setText(Constants.getPrefs(activity!!)?.getString(Constants.EMAIL, ""))
    }

    private fun setCategory() {
        var savedCategory = ""
        val mainCatValue = Constants.getPrefs(activity!!)!!.getString(Constants.MAIN_CATEGORY, "")
        if (!mainCatValue.isEmpty()) {
            val mainCat = mainCatValue.split(",")
            val arrayName = arrayListOf<String>()
            out@ for (p in 0 until mainCat.size) {
                inn@ for (q in 0 until cat_list.size) {
                    if (mainCat[p] == cat_list[q].id) {
                        arrayName.add(cat_list[q].name)
                        savedCategory = if (savedCategory.isEmpty()) {
                            cat_list[q].name
                        } else {
                            savedCategory + ", " + cat_list[q].name
                        }

                    }
                }
            }
        }
        select_category.text = savedCategory
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
            val sortedList = cat_list.sortedWith(compareBy { it.name })
            val array = arrayOfNulls<ModelCategoryData>(sortedList.size)
            cat_list.toArray(array)
            loc_list = Constants.getTownData(loc)!!
            val hashSet2 = HashSet<ModelTown>()
            hashSet2.addAll(loc_list)
            loc_list.clear()
            loc_list.addAll(hashSet2)
            val sortedList1 = loc_list.sortedWith(compareBy { it.townname })
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


    @SuppressLint("SetTextI18n")
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
                mPresenter.openCamera()
            }
        }
        gallery.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                return@setOnClickListener
            }
            if (AllImages != null) {
                mPresenter.openGallery()
            }
        }
        emailIdText.setOnClickListener {
            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                emailIdText.isFocusableInTouchMode = false
                emailIdText.inputType = InputType.TYPE_NULL
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
            if (closeTimeLayout.isEnabled) {
                closeTimeLayout.isEnabled = false
                closeTimeLayout.isClickable = false
                closeEndHours.isClickable = false
                closeStartHours.isClickable = false
                closeEndHours.text = "12:00 AM"
                closeStartHours.text = "12:00 AM"
                closeEndHours.setBackgroundColor(resources.getColor(R.color.light_grey))
                closeStartHours.setBackgroundColor(resources.getColor(R.color.light_grey))
                callCrossButton(true)
            } else {
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
        closeBox.setOnCheckedChangeListener { buttonView, isChecked ->

            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                closeBox.isChecked = false
                return@setOnCheckedChangeListener
            }
            if (from != "set") {
                if (isChecked) {

                    close = true
                    transLayout.visibility = View.VISIBLE
                    setClosedAccToDays()
                    disablePickers()
                    setData(true)
                } else {
                    close = false
                    transLayout.visibility = View.GONE
                    setOpenAccToDays()
                    enablePickers()
                    setData(false)
                }
            }
        }

    }

    private fun locationOnOFF(context: Context) {

        var lm: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (q: Exception) {
        }

        if (!gpsEnabled && !networkEnabled) {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Alert")
            dialog.setMessage(("Your location is turned Off"))
            dialog.setPositiveButton(("Turn On"), DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
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
            timeList[0].closeStatus = b
        }
        if (day == "Tuesday") {
            timeList[1].closeFrom = "12:00 AM"
            timeList[1].closeTo = "12:00 AM"
            timeList[1].closeStatus = b
        }
        if (day == "Wednesday") {
            timeList[2].closeFrom = "12:00 AM"
            timeList[2].closeTo = "12:00 AM"
            timeList[2].closeStatus = b
        }
        if (day == "Thursday") {
            timeList[3].closeFrom = "12:00 AM"
            timeList[3].closeTo = "12:00 AM"
            timeList[3].closeStatus = b
        }
        if (day == "Friday") {
            timeList[4].closeFrom = "12:00 AM"
            timeList[4].closeTo = "12:00 AM"
            timeList[4].closeStatus = b
        }
        if (day == "Saturday") {
            timeList[5].closeFrom = "12:00 AM"
            timeList[5].closeTo = "12:00 AM"
            timeList[5].closeStatus = b
        }
        if (day == "Sunday") {
            timeList[6].closeFrom = "12:00 AM"
            timeList[6].closeTo = "12:00 AM"
            timeList[6].closeStatus = b
        }
    }


    private fun setData(b: Boolean) {
        try {
            if (day == "Monday") {
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

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun showOpenStartTimePicker(day: String) {


        val values = arrayOf("AM", "PM")
        var amPm = 0
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

        numberPicker.setOnValueChangedListener { _, _, _ ->
        }

        d.setPositiveButton("Done") { dialogInterface, _ ->
            hours = numberPicker.value
            minutes = numberPickerMin.value
            amPm = numberPicker2.value
            val am = values[amPm]
            val form = DecimalFormat("00")
            openStartTime = hours.toString() + ":" + form.format(minutes) + " " + am
            openHoursStart.text = openStartTime
            checks = "openstart"
            editTimeList(day, openStartTime, "startFrom")
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
        var amPm = 0
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

        numberPicker.setOnValueChangedListener { _, _, _ ->
        }
        d.setPositiveButton("Done") { dialogInterface, _ ->
            hours = numberPicker.value
            minutes = numberPickerMin.value
            amPm = numberPicker2.value
            val am = values[amPm]
            val form = DecimalFormat("00")
            openEndTime = hours.toString() + ":" + form.format(minutes) + " " + am
            openHoursEnd.text = openEndTime
            checks = "openend"
            editTimeList(day, openEndTime, "startTo")
            dialogInterface.dismiss()
        }
        d.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
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
        var amPm = 0
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
        d.setPositiveButton("Done") { dialogInterface, i ->
            hours = numberPicker.value
            minutes = numberPickerMin.value
            amPm = numberPicker2.value
            val am: String = values[amPm]
            val form = DecimalFormat("00")
            closeStartTime = hours.toString() + ":" + form.format(minutes) + " " + am
            closeStartHours.text = closeStartTime
            checks = "closestart"
            editTimeList(day, closeStartTime, "closeFrom")
            dialogInterface.dismiss()
        }
        d.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        val alertDialog = d.create()
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun showCloseEndTimePicker() {
        var amPm = 0
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
        numberPicker2.wrapSelectorWheel = false
        numberPicker2.maxValue = 1
        numberPicker2.minValue = 0

        numberPicker.setOnValueChangedListener { numberPicker, i, i1 ->
        }

        d.setPositiveButton("Done") { dialogInterface, i ->
            hours = numberPicker.value
            minutes = numberPickerMin.value
            amPm = numberPicker2.value
            val am = numberPicker2.displayedValues[amPm]
            val form = DecimalFormat("00")
            closeEndTime = hours.toString() + ":" + form.format(minutes) + " " + am
            closeEndHours.text = closeEndTime
            checks = "closeend"
            editTimeList(day, closeEndTime, "closeTo")
            dialogInterface.dismiss()
        }
        d.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
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
                        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 9) {
            if (data != null) {
                value = data.getStringExtra("result")
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
                type = "new"
                mPresenter.setAdapterForCroppedImages(AllImages, CroppedImages!!, preLoadImages, type)

            } catch (e: Exception) {
                e.printStackTrace()
            }


        } else if (requestCode == 7) {
            try {
                val args = data!!.getBundleExtra("BUNDLE")
                CoverFromCropper = (args.getSerializable("ARRAYLIST") as java.util.ArrayList<Uri>?)
                val b = CoverFromCropper!![0]
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
        builder.show()


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

    override fun getPreFillTownId(): String {
        return model.town_id
    }

    override fun getPreFillCatId(): String {
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
        return jsonContact
    }

    override fun getClosingHours(): String {
        val timeList1: ArrayList<ModelTimeString> = arrayListOf()
        for (i in 0 until timeList.size) {
            val model = ModelTimeString()
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

    @SuppressLint("SetTextI18n")
    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
        progressBar2.visibility = View.VISIBLE
        save.text = "saving to the server..."
    }

    override fun setClickFalse() {
        isServiceRunning = false
    }

    override fun setClickTrue() {
        isServiceRunning = true
    }

    @SuppressLint("SetTextI18n")
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
        return gsonBuilder.toJson(preLoadImages)
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
            intent.putExtra("subscriptionType", Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, ""))
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        isServiceRunning = false
    }

    override fun showNoImageMessage(s: String) {
        Toast.makeText(activity!!, s, Toast.LENGTH_SHORT).show()
    }

    override fun setPreFilledData() {
        val busName = Constants.getPrefs(activity!!)?.getString(Constants.MY_BUSINESS_NAME, "")
        bus_name.setText(busName)
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

    override fun setAddressError(s: String) {
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

    @SuppressLint("SetTextI18n")
    override fun setBusinessPreFilledData(data: String, end_time: String, user_type: String, bid: String) {
        if (activity != null && isAdded) {
            if (end_time != "N/A") {

                try {
                    val currentTime = getCurrentTime()
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val start = sdf.parse(currentTime)
                    val end = sdf.parse(end_time)
                    if (start.after(end)) {
                        expirePlanOn.text = "Expired on- " + parseDateToFormat(end_time)
                    } else {
                        expirePlanOn.text = "Expires on- " + parseDateToFormat(end_time)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.SINGLE_BUSINESS_LIST, data)?.apply()
            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.USERTYPE, user_type)?.apply()
            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.MYBUSINESS_ID, bid)?.apply()
            mPresenter.setSingleBusinessPrefilledData()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date())
    }


    private var initialSetting: Boolean = false

    private var list: java.util.ArrayList<SingleBusinessModel>? = arrayListOf()

    fun parseDateToFormat(time: String): String? {
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
    }

    private fun setClosedAccToDays() {
        when (day) {
            "Monday" ->
                Constants.closedList[0] = close
            "Tuesday" ->
                Constants.closedList[1] = close
            "Wednesday" ->
                Constants.closedList[2] = close
            "Thursday" ->
                Constants.closedList[3] = close
            "Friday" ->
                Constants.closedList[4] = close
            "Saturday" ->
                Constants.closedList[5] = close
            "Sunday" ->
                Constants.closedList[6] = close
        }
    }

    private fun setOpenAccToDays() {
        when (day) {
            "Monday" -> Constants.closedList[0] = close
            "Tuesday" -> Constants.closedList[1] = close
            "Wednesday" -> Constants.closedList[2] = close
            "Thursday" -> Constants.closedList[3] = close
            "Friday" -> Constants.closedList[4] = close
            "Saturday" -> Constants.closedList[5] = close
            "Sunday" -> Constants.closedList[6] = close
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
            word_count.text = s.length.toString() + "/" + MAX_WORDS

        }

        override fun afterTextChanged(s: Editable) {}
    }
    val mTextEditorWatcher2 = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
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

                }
                .setNegativeButton("Cancel"
                ) { dialog, which -> dialog.dismiss() }.create()
        builder.show()

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
    }

    private fun getRealPathFromURI(uri: Uri?): String {
        var cursor = activity!!.contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        var documentId = cursor.getString(0)
        documentId = documentId.substring(documentId.lastIndexOf(":") + 1)
        cursor.close()
        cursor = activity!!.contentResolver.query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", arrayOf(documentId), null)
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
            val builder: Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Builder(activity!!, android.R.style.Theme_Material_Dialog_Alert)
            } else {
                Builder(activity!!)
            }
            builder.setTitle("Alert!")
                    .setMessage("User Inactive")
                    .setPositiveButton("Ok") { dialog, which ->
                        Constants.getPrefs(activity!!)?.edit()!!.putBoolean(Constants.LOGIN, false)!!.apply()
                        Constants.getPrefs(activity!!)!!.edit().remove(Constants.LOGGED).apply()
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

            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
                Constants.showPopup(activity!!)
                disablePickers()
                return@setOnCheckedChangeListener
            }
            open = 1
            radioButtonClick = 1
            from = "set"
            when (checkedId) {

                R.id.mon -> {
                    day = "Monday"
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
                }
                R.id.tues -> {
                    day = "Tuesday"
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
                }
                R.id.sat -> {
                    day = "Saturday"
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
        } else {
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
        } else {
            close = false
            transLayout.visibility = View.GONE
            setOpenAccToDays()
            enablePickers()
        }
        from = "click"
    }


    //******************************************************************************************

    @SuppressLint("CommitPrefEdits")
    override fun setSingleBusinessPreFilledData() {
        from = "set"
        val data = Constants.getPrefs(activity!!)?.getString(Constants.SINGLE_BUSINESS_LIST, "")!!
        list = Constants.getSingleBusinessData(data)
        try {
            if (list != null) {
                initialSetting = false
                mPresenter.setInitialTimeData(arr)
                for (i in 0 until list!!.size) {
                    model = list!![i]
                    addresss.text = model.address
                    bus_name.setText(model.business_name)
                    Constants.getPrefs(activity!!)?.edit()!!.putString(Constants.MY_BUSINESS_NAME, model.business_name).apply()
                    number.setText(model.contact_no)
                    lat = model.lat
                    long = model.long
                    if (model.description != "[]") {
                        bus_desc.setText(model.description)
                    } else {
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
                    getCatAndLocData()
                    try {
                        val townId = model.town_id
                        val catId = model.category_id
                        var savedCategory = ""
                        val mainCat = catId.split(",")
                        for (i1 in 0 until cat_list.size) {
                            for (j in 0 until mainCat.size)
                                if (cat_list[i1].id == mainCat[j]) {
                                    savedCategory = if (savedCategory.isEmpty()) {
                                        cat_list[i1].name
                                    } else {
                                        savedCategory + ", " + cat_list[i1].name
                                    }


                                }
                        }
                        select_category.text = savedCategory
                        for (i2 in 0 until loc_list.size) {
                            if (loc_list[i2].id == townId) {
                                select_town.text = loc_list[i2].townname
                                Constants.getPrefs(activity!!)?.edit()?.putString(Constants.TOWN_ID2, loc_list[i2].id)?.apply()
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

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
                                        Constants.closedList[i1] = true
                                        timeList[i1].status = true
                                    } else {
                                        Constants.closedList[i1] = false
                                        timeList[i1].status = false
                                    }
                                    if (closeStatus == "true") {
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
                initialSetting = true
                mPresenter.setInitialTimeData(arr)
                from = "click"
            }
        } catch (E: Exception) {
            E.printStackTrace()
        }

    }

    override fun updateHorizontalImageList(position: Int) {

    }


    override fun getUserType(): String {
        return Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "")
    }
}
