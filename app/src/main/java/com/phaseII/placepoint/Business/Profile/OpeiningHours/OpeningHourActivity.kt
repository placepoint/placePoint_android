package com.phaseII.placepoint.Business.Profile.OpeiningHours

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.phaseII.placepoint.Business.Profile.ModelTime
import com.phaseII.placepoint.R
import java.text.DecimalFormat
import java.util.ArrayList

class OpeningHourActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView
    lateinit var mondayOpen: TextView
    lateinit var tuesdayOpen: TextView
    lateinit var wednesdayOpen: TextView
    lateinit var thursdayOpen: TextView
    lateinit var fridayOpen: TextView
    lateinit var saturdayOpen: TextView
    lateinit var sundayOpen: TextView
    lateinit var mondayStartTime: TextView
    lateinit var tuesdayStartTime: TextView
    lateinit var wednesdayStartTime: TextView
    lateinit var thursdayStartTime: TextView
    lateinit var fridayStartTime: TextView
    lateinit var saturdayStartTime: TextView
    lateinit var sundayStartTime: TextView
    lateinit var mondayCloseTime: TextView
    lateinit var tuesdayCloseTime: TextView
    lateinit var wednesdayCloseTime: TextView
    lateinit var thursdayCloseTime: TextView
    lateinit var fridayCloseTime: TextView
    lateinit var saturdayCloseTime: TextView
    lateinit var sundayCloseTime: TextView
    lateinit var mondaySwitch: Switch
    lateinit var tuesdaySwitch: Switch
    lateinit var wednesdaySwitch: Switch
    lateinit var thursdaySwitch: Switch
    lateinit var fridaySwitch: Switch
    lateinit var saturdaySwitch: Switch
    lateinit var sundaySwitch: Switch
    lateinit var tallDays: LinearLayout
    lateinit var tallDays2: LinearLayout
    lateinit var tallDays3: LinearLayout
    lateinit var tallDays4: LinearLayout
    lateinit var tallDays5: LinearLayout
    lateinit var tallDays6: LinearLayout
    lateinit var tallDays7: LinearLayout
    lateinit var mondayCloseDay: LinearLayout
    lateinit var tuesdayCloseDay: LinearLayout
    lateinit var wednesdayCloseDay: LinearLayout
    lateinit var thursdayCloseDay: LinearLayout
    lateinit var fridayCloseDay: LinearLayout
    lateinit var saturdayCloseDay: LinearLayout
    lateinit var sundayCloseDay: LinearLayout
    lateinit var arrayList: ArrayList<ModelTime>
    var newArrayee: ArrayList<ModelTime> = arrayListOf()

    private var openStartTime: String = "12:00 AM"
    private var openEndTime: String = "12:00 AM"
    private var closeStartTime: String = "12:00 AM"
    private var closeEndTime: String = "12:00 AM"
    var day: String = "Monday"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening_hour)
        setToolBar()
        init()

        arrayList = intent.getParcelableArrayListExtra("openingHour")
        if (arrayList.size > 1) {
            for (i in 0 until 7) {
                newArrayee.add(arrayList[i])
            }
            arrayList.clear()
            arrayList.addAll(newArrayee)
            setTimeAndValues(arrayList)
        } else {
            for (i in 0 until 7) {
                var modelTime = ModelTime()
                modelTime.closeFrom = "12:00 AM"
                modelTime.closeTo = "12:00 AM"
                modelTime.closeStatus = true
                modelTime.status = true
                modelTime.startFrom = "12:00 AM"
                modelTime.startTo = "12:00 AM"
                newArrayee.add(modelTime)
            }
            arrayList.clear()
            arrayList.addAll(newArrayee)
            setTimeAndValues(arrayList)
        }
        switchesOpenClose()
        startCloseClicks()

        tallDays.setOnClickListener {
            //            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
//                Constants.showPopup(activity!!)
//                return@setOnClickListener
//            }
            if (!arrayList[0].startFrom.equals("12:00 AM")) {

                showDialogToSetSameTime(0)
            } else {
                if (!arrayList[0].startTo.equals("12:00 AM")) {
                    showDialogToSetSameTime(0)
                } else {
                    showErrorDialog()
                }
            }
        }
        tallDays2.setOnClickListener {
            //            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
//                Constants.showPopup(activity!!)
//                return@setOnClickListener
//            }
            if (!arrayList[1].startFrom.equals("12:00 AM")) {

                showDialogToSetSameTime(1)
            } else {
                if (!arrayList[1].startTo.equals("12:00 AM")) {
                    showDialogToSetSameTime(1)
                } else {
                    showErrorDialog()
                }
            }
        }
        tallDays3.setOnClickListener {
            //            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
//                Constants.showPopup(activity!!)
//                return@setOnClickListener
//            }
            if (!arrayList[2].startFrom.equals("12:00 AM")) {

                showDialogToSetSameTime(2)
            } else {
                if (!arrayList[2].startTo.equals("12:00 AM")) {
                    showDialogToSetSameTime(2)
                } else {
                    showErrorDialog()
                }
            }
        }
        tallDays4.setOnClickListener {
            //            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
//                Constants.showPopup(activity!!)
//                return@setOnClickListener
//            }
            if (!arrayList[3].startFrom.equals("12:00 AM")) {

                showDialogToSetSameTime(3)
            } else {
                if (!arrayList[3].startTo.equals("12:00 AM")) {
                    showDialogToSetSameTime(3)
                } else {
                    showErrorDialog()
                }
            }
        }
        tallDays5.setOnClickListener {
            //            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
//                Constants.showPopup(activity!!)
//                return@setOnClickListener
//            }
            if (!arrayList[4].startFrom.equals("12:00 AM")) {

                showDialogToSetSameTime(4)
            } else {
                if (!arrayList[4].startTo.equals("12:00 AM")) {
                    showDialogToSetSameTime(4)
                } else {
                    showErrorDialog()
                }
            }
        }
        tallDays6.setOnClickListener {
            //            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
//                Constants.showPopup(activity!!)
//                return@setOnClickListener
//            }
            if (!arrayList[5].startFrom.equals("12:00 AM")) {

                showDialogToSetSameTime(5)
            } else {
                if (!arrayList[5].startTo.equals("12:00 AM")) {
                    showDialogToSetSameTime(5)
                } else {
                    showErrorDialog()
                }
            }
        }
        tallDays7.setOnClickListener {
            //            if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") == "3") {
//                Constants.showPopup(activity!!)
//                return@setOnClickListener
//            }
            if (!arrayList[6].startFrom.equals("12:00 AM")) {

                showDialogToSetSameTime(6)
            } else {
                if (!arrayList[6].startTo.equals("12:00 AM")) {
                    showDialogToSetSameTime(6)
                } else {
                    showErrorDialog()
                }
            }
        }
    }

    private fun showErrorDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(false)
        dialog.setTitle("Alert!")
        dialog.setMessage("Start and Close time cannot be same.")
        dialog.setPositiveButton("Ok") { dialog, id ->

            dialog.dismiss()
        }
        val alert = dialog.create()
        alert.show()

    }

    private fun showDialogToSetSameTime(position: Int) {
        val dialog = AlertDialog.Builder(this)
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
                arrayList[i].startFrom = arrayList[position].startFrom
                arrayList[i].startTo = arrayList[position].startTo
                arrayList[i].closeFrom = arrayList[position].closeFrom
                arrayList[i].closeTo = arrayList[position].closeTo
                arrayList[i].status = arrayList[position].status
                arrayList[i].closeStatus = arrayList[position].closeStatus

            }
            setTimeAndValues(arrayList)
            dialog.dismiss()
        }
                .setNegativeButton("Cancel ") { dialog, which ->
                    dialog.dismiss()
                }

        val alert = dialog.create()
        alert.show()

    }

    fun openStartTimePicker(day: String, view: TextView, position: Int, fromTo: Int) {


        val values = arrayOf("AM", "PM")
        var amPm = 0
        var hours = 0
        var minutes = 0
        val d = AlertDialog.Builder(this)
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

            view.text = openStartTime

            if (fromTo == 0) {
                arrayList[position].startFrom = openStartTime
            } else {
                arrayList[position].startTo = openStartTime
            }
            //editTimeList(day, openStartTime, "startFrom")
            dialogInterface.dismiss()
        }
        d.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        val alertDialog = d.create()
        alertDialog.show()
    }

    private fun startCloseClicks() {

        mondayStartTime.setOnClickListener {
            openStartTimePicker("Monday", mondayStartTime, 0, 0)
        }
        mondayCloseTime.setOnClickListener {
            openStartTimePicker("Monday", mondayCloseTime, 0, 1)
        }
        tuesdayStartTime.setOnClickListener {
            openStartTimePicker("Tuesday", tuesdayStartTime, 1, 0)
        }
        tuesdayCloseTime.setOnClickListener {
            openStartTimePicker("Tuesday", tuesdayCloseTime, 1, 1)
        }
        wednesdayStartTime.setOnClickListener {
            openStartTimePicker("Wednesday", wednesdayStartTime, 2, 0)
        }
        wednesdayCloseTime.setOnClickListener {
            openStartTimePicker("Wednesday", wednesdayCloseTime, 2, 1)
        }
        thursdayStartTime.setOnClickListener {
            openStartTimePicker("Thursday", thursdayStartTime, 3, 0)
        }
        thursdayCloseTime.setOnClickListener {
            openStartTimePicker("Thursday", thursdayCloseTime, 3, 1)
        }
        fridayStartTime.setOnClickListener {
            openStartTimePicker("Friday", fridayStartTime, 4, 0)
        }
        fridayCloseTime.setOnClickListener {
            openStartTimePicker("Friday", fridayCloseTime, 4, 1)
        }
        saturdayStartTime.setOnClickListener {
            openStartTimePicker("Saturday", saturdayStartTime, 5, 0)
        }
        saturdayCloseTime.setOnClickListener {
            openStartTimePicker("Saturday", saturdayCloseTime, 5, 1)
        }
        sundayStartTime.setOnClickListener {
            openStartTimePicker("Sunday", sundayStartTime, 6, 0)
        }
        sundayCloseTime.setOnClickListener {
            openStartTimePicker("Sunday", sundayCloseTime, 6, 1)
        }
    }

    private fun switchesOpenClose() {


        mondaySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            arrayList[0].status = isChecked
            if (isChecked) {
                mondayOpen.text = "Open"
                mondayStartTime.visibility = View.VISIBLE
                mondayCloseDay.visibility = View.VISIBLE
                arrayList[0].status = false
            } else {
                mondayStartTime.visibility = View.GONE
                mondayCloseDay.visibility = View.GONE
                arrayList[0].startFrom = "12:00 AM"
                arrayList[0].startTo = "12:00 AM"
                mondayStartTime.text = "12:00 AM"
                mondayCloseTime.text = "12:00 AM"
                mondayOpen.text = "Closed"
                arrayList[0].status = true
            }
        }
        tuesdaySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            arrayList[1].status = isChecked
            if (isChecked) {
                tuesdayOpen.text = "Open"
                tuesdayStartTime.visibility = View.VISIBLE
                tuesdayCloseDay.visibility = View.VISIBLE
                arrayList[1].status = false
            } else {
                tuesdayStartTime.visibility = View.GONE
                tuesdayCloseDay.visibility = View.GONE
                arrayList[1].startFrom = "12:00 AM"
                arrayList[1].startTo = "12:00 AM"
                tuesdayOpen.text = "Closed"
                tuesdayStartTime.text = "12:00 AM"
                tuesdayCloseTime.text = "12:00 AM"
                arrayList[1].status = true
            }
        }
        wednesdaySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            arrayList[2].status = isChecked
            if (isChecked) {
                wednesdayOpen.text = "Open"
                wednesdayStartTime.visibility = View.VISIBLE
                wednesdayCloseDay.visibility = View.VISIBLE
                arrayList[2].status = false
            } else {
                wednesdayStartTime.visibility = View.GONE
                wednesdayCloseDay.visibility = View.GONE
                arrayList[2].startFrom = "12:00 AM"
                arrayList[2].startTo = "12:00 AM"
                wednesdayStartTime.text = "12:00 AM"
                wednesdayCloseTime.text = "12:00 AM"
                wednesdayOpen.text = "Closed"
                arrayList[2].status = true
            }
        }
        thursdaySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            arrayList[3].status = isChecked
            if (isChecked) {
                thursdayOpen.text = "Open"
                thursdayStartTime.visibility = View.VISIBLE
                thursdayCloseDay.visibility = View.VISIBLE
                arrayList[3].status = false
            } else {
                thursdayStartTime.visibility = View.GONE
                thursdayCloseDay.visibility = View.GONE
                arrayList[3].startFrom = "12:00 AM"
                arrayList[3].startTo = "12:00 AM"
                thursdayStartTime.text = "12:00 AM"
                thursdayCloseTime.text = "12:00 AM"
                thursdayOpen.text = "Closed"
                arrayList[3].status = true
            }
        }
        fridaySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            arrayList[4].status = isChecked
            if (isChecked) {
                fridayOpen.text = "Open"
                fridayStartTime.visibility = View.VISIBLE
                fridayCloseDay.visibility = View.VISIBLE
                arrayList[4].status = false
            } else {
                fridayStartTime.visibility = View.GONE
                fridayCloseDay.visibility = View.GONE
                arrayList[4].startFrom = "12:00 AM"
                arrayList[4].startTo = "12:00 AM"
                fridayCloseTime.text = "12:00 AM"
                fridayStartTime.text = "12:00 AM"
                fridayOpen.text = "Closed"
                arrayList[4].status = true
            }
        }
        saturdaySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            arrayList[5].status = isChecked
            if (isChecked) {
                saturdayOpen.text = "Open"
                saturdayStartTime.visibility = View.VISIBLE
                saturdayCloseDay.visibility = View.VISIBLE
                arrayList[5].status = false
            } else {
                saturdayStartTime.visibility = View.GONE
                saturdayCloseDay.visibility = View.GONE
                arrayList[5].startFrom = "12:00 AM"
                arrayList[5].startTo = "12:00 AM"
                saturdayStartTime.text = "12:00 AM"
                saturdayCloseTime.text = "12:00 AM"
                saturdayOpen.text = "Closed"
                arrayList[5].status = true
            }
        }
        sundaySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            arrayList[6].status = isChecked
            if (isChecked) {
                sundayOpen.text = "Open"
                sundayStartTime.visibility = View.VISIBLE
                sundayCloseDay.visibility = View.VISIBLE
                arrayList[6].status = false
            } else {
                sundayStartTime.visibility = View.GONE
                sundayCloseDay.visibility = View.GONE
                arrayList[6].startFrom = "12:00 AM"
                arrayList[6].startTo = "12:00 AM"
                sundayStartTime.text = "12:00 AM"
                sundayCloseTime.text = "12:00 AM"
                sundayOpen.text = "Closed"
                arrayList[6].status = true
            }
        }
    }

    private fun setTimeAndValues(arrayList: ArrayList<ModelTime>) {

        for (i in 0 until arrayList.size) {
            if (i == 0) {
                if (arrayList[i].status) {
                    mondayOpen.text = "Closed"
                    mondaySwitch.isChecked = false
                    mondayStartTime.visibility = View.GONE
                    mondayCloseDay.visibility = View.GONE
                } else {
                    mondayStartTime.visibility = View.VISIBLE
                    mondayCloseDay.visibility = View.VISIBLE
                    mondayOpen.text = "Open"
                    mondaySwitch.isChecked = true
                }
                mondayStartTime.text = arrayList[i].startFrom
                mondayCloseTime.text = arrayList[i].startTo
            } else if (i == 1) {

                if (arrayList[i].status) {
                    tuesdayOpen.text = "Closed"
                    tuesdaySwitch.isChecked = false
                    tuesdayStartTime.visibility = View.GONE
                    tuesdayCloseDay.visibility = View.GONE
                } else {
                    tuesdayStartTime.visibility = View.VISIBLE
                    tuesdayCloseDay.visibility = View.VISIBLE
                    tuesdayOpen.text = "Open"
                    tuesdaySwitch.isChecked = true
                }
                tuesdayStartTime.text = arrayList[i].startFrom
                tuesdayCloseTime.text = arrayList[i].startTo
            } else if (i == 2) {

                if (arrayList[i].status) {
                    wednesdayOpen.text = "Closed"
                    wednesdaySwitch.isChecked = false
                    wednesdayStartTime.visibility = View.GONE
                    wednesdayCloseDay.visibility = View.GONE
                } else {
                    wednesdayOpen.text = "Open"
                    wednesdaySwitch.isChecked = true
                    wednesdayStartTime.visibility = View.VISIBLE
                    wednesdayCloseDay.visibility = View.VISIBLE
                }
                wednesdayStartTime.text = arrayList[i].startFrom
                wednesdayCloseTime.text = arrayList[i].startTo
            } else if (i == 3) {

                if (arrayList[i].status) {
                    thursdayOpen.text = "Closed"
                    thursdaySwitch.isChecked = false
                    thursdayStartTime.visibility = View.GONE
                    thursdayCloseDay.visibility = View.GONE
                } else {
                    thursdayStartTime.visibility = View.VISIBLE
                    thursdayCloseDay.visibility = View.VISIBLE
                    thursdayOpen.text = "Open"
                    thursdaySwitch.isChecked = true
                }
                thursdayStartTime.text = arrayList[i].startFrom
                thursdayCloseTime.text = arrayList[i].startTo
            } else if (i == 4) {

                if (arrayList[i].status) {
                    fridayOpen.text = "Closed"
                    fridaySwitch.isChecked = false
                    fridayStartTime.visibility = View.GONE
                    fridayCloseDay.visibility = View.GONE
                } else {
                    fridayStartTime.visibility = View.VISIBLE
                    fridayCloseDay.visibility = View.VISIBLE
                    fridayOpen.text = "Open"
                    fridaySwitch.isChecked = true
                }
                fridayStartTime.text = arrayList[i].startFrom
                fridayCloseTime.text = arrayList[i].startTo
            } else if (i == 5) {
                if (arrayList[i].status) {
                    saturdayOpen.text = "Closed"
                    saturdaySwitch.isChecked = false
                    saturdayStartTime.visibility = View.GONE
                    saturdayCloseDay.visibility = View.GONE
                } else {
                    saturdayStartTime.visibility = View.VISIBLE
                    saturdayCloseDay.visibility = View.VISIBLE
                    saturdayOpen.text = "Open"
                    saturdaySwitch.isChecked = true
                }
                saturdayStartTime.text = arrayList[i].startFrom
                saturdayCloseTime.text = arrayList[i].startTo
            } else if (i == 6) {
                if (arrayList[i].status) {
                    sundayOpen.text = "Closed"
                    sundaySwitch.isChecked = false
                    sundayStartTime.visibility = View.GONE
                    sundayCloseDay.visibility = View.GONE
                } else {
                    sundayStartTime.visibility = View.VISIBLE
                    sundayCloseDay.visibility = View.VISIBLE
                    sundayOpen.text = "Open"
                    sundaySwitch.isChecked = true
                }
                sundayStartTime.text = arrayList[i].startFrom
                sundayCloseTime.text = arrayList[i].startTo
            }
        }
    }

    private fun init() {
        mondayCloseDay = findViewById(R.id.mondayCloseDay)
        tuesdayCloseDay = findViewById(R.id.tuesdayCloseDay)
        wednesdayCloseDay = findViewById(R.id.wednesdayCloseDay)
        thursdayCloseDay = findViewById(R.id.thursdayCloseDay)
        fridayCloseDay = findViewById(R.id.fridayCloseDay)
        saturdayCloseDay = findViewById(R.id.saturdayCloseDay)
        sundayCloseDay = findViewById(R.id.sundayCloseDay)
        tallDays = findViewById(R.id.tallDays)
        tallDays2 = findViewById(R.id.tallDays2)
        tallDays3 = findViewById(R.id.tallDays3)
        tallDays4 = findViewById(R.id.tallDays4)
        tallDays5 = findViewById(R.id.tallDays5)
        tallDays6 = findViewById(R.id.tallDays6)
        tallDays7 = findViewById(R.id.tallDays7)
        mondaySwitch = findViewById(R.id.mondaySwitch)
        tuesdaySwitch = findViewById(R.id.tuesdaySwitch)
        wednesdaySwitch = findViewById(R.id.wednesdaySwitch)
        thursdaySwitch = findViewById(R.id.thursdaySwitch)
        fridaySwitch = findViewById(R.id.fridaySwitch)
        saturdaySwitch = findViewById(R.id.saturdaySwitch)
        sundaySwitch = findViewById(R.id.sundaySwitch)
        mondayOpen = findViewById(R.id.mondayOpen)
        tuesdayOpen = findViewById(R.id.tuesdayOpen)
        wednesdayOpen = findViewById(R.id.wednesdayOpen)
        thursdayOpen = findViewById(R.id.thursdayOpen)
        fridayOpen = findViewById(R.id.fridayOpen)
        saturdayOpen = findViewById(R.id.saturdayOpen)
        sundayOpen = findViewById(R.id.sundayOpen)
        mondayStartTime = findViewById(R.id.mondayStartTime)
        tuesdayStartTime = findViewById(R.id.tuesdayStartTime)
        wednesdayStartTime = findViewById(R.id.wednesdayStartTime)
        thursdayStartTime = findViewById(R.id.thursdayStartTime)
        fridayStartTime = findViewById(R.id.fridayStartTime)
        saturdayStartTime = findViewById(R.id.saturdayStartTime)
        sundayStartTime = findViewById(R.id.sundayStartTime)
        mondayCloseTime = findViewById(R.id.mondayCloseTime)
        tuesdayCloseTime = findViewById(R.id.tuesdayCloseTime)
        wednesdayCloseTime = findViewById(R.id.wednesdayCloseTime)
        thursdayCloseTime = findViewById(R.id.thursdayCloseTime)
        fridayCloseTime = findViewById(R.id.fridayCloseTime)
        saturdayCloseTime = findViewById(R.id.saturdayCloseTime)
        sundayCloseTime = findViewById(R.id.sundayCloseTime)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_des, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        mTitle.text = "Opening Hours"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.done) {
            var same = 0
            var pos = -1
            for (a in 0 until arrayList.size) {
                if (!arrayList[a].status) {
                    if (arrayList[a].startFrom == "12:00 AM" && arrayList[a].startTo == "12:00 AM") {
                        same = 1
                        pos = a
                        break
                    }
                }
            }
            if (same == 0) {
                val intent = intent
                intent.putExtra("openingHours", arrayList)
                setResult(403, intent)
                finish()
            } else {
                var name = ""
                if (pos == 0) {
                    name = "Monday"
                } else if (pos == 1) {
                    name = "Tuesday"
                } else if (pos == 2) {
                    name = "Wednesday"
                } else if (pos == 3) {
                    name = "Thursday"
                } else if (pos == 4) {
                    name = "Friday"
                } else if (pos == 5) {
                    name = "Saturday"
                } else if (pos == 6) {
                    name = "Sunday"
                }
                Toast.makeText(this, "Start Time and Close Time cannot be same on " + name, Toast.LENGTH_LONG).show()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
