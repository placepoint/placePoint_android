package com.phaseII.placepoint.Business.AddPost


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
import kotlinx.android.synthetic.main.activity_add_new.*


import org.json.JSONObject
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

    val MAX_WORDS: Int = 1000
    protected val SELECT_PICTURES = 110
    lateinit var toolbar: Toolbar
    private var filter: InputFilter? = null
    lateinit var mPresenter: AddNewPresenter
    var filePath: String = ""
    lateinit var camera: ImageView
    lateinit var editText: EditText
    lateinit var croppedImage: DynamicImageView
    lateinit var gallery: ImageView
    lateinit var cancel: ImageView
    lateinit var profileText: TextView
    lateinit var everyWeekCheck: TextView
    lateinit var monthCheck: TextView
    lateinit var specificCheck: TextView
    lateinit var specificDayLay: LinearLayout
    lateinit var monthLay: LinearLayout
    lateinit var dayLay: LinearLayout

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
    lateinit var addPost: TextView
    lateinit var youtube: ImageView
    lateinit var youtubeField: EditText
    lateinit var postText: EditText
    lateinit var progressBar: ProgressBar
    var recyclerItems: ArrayList<Uri> = arrayListOf()
    var imageUri: Uri? = null
    var isServiceRunning = false
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

    private lateinit var editTime: String
    private lateinit var editDay: String
    private lateinit var editType: String
    private lateinit var imagePath: String
    //  lateinit var updater:setToMyPost
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_add_new, container, false)
        Constants.getSSlCertificate(activity!!)
        mPresenter = AddNewPresenter(this)
        seText = view.findViewById(R.id.seText)
        nowCheck = view.findViewById(R.id.nowCheck)
        expandLayout = view.findViewById(R.id.expandLay)
        hidenLay = view.findViewById(R.id.hidenLay)
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
        gallery = view.findViewById(R.id.gallery)
        profileText = view.findViewById(R.id.profileText)
        selectCategory = view.findViewById(R.id.selectCategory)
        youtubeField = view.findViewById(R.id.youtubeField)
        youtube = view.findViewById(R.id.youtube)
        postText = view.findViewById(R.id.editText)
        cancel = view.findViewById(R.id.cancel)
        progressBar = view.findViewById(R.id.progressBar)

        nowCheck.isChecked = false
        editText.imeOptions = EditorInfo.IME_ACTION_DONE
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
        editText.addTextChangedListener(mTextEditorWatcher)
        // setToolBar(view)
       // croppedImage.visibility = View.GONE
        saveForDialog()
        clearPrefs()
        Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_CATEGORY).apply()
        camera.setOnClickListener {
            mPresenter.openCamera()
        }
        upgrade.setOnClickListener {
            val intent = Intent(context, SubscriptionActivity::class.java)
            activity!!.startActivity(intent)
        }
        gallery.setOnClickListener {
            mPresenter.openGallery()
        }
        cancel.setOnClickListener {
            if (listFromCropper != null && listFromCropper!!.size > 0) {
                for (i in listFromCropper!!) {
                    listFromCropper!!.remove(i)
                }
            }
            croppedImage.visibility = View.GONE
            cancel.visibility = View.GONE
        }
        youtube.setOnClickListener {
            youtubeField.visibility = View.VISIBLE
        }
        val s = Constants.getPrefs(activity!!)?.getString(Constants.MY_BUSINESS_NAME, "")!!
        if (s != null) {
            profileText.text = s
        }

        selectCategory.setOnClickListener {
            val intent = Intent(activity!!, MultipleCategories::class.java)
            intent.putExtra("from", "addPost")
            startActivityForResult(intent, 155)
        }
        expandLayout.setOnClickListener {
            editDay = ""
            editType = ""
            editTime = ""
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_DAY).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TIME).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_NOW_STATUS).apply()
            openDialog(stringList, 1, "Select Scheduled Option")
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TYPE).apply()
        }

        schedulePost.setOnClickListener {
            val post = addPost.text.toString()
            if (post.equals("DONE")) {

            } else {
                clearPrefs()
                adposition = 0
                adposition1 = 0
                adposition2 = 0
                checkedItem = -1
                checkedItem1 = -1
                checkedItem2 = -1
                if (open == 0) {
                    open = 1
                    specificTime.text = "Time"
                    dayTime.text = "Time"
                    monthTime.text = "Time"
                    seText.visibility = View.VISIBLE
                    expandLayout.visibility = View.VISIBLE
                    nowCheck.visibility = View.VISIBLE
                    hidenLay.visibility = View.VISIBLE
                    scrollView.fullScroll(View.FOCUS_DOWN)
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
            if (post.equals("DONE")) {
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

        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);

//        editText.setOnEditorActionListener() { v, actionId, event ->
//            if(actionId == EditorInfo.IME_ACTION_DONE){
//
//                true
//            } else {
//                false
//            }
//        }
        return view

    }

    private fun clearPrefs() {
        try {
            editTime = ""
            editDay = ""
            editType = ""

            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TYPE).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_DAY).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_TIME).apply()
            Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_NOW_STATUS).apply()

            Constants.getPrefs(activity!!)!!.edit().remove(Constants.CATEGORY_NAMES_ADDPOST).apply()

            croppedImage.visibility=View.GONE
            cancel.visibility=View.GONE
            //Picasso.with(activity!!).load("cc").into(croppedImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun showError(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
    }

    override fun clearPrefsall(type: String) {

        clearPrefs()
        Constants.getPrefs(activity!!)!!.edit().remove(Constants.ADDPOST_CATEGORY).apply()
        if (!type.isEmpty()) {
            Constants.getBus().post(ScheduleChangEvent("schedule"))
        } else {
            Constants.getBus().post(PositionChangEvent("change"))
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
//        val myFormat = "hh:mm a"
//        var sdf = SimpleDateFormat(myFormat, Locale.US);
//        val formated_time = sdf.format(mcurrentTime.getTime());

        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener() { timePicker: TimePicker, selectedHour: Int, selectedMinute: Int ->
            var am_pm = ""
            var hour = 0
            hour = selectedHour

//            mcurrentTime.set(Calendar.MINUTE, minute)
//            mcurrentTime.set(Calendar.HOUR,hour)
            // am_pm = calendar.get(Calendar.HOUR).toString() + if (calendar.get(Calendar.AM_PM) === Calendar.AM) "am" else "pm"
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
            //    @Override
//    public void onClick(DialogInterface dialog, int which) {
//        if (from == 0) {
//            Toast.makeText(activity, "Select One Choice",
//                        Toast.LENGTH_SHORT).show();
//        } else if (from == 1) {
//            // Your Code
//        } else if (from == 2) {
//            // Your Code
//        }
//    }
        });
        alert.show();


//        var dialogBuilder = AlertDialog.Builder(activity!!)
//
//// ...Irrelevant code for customizing the buttons and title
//        var inflater = this.getLayoutInflater();
//
//        var alertView = inflater.inflate(R.layout.schedulelist, null)
//
//
//        listRecycle = alertView.findViewById(R.id.list)
//        val linear = LinearLayoutManager(activity)
//        val adapter2 = ScheduleListOptionAdapter(activity!!, stringList1)
//        listRecycle.layoutManager = linear
//        listRecycle.adapter = adapter2
//
//
//
//
//
//        dialogBuilder.setView(alertView)
//                .setPositiveButton("OK", DialogInterface.OnClickListener() { dialog, which ->
//
//                    dialog.dismiss()
//                })
//
//
//                .setNegativeButton("Cancel", DialogInterface.OnClickListener() { dialog, which ->
//                    dialog.dismiss()
//                })
//
//        dialogBuilder.create()
//        dialogBuilder.show()


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

//    override fun onStop() {
//        super.onStop()
//        if (mAlertDialog != null && mAlertDialog.isShowing()) {
//            mAlertDialog.dismiss()
//        }
//    }

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
//    private fun setToolBar(view:View) {
//      //  toolbar = view.findViewById(R.id.toolbar)
//        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
//        val mArrow = toolbar.findViewById(R.id.arrow_down) as ImageView
//        mArrow.visibility = View.GONE
//        mTitle.text = "Add New Post"
//
//    }


    val mTextEditorWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            val wordsLength = countWords(s.toString())// words.length;
//            // count == 0 means a new word is going to start
//            if (count == 0 && wordsLength >= MAX_WORDS) {
//                setCharLimit(editText, editText.text.length)
//            } else {
//                removeFilter(editText)
//            }
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

    private fun getRealPathFromURI(uri: Uri?): String {
        val result: String
        val cursor = activity!!.contentResolver.query(uri, null, null, null, null)
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
            val intent = Intent(Intent.ACTION_GET_CONTENT)
                    .setType("image/*")
                    .addCategory(Intent.CATEGORY_OPENABLE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
            /*  try {
                  val picker: BSImagePicker = BSImagePicker.Builder("com.example.user24.placepoint.fileprovider")
                          .setMaximumDisplayingImages(Int.MAX_VALUE)
                          .setMaximumMultiSelectCount(1)
                          .setMinimumMultiSelectCount(0)
                          .build()
                  picker.show(supportFragmentManager, "picker")
              } catch (e: Exception) {
                  e.printStackTrace()
              }*/


            val intent1 = Intent()
            intent1.type = "image/*"
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent1, "Select Picture"), SELECT_PICTURES)
        }
    }

    /* override fun onMultiImageSelected(uriList: MutableList<Uri>?) {
         if (uriList != null) {
             recyclerItems.clear()
             recyclerItems.addAll(uriList)
             if (uriList.size <= 1) {
                 mPresenter.openCropper(recyclerItems)
             }
         }
     }*/

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
                        // if device support camera?
                        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                            //yes
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
                    recyclerItems.add(imageUri!!)
                    mPresenter.openCropper(recyclerItems)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (resultCode == 2) {
            try {
                val args = data!!.getBundleExtra("BUNDLE1")
                listFromCropper = (args.getSerializable("ARRAYLIST1") as java.util.ArrayList<Uri>?)
                if (listFromCropper!!.size == 0) {
                    val url = data.getStringExtra("url")
                    listFromCropper!!.add(Uri.parse(url))
                    croppedImage.visibility = View.VISIBLE
                    cancel.visibility = View.VISIBLE
                    Glide.with(activity!!)
                            .load(url)
                            .into(croppedImage)

                } else
                    if (listFromCropper!!.size > 0) {
                        croppedImage.visibility = View.VISIBLE
                        cancel.visibility = View.VISIBLE
                        for (i in listFromCropper!!) {
                            Glide.with(activity!!)
                                    .load(i)
                                    .into(croppedImage)
                        }
                    }
                var h = croppedImage.height
                var w = croppedImage.width
//                if (h>200){
//                    croppedImage.layoutParams.height = 200
//                }
//                if (w>200){
//                    croppedImage.layoutParams.width = 200
//                }
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
            // val selectedImage = data!!.data
//            recyclerItems.add(selectedImage!!)
//            val picturePath = getRealPathFromURI2(selectedImage,
//                    this)
//            val args = Bundle()
//            val i = Intent(activity!!, PostCropper::class.java)
////            i.putExtra("filePath", selectedImage!!.toString())
//            args.putSerializable("LIST", recyclerItems)
//            i.putExtra("BUN", args)
//            i.putExtra("from", "post")
//            startActivity(i)
            try {
                recyclerItems.clear()
                val uri = data!!.data
                recyclerItems.add(uri)
                mPresenter.openCropper(recyclerItems)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun getRealPathFromURI2(contentURI: Uri, context: Activity): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.managedQuery(contentURI, projection, null, null, null) ?: return null
        val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        return if (cursor.moveToFirst()) {
// cursor.close();
            cursor.getString(column_index)
        } else null
        // cursor.close();
    }

    override fun openCropper(list: ArrayList<Uri>) {
        val intent = Intent(activity!!, PostCropper::class.java)
        val args = Bundle()
        args.putSerializable("LIST", list)
        intent.putExtra("BUN", args)
        intent.putExtra("from", "post")
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
        Toast.makeText(activity!!, getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(msg: String?) {
        if (msg != null) {
            Toast.makeText(activity!!, msg, Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_addpost, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        if (item!!.itemId == android.R.id.home) {
//            onBackPressed()
//        } else if (item.itemId == R.id.addPost) {
//            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(postText.windowToken, 0)
//            if(!isServiceRunning){
//
//                mPresenter.addPost(isServiceRunning)
//            }
//
//        }
//        return super.onOptionsItemSelected(item)
//    }


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
//        try {
//            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.CATEGORY, data.optString("category"))?.apply()
//            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.TOWN_ID, data.optString("location"))?.apply()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        val intent = Intent()
        activity!!.setResult(Activity.RESULT_OK, intent)
        //GlobalBus.getBus().post(Events.FragmentActivityMessage("hello"));
//        updater=activity as setToMyPost
//        updater.myPost()

    }

    override fun getImageWidth(): String {
        return croppedImage.width.toString()
    }

    override fun getImageHeight(): String {
        return croppedImage.height.toString()
    }

    override fun getType(): String {
        return editType
    }

    override fun getDay(): String {
        return editDay
    }

    override fun getTime(): String {
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

    override fun onPause() {
        super.onPause()
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


    @Subscribe
    fun getEventValue(event: SetwhileEditScheduleEvent) {
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
            croppedImage.visibility = View.VISIBLE
            cancel.visibility=View.VISIBLE
            Picasso.with(activity).load(event.value.image_url).into(croppedImage)
            imagePath=event.value.image_url
        }else{
            imagePath=""
            croppedImage.visibility = View.GONE
            cancel.visibility=View.GONE
        }
        if (event.value.video_link.isEmpty()) {
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
                selectDay.text =  "Tuesday"
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
                selectDay.text =  "Sunday"
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
//            val post = addPost.text.toString()
//            if (post.equals("DONE")) {
//                // Toast.makeText(activity,"Pending",Toast.LENGTH_LONG).show()
//                mPresenter.editPost(postID)
//            }
            val post = addPost.text.toString()
            if (post.equals("DONE")) {
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
}

