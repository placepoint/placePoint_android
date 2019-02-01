package com.phaseII.placepoint.Home.BusinessListing


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phaseII.placepoint.BusEvents.*
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.TitleModel
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.business_listing_fragment.*
import android.location.LocationManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.widget.*
import com.github.florent37.tutoshowcase.TutoShowcase
import com.google.android.gms.location.FusedLocationProviderClient
import com.phaseII.placepoint.ConstantClass.GpsTracker
import kotlinx.android.synthetic.main.filter_dialog.view.*
import java.util.*
import kotlin.collections.ArrayList


class BusinessListingFragment : Fragment(), BusinessHelper, LocationListener {
    override fun onLocationChanged(location: Location?) {
        currentLatitude = location!!.latitude
        currentLongitude = location.longitude
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationManager: LocationManager
    private lateinit var mPresenter: BusinessPresenter
    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView
    lateinit var noData: TextView

    lateinit var toolbarArrow: ImageView
    lateinit var progressBar: ProgressBar
    var cat_list: ArrayList<ModelCategoryData> = arrayListOf()
    var cat_name: ArrayList<String> = arrayListOf()
    private lateinit var checked: String
    private lateinit var cat_id: String
    var imglist: MutableList<String> = arrayListOf()
    var currentLatitude: Double = 0.toDouble()
    var currentLongitude: Double = 0.toDouble()
    lateinit var mData: String
    lateinit var mrelatedTo: String
    lateinit var phoneNumber: String
    lateinit var pCat: String
    lateinit var mc: String
    var mCat: Int = 0
    var c: Int = 0
    var m: Int = 0
    var mh: Int = 0
    lateinit var adapter: BusinessListAdapter
    var openingDialog = 0
    private var dialogo: AlertDialog? = null
    var dialogsAll = Vector<AlertDialog>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.business_listing_fragment, container, false)

        progressBar = view.findViewById(R.id.progressBar)

        noData = view.findViewById(R.id.noData)
        mPresenter = BusinessPresenter(this)
        val tracker = GpsTracker(activity)
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert()
        } else {
            var loc = tracker.getLocation()
            currentLatitude = tracker.getLatitude()
            currentLongitude = tracker.getLongitude()
        }
        Constants.getPrefs(activity!!)!!.edit().putString("showTaxiAtHome", "no").apply()

        mPresenter.prepareBusinessData("catRelatedData")


        return view
    }


    override fun setDataToAdapter(data: String, cat: Int, parent_category_name: String, relatedTo: String) {
        mData = data
        mrelatedTo = relatedTo
        mCat = cat
        pCat = parent_category_name

        try {
            Constants.getPrefs(activity!!)!!.edit().putString("showTaxiAtHome", "no").apply()
            saveBusinessData(data)
            val list1 = Constants.getBusinessData(data)
            val list33 = arrayListOf<ModelBusiness>()
            val list44 = arrayListOf<ModelBusiness>()
            if (list1 != null) {
                if (list1.size > 0) {
                    for (i in 0 until list1.size) {
                        if (list1[i].business_user_id == "0") {
                            list44.add(list1[i])
                        } else {
                            list33.add(list1[i])
                        }
                    }
                    list1.clear()
                    for (i in 0 until list33.size) {
                        list1.add(list33[i])
                    }
                    for (i in 0 until list44.size) {
                        list1.add(list44[i])
                    }
                    Constants.getPrefs(activity!!)!!.edit().putString("showBDLive", "yes").apply()
                    if (Constants.getPrefs(activity!!)!!.getString("showBBLiveYes", "no") == "no") {
                        if (Constants.getPrefs(activity!!)!!.getString("showBDLive", "no") == "yes") {
                            Constants.getPrefs(activity!!)!!.edit().putString("showBBLiveYes", "yes").apply()
                            showCaseBusinessList()
                        }
                    }
                    if (relatedTo == "TaxiRelatedData") {
                        noData.visibility = View.GONE
                        taxiList.visibility = View.VISIBLE
                        businessList.visibility = View.GONE
                        //  Constants.getBus().register(this)
                        var permission = Constants.getPrefs(activity!!)!!.getString("permission", "")
                        if (permission.isEmpty() || permission == "no") {
                            if (Build.VERSION.SDK_INT >= 23 &&
                                    ActivityCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity!!, arrayOf(
                                        android.Manifest.permission.ACCESS_FINE_LOCATION
                                ), 10)
                            }
                        }
                        taxiList.layoutManager = LinearLayoutManager(this.activity!!) as RecyclerView.LayoutManager?
                        adapter = BusinessListAdapter(this.activity!!, list1, relatedTo, currentLatitude, currentLongitude)
                        taxiList.adapter = adapter
                        val modell = TitleModel()

                        if (cat == 1) {
                            modell.name = "Taxis"
                            modell.status = "1"
                        } else {
                            modell.name = "Taxis"
                            modell.status = "0"
                        }

                        Constants.getBus().post(SeTaxitTitleEvent("Taxi"))
                    } else {
                        taxiList.visibility = View.GONE
                        businessList.visibility = View.VISIBLE
                        val modell = TitleModel()
                        if (cat == 1) {
                            modell.name = parent_category_name
                            modell.status = "1"
                        } else {
                            modell.name = "All"
                            modell.status = "0"
                        }
                        Constants.getBus().post(SetTitleEvent(modell))
                        Constants.getPrefs(activity!!)!!.edit().putString("idtitle", "All " + parent_category_name + " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, ""))
                        var gps = GpsTracker(this.activity)
                        noData.visibility = View.GONE
                        //  Constants.getBus().register(this)
                        var permission = Constants.getPrefs(activity!!)!!.getString("permission", "")
                        if (permission.isEmpty() || permission == "no") {
                            if (Build.VERSION.SDK_INT >= 23 &&
                                    ActivityCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity!!, arrayOf(
                                        android.Manifest.permission.ACCESS_FINE_LOCATION
                                ), 10)
                            }
                        }
                        businessList.layoutManager = LinearLayoutManager(this.activity!!) as RecyclerView.LayoutManager?
                        adapter = BusinessListAdapter(this.activity!!, list1, relatedTo, gps.getLatitude(), gps.getLongitude())
                        businessList.adapter = adapter
                    }

                } else {
                    Constants.getPrefs(activity!!)!!.edit().putString("showBDLive", "no").apply()
                    if (relatedTo == "TaxiRelatedData") {

                        noData.visibility = View.VISIBLE
                        taxiList.visibility = View.VISIBLE
                        businessList.visibility = View.GONE
                        // Constants.getBus().register(this)
                        val permission = Constants.getPrefs(activity!!)!!.getString("permission", "")
                        if (permission.isEmpty() || permission == "no") {
                            if (Build.VERSION.SDK_INT >= 23 &&
                                    ActivityCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity!!, arrayOf(
                                        android.Manifest.permission.ACCESS_FINE_LOCATION
                                ), 10)
                            }
                        }
                        val modell = TitleModel()

                        if (cat == 1) {
                            modell.name = "Taxis"
                            modell.status = "1"
                        } else {
                            modell.name = "Taxis"
                            modell.status = "0"
                        }

                        Constants.getBus().post(SeTaxitTitleEvent("Taxi"))
                    } else {

                        val modell = TitleModel()
                        if (cat == 1) {
                            modell.name = parent_category_name
                            modell.status = "1"
                        } else {
                            modell.name = "All"
                            modell.status = "0"
                        }
                        Constants.getBus().post(SetTitleEvent(modell))
                        Constants.getPrefs(activity!!)!!.edit().putString("idtitle", "All " + parent_category_name + " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, ""))
                        noData.visibility = View.VISIBLE
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showCaseBusinessList() {
        TutoShowcase.from(activity!!)
                .setContentView(R.layout.tutorialfour)
                .showOnce("yes")
                .show()
    }

    private fun saveBusinessData(data: String) {
        try {
            Constants.getPrefs(this.activity!!)?.edit()?.putString(Constants.BUSINESS_LIST, data)?.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun showNetworkError(resId: Int) {

    }

    override fun getAuthCode(): String {
        return Constants.getPrefs(this.activity!!)?.getString(Constants.AUTH_CODE, "")!!
    }

    override fun getTownId(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID, "")!!
    }

    override fun getCatId(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_IDSUB, "")!!
    }

    override fun showNodata() {
        noData.visibility = View.VISIBLE
    }

    override fun getTaxiCatId(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.TAXI_SUB_ID, "")!!
    }


    override fun onResume() {
        super.onResume()
        c = 0
        m = 0
        Constants.getSSlCertificate(activity!!)
        if (Constants.getPrefs(activity!!)!!.getString("showTaxiAtHome", "no") == "yes") {
            mPresenter.prepareBusinessData("TaxiRelatedData")
        }

        try {
            Constants.getBus().register(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (ActivityCompat.checkSelfPermission(activity!!,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!! as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
            return
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Constants.getPrefs(activity!!)!!.edit().putString("permission", "yes").apply()

        } else {
            Constants.getPrefs(activity!!)!!.edit().putString("permission", "no").apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        try {
//            Constants.getBus().unregister(this)
//        } catch (e: Exception) {
//
//        }
    }

    override fun onPause() {
        super.onPause()
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {

        }

    }

    override fun onStop() {
        super.onStop()
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {

        }
    }
    @Subscribe
    fun getEventValue(event: BusiniessListingTaxiEvent) {

        mPresenter.prepareBusinessData("TaxiRelatedData")

    }

    @Subscribe
    fun getEventValue(event: CALL_EVENT) {
        showDialog(event.value)
    }


    private fun showDialog(phoneNo: String) {

        phoneNumber = phoneNo
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setCancelable(false)
        dialog.setTitle("Make a call")
        dialog.setMessage(phoneNo)
        dialog.setPositiveButton("Call", DialogInterface.OnClickListener { dialog, id ->
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNo")
            if (ActivityCompat.checkSelfPermission(activity!!,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 671)
                return@OnClickListener
            }

            activity!!.startActivity(callIntent)
        })
                .setNegativeButton("Cancel ", DialogInterface.OnClickListener { dialog, which ->
                    //Action for "Cancel".
                })

        val alert = dialog.create()
        alert.show()
    }

    @Subscribe
    fun getEventValue(event: BusiniessListingBackEvent) {
        var model = TitleModel()
        model.status = "0"
        model.name = Constants.getPrefs(activity!!)!!.getString("Title", "")
        Constants.getBus().post(SetTitleEvent(model))
        taxiList.visibility = View.GONE
        businessList.visibility = View.VISIBLE

    }



    @Subscribe
    fun getEventValue(event: ShowFilter) {
//
//      c++


        val dd=Constants.getPrefs(activity!!)!!.getString("hit", "0").toInt()
//        if (c ==dd ) {

//if (dd==0) {
    try {
        Constants.getPrefs(activity!!)!!.edit().putString("hit", "1").apply()

        askBeforeFilter()
Constants.getBus().register(this)
    } catch (e: Exception) {
        e.printStackTrace()
   // }
}








    }


    lateinit var mAlertDialog: AlertDialog

    @SuppressLint("CommitPrefEdits")
    private fun askBeforeFilter() {




        val mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.filter_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity!!)
                .setView(mDialogView)
                .setTitle("SortBy")
        //.setCancelable(false)
        mAlertDialog = mBuilder.create()
        dialogsAll.add(mAlertDialog)
        mAlertDialog.show()
        mDialogView.radioOne.isChecked = true
        mDialogView.okButton.setOnClickListener {
            for (dialog in dialogsAll){
                if (dialog.isShowing){
                    dialog.dismiss()


                }
            }

            Constants.getPrefs(activity!!)!!.edit().putString("cob", "0").apply()
            if (mDialogView.radioOne.isChecked) {

                val list1: ArrayList<ModelBusiness> = Constants.getBusinessData(mData)!!
                if (list1 != null) {
                    for (i in 0 until list1.size) {
                        if (list1[i].lat.isEmpty() || list1[i].lat == null) {
                            list1[i].lat = "0"
                        }
                        if (list1[i].long.isEmpty() || list1[i].long == null) {
                            list1[i].long = "0"
                        }
                        val distance = Constants.findDistanceFromCurrentPosition(currentLatitude, currentLongitude
                                , list1.get(i).lat.replace("[;\\\\/:*?\\\"<>|&']", "").toDouble(), list1.get(i).long.replace("[;\\\\/:*?\\\"<>|&']", "").toDouble())
                        val roundDis = String.format("%.2f", distance)
                        val model = ModelBusiness()
                        model.distance = roundDis
                        list1[i].distance = roundDis
                    }
                }
                for (jj in 0 until list1.size) {
                    System.out.println("LL: " + list1[jj].distance)
                }
                val sortedList = list1.sortedWith(compareBy { it.distance.toDouble() })
                val gps = GpsTracker(this.activity)
                //   adapter.refreshApaper(sortedList)
                businessList.layoutManager = LinearLayoutManager(activity!!)
                adapter = BusinessListAdapter(activity!!, sortedList, mrelatedTo, gps.getLatitude(), gps.getLongitude())
                businessList.adapter = adapter

            }
            if (mDialogView.radioTwo.isChecked) {

                mPresenter.prepareBusinessData("catRelatedData")
            }

        }
        //cancel button click of custom layout
        mDialogView.cancleButton.setOnClickListener {
            //dismiss dialog
            for (dialog in dialogsAll){
                if (dialog.isShowing){
                    dialog.dismiss()
                    dialogsAll.remove(dialog)

                }
            }

        }



//        dialogo = AlertDialog.Builder(activity!!)
//                .setTitle("Alert!")
//                .setMessage("Are you sure you want to  sort by Nearest Listing?")
//                .setPositiveButton("Yes") { dialog, which ->
//                    openingDialog = 0
//
//
//                }
//                .setNegativeButton("No") { dialog, Which ->
//                    openingDialog = 0
//                    dialog.dismiss()
//                }.show()

    }

    fun showData() {
        askBeforeFilter()

    }


}