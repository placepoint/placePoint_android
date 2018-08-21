package com.phaseII.placepoint.Home.BusinessListing


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.phaseII.placepoint.BusEvents.*
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.TitleModel
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.business_listing_fragment.*
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.phaseII.placepoint.ConstantClass.GpsTracker


class BusinessListingFragment : Fragment(), BusinessHelper, LocationListener {
    override fun onLocationChanged(location: Location?) {
        currentLatitude = location!!.latitude
        currentLongitude = location.longitude
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
    lateinit var phoneNumber: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.business_listing_fragment, container, false)

        progressBar = view.findViewById(R.id.progressBar)
        noData = view.findViewById(R.id.noData)
        mPresenter = BusinessPresenter(this)
        val tracker = GpsTracker(activity)
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert()
        } else {
            var loc=tracker.getLocation()
            currentLatitude = tracker.getLatitude()
            currentLongitude = tracker.getLongitude()
        }
        if (Constants.getPrefs(activity!!)!!.getString("showTaxiAtHome", "no") == "yes") {
            Constants.getPrefs(activity!!)!!.edit().putString("showTaxiAtHome", "no").apply()

            mPresenter.prepareBusinessData("TaxiRelatedData")
        }else{
            mPresenter.prepareBusinessData("catRelatedData")
        }

        return view
    }


    private fun getCatAndLocData() {
        val cat = Constants.getPrefs(this.activity!!)?.getString(Constants.CATEGORY_LIST, "")!!
        try {
            cat_list = Constants.getCategoryData(cat)!!
            val hashSet = HashSet<ModelCategoryData>()
            hashSet.addAll(cat_list)
            cat_list.clear()
            cat_list.addAll(hashSet)
            for (i in 0 until cat_list.size) {
                cat_name.add(cat_list[i].name)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun setDataToAdapter(data: String, cat: Int, parent_category_name: String, relatedTo: String) {

        try {
            saveBusinessData(data)
            val list1 = Constants.getbusinessData(data)
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

                    if (relatedTo == "TaxiRelatedData") {
                        noData.visibility = View.GONE
                        taxiList.visibility = View.VISIBLE
                        businessList.visibility = View.GONE
                        Constants.getBus().register(this)
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
                        taxiList.adapter = BusinessListAdapter(this.activity!!, list1, relatedTo, currentLatitude, currentLongitude)

                        var modell = TitleModel()

                        if (cat == 1) {
                            modell.name = "Taxis"
                            modell.status = "1"
                        } else {
                            modell.name = "Taxis"
                            modell.status = "0"
                        }

                        Constants.getBus().post(SeTaxitTitleEvent("Taxi"))
                        // Constants.getPrefs(activity!!)!!.edit().putString("idtitle",parent_category_name+" in "+Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME,""))

                    } else {
                        taxiList.visibility = View.GONE
                        businessList.visibility = View.VISIBLE
                        var modell = TitleModel()
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

                        Constants.getBus().register(this)
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
                        businessList.adapter = BusinessListAdapter(this.activity!!, list1, relatedTo, gps.getLatitude(), gps.getLongitude())
                    }
//                if (list44.size>0){
//                    freeList.layoutManager = LinearLayoutManager(this.activity!!) as RecyclerView.LayoutManager?
//                    freeList.adapter = FreeListingAdapter(this.activity!!, list44)
//                }

                } else {
                    var modell = TitleModel()
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

        } catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun saveBusinessData(data: String) {
        try {
            Constants.getPrefs(this.activity!!)?.edit()?.putString(Constants.BUSINESS_LIST, data)?.apply()
        }catch (e:Exception){
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
        return Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_IDS, "")!!
    }

    override fun showNodata() {
        noData.visibility = View.VISIBLE
    }

    override fun getTaxiCatId(): String {
        return Constants.getPrefs(activity!!)?.getString(Constants.TAXI_SUB_ID, "")!!
    }

    override fun onResume() {
        super.onResume()
        if (Constants.getPrefs(activity!!)!!.getString("showTaxiAtHome", "no") == "yes") {
            Constants.getPrefs(activity!!)!!.edit().putString("showTaxiAtHome", "no").apply()

            mPresenter.prepareBusinessData("TaxiRelatedData")
        }

        try {
            Constants.getBus().register(this)
        }catch (e:Exception){
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

//        if (requestCode==671){
//            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
//
//            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                startActivity(intent)
//            }
//        }else {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.getPrefs(activity!!)!!.edit().putString("permission", "yes").apply()
                mPresenter.prepareBusinessData("catRelatedData")
            } else {
                Constants.getPrefs(activity!!)!!.edit().putString("permission", "no").apply()
            }
//        }
    }
    override fun onPause() {
        super.onPause()
        try {
            Constants.getBus().unregister(this)
        }catch (e:Exception){

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

        phoneNumber=phoneNo
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setCancelable(false)
        dialog.setTitle("Make a call")
        dialog.setMessage(phoneNo)
        dialog.setPositiveButton("Call", DialogInterface.OnClickListener { dialog, id ->
            //            val callIntent = Intent(Intent.ACTION_CALL)
//            callIntent.data = Uri.parse(phoneNo)
//            startActivity(callIntent)
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


}