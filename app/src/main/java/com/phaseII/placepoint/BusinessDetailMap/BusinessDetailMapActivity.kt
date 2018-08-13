package com.phaseII.placepoint.BusinessDetailMap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.phaseII.placepoint.R

class BusinessDetailMapActivity : AppCompatActivity(), OnMapReadyCallback, BusinessDetailMapHelper, GoogleApiClient.ConnectionCallbacks {

    var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mMap: GoogleMap
    lateinit var toolbar: Toolbar
    private lateinit var mPresenter: BusinessDetailMapPresenter
    private val locationRefreshTime: Long = 0
    private val locationRefreshDistance = 0f
    private lateinit var myLocation: LatLng
    private var mLocationManager: LocationManager? = null
    lateinit var marker: MarkerOptions
    private var lat: String? = null
    private var long: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_detail_map)
        setToolBar()
        mPresenter = BusinessDetailMapPresenter(this)
        mPresenter.setMapConfiguration()
      //  mPresenter.getIntentData(intent)
        try {
            lat=intent.getStringExtra("lat")
            long=intent.getStringExtra("long")
        }catch (e:Exception){
        }

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
      //  getCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {

        @SuppressLint("MissingPermission") val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
        if (mLastLocation != null) {
            mMap.isMyLocationEnabled = true
            mMap.clear()
            myLocation = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            marker = MarkerOptions().position(myLocation)
            mMap.addMarker(marker)
        }
    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = ""
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        val mArrow = toolbar.findViewById(R.id.arrow_down) as ImageView
        mArrow.visibility = View.GONE
        mTitle.text = "Address"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setMapMethods() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.view!!.isClickable = false
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build()
        }
        mGoogleApiClient!!.connect()
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mLocationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, locationRefreshTime,
                locationRefreshDistance, mLocationListener)

    }

    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {


        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

        }

        override fun onProviderEnabled(s: String) {

        }

        override fun onProviderDisabled(s: String) {

        }
    }

    private var latitudeValue: Double = 0.0

    private var longitudeValue: Double = 0.0

    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
        if (mLastLocation != null) {
            if ((lat.equals("0.0") || long.equals("0.0"))||
                    (lat.equals("") || long.equals(""))||
                    (lat!!.isEmpty() || long!!.isEmpty())) {
                mMap.isMyLocationEnabled = true
                myLocation = LatLng(mLastLocation.latitude, mLastLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12f))
                marker = MarkerOptions().position(myLocation)
                mMap.addMarker(marker)
                latitudeValue = myLocation.latitude
                longitudeValue = myLocation.longitude
            }else{
                mMap.isMyLocationEnabled = true
                myLocation = LatLng(lat!!.toDouble(), long!!.toDouble())
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 20f))
                marker = MarkerOptions().position(myLocation)
                mMap.addMarker(marker)
                latitudeValue = lat!!.toDouble()
                longitudeValue =long!!.toDouble()
            }

        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient!!.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        mGoogleApiClient!!.disconnect()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {

            }
        }

        if (requestCode == 200) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "You need to accept Permissions to view Map.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setLocationToMap(location: String) {

    }
}
