package com.phaseII.placepoint.Town

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.android.gms.location.*
import com.onesignal.OneSignal
import com.phaseII.placepoint.ConstantClass.GpsTracker
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.R

class TownActivity : AppCompatActivity(), TownHelper {
    private var isRunning: Boolean = false
    private lateinit var mPresenter: TownPresenter
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: Toolbar
    private lateinit var adapter: TownAdapter
    private var list: ArrayList<ModelTown>? = arrayListOf()
    var p: String = "false"

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    // bunch of location related apisTOWN_ID2
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var mSettingsClient: SettingsClient
    lateinit var mLocationRequest: LocationRequest
    lateinit var mLocationSettingsRequest: LocationSettingsRequest
    lateinit var mLocationCallback: LocationCallback
    lateinit var mCurrentLocation: Location

    // boolean flag to toggle the ui
    var mRequestingLocationUpdates: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_town)

        Constants.getSSlCertificate(this)
        mPresenter = TownPresenter(this)

        try {
            val s = intent.getStringExtra("fromNav")
            p = if (s == "true") {
                "true"
            } else {
                "false"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setToolBar()
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 10)
        }
        val tracker = GpsTracker(this)
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert()
        } else {
            var loc = tracker.getLocation()
            latitude = tracker.getLatitude()
            longitude = tracker.getLongitude()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Constants.getPrefs(this)!!.edit().putString("permission", "yes").apply()
        } else {
            Constants.getPrefs(this)!!.edit().putString("permission", "no").apply()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        setSupportActionBar(toolbar)
        title = ""
        var userId=""
        OneSignal.idsAvailable { userId1, registrationId ->
            //Log.d("debug", "User:$userId")
            userId=userId1
            System.out.println("User Id: "+userId)
            if (registrationId != null)
            //    Log.d("debug", "registrationId:$registrationId")
                System.out.println("registrationId Id: "+registrationId)
        }
        if (intent.getStringExtra("from") == "false") {
            supportActionBar!!.setDisplayShowTitleEnabled(false)

            mPresenter.prepareDataForAdapter("1",userId)

        } else {
            mPresenter.prepareDataForAdapter("0", userId)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        //title="Choose a Town"
        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        mTitle.text = "Choose a Town"
    }

    override fun setDataToAdapter(data: String) {

        list = Constants.getTownData(data)
        if (list!!.size > 0) {
            val sortedList = list!!.sortedWith(compareBy { it.townname })
            saveTownListToPrefs(data)
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = TownAdapter(this, sortedList, p, intent.getStringExtra("from"))
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }
    }

    private fun saveTownListToPrefs(data: String) {
        Constants.getPrefs(this@TownActivity)?.edit()?.putString(Constants.LOCATION_LIST, data)?.apply()
    }

    override fun showNetworkError(resId: Int) {
        Toast.makeText(this@TownActivity, getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    @SuppressLint("CommitPrefEdits")
    override fun saveCategories(toString: String) {
        Constants.getPrefs(this@TownActivity)?.edit()!!.remove(Constants.CATEGORY_LIST).apply()
        Constants.getPrefs(this@TownActivity)?.edit()?.putString(Constants.CATEGORY_LIST, toString)?.apply()
        var list = Constants.getCategoryData(toString)

        for (i in 0 until list!!.size) {
            if (list[i].name == "Taxis" && list[i].parent_category == "0") {
                Constants.getPrefs(this@TownActivity)!!.edit().putString(Constants.TAXI_TOWNID, list[i].town_id).apply()
            }
            if (list[i].name == "Taxis" && list[i].parent_category != "0") {
                Constants.getPrefs(this@TownActivity)!!.edit().putString(Constants.TAXI_TOWNID, list[i].town_id).apply()
                Constants.getPrefs(this@TownActivity)!!.edit().putString(Constants.TAXI_SUB_ID, list[i].id).apply()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                var fromWhere=Constants.getPrefs(this)!!.getString("comingFrom", "")
                if (fromWhere=="more") {
                    var intent= Intent(this,DashBoardActivity::class.java)
                    intent.putExtra("openMore","yes")
                    startActivity(intent)
                    finish()
                }else{
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
