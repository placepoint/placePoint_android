package com.phaseII.placepoint.Town

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.phaseII.placepoint.ConstantClass.GpsTracker
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import org.json.JSONObject

class TownActivity : AppCompatActivity(), TownHelper  {
    private var isRunning: Boolean = false
    private lateinit var mPresenter: TownPresenter
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: Toolbar
    lateinit var toolbarArrow: ImageView
    private lateinit var adapter: TownAdapter
    private var list: ArrayList<ModelTown>? = arrayListOf()
    var p: String = "false"

    private var latitude: Double=0.0
    private var longitude: Double=0.0
// bunch of location related apis
lateinit var  mFusedLocationClient:FusedLocationProviderClient
    lateinit var  mSettingsClient:SettingsClient
    lateinit var  mLocationRequest:LocationRequest
    lateinit var  mLocationSettingsRequest:LocationSettingsRequest
    lateinit var  mLocationCallback:LocationCallback
    lateinit var  mCurrentLocation:Location

    // boolean flag to toggle the ui
     var  mRequestingLocationUpdates:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_town)
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
        if ( Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,  arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 10)
        }
        val tracker = GpsTracker(this)
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert()
        } else {
            var loc=tracker.getLocation()
            latitude = tracker.getLatitude()
            longitude = tracker.getLongitude()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Constants.getPrefs(this)!!.edit().putString("permission","yes").apply()
        }else{
            Constants.getPrefs(this)!!.edit().putString("permission","no").apply()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        toolbarArrow = toolbar.findViewById(R.id.arrow_down) as ImageView
        toolbarArrow.visibility = View.GONE
        setSupportActionBar(toolbar)
        title = ""
        if ( intent.getStringExtra("from")=="false"){
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            mPresenter.prepareDataForAdapter()
        }else{
            mPresenter.prepareDataForAdapter()
           // setDataToAdapter(Constants.getPrefs(this@TownActivity)!!.getString(Constants.LOCATION_LIST, ""))
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        mTitle.text = "Choose a Town"
    }
    override fun setDataToAdapter(data: String) {

        list = Constants.getTownData(data)
        val sortedList = list!!.sortedWith(compareBy { it.townname })
        saveTownListToPrefs(data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TownAdapter(this, sortedList, p,intent.getStringExtra("from"))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
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

      //Getting those town which shows taxi facility in Business Detail
        for (i in 0 until list!!.size){
            if (list[i].name=="Taxis"&&list[i].parent_category=="0"){
               Constants.getPrefs(this@TownActivity)!!.edit().putString(Constants.TAXI_TOWNID,list[i].town_id).apply()
            }
            if (list[i].name=="Taxis"&&list[i].parent_category!="0"){
                Constants.getPrefs(this@TownActivity)!!.edit().putString(Constants.TAXI_TOWNID,list[i].town_id).apply()
                Constants.getPrefs(this@TownActivity)!!.edit().putString(Constants.TAXI_SUB_ID,list[i].id).apply()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId==android.R.id.home){
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
