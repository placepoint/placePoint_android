package com.phaseII.placepoint.Business.Profile.BusinessProfileMap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.Html
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.profile_map_scroll.*
import java.util.*

class ProfileMapActivity : AppCompatActivity(), OnMapReadyCallback, ProfileMapHelper, GoogleApiClient.ConnectionCallbacks {
    var mGoogleApiClient: GoogleApiClient? = null
    private val locationRefreshTime: Long = 0
    private val locationRefreshDistance = 0f
    private lateinit var myLocation: LatLng
    private var mLocationManager: LocationManager? = null
    lateinit var marker: MarkerOptions
    private lateinit var mMap: GoogleMap
    lateinit var toolbar: Toolbar
    private lateinit var mPresenter: ProfileMapPresenter
    private var latitudeValue: Double = 0.0
    private var longitudeValue: Double = 0.0
    private var lat: Double = 0.0

    private var long: Double = 0.0
    private lateinit var placeAutoComplete: PlaceAutocompleteFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_map)
        Constants.getSSlCertificate(this)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mPresenter = ProfileMapPresenter(this)
        mPresenter.setMapConfiguration()
        setToolBar()
        placeAutoComplete = fragmentManager.findFragmentById(R.id.place_autocomplete) as PlaceAutocompleteFragment

        placeAutoComplete.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place?) {
                mPresenter.setLocationData(p0)
                Log.e("places: ", p0.toString())
            }

            override fun onError(p0: Status?) {
            }

        })

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        getCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {

        @SuppressLint("MissingPermission") val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
        if (mLastLocation != null) {
            mMap.isMyLocationEnabled = true
            mMap.clear()
            myLocation = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            lat=mLastLocation.latitude
            long=mLastLocation.longitude
            marker = MarkerOptions().position(myLocation)
            mMap.addMarker(marker)
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address>
            addresses = geocoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1)
            val address = addresses[0].getAddressLine(0)
//            val cityName = addresses[0].locality
//            val stateName = addresses[0].adminArea
            val country = addresses[0].countryName
            val knownName = addresses[0].featureName
//        val knownName1 = m.name.toString()
////        val latlongValue = p0.latLng
//        lat = latlongValue.latitude
//        long = latlongValue.longitude
//        myLocation = latlongValue
//        houseNo.setText(knownName1)
            street.setText(address)
//            state.setText(stateName)
//            city.setText(cityName)
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 20f))
//        marker = MarkerOptions().position(myLocation)
//        mMap.addMarker(marker)
        }


    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = ""
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        val mArrow = toolbar.findViewById(R.id.arrow_down) as ImageView
        val done = toolbar.findViewById(R.id.done) as TextView
        done.visibility=View.VISIBLE
        mArrow.visibility = View.GONE
        mTitle.text = "Address"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        done.setOnClickListener{
            intent.putExtra("location",  street.text.toString() )
            intent.putExtra("lat", lat.toString())
            intent.putExtra("long", long.toString())
            setResult(5, intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
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

    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
        if (mLastLocation != null) {
            try {
                mMap.isMyLocationEnabled = true
                myLocation = LatLng(mLastLocation.latitude, mLastLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 25f))
                marker = MarkerOptions().position(myLocation)
                mMap.addMarker(marker)
                latitudeValue = myLocation.latitude
                longitudeValue = myLocation.longitude
                lat = myLocation.latitude
                long = myLocation.longitude
                mMap.setOnMapClickListener { point ->
                    mMap.clear()
                    myLocation = LatLng(point.latitude, point.longitude)
                    marker = MarkerOptions().position(myLocation)
                    mMap.addMarker(marker)
                    println(point.latitude.toString() + "---" + point.longitude)
                    latitudeValue = point.latitude
                    longitudeValue = point.longitude
                }
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address>
                addresses = geocoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1)
                val address = addresses[0].getAddressLine(0)
                val cityName = addresses[0].locality
                val stateName = addresses[0].adminArea
                val country = addresses[0].countryName
                val knownName = addresses[0].featureName
                houseNo.setText(knownName)
                street.setText(address)
                state.setText(stateName)
                city.setText(cityName)
            }catch (e:Exception){
              e.printStackTrace()
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



    override fun setGeoCoderLocation(p0: Place?) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>
        addresses = geocoder.getFromLocation(p0!!.latLng.latitude, p0.latLng.longitude, 1)
        val address = addresses[0].getAddressLine(0)
        val cityName = addresses[0].locality
        val stateName = addresses[0].adminArea
        val country = addresses[0].countryName
        val knownName = addresses[0].featureName
        val knownName1 = p0.name.toString()
        val latlongValue = p0.latLng

        myLocation = latlongValue
        lat = myLocation.latitude
        long = myLocation.longitude
        houseNo.setText(knownName1)
        street.setText(address)
        state.setText(stateName)
        city.setText(cityName)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 20f))
        marker = MarkerOptions().position(myLocation)
        mMap.addMarker(marker)
    }
}
