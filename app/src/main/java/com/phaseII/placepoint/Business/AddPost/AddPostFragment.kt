package com.phaseII.placepoint.Business.AddPost


import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog

import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.phaseII.placepoint.*
import com.phaseII.placepoint.BusEvents.EmptyFields
import com.phaseII.placepoint.BusEvents.PositionChangEvent
import com.phaseII.placepoint.BusEvents.ScheduleChangEvent
import com.phaseII.placepoint.BusEvents.SetwhileEditScheduleEvent

import com.phaseII.placepoint.Cropper.BaseActivity
import com.phaseII.placepoint.Cropper.BaseActivity.*
import com.phaseII.placepoint.MultichoiceCategories.MultipleCategories
import com.phaseII.placepoint.SubscriptionPlan.SubscriptionActivity

import com.squareup.otto.Subscribe
import com.squareup.picasso.Picasso


import org.json.JSONObject
import java.io.*
import java.lang.Byte.decode
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddPostFragment : Fragment(), AddNewHelper
//  ,BSImagePicker.OnMultiImageSelectedListener
{

    val REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101
    val REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102
    val REQUEST_STORAGE_WRITE_ACCESS = 112
    val REQUEST_CAMERA = 100
    var GALLERY: Boolean = false
    var mAlertDialog: AlertDialog? = null

    var videoAttached: Int = 0
    val MAX_WORDS: Int = 1000
    protected val SELECT_PICTURES = 110
    lateinit var toolbar: Toolbar
    private var filter: InputFilter? = null
    lateinit var mPresenter: AddNewPresenter
    var filePath: String = ""
    lateinit var camera: ImageView
    lateinit var editText: EditText
    lateinit var sLayout: ConstraintLayout
    lateinit var croppedImage: DynamicImageView
    lateinit var gallery: ImageView
    lateinit var cancel: ImageView
    lateinit var profileText: TextView
    lateinit var everyWeekCheck: TextView
    lateinit var monthCheck: TextView
    lateinit var specificCheck: TextView
    lateinit var specificDayLay: LinearLayout
    lateinit var flashHiddenLayout: LinearLayout
    lateinit var flashClick: RelativeLayout
    lateinit var mainLayout: RelativeLayout

    lateinit var mainLay: RelativeLayout
    lateinit var monthLay: LinearLayout
    lateinit var dayLay: LinearLayout
    lateinit var noSubLay: ConstraintLayout

    lateinit var seText: TextView
    lateinit var nowCheck: CheckBox
    lateinit var expandLayout: ConstraintLayout
    lateinit var hidenLay: ConstraintLayout

    lateinit var specificTime: TextView
    lateinit var specificDay: TextView
    lateinit var monthTime: TextView
    lateinit var selectMonth: TextView
    lateinit var selectDay: TextView
    lateinit var dayTime: TextView
    lateinit var scrollView: ScrollView

    lateinit var selectCategory: TextView
    lateinit var upgrade: Button
    lateinit var schedulePost: TextView
    lateinit var word_count: TextView
    lateinit var addPost: TextView
    lateinit var videoName: TextView
    lateinit var videoCancel: ImageView
    lateinit var videoLayout: RelativeLayout
    lateinit var youtube: ImageView
    lateinit var youtubeField: EditText
    lateinit var postText: EditText
    lateinit var progressBar: ProgressBar
    lateinit var switchFlash: Switch
    lateinit var flashMax: EditText
    lateinit var flashPerPerson: TextView
    lateinit var flashselectDay: TextView
    lateinit var flashdayTime: TextView
    var recyclerItems: ArrayList<Uri> = arrayListOf()
    var imageUri: Uri? = null
    var isServiceRunning = false
    var isflashVisible = false
    private var listFromCropper: java.util.ArrayList<Uri>? = arrayListOf()
    var open = 0
    var dayOpen = 0
    var monthOpen = 0
    var specificOpen = 0
    private lateinit var stringList: ArrayList<ModelSelectOption>
    private lateinit var dayList: ArrayList<ModelSelectOption>
    private lateinit var monthList: ArrayList<ModelSelectOption>
    private lateinit var specificList: ArrayList<ModelSelectOption>
    private lateinit var date: DatePickerDialog.OnDateSetListener
    lateinit var listRecycle: RecyclerView
    var adposition = 0
    var adposition1 = 0
    var adposition2 = 0
    var checkedItem = -1
    var checkedItem1 = -1

    var checkedItem2 = -1
    private var postID: String = "0"
    private var imagestatus: String = "false"

    private var editTime: String = ""
    private var editDay: String = ""
    private var editType: String = ""
    private lateinit var imagePath: String
    private var maxChecked: String = ""
    private var maxCheckedPos: Int = 0
    private var flashCheckedPos: Int = -1
    private var maxPersonValue: String = ""
    private var flashPersonValue: String = ""
    private var imageName: String = ""
    //  lateinit var updater:setToMyPost
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_add_new, container, false)
        Constants.getSSlCertificate(activity!!)
        mPresenter = AddNewPresenter(this)
        videoCancel = view.findViewById(R.id.videoCancel)
        videoName = view.findViewById(R.id.videoName)
        videoLayout = view.findViewById(R.id.videoLayout)
        word_count = view.findViewById(R.id.word_count)
        noSubLay = view.findViewById(R.id.noSubLay)
        mainLay = view.findViewById(R.id.mainLay)
        seText = view.findViewById(R.id.seText)
        nowCheck = view.findViewById(R.id.nowCheck)
        expandLayout = view.findViewById(R.id.expandLay)
        hidenLay = view.findViewById(R.id.hidenLay)
        flashHiddenLayout = view.findViewById(R.id.flashHiddenLayout)
        flashClick = view.findViewById(R.id.flashClick)
        upgrade = view.findViewById(R.id.upgrade)
        scrollView = view.findViewById(R.id.scrollView)

        specificTime = view.findViewById(R.id.specificTime)
        specificDay = view.findViewById(R.id.specificDay)
        monthTime = view.findViewById(R.id.monthTime)
        selectMonth = view.findViewById(R.id.selectMonth)
        selectDay = view.findViewById(R.id.selectDay)
        dayTime = view.findViewById(R.id.dayTime)

        everyWeekCheck = view.findViewById(R.id.everyWeekCheck)
        monthCheck = view.findViewById(R.id.monthCheck)
        specificCheck = view.findViewById(R.id.specificCheck)
        specificDayLay = view.findViewById(R.id.specificDayLay)
        monthLay = view.findViewById(R.id.monthLay)
        dayLay = view.findViewById(R.id.dayLay)
        schedulePost = view.findViewById(R.id.schedulePost)
        addPost = view.findViewById(R.id.addPost)
        croppedImage = view.findViewById(R.id.croppedImage)


        camera = view.findViewById(R.id.camera)
        editText = view.findViewById(R.id.editText)
        mainLayout = view.findViewById(R.id.mainLay)
        sLayout = view.findViewById(R.id.slayout)
        gallery = view.findViewById(R.id.gallery)
        profileText = view.findViewById(R.id.profileText)
        selectCategory = view.findViewById(R.id.selectCategory)
        youtubeField = view.findViewById(R.id.youtubeField)
        youtube = view.findViewById(R.id.youtube)
        postText = view.findViewById(R.id.editText)
        cancel = view.findViewById(R.id.cancel)
        progressBar = view.findViewById(R.id.progressBar)
        switchFlash = view.findViewById(R.id.switchFlash)
        flashMax = view.findViewById(R.id.flashMax)
        flashPerPerson = view.findViewById(R.id.flashPerPerson)
        flashselectDay = view.findViewById(R.id.flashselectDay)
        flashdayTime = view.findViewById(R.id.flashdayTime)

        nowCheck.isChecked = false
        // editText.imeOptions = EditorInfo.IME_ACTION_DONE
        //  editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
        editText.addTextChangedListener(mTextEditorWatcher)
        // setToolBar(view)
        // croppedImage.visibility = View.GONE
        saveForDialog()
        clearPrefs()
        Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_CATEGORY).apply()


        //-----------------------FlashWork--------------------------------------------
        flashMax.setText("")
        flashPerPerson.text = ""
        flashselectDay.text = ""
        flashdayTime.text = "Time"
        flashClick.setOnClickListener {
            flashMax.setText("")
            if (!isflashVisible) {
                isflashVisible = true
                flashHiddenLayout.visibility = View.VISIBLE
            } else {
                flashHiddenLayout.visibility = View.GONE
                isflashVisible = false
            }
        }


        //----------------------------------------------------------------------------


        videoCancel.setOnClickListener {
            recyclerItems.clear()
            listFromCropper!!.clear()
            videoAttached = 0
            videoLayout.visibility = View.GONE
        }
        camera.setOnClickListener {
            mPresenter.openCamera()
        }


        mainLayout.setOnClickListener {
            hideKeyboard(editText)
            editText.clearFocus()
        }

        sLayout.setOnClickListener {
            editText.clearFocus()
            hideKeyboard(editText)
        }
        upgrade.setOnClickListener {
            val intent = Intent(context, SubscriptionActivity::class.java)
            activity!!.startActivity(intent)
        }
        gallery.setOnClickListener {
            mPresenter.openGallery()

        }
        cancel.setOnClickListener {
            imagestatus = "true"
            if (listFromCropper != null && listFromCropper!!.size > 0) {
                for (i in listFromCropper!!) {
                    listFromCropper!!.remove(i)
                }
            }
            croppedImage.visibility = View.GONE
            cancel.visibility = View.GONE
        }
        youtube.setOnClickListener {
            //            if (videoAttached==1){
//                Toast.makeText(activity!!,"Please remove Attached video First.",Toast.LENGTH_LONG).show()
//            }else{
            croppedImage.visibility = View.GONE
            cancel.visibility = View.GONE
            videoLayout.visibility = View.GONE
            videoName.text = ""
            recyclerItems.clear()
            youtubeField.visibility = View.VISIBLE
//            }
        }
        val s = Constants.getPrefs(activity!!)?.getString(Constants.MY_BUSINESS_NAME, "")!!
        if (s != null) {
            profileText.text = s
        }

        /* editText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
             if (actionId == EditorInfo.IME_ACTION_DONE) {
                 editText.clearFocus()

                 hideKeyboard(editText)
                 return@OnEditorActionListener true
             }
             false
         })*/
        /*  editText.setOnKeyListener(object : View.OnKeyListener {
              override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

              *//*    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    // Perform action on Enter key press
                    txtUserid.clearFocus();
                    txtUserPasword.requestFocus();
                    return true;
                }*//*
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Toast.makeText(activity, "test", Toast.LENGTH_SHORT).show()
                    return true
                }
                return false
            }
        })
*/
        editText.setOnClickListener {
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
        }

        selectCategory.setOnClickListener {
            val intent = Intent(activity!!, MultipleCategories::class.java)
            intent.putExtra("from", "addpost")
            startActivityForResult(intent, 155)
        }
        expandLayout.setOnClickListener {
            editDay = ""
            editType = ""
            editTime = ""
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_DAY).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TIME).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_NOW_STATUS).apply()
            openDialog(stringList, 1, "Select Scheduled Option")
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TYPE).apply()
        }

        schedulePost.setOnClickListener {
            //Footer.requestFocus()

            val post = addPost.text.toString()
            if (post.equals("DONE")) {

            } else {
                var youtubetext = youtubeField.text.toString().trim()
                clearPrefs()
                if (!youtubetext.isEmpty()) {
                    youtubeField.visibility = View.VISIBLE
                    youtubeField.setText(youtubetext)
                }
                adposition = 0
                adposition1 = 0
                adposition2 = 0
                checkedItem = -1
                checkedItem1 = -1
                checkedItem2 = -1
                editType = ""
                if (open == 0) {
                    open = 1
                    specificTime.text = "Time"
                    dayTime.text = "Time"
                    monthTime.text = "Time"
                    seText.visibility = View.VISIBLE
                    expandLayout.visibility = View.VISIBLE
                    nowCheck.visibility = View.VISIBLE
                    hidenLay.visibility = View.VISIBLE
                    // scrollView.fullScroll(View.FOCUS_DOWN)
                } else {
                    open = 0
                    expandLayout.visibility = View.GONE
                    nowCheck.visibility = View.GONE
                    seText.visibility = View.GONE
                    hidenLay.visibility = View.GONE
                    dayLay.visibility = View.GONE
                    monthLay.visibility = View.GONE
                    specificDayLay.visibility = View.GONE
                    specificTime.text = ""
                    dayTime.text = ""
                    monthTime.text = ""

                    for (i in 0 until stringList.size) {
                        stringList[i].checked = false
                    }
                }
            }


        }
//        flashMax .setOnClickListener {
//
//            lateinit var dialog: AlertDialog
//            val array = arrayOf("20", "40", "60", "80", "100", "120", "140", "160", "180", "200")
//
//            val builder = AlertDialog.Builder(activity!!)
//            builder.setTitle("Select")
//            builder.setSingleChoiceItems(array, maxCheckedPos) { _, which ->
//                maxPersonValue = array[which]
//
//                try {
//                    maxCheckedPos = which
//                    flashMax.text = maxPersonValue
//                } catch (e: Exception) {
//
//                }
//                dialog.dismiss()
//            }
//            dialog = builder.create()
//            dialog.show()
//
//        }
        flashPerPerson.setOnClickListener {

            lateinit var dialog: AlertDialog
            val array = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("Click the max number of offers to be redeemed per person")
            builder.setSingleChoiceItems(array, flashCheckedPos) { _, which ->
                flashPersonValue = array[which]
                flashPerPerson.text = flashPersonValue
                try {
                    flashCheckedPos = which
                } catch (e: Exception) {

                }
                dialog.dismiss()
            }
            dialog = builder.create()
            dialog.show()


        }
        flashselectDay.setOnClickListener {

            datePicker(flashselectDay)

        }
        flashdayTime.setOnClickListener {

            openTimePicker(flashdayTime)

        }
        specificTime.setOnClickListener {
            openTimePicker(specificTime)
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TIME).apply()
            editTime = ""
        }
        specificDay.setOnClickListener {
            editDay = ""
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_DAY).apply()
            datePicker(specificDay)
        }
        monthTime.setOnClickListener {
            editTime = ""
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TIME).apply()
            openTimePicker(monthTime)
        }
        selectMonth.setOnClickListener {
            editDay = ""
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_DAY).apply()
            openDialog(monthList, 3, "Select day of Month")
        }
        selectDay.setOnClickListener {
            editDay = ""
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_DAY).apply()
            openDialog(dayList, 2, "Select Day")
        }
        dayTime.setOnClickListener {
            editTime = ""
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TIME).apply()
            openTimePicker(dayTime)
        }

        addPost.setOnClickListener {
            val post = addPost.text.toString()
            if (post == "DONE") {
                //Toast.makeText(activity,"Pending",Toast.LENGTH_LONG).show()
                mPresenter.editPost(postID)
            } else {
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(postText.windowToken, 0)
                if (!isServiceRunning) {

                    mPresenter.addPost(isServiceRunning)
                }
            }
        }
        nowCheck.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_NOW_STATUS, "1").apply()
            } else {
                Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_NOW_STATUS, "0").apply()
            }


        }

        editText.isFocusableInTouchMode = false
        editText.isFocusable = false
        editText.isFocusableInTouchMode = true
        editText.isFocusable = true


        return view

    }

    private fun clearPrefs() {
        try {
            editTime = ""
            editDay = ""
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TYPE).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_DAY).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TIME).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_NOW_STATUS).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.CATEGORY_NAMES_ADDPOST).apply()
            youtubeField.setText("")
            youtubeField.visibility = View.GONE
            croppedImage.visibility = View.GONE
            cancel.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun showError(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
    }

    override fun clearPrefsall(type: String, ftype: String) {

        clearPrefs()
        Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_CATEGORY).apply()
        if (!type.isEmpty()) {
            var model = ShowFlashOrPost()
            model.ftype = ftype
            model.name = "schedule"
            Constants.getBus().post(ScheduleChangEvent("schedule"))
        } else {
            var model = ShowFlashOrPost()
            model.ftype = ftype
            model.name = "change"

            Constants.getBus().post(PositionChangEvent(model))
        }
    }

    private fun datePicker(specificDay: TextView) {
        val myCalendar = Calendar.getInstance()




        date = DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd MMM yyyy"//In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val myFormatSave = "yyyy-MM-dd"//In which you need put here
            val sdf2 = SimpleDateFormat(myFormatSave, Locale.US)
            var c = Calendar.getInstance().time;
            val current = sdf.format(c)
            val selected = sdf.format(myCalendar.time)
            if (current.equals(selected)) {
                specificDay.text = sdf.format(myCalendar.time)
                editDay = sdf.format(myCalendar.time)
                Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_DAY, sdf2.format(myCalendar.time).toString()).apply()

            } else {
                if (myCalendar.time.before(c)) {
                    Toast.makeText(activity, "You cannot set previous date.", Toast.LENGTH_LONG).show()
                    return@OnDateSetListener
                }
            }
            specificDay.text = sdf.format(myCalendar.time)
            editDay = sdf2.format(myCalendar.time).toString()
            Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_DAY, sdf2.format(myCalendar.time).toString()).apply()
        }
        DatePickerDialog(activity, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    //lateinit var  calendar: Calendar

    private fun openTimePicker(view: TextView) {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener() { timePicker: TimePicker, selectedHour: Int, selectedMinute: Int ->
            var am_pm = ""
            var hour = 0
            hour = selectedHour
            if (selectedHour > 12) {
                hour = hour - 12
                am_pm = "PM"

            } else {
                am_pm = "AM"
            }
            var round = "0"
            if (hour == 0) {
                round = "00"
            } else if (hour == 1) {
                round = "01"
            } else if (hour == 2) {
                round = "02"
            } else if (hour == 3) {
                round = "03"
            } else if (hour == 4) {
                round = "04"
            } else if (hour == 5) {
                round = "05"
            } else if (hour == 6) {
                round = "06"
            } else if (hour == 7) {
                round = "07"
            } else if (hour == 8) {
                round = "08"
            } else if (hour == 9) {
                round = "09"
            } else {
                round = hour.toString()
            }
            var selMin = "0"
            if (selectedMinute == 0) {
                selMin = "00"
            } else if (selectedMinute == 1) {
                selMin = "01"
            } else if (selectedMinute == 2) {
                selMin = "02"
            } else if (selectedMinute == 3) {
                selMin = "03"
            } else if (selectedMinute == 4) {
                selMin = "04"
            } else if (selectedMinute == 5) {
                selMin = "05"
            } else if (selectedMinute == 6) {
                selMin = "06"
            } else if (selectedMinute == 7) {
                selMin = "07"
            } else if (selectedMinute == 8) {
                selMin = "08"
            } else if (selectedMinute == 9) {
                selMin = "09"
            } else {
                selMin = selectedMinute.toString()
            }
            // var time = hour.toString() + ":" + selectedMinute.toString() + " " + am_pm
            var time = round + ":" + selMin + " " + am_pm

            val sdf = SimpleDateFormat("hh:mm a")
            val sdfs = SimpleDateFormat("hh:mm a")
            val dt: Date
            try {
                //dt = sdf.parse(time)
                // view.text = sdfs.format(dt)
                view.text = time
                Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_TIME, view.text.toString()).apply()
                editTime = time
            } catch (e: Exception) {
                view.text = time
                e.printStackTrace()
            }

//            if (hour == 0) {
//                view.text = "00" + ":" + selectedMinute.toString() + " " + am_pm
//                Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_TIME, view.text.toString()).apply()
//            } else {
//                 }


        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }


    private fun saveForDialog() {

        stringList = arrayListOf<ModelSelectOption>()
        val stringList2 = arrayListOf<String>()
        val stringList3 = arrayListOf<String>()
        val stringList4 = arrayListOf<String>()
        stringList2.add("Schedule post every week")
        stringList2.add("Schedule post on a specific day each month")
        stringList2.add("Schedule post for a specific day")
        for (i in 0 until 3) {
            val models = ModelSelectOption()
            models.id = i.toString()
            models.name = stringList2[i]
            models.checked = false
            stringList.add(models)
        }

        dayList = arrayListOf<ModelSelectOption>()
        stringList3.add("Monday")
        stringList3.add("Tuesday")
        stringList3.add("Wednesday")
        stringList3.add("Thursday")
        stringList3.add("Friday")
        stringList3.add("Saturday")
        stringList3.add("Sunday")
        for (j in 0 until 7) {
            val models = ModelSelectOption()
            models.id = j.toString()
            models.name = stringList3[j]
            models.checked = false
            dayList.add(models)

        }

        monthList = arrayListOf<ModelSelectOption>()
        for (p in 1 until 31) {
            stringList4.add(p.toString())
        }
        for (j in 0 until stringList4.size) {
            val models = ModelSelectOption()
            models.id = j.toString()
            models.name = stringList4[j]
            models.checked = false
            monthList.add(models)

        }
    }


    private fun openDialog(stringList1: ArrayList<ModelSelectOption>, from: Int, s: String) {
        val ar = arrayOfNulls<String>(stringList1.size)

        for (i in 0 until stringList1.size) {
            ar[i] = stringList1[i].name

        }
        if (from == 1) {
            for (i in 0 until stringList1.size) {
                if (stringList1[i].checked) {
                    checkedItem = i
                }

            }
        }

        var alert = AlertDialog.Builder(activity!!)
        alert.setTitle(s)
        if (from == 1) {
            alert.setSingleChoiceItems(ar, checkedItem, DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->

                adposition = i
                checkedItem = i

            })
        } else if (from == 2) {
            alert.setSingleChoiceItems(ar, checkedItem1, DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->

                adposition1 = i
                checkedItem1 = i

            })
        } else if (from == 3) {
            alert.setSingleChoiceItems(ar, checkedItem2, DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->

                adposition2 = i
                checkedItem2 = i

            })
        }
        alert.setPositiveButton("OK", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
            if (from == 1) {
                setValue("Select", selectDay)
                setValue("Select", selectMonth)
                setValue("Select", specificDay)
                showView(adposition)

            } else if (from == 2) {
                if (checkedItem1 == -1) {
                    setValue("Select", selectDay)
                    editDay = ""
                    Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_DAY).apply()
                } else {
                    Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_DAY, (adposition1 + 1).toString()).apply()
                    editDay = (adposition1 + 1).toString()
                    setValue("Select", selectMonth)
                    setValue("Select", specificDay)
                    var getTimeValue = ar[adposition1]

                    if (getTimeValue != null) {
                        setValue(getTimeValue, selectDay)
                    }
                }
            } else if (from == 3) {
                if (checkedItem2 == -1) {
                    setValue("Select", selectMonth)
                    Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_DAY).apply()
                    editDay = ""
                } else {
                    Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_DAY, (adposition2 + 1).toString()).apply()
                    editDay = (adposition2 + 1).toString()
                    setValue("Select", selectDay)
                    setValue("Select", selectMonth)
                    var getTimeValue = ar[adposition2]
                    if (getTimeValue == "1") {
                        getTimeValue = "1st of every month"
                    } else if (getTimeValue == "2") {
                        getTimeValue = "2nd of every month"
                    } else if (getTimeValue == "3") {
                        getTimeValue = "3rd of every month"
                    } else {
                        getTimeValue = getTimeValue.toString() + "th of every month"
                    }
                    setValue(getTimeValue.toString(), selectMonth)
                }
            }
        })
        alert.show()

    }

    private fun setValue(timeValue: String, view: TextView) {
        view.text = timeValue
    }

    private fun showView(adposition: Int) {
        if (adposition != -1) {
            Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_TYPE, (adposition + 1).toString()).apply()
            editType = (adposition + 1).toString()
            hidenLay.visibility = View.VISIBLE


            if (adposition == 0) {
                dayLay.visibility = View.VISIBLE
            } else {
                dayLay.visibility = View.GONE
            }
            if (adposition == 1) {
                monthLay.visibility = View.VISIBLE
            } else {
                monthLay.visibility = View.GONE
            }
            if (adposition == 2) {
                specificDayLay.visibility = View.VISIBLE
            } else {
                specificDayLay.visibility = View.GONE
            }
        } else {
            hidenLay.visibility = View.GONE
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TYPE).apply()
            editType = ""
        }
    }

    fun showAlertDialog(title: String?, message: String?,
                        onPositiveButtonClickListener: DialogInterface.OnClickListener?,
                        positiveText: String,
                        onNegativeButtonClickListener: DialogInterface.OnClickListener?,
                        negativeText: String) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener)
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener)
        mAlertDialog = builder.show()
    }


    val mTextEditorWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            try {
                word_count.text = s.length.toString() + "/" + MAX_WORDS
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    private fun countWords(s: String): Int {
        val trim = s.trim { it <= ' ' }
        return if (trim.isEmpty()) 0 else trim.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size
    }

    private fun setCharLimit(et: EditText, max: Int) {
        filter = InputFilter.LengthFilter(max)
        et.filters = arrayOf(filter)
    }

    private fun removeFilter(et: EditText) {
        if (filter != null) {
            et.filters = arrayOfNulls(0)
            filter = null
        }
    }

    override fun openCamera() {
        GALLERY = false
        cameraIntent()
    }

    private fun cameraIntent() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), BaseActivity.REQUEST_CAMERA)
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
                startActivityForResult(intent, BaseActivity.REQUEST_CAMERA)

            } else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        getString(R.string.permission_write_storage_rationale),
                        REQUEST_STORAGE_WRITE_ACCESS_PERMISSION)
            }
        }

    }

    fun requestPermission(permission: String, rationale: String, requestCode: Int) {
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getRealPathFromURIForVideo(selectedVideoUri: Uri): String {
        try {
            var wholeID = DocumentsContract.getDocumentId(selectedVideoUri);
            var id = wholeID.split(":")[1]
//        if (wholeID.contains(":")){
//             id = wholeID.split(":")[1]
//        }else{
//            id=wholeID.substring(wholeID.lastIndexOf("/")+1)
//        }
            val column = arrayOf(MediaStore.Video.Media.DATA)

            var sel = MediaStore.Video.Media._ID + "=?"
            var cursor = activity!!.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, column, sel, arrayOf(id), null);
            var filePath = "";

            var columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } catch (e: Exception) {
            if (isGoogleDriveUri(selectedVideoUri)) {
                return getDriveFilePath(selectedVideoUri, activity);
            } else {
                return getRealPathFromURI(selectedVideoUri)
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri?): String {
        val result: String


        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity!!.contentResolver.query(uri, proj, null, null, null)
        if (cursor == null) {
            result = uri!!.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)

            result = cursor.getString(idx)
            cursor.close()
        }
        return result

    }

    private fun getDriveFilePath(uri: Uri?, context: Context?): String {

        val returnCursor = context!!.getContentResolver().query(uri, null, null, null, null)

        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
        val file = File(context.getCacheDir(), name)
        try {
            val inputStream = context.getContentResolver().openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream.available()

            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)

            val buffers = ByteArray(bufferSize)

//            while ((read = inputStream.read(buffers)) != -1) {
//                outputStream.write(buffers, 0, read)
//            }
            Log.e("File Size", "Size " + file.length())
            inputStream.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)
            Log.e("File Size", "Size " + file.length())
        } catch (e: Exception) {
            Log.e("Exception", e.message)
        }

        return file.path

    }


    private fun isGoogleDriveUri(uri: Uri?): Boolean {

        return "com.google.android.apps.docs.storage".equals(uri!!.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri!!.getAuthority());
    }


    override fun openGallery() {
        GALLERY = true
        pickFromGallery()
    }


    private fun pickFromGallery() {
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
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//                    .setType("image/*")
//                    .addCategory(Intent.CATEGORY_OPENABLE)
//            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            intent.type = "image/* video/*"
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                val mimeTypes = arrayOf("image/jpeg", "image/png")
//                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//            }
            val photoLibraryIntent1 = Intent(Intent.ACTION_GET_CONTENT)
//            val photoLibraryIntent1 = Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            photoLibraryIntent1.type = "*/*"
//            val intent1 = Intent()
//            intent1.type = "image/*"
//            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(photoLibraryIntent1, "Select Picture"), SELECT_PICTURES)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            BaseActivity.REQUEST_STORAGE_READ_ACCESS_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (GALLERY)
                    pickFromGallery()
            }
            REQUEST_STORAGE_WRITE_ACCESS_PERMISSION ->

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        val packageManager = activity!!.packageManager
                        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                            cameraIntent()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            REQUEST_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent()
                } else {
                    Toast.makeText(activity, "camera permission denied", Toast.LENGTH_LONG).show()
                }
            }

            REQUEST_STORAGE_WRITE_ACCESS -> {
                if (GALLERY)
                    pickFromGallery()
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    recyclerItems.clear()
                    imageName = getFileName(this!!.imageUri!!)

                    recyclerItems.add(imageUri!!)
                    mPresenter.openCropper(recyclerItems)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (resultCode == 2) {
            try {
                recyclerItems.clear()
                val args = data!!.getBundleExtra("BUNDLE1")
                listFromCropper = (args.getSerializable("ARRAYLIST1") as java.util.ArrayList<Uri>?)
                if (listFromCropper!!.size == 0) {
                    val url = data.getStringExtra("url")
                    listFromCropper!!.add(Uri.parse(url))
                    //croppedImage.visibility = View.VISIBLE
                    // cancel.visibility = View.VISIBLE

                    videoLayout.visibility = View.VISIBLE
                    //  videoName.text = url.substring(url.lastIndexOf("/")+1)
                    videoName.text = imageName
                    Toast.makeText(activity!!, "Image is attached", Toast.LENGTH_SHORT).show()
                    youtubeField.visibility = View.GONE
                    Glide.with(activity!!)
                            .load(url)
                            .into(croppedImage)

                } else
                    if (listFromCropper!!.size > 0) {
                        // croppedImage.visibility = View.VISIBLE
                        // cancel.visibility = View.VISIBLE
                        for (i in listFromCropper!!) {
                            Glide.with(activity!!)
                                    .load(i)
                                    .into(croppedImage)
                        }
                    }
                imagestatus = "true"
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        if (requestCode === 155) {
            if (data != null) {
                selectCategory.text = data.getStringExtra("result")
            }
        }
        if (requestCode === SELECT_PICTURES) {

            try {


                //------------------------------


                //---------------------------------
                val uri = data!!.data
                var type = isImageFile(uri)

                if (type.contains("image")
                        || type.contains("jpeg")
                        || type.contains("png")
                        || type.contains("svg")
                        || type.contains("svg")
                        || type.contains("jpg")) {
                    val returnCursor = activity!!.getContentResolver().query(uri, null, null, null, null)
                    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
                    returnCursor.moveToFirst()
                    val size = returnCursor.getLong(sizeIndex).toInt()
                    val sizeInMb = size / (1024)
                    if (sizeInMb > 0) {
                        imageName = getFileName(uri)
                        recyclerItems.clear()
                        recyclerItems.add(uri)
                        mPresenter.openCropper(recyclerItems)
                    } else {
                        Toast.makeText(activity!!, "Unable to upload", Toast.LENGTH_SHORT).show()
                    }
                } else if (type.contains("video")) {
                    var sizeInMb: Int
                    val returnCursor = activity!!.getContentResolver().query(uri, null, null, null, null)
                    if (returnCursor == null) {
                        val file = File(uri.toString())
                        sizeInMb = Integer.parseInt((file.length() / (1024 * 1024)).toString())
                    } else {
                        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
                        returnCursor.moveToFirst()
                        val size = returnCursor.getLong(sizeIndex).toInt()
                        sizeInMb = (size / (1024 * 1024))
                    }
                    if (sizeInMb > 100) {
                        Toast.makeText(activity, "Unable to upload video more than 100 MB.", Toast.LENGTH_LONG).show()
                    } else {
//                        if (isGoogleDriveUri(uri)) {
//
////var url=GetGoogleDrivePath.getPath(activity!!,uri)
//                           // Toast.makeText(activity, "Unable to upload video from google drive please select other video from internal storage", Toast.LENGTH_LONG).show()
//
//                            youtubeField.visibility=View.GONE
//                            videoAttached=1
//                            videoLayout.visibility = View.VISIBLE
//                            videoName.text = getFileName(uri)
//                            cancel.visibility = View.VISIBLE
//                            youtubeField.visibility = View.GONE
//                            Glide.with(activity!!)
//                                    .load("")
//                                    .into(croppedImage)
//                            listFromCropper!!.clear()
//                            recyclerItems.clear()
//                             recyclerItems.add( data!!.data)
//                        } else {
                            if (youtubeField.text.toString().length > 0) {
                                Toast.makeText(activity, "Please remove YouTube url first.", Toast.LENGTH_LONG).show()

                            } else {
                                videoAttached = 1
                                videoLayout.visibility = View.VISIBLE
                                videoName.text = getFileName(uri)
                                cancel.visibility = View.GONE
                                youtubeField.visibility = View.GONE
                                Glide.with(activity!!)
                                        .load("")
                                        .into(croppedImage)
                                listFromCropper!!.clear()
                                recyclerItems.clear()
                                recyclerItems.add(data!!.data)
                                Toast.makeText(activity, "Video is attached", Toast.LENGTH_LONG).show()
                            }
//                        }
                    }
                } else {
                    Toast.makeText(activity, "Select either video or Image", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    public fun getFileName(uri: Uri): String {
        var result: String = "";
        if (uri.getScheme().equals("content")) {
            var cursor: Cursor = activity!!.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor.close()
            }
        }
        if (result == null || result.isEmpty()) {
            result = uri.getPath()
            var cut = result.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    public fun isImageFile(uri: Uri): String {
        var mimeType: String = "";
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            var cr = activity!!.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            var fileExtension: String = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
//        var cR = activity!!.getContentResolver();
//        var type = cR.getType(uri)
//        return type
    }


    override fun openCropper(list: ArrayList<Uri>) {
        val intent = Intent(activity!!, PostCropper::class.java)
        val args = Bundle()
        args.putSerializable("LIST", list)
        intent.putExtra("BUN", args)
        intent.putExtra("from",                //Toast.makeText(activity,"Pending",Toast.LENGTH_LONG).show()
                "post")
        startActivityForResult(intent, 2)

    }

    override fun getAuthCode(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.AUTH_CODE, "")!!
    }

    override fun getDesc(): String {
        return postText.text.toString().trim()
    }

    override fun getVideoLink(): String {
        return youtubeField.text.toString().trim()
    }

    override fun getImage(): String {
        try {
            val uri = listFromCropper!!.get(0)
            val ur = getRealPathFromURI(uri)
            return ur
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun uploadVideo(): String {
        try {
            var ur:String=""
            val uri = recyclerItems!!.get(0)
            if (isGoogleDriveUri(uri)){
                ur=GetGoogleDrivePath.getPath(activity!!,uri)
            }else{
                ur = getRealPathFromURIForVideo(uri)
            }
            // val ur = uri.path
            return ur
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""

    }


    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun getPostText(): String {
        return postText.text.toString().trim()
    }

    override fun showNetworkError(resId: Int) {
        if (activity != null) {
            try {
                Toast.makeText(activity!!, getString(resId), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {

            }
        }
    }

    override fun showMessage(msg: String?) {
        if (msg != null) {
            try {
                Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun serviceIsRunning(b: Boolean) {
        isServiceRunning = b
    }

    override fun showEmptyPostMsg() {
        Toast.makeText(activity!!, "Post is Empty", Toast.LENGTH_SHORT).show()
    }

    override fun getBusinessName(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.MY_BUSINESS_NAME, "")!!
    }

    override fun saveLocAndCat(data: JSONObject) {

        val intent = Intent()
        activity!!.setResult(Activity.RESULT_OK, intent)

    }

    override fun getImageWidth(): String {
        return croppedImage.width.toString()
    }

    override fun getImageHeight(): String {
        return croppedImage.height.toString()
    }

    override fun getType(): String {
        if (editType == null) {
            return ""
        }
        return editType
    }

    override fun getDay(): String {
        if (editDay == null) {
            return ""
        }
        return editDay
    }

    override fun getTime(): String {
        if (editTime == null) {
            return ""
        }
        return editTime
    }

    override fun getNowStatus(): String {
        return Constants.getPrefs(activity!!)!!.getString(Constants.ADDPOST_NOW_STATUS, "0")
    }

    override fun getCategory(): String {
        return Constants.getPrefs(activity!!)!!.getString(Constants.ADDPOST_CATEGORY, "")

    }

    override fun onResume() {
        super.onResume()
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3" ||
                Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "2") {
            noSubLay.visibility = View.VISIBLE
            mainLay.visibility = View.GONE

        } else {
            noSubLay.visibility = View.GONE
            mainLay.visibility = View.VISIBLE
        }
        Constants.getBus().register(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        Constants.getBus().unregister(this)
    }

    @Subscribe
    fun getEventValue(event: EmptyFields) {
        editText.setText("")
        specificTime.text = "Time"
        addPost.setText("POST")
        dayTime.text = "Time"
        monthTime.text = "Time"
        selectCategory.text = "Choose a category"
        open = 0
        expandLayout.visibility = View.GONE
        nowCheck.visibility = View.GONE
        seText.visibility = View.GONE
        hidenLay.visibility = View.GONE
        dayLay.visibility = View.GONE
        monthLay.visibility = View.GONE
        specificDayLay.visibility = View.GONE
        clearPrefs()
        Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_CATEGORY).apply()

    }

    fun parseDateToddMMyyyy(time: String): String? {
        val inputPattern = "yyyy-MM-dd hh:mm:ss"
        // val outputPattern = "dd MMM yyyy h:mm a"
        val outputPattern = "dd MMM yyyy"
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

    fun parseDateToddMMyyyy2(time: String): String? {
        val inputPattern = "yyyy-MM-dd hh:mm:ss"
        // val outputPattern = "dd MMM yyyy h:mm a"
        val outputPattern = "hh:mm a"
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

    @Subscribe
    fun getEventValue(event: SetwhileEditScheduleEvent) {
        isflashVisible = false
        flashMax.setText("")
        flashPerPerson.text = ""
        flashselectDay.text = ""
        flashdayTime.text = "Time"
        flashHiddenLayout.visibility = View.GONE
        if (event.value.ftype.equals("1")) {
            isflashVisible = true
            flashHiddenLayout.visibility = View.VISIBLE
            flashMax.setText(event.value.max_redemption)
            flashPerPerson.text = event.value.per_person_redemption
            flashselectDay.text = parseDateToddMMyyyy(event.value.validity_date)
            flashdayTime.text = parseDateToddMMyyyy2(event.value.validity_date)
            flashPersonValue = event.value.per_person_redemption
            maxPersonValue = event.value.max_redemption

        }
        postID = event.value.id
        addPost.text = "DONE"
        open = 1
        clearPrefs()
        editText.setText(event.value.description)
        val catId = event.value.category_id
        Constants.getPrefs(activity!!)?.edit()?.putString(Constants.ADDPOST_CATEGORY, catId)?.apply()
        val data = Constants.getPrefs(this.activity!!)?.getString(Constants.CATEGORY_LIST, "")!!
        var list = Constants.getCategoryData(data)
        var idList = catId!!.split(",")
        val highlightCat = arrayListOf<String>()
        val stringBuilder = StringBuilder("")
        var prefix = ""
        if (list != null) {
            for (i in 0 until idList.size) {
                for (j in 0 until list.size) {
                    if (idList[i] == list[j].id) {
                        highlightCat.add(list[j].name)
                        stringBuilder.append(prefix)
                        prefix = " ,"
                        stringBuilder.append(list[j].name)
                    }
                }
            }
        }
        val main = String(stringBuilder)
        var first = main.substring(0, 1)
        var last = main.substring(0, main.length)
        if (first == ",") {
            main.substring(1)
        }
        if (last == ",") {
            main.substring(0, main.length - 1)
        }
        selectCategory.text = main
        if (!event.value.image_url.isEmpty()) {
//            croppedImage.visibility = View.VISIBLE
//            cancel.visibility = View.VISIBLE
            Picasso.with(activity).load(event.value.image_url).into(croppedImage)
            imagePath = event.value.image_url
            videoLayout.visibility = View.VISIBLE
            videoLayout.visibility = View.VISIBLE
            videoName.text = imagePath.substring(imagePath.lastIndexOf("/") + 1)

        } else {
            imagePath = ""
            croppedImage.visibility = View.GONE
            cancel.visibility = View.GONE
        }
        if (event.value.video_link.isEmpty()) {
            youtubeField.setText("")
            youtubeField.visibility = View.GONE
        } else {
            youtubeField.visibility = View.VISIBLE
            youtubeField.setText(event.value.video_link)

        }

        // if (!event.value.type.isEmpty()) {
        specificTime.text = "Time"
        dayTime.text = "Time"
        monthTime.text = "Time"
        seText.visibility = View.VISIBLE
        expandLayout.visibility = View.VISIBLE
        hidenLay.visibility = View.VISIBLE
        editType = event.value.type
        editTime = event.value.time
        editDay = event.value.day
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_TYPE, editType)
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_TIME, editTime)
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.ADDPOST_DAY, editDay)
        adposition = event.value.type.toInt() - 1
        checkedItem = event.value.type.toInt() - 1
        if (event.value.type == "1") {
            dayLay.visibility = View.VISIBLE
            dayTime.text = event.value.time

            val day = event.value.day
            if (day == "1") {
                adposition2 = 0
                checkedItem2 = 0
                selectDay.text = "Monday"
            }
            if (day == "2") {
                adposition2 = 1
                checkedItem2 = 1
                selectDay.text = "Tuesday"
            }
            if (day == "3") {
                adposition2 = 2
                checkedItem2 = 2
                selectDay.text = "Wednesday"
            }
            if (day == "4") {
                adposition2 = 3
                checkedItem2 = 3
                selectDay.text = "Thursday"
            }
            if (day == "5") {
                adposition2 = 4
                checkedItem2 = 4
                selectDay.text = "Friday"
            }
            if (day == "6") {
                adposition2 = 5
                checkedItem2 = 5
                selectDay.text = "Saturday"
            }
            if (day == "7") {
                adposition2 = 6
                checkedItem2 = 6
                selectDay.text = "Sunday"
            }
        }
        if (event.value.type == "2") {
            monthLay.visibility = View.VISIBLE
            monthTime.text = event.value.time
            selectMonth.text = event.value.day
            val pos: Int = event.value.day.toInt()
            adposition1 = pos - 1
            checkedItem1 = pos - 1
        }
        if (event.value.type == "3") {
            specificDayLay.visibility = View.VISIBLE
            specificTime.text = event.value.time
            specificDay.text = event.value.day


        }
        // }
        addPost.setOnClickListener {
            val post = addPost.text.toString()
            if (post.equals("DONE")) {
                mPresenter.editPost(postID)
            } else {
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(postText.windowToken, 0)
                if (!isServiceRunning) {

                    mPresenter.addPost(isServiceRunning)
                }
            }
        }
        nowCheck.visibility = View.GONE
    }

    override fun showScheduleScreen() {

        Constants.getBus().post(ScheduleChangEvent("schedule"))
    }

    override fun getEditTime(): String {
        return editTime
    }

    override fun getEditDay(): String {
        return editDay
    }

    override fun getEditType(): String {
        return editType
    }

    override fun getImageChanged(): String {
        return imagestatus
    }

    override fun isFlashSwitchIsOn(): Boolean {
        return isflashVisible
    }

    override fun getMaxFlashValue(): String {
        return flashPersonValue
    }

    override fun getPersonFlashValue(): String {
        maxPersonValue = flashMax.text.toString()
        return maxPersonValue
    }

    override fun getFlashDate(): String {
        if (!flashselectDay.text.toString().isEmpty()) {
            return parseDateToddMMyyyy22(flashselectDay.text.toString())!!
        }
        return flashselectDay.text.toString()
    }

    override fun getFlashTime(): String {
        return flashdayTime.text.toString()
    }

    fun parseDateToddMMyyyy22(time: String): String? {
        val inputPattern = "dd MMM yyyy"
        val outputPattern = "yyyy-MM-dd"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return str
    }

    private fun hideKeyboard(activity: View) {

        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.windowToken, 0)
    }
}

