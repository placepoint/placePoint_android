package com.phaseII.placepoint.HomeNew

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.phaseII.placepoint.ConstantClass.GpsTracker
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.BusinessListing.BusinessListAdapter
import com.phaseII.placepoint.Home.BusinessListing.ModelBusiness
import com.phaseII.placepoint.R
import com.phaseII.placepoint.sdk.JivoActivity

class HomeFragment : Fragment(), HomeNewContract.View , LocationListener {
    override fun onLocationChanged(location: Location?) {

        currentLatitude = location!!.latitude
        currentLongitude = location.longitude
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    lateinit var progressBar: ProgressBar
    lateinit var townName: TextView
    lateinit var businessList: RecyclerView
    lateinit var adapter: BusinessListAdapter
    var currentLatitude: Double = 0.toDouble()
    var currentLongitude: Double = 0.toDouble()

    lateinit var switch: SwitchFragment
    lateinit var seeAllLayout: LinearLayout
    lateinit var seeCategoriesLayout: LinearLayout
    lateinit var featureLayout: LinearLayout
    lateinit var updateLay: LinearLayout
    lateinit var floatingButton: TextView
    lateinit var toolbar: Toolbar
    lateinit var mText: TextView
    private lateinit var mPresenter: HomeNewPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_newhome, container, false)
        floatingButton = view.findViewById(R.id.floatingButton)
        featureLayout = view.findViewById(R.id.featureLayout)
        progressBar = view.findViewById(R.id.progressBar)
        townName = view.findViewById(R.id.townName)
        businessList = view.findViewById(R.id.businessList)
        townName.text=Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME,"")
        mPresenter = HomeNewPresenter(this)

        val tracker = GpsTracker(activity)
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert()
        } else {
            var loc = tracker.getLocation()
            currentLatitude = tracker.getLatitude()
            currentLongitude = tracker.getLongitude()
        }

        updateLay = view.findViewById(R.id.updateLay)
        seeCategoriesLayout = view.findViewById(R.id.seeCategoriesLayout2)
        seeAllLayout = view.findViewById(R.id.seeAllLayout2)
        toolbar = view.findViewById(R.id.toolbar)
        mText = toolbar.findViewById(R.id.toolbar_title)
        mText.text = "Welcome to"
        seeAllLayout.setOnClickListener {
            Constants.getPrefs(activity!!)!!.edit().putString("showNewHome", "no").apply()

            Constants.getPrefs(activity!!)!!.edit().putString("showLay", "static").apply()
            switch.homeClick(1)
        }
        seeCategoriesLayout.setOnClickListener {
            Constants.getPrefs(activity!!)!!.edit().putString("showNewHome", "no").apply()
            switch.homeClick(2)
        }
        floatingButton.setOnClickListener {
            val browserIntent = Intent(activity!!,JivoActivity::class.java)
            activity!!.startActivity(browserIntent)
        }
        updateLay.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.placepoint.ie/download.php"));
            activity!!.startActivity(browserIntent);
        }
//       if (Constants.getPrefs(activity!!)!!.getString(Constants.USERTYPE, "") != "3"){
           featureLayout.visibility=View.VISIBLE
//       }else{
//           featureLayout.visibility=View.GONE
//       }

        return view
    }

    //--------------------------New HomeDesign Mar 15 2019-------------
    override fun getAuthCode(): String {
        return Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE,"")
    }

    override fun showLoader() {
        progressBar.visibility=View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility=View.GONE
    }

    override fun setDataToAdapter(data: String) {


        val list1 = Constants.getBusinessData(data) as List<ModelBusiness>
        val list2:ArrayList<ModelBusiness> = ArrayList()
        for(i in 0 until list1.size){

                list2.add(list1[i])

        }
        if (list2.size>0) {
            var gson = Gson();
            var textList = ArrayList<ModelBusiness>()
            textList.addAll(list2)
            var jsonText = gson.toJson(textList)
            var prefsEditor = Constants.getPrefs(activity!!)!!.edit()
            prefsEditor.putString("staticBusiness", jsonText)
            prefsEditor.apply()
        }
        var gps = GpsTracker(this.activity)
        businessList.layoutManager = LinearLayoutManager(this.activity!!) as RecyclerView.LayoutManager?
        adapter = BusinessListAdapter(this.activity!!, list2, "catRelatedData", gps.getLatitude(), gps.getLongitude(), "hideTaxi")
        businessList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        var prefsEditor=Constants.getPrefs(activity!!)!!
        var data=prefsEditor.getString("staticBusiness","")
        mPresenter.prepareBusinessData("catRelatedData")
        if (data.isEmpty()||data.equals("[]")){

        }else {

        }
    }

    override fun showNodata() {
    }

    override fun showNetworkError(network_error: Int) {
    }

    //-----------------------------------------------------------------


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        switch = context as SwitchFragment
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        switch = activity as SwitchFragment
    }

    interface SwitchFragment {
        fun homeClick(value: Int)
    }

    private lateinit var callPhoneNO: String
//
//    override fun callPhone(phoneNo: String) {
//        callPhoneNO=phoneNo
//        if (ActivityCompat.checkSelfPermission(activity!!,
//                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 167)
//
//        }
//        else{
//            val callIntent = Intent(Intent.ACTION_CALL)
//            callIntent.data = Uri.parse("tel:$phoneNo")
//            activity!!.startActivity(callIntent)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == 199) {
//            try {
//                if (checkPhonePermission()) {
//                    val callIntent = Intent(Intent.ACTION_CALL)
//                    callIntent.data = Uri.parse("tel:$callPhoneNO")
//                    activity!!.startActivity(callIntent)
//                }
//            } catch (ignored: Exception) {
//            }
//        }
//    }
//    private fun checkPhonePermission(): Boolean {
//        var permission = android.Manifest.permission.CALL_PHONE;
//        var res = activity!!.checkCallingOrSelfPermission(permission);
//        return (res == PackageManager.PERMISSION_GRANTED);
//    }

}
