package com.phaseII.placepoint.Business.Profile.Subscription

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.SubscriptionPlan.SubscriptionActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SubscriptionDetailActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView
    lateinit var packageName: TextView
    lateinit var change: TextView
    lateinit var expireOn: TextView
    lateinit var end_time: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription2)
        setToolBar()
        packageName=findViewById(R.id.packageName)
        change=findViewById(R.id.change)
        expireOn=findViewById(R.id.expireOn)
        if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "3") {
            packageName.text = "Free Package"
        }else if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "2") {
            packageName.text = "Standard Package"
        }else if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "1") {
            packageName.text = "Pro Package"
            change.visibility = View.GONE
        }else if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "4") {
            packageName.text = "Admin"
            change.visibility = View.GONE
        }
        end_time=intent.getStringExtra("endTime")
        if (end_time != "N/A") {

            try {
                val currentTime = getCurrentTime()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val start = sdf.parse(currentTime)
                val end = sdf.parse(end_time)
                if (start.after(end)) {
                    expireOn.text = "Expired on- " + parseDateToFormat(end_time)
                } else {
                    expireOn.text = "Expires on- " + parseDateToFormat(end_time)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        change.setOnClickListener{
            if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "3" ||
                    Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "2") {
                val intent = Intent(this, SubscriptionActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "You are already a Premium User.", Toast.LENGTH_LONG).show()
            }
        }
    }
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
    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date())
    }
    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        mTitle.text = "Subscription Plan"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
