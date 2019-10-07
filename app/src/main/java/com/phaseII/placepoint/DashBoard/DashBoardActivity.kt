package com.phaseII.placepoint.DashBoard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentTransaction
import com.phaseII.placepoint.BusEvents.*
import com.phaseII.placepoint.Business.BusinessFragment
import com.phaseII.placepoint.Categories.CategoriesFragment
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.DealsFragment
import com.phaseII.placepoint.Login.LoginActivity
import com.phaseII.placepoint.More.MoreFragment
import com.phaseII.placepoint.R
import com.squareup.otto.Subscribe
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.onesignal.OneSignal
import com.phaseII.placepoint.Home.BusinessListing.BusinessListAdapter
import com.phaseII.placepoint.HomeNew.HomeFragment
import com.phaseII.placepoint.More.MoreAdapter
import com.phaseII.placepoint.Service
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class DashBoardActivity : AppCompatActivity(), DealsFragment.PopupShow,
        HomeFragment.SwitchFragment, MoreAdapter.OpenBusinessFragment,
        BusinessFragment.BackToMoreFragment, BusinessListAdapter.callPermissionDialog {

    lateinit var bottomNavigation: BottomNavigationView
    private lateinit var currentFragment: Fragment
    lateinit var ft: FragmentTransaction
    var positon = -1
    var firstexe = 0
    lateinit var jpp: showpp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        OneSignal.idsAvailable { userId, registrationId ->
            //Log.d("debug", "User:$userId")
            System.out.println("User Id: " + userId)
            if (registrationId != null)
            //    Log.d("debug", "registrationId:$registrationId")
                System.out.println("registrationId Id: " + registrationId)
        }
//         val android_id = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.ANDROID_ID)
//        System.out.println("Device Id: "+android_id)
        bottomNavigation = findViewById(R.id.navigationView)
        Constants.disableShiftMode(bottomNavigation)
        if (intent.hasExtra("noti")) {
            showDealsFragment()
        } else {
            addHomeFragment()
        }
        onBottomNavigationClicks()
        Constants.getPrefs(this)!!.edit().putString("showBackYesOrNo", "home").apply()
        try {
            // Constants.getBus().unregister(this)
            Constants.getBus().register(this)
        } catch (e: Exception) {

        }
        var userId = ""
        OneSignal.idsAvailable { userId1, registrationId ->
            //Log.d("debug", "User:$userId")
            userId = userId1
            System.out.println("User Id: " + userId)
            if (registrationId != null)
            //    Log.d("debug", "registrationId:$registrationId")
                System.out.println("registrationId Id: " + registrationId)
        }

        updateOnesignalidService(Constants.getPrefs(this)!!.getString(Constants.AUTH_CODE, ""), userId, Constants.getPrefs(this)!!.getString(Constants.TOWN_ID, ""))


    }


    private fun updateOnesignalidService(auth_code1: String, userId: String, townId: String) {
        var pprefs1: SharedPreferences = getSharedPreferences("MultiTown", 0);


        var pprefs2: SharedPreferences = getSharedPreferences("MultiCategory", 0);

        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.updateOnesignalid(auth_code1, userId, pprefs1.getString("MultiTownIds", ""), pprefs2.getString("MultiCategoryIds", ""))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {

                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })

    }

    private fun addHomeFragment() {
        if (intent.hasExtra("openMore")) {
            positon = 3
            replaceFragment(MoreFragment())
            val menu = bottomNavigation.getMenu()
            var i = 0
            val size = menu.size()
            our@ while (i < size) {
                val item = menu.getItem(i)
                if (item.itemId === R.id.nav_more) {
                    item.isChecked = item.itemId === R.id.nav_more
                    break@our
                }
                i++
            }
        } else {
            var fromWhere = Constants.getPrefs(this)!!.getString("comingFrom", "")
            firstexe = 0
            if (fromWhere == "more") {
                firstexe = 1
                positon = 1
                replaceFragment(DealsFragment())

                val menu = bottomNavigation.getMenu()
                var i = 0
                val size = menu.size()
                our@ while (i < size) {
                    val item = menu.getItem(i)
                    if (item.itemId === R.id.nav_home) {
                        item.isChecked = item.itemId === R.id.nav_home
                        break@our
                    }
                    i++
                }
            } else {
                try {
                    firstexe = 1
                    positon = 0
                    replaceFragment(HomeFragment())
                    val menu = bottomNavigation.getMenu()
                    var i = 0
                    val size = menu.size()
                    our@ while (i < size) {
                        val item = menu.getItem(i)
                        if (item.itemId === R.id.nav_newHome) {
                            item.isChecked = item.itemId === R.id.nav_newHome
                            break@our
                        }
                        i++
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Constants.getPrefs(this)!!.edit().putString("showLay", "static").apply()
        }
    }

    override fun onResume() {
        super.onResume()
        Constants.getSSlCertificate(this)
        GetPlayStoreVersion(this,"https://play.google.com/store/apps/details?id=com.user24.placepoint").execute()
        val showback = Constants.getPrefs(this)!!.getString("showHomeBackButton", "no")
        val doNothing =Constants.getPrefs(this)?.getString("doNothing", "no")

        if (showback == "no") {
            firstexe = 0
        }
        if (doNothing.equals("no")) {
            if (firstexe == 0) {
                if (showback == "no") {
                    var fromWhere = Constants.getPrefs(this)!!.getString("comingFrom", "")
                    var fromWhere2 = Constants.getPrefs(this)!!.getString("loginfromMore", "")

                    var addPostActivity = Constants.getPrefs(this)!!.getString("addPostActivity", "")
                    if (fromWhere != "addPost") {
                        if (addPostActivity.isEmpty()) {

                            if (Constants.getPrefs(this)!!.getString("showTaxiAtHome", "no") == "yes") {

                            } else {
                                if (Constants.getPrefs(this)!!.getString("showNewHome", "no") == "yes") {

                                } else {
                                    if (fromWhere2.isEmpty()) {
                                        addFragment()
                                    } else {
                                        Constants.getPrefs(this)!!.edit()!!.remove("loginfromMore").apply()
                                    }
                                }
                            }

                        }
                    } else {
                        Constants.getPrefs(this)?.edit()?.putString("comingFrom", "")?.apply()
                    }
                }
            } else {
                var fromWhere = Constants.getPrefs(this)!!.getString("comingFrom", "")
                firstexe = 0
                if (fromWhere == "more") {
                    positon = 3
                    Constants.getPrefs(this)!!.edit().putString("showLay", "main").apply()
                    val menu = bottomNavigation.getMenu()
                    var i = 0
                    val size = menu.size()
                    our@ while (i < size) {
                        val item = menu.getItem(i)
                        if (item.itemId === R.id.nav_more) {
                            item.isChecked = item.itemId === R.id.nav_more
                            break@our
                        }
                        i++
                    }
                } else {
                    if (fromWhere == "addPost") {

                    } else {
                        positon = 0
                        Constants.getPrefs(this)!!.edit().putString("showLay", "static").apply()
                        val menu = bottomNavigation.getMenu()
                        var i = 0
                        val size = menu.size()
                        our@ while (i < size) {
                            val item = menu.getItem(i)
                            if (item.itemId === R.id.nav_newHome) {
                                item.isChecked = item.itemId === R.id.nav_newHome
                                break@our
                            }
                            i++
                        }
                    }
                    Constants.getPrefs(this)?.edit()?.putString("comingFrom", "")?.apply()
                }

            }
        }else{
            if ( Constants.getPrefs(this)?.getString("doNothing", "no").equals("yes")){
                openBusinessFragment()
            }
            Constants.getPrefs(this)?.edit()?.putString("doNothing", "no")?.apply()

        }

    }



    override fun onDestroy() {
        super.onDestroy()
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {

        }
    }

    @SuppressLint("ResourceType")
    private fun addFragment() {
        val freeRegister = Constants.getPrefs(this)?.getString("registers", "no")
        val firstTime = Constants.getPrefs(this)!!.getString("firstTime", "no")
        if (freeRegister == "yes") {

            Constants.getPrefs(this)?.edit()?.putString("registers", "no")?.apply()
            Constants.getPrefs(this)?.edit()?.putString("showbb", "yes")?.apply()
            positon = 3
            replaceFragment(BusinessFragment())

            val menu = bottomNavigation.getMenu()
            var i = 0
            val size = menu.size()
            our@ while (i < size) {
                val item = menu.getItem(i)
                if (item.itemId === R.id.nav_more) {
                    item.isChecked = item.itemId === R.id.nav_more
                    break@our
                }
                i++
            }
        } else if (firstTime == "Town") {
            Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
            Constants.getPrefs(this)!!.edit().putString("showLay", "static").apply()
            positon = 0
            Constants.getPrefs(this)?.edit()?.putString("firstTime", "no")?.apply()
            replaceFragment(HomeFragment())

            val menu = bottomNavigation.getMenu()
            var i = 0
            val size = menu.size()
            our@ while (i < size) {
                val item = menu.getItem(i)
                if (item.itemId === R.id.nav_newHome) {
                    item.isChecked = item.itemId === R.id.nav_newHome
                    break@our
                }
                i++
            }
        }
        if (firstTime == "sub") {
            try {
                Constants.getPrefs(this)!!.edit().putString("showLay", "static").apply()
                Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
                positon = 1
                Constants.getPrefs(this)?.edit()?.putString("firstTime", "no")?.apply()

                replaceFragment(DealsFragment())
                val menu = bottomNavigation.getMenu()
                var i = 0
                val size = menu.size()
                our@ while (i < size) {
                    val item = menu.getItem(i)
                    if (item.itemId === R.id.nav_newHome) {
                        item.isChecked = item.itemId === R.id.nav_newHome
                        break@our
                    }
                    i++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    @SuppressLint("CommitTransaction", "ResourceType")
    private fun onBottomNavigationClicks() {
        //----Disable the scrolling BottomNavigation View----------

        //--------BottomNavigation Clicks
        bottomNavigation.setOnNavigationItemSelectedListener {
            Constants.getPrefs(this)!!.edit().remove("backPress").apply()
            Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
            Constants.getPrefs(this)!!.edit().putString("comingFrom", "static").apply()
            Constants.getPrefs(this)!!.edit().putString("addPostActivity", "").apply()


            when (it.itemId) {
                R.id.nav_newHome -> {
                    if (positon != 0) {
                        positon = 0

                        Constants.getPrefs(this)!!.edit().putString("showNewHome", "yes").apply()
                        //viewPager.currentItem = 0
                        //it.itemId=R.id.home
                        replaceFragment(HomeFragment())

                    }
                }
                R.id.nav_home -> {
                    // if (positon != 1) {
                    Constants.getPrefs(this)!!.edit().putString("showNewHome", "no").apply()

                    positon = 1
                    if (getSharedPreferences("NewLoginRegister", Context.MODE_PRIVATE).getString("NewLoginRegister", "").isEmpty()) {

                        showAlertFirst()
                    }
                    Constants.getPrefs(this)!!.edit().putString("showBackYesOrNo", "home").apply()
                    Constants.getPrefs(this)!!.edit().putString("showLay", "static").apply()
                    //viewPager.currentItem = 0
                    //it.itemId=R.id.home

                    replaceFragment(DealsFragment())
                    // }
                }
                R.id.nav_category -> {
                    Constants.getPrefs(this)!!.edit().putString("showNewHome", "no").apply()
                    Constants.getPrefs(this)!!.edit().putString("showTaxiAtHome", "no").apply()
                    // if (positon != 1) {
                    positon = 2
                    var inSubCategory = Constants.getPrefs(this)!!.getString("subcategory", "0")

                    Constants.getPrefs(this)!!.edit().putString("showBack", "no").apply()
                    Constants.getPrefs(this)!!.edit().putString("subcategory", "0").apply()
                    Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
                    Constants.getPrefs(this)!!.edit().putString("showBackYesOrNo", "category").apply()

                    // viewPager.currentItem = 1

                    replaceFragment(CategoriesFragment())
                }
                R.id.nav_business -> {

                    Constants.getPrefs(this)!!.edit().putString("showNewHome", "no").apply()
                    Constants.getPrefs(this)!!.edit().putString("showTaxiAtHome", "no").apply()
                    // viewPager.currentItem = 2
                    val loggedIn = Constants.getPrefs(this@DashBoardActivity)!!.getBoolean(Constants.LOGGED, false)
                    if (loggedIn) {
                        Constants.getPrefs(this)?.edit()?.putString("addPostActivity", "addPost")?.apply()
                        if (positon != 2) {
                            positon = 2

                            replaceFragment(BusinessFragment())
                        }
                    } else {
                        Constants.getPrefs(this)?.edit()?.putString("addPostActivity", "")?.apply()
                        Constants.getPrefs(this)?.edit()?.putString("firstTime", "Town")?.apply()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.nav_more -> {
                    Constants.getPrefs(this)!!.edit().putString("showNewHome", "yes").apply()
                    Constants.getPrefs(this)?.edit()?.putString("showbb", "back")?.apply()
                    Constants.getPrefs(this)!!.edit().putString("showTaxiAtHome", "no").apply()
                    //if (positon != 3) {
                        positon = 3
                        //viewPager.currentItem = 3

                    replaceFragment(MoreFragment())
                    //}
                }
            }
            true
        }
    }


    @SuppressLint("CommitTransaction")
    override fun onBackPressed() {
        if (Constants.getPrefs(this)!!.getString("backPress", "0") == "0") {
            if (positon == 0) {
                val showback = Constants.getPrefs(this)!!.getString("showHomeBackButton", "no")
                if (showback == "no") {
                    askBeforeExit()
                } else {
                    finish()
                }

            } else if (positon == 3) {


                if (Constants.getPrefs(this)!!.getString("showbb", "") == "yes") {
                    Constants.getPrefs(this)?.edit()?.putString("showbb", "back")?.apply()
                    showMoreFragment()
                } else if (Constants.getPrefs(this)!!.getString("showbb", "") == "back") {
                    Constants.getPrefs(this)?.edit()?.putString("showbb", "no")?.apply()
                  //  Constants.getPrefs(this)?.edit()?.putString("comingFrom", "")?.apply()
                    addHomeFragment()
                } else {
                    finish()
                }


            } else {
                var inSubCategory = Constants.getPrefs(this)!!.getString("subcategory", "0");
                if (positon == 1) {
                    if (inSubCategory == "0") {
                        try {
                            positon = 0
                            replaceFragment(HomeFragment())

                            val menu = bottomNavigation.menu
                            var i = 0
                            val size = menu.size()
                            our@ while (i < size) {
                                val item = menu.getItem(i)
                                if (item.itemId === R.id.nav_newHome) {
                                    item.isChecked = item.itemId === R.id.nav_newHome
                                    break@our
                                }
                                i++
                            }
                            Constants.getPrefs(this)!!.edit().putString("showBack", "no").apply()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else if (inSubCategory == "1") {
                        Constants.getPrefs(this)!!.edit().putString("showBack", "no").apply()
                        Constants.getPrefs(this)!!.edit().putString("subcategory", "0").apply()
                        positon = 1
                        replaceFragment(CategoriesFragment())

                        val menu = bottomNavigation.menu
                        var i = 0
                        val size = menu.size()
                        our@ while (i < size) {
                            val item = menu.getItem(i)
                            if (item.itemId === R.id.nav_category) {
                                item.isChecked = item.itemId === R.id.nav_category
                                break@our
                            }
                            i++
                        }
                    } else {
                        try {


                            Constants.getPrefs(this)!!.edit().putString("showBack", "yes").apply()
                            Constants.getPrefs(this)!!.edit().putString("subcategory", "1").apply()
                            positon = 1
                            replaceFragment(CategoriesFragment())
                            val menu = bottomNavigation.menu
                            var i = 0
                            val size = menu.size()
                            our@ while (i < size) {
                                val item = menu.getItem(i)
                                if (item.itemId === R.id.nav_category) {
                                    item.isChecked = item.itemId === R.id.nav_category
                                    break@our
                                }
                                i++
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                } else {
                    positon = 0
                    replaceFragment(HomeFragment())
                    val menu = bottomNavigation.menu
                    var i = 0
                    val size = menu.size()
                    our@ while (i < size) {
                        val item = menu.getItem(i)
                        if (item.itemId === R.id.nav_newHome) {
                            item.isChecked = item.itemId === R.id.nav_newHome
                            break@our
                        }
                        i++
                    }
                }
            }
        } else {
            Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
            Constants.getBus().post(HideBackButton("hide"))
            Constants.getPrefs(this)!!.edit().remove("backPress").apply()
            Constants.getBus().post(BusiniessListingBackEvent("TaxiToHome"))
            Constants.getBus().post(LiveListingBackEvent("TaxiToHome"))
            Constants.getPrefs(this)!!.edit().putString("freeListing", "no").apply()
        }
    }



    private fun askBeforeExit() {

        AlertDialog.Builder(this)
                .setTitle("Closing application")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    finishAffinity()
                }).setNegativeButton("No", null).show()
    }

    @Subscribe
    fun getMessage(event: SetPagerToHome) {
        positon = 1
        replaceFragment(DealsFragment())
        val menu = bottomNavigation.getMenu()
        var i = 0
        val size = menu.size()
        our@ while (i < size) {
            val item = menu.getItem(i)
            if (item.itemId === R.id.nav_newHome) {
                item.isChecked = item.itemId === R.id.nav_newHome
                break@our
            }
            i++
        }
    }

    @Subscribe
    fun getMessage(event: SwitchToMainCategory) {
        try {
            val editor = Constants.getPrefs(this)!!.edit()
            editor.putString("showLay", "main")
            editor.apply()
            positon = 1
            replaceFragment(DealsFragment())

            val menu = bottomNavigation.menu
            var i = 0
            val size = menu.size()
            our@ while (i < size) {
                val item = menu.getItem(i)
                if (item.itemId === R.id.nav_category) {
                    item.isChecked = item.itemId === R.id.nav_category
                    break@our
                }
                i++
            }
        } catch (e: Exception) {

        }
    }

    @Subscribe
    fun getMessage(event: DoBackActionInDashBoard) {

        onBackPressed()
    }

    override fun showPopUpBL() {

//        var frag=BusinessListingFragment()
//        frag.showData()
        Constants.getBus().post(ShowFilter("show"))
    }

    interface showpp {
        fun ppshow()
    }

    private fun showAlertFirst() {

        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(this)
        } else {
            builder = AlertDialog.Builder(this)
        }
        builder.setCancelable(false)
        builder.setTitle("Alert")
                .setMessage("Thanks for Downloading PlacePoint. Deals are limited to a specific number so they can sometimes sell out in hours. We usually have 1-3 deals per week so keep the app installed if the current deals are expired to get an instant notification when the next deal is available.")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    val firstTimeRegister = getSharedPreferences("NewLoginRegister", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = firstTimeRegister.edit()
                    editor.putString("NewLoginRegister", "yes")
                    editor.apply()
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    override fun homeClick(value: Int) {
        if (value == 1) {
            if (getSharedPreferences("NewLoginRegister", Context.MODE_PRIVATE).getString("NewLoginRegister", "").isEmpty()) {

                showAlertFirst()
            }
            positon = 1
            replaceFragment(DealsFragment())
            val menu = bottomNavigation.getMenu()
            var i = 0
            val size = menu.size()
            our@ while (i < size) {
                val item = menu.getItem(i)
                if (item.itemId === R.id.nav_home) {
                    item.isChecked = item.itemId === R.id.nav_home
                    break@our
                }
                i++
            }
        } else {
            positon = 1
            Constants.getPrefs(this)!!.edit().putString("showTaxiAtHome", "no").apply()
            // if (positon != 1) {
            positon = 2
            var inSubCategory = Constants.getPrefs(this)!!.getString("subcategory", "0")

            Constants.getPrefs(this)!!.edit().putString("showBack", "no").apply()
            Constants.getPrefs(this)!!.edit().putString("subcategory", "0").apply()
            Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
            Constants.getPrefs(this)!!.edit().putString("showBackYesOrNo", "category").apply()

            // viewPager.currentItem = 1

            replaceFragment(CategoriesFragment())
            val menu = bottomNavigation.getMenu()
            var i = 0
            val size = menu.size()
            our@ while (i < size) {
                val item = menu.getItem(i)
                if (item.itemId === R.id.nav_category) {
                    item.isChecked = item.itemId === R.id.nav_category
                    break@our
                }
                i++
            }
        }

    }

    override fun openBusinessFragment() {
        Constants.getPrefs(this)!!.edit().putString("showTaxiAtHome", "no").apply()
        // viewPager.currentItem = 2
        val loggedIn = Constants.getPrefs(this@DashBoardActivity)!!.getBoolean(Constants.LOGGED, false)
        if (loggedIn) {
            Constants.getPrefs(this)?.edit()?.putString("addPostActivity", "addPost")?.apply()
//            if (positon != 3) {
            positon = 3

            replaceFragment(BusinessFragment())
            val menu = bottomNavigation.getMenu()
            var i = 0
            val size = menu.size()
            our@ while (i < size) {
                val item = menu.getItem(i)
                if (item.itemId === R.id.nav_more) {
                    item.isChecked = item.itemId === R.id.nav_more
                    break@our
                }
                i++
            }

//            }
        } else {
            Constants.getPrefs(this)?.edit()?.putString("addPostActivity", "")?.apply()
            Constants.getPrefs(this)?.edit()?.putString("firstTime", "Town")?.apply()
            Constants.getPrefs(this)?.edit()?.putString("loginfromMore", "yes")?.apply()
            Constants.getPrefs(this)?.edit()?.putString("doNothing", "yes")?.apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    override fun showMoreFragment() {

        positon = 3

        replaceFragment(MoreFragment())
        val menu = bottomNavigation.getMenu()
        var i = 0
        val size = menu.size()
        our@ while (i < size) {
            val item = menu.getItem(i)
            if (item.itemId === R.id.nav_more) {
                item.isChecked = item.itemId === R.id.nav_more
                break@our
            }
            i++
        }
    }

    fun showDealsFragment() {

        positon = 1

        replaceFragment(DealsFragment())
        val menu = bottomNavigation.getMenu()
        var i = 0
        val size = menu.size()
        our@ while (i < size) {
            val item = menu.getItem(i)
            if (item.itemId === R.id.nav_home) {
                item.isChecked = item.itemId === R.id.nav_home
                break@our
            }
            i++
        }
    }

    private lateinit var callPhoneNO: String

    override fun callPhone(phoneNo: String) {
        callPhoneNO=phoneNo
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this as Activity, arrayOf(Manifest.permission.CALL_PHONE), 167)

        }
        else{
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNo")
          startActivity(callIntent)
        }
    }



    private fun replaceFragment(fragment: Fragment) {
        currentFragment=fragment
        ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.pager, fragment)
        ft.commit()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Constants.getPrefs(this)!!.edit().putString("permission", "yes").apply()

        } else {
            Constants.getPrefs(this)!!.edit().putString("permission", "no").apply()
        }
        if (requestCode == 167) {
            try {
                if (checkPhonePermission()) {
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:$callPhoneNO")
                startActivity(callIntent)
                }
            } catch (ignored: Exception) {
            }
        }
        if (currentFragment is BusinessFragment){
            if (requestCode == 9004) {
                (currentFragment as BusinessFragment).cameraShowAddPost()
            }
       if (requestCode == 894) {
           (currentFragment as BusinessFragment).galleryShowAddPost()
            }

        }
    }
    private fun checkPhonePermission(): Boolean {
        var permission = android.Manifest.permission.CALL_PHONE;
        var res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

}

//private fun getPlayStoreAppVersion( appUrlString:String) :String{
//
//    return playStoreAppVersion;
//}
private fun getAppVersion( patternString:String,  inputString:String):String{
    try{
        //Create a pattern
        var pattern: Pattern = Pattern.compile(patternString);
        if (null == pattern) {
            return "";
        }

        //Match the pattern string in provided string
        var matcher: Matcher = pattern.matcher(inputString);
        if (null != matcher && matcher.find()) {
            return matcher.group(1);
        }

    }catch ( ex: PatternSyntaxException) {

        ex.printStackTrace();
    }

    return "";
}

 class GetPlayStoreVersion(
         var context: DashBoardActivity,
         var commingUrl: String
//        var listener : Constants.ClickListener
) : AsyncTask<Void, Void, Void>() {
     var playStoreAppVersion :String= "";
     var newVersion = "";

     override fun doInBackground(vararg params: Void?): Void? {

    try {
//It retrieves the latest version by scraping the content of current version from play store at runtime
                var doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.user24.placepoint").get()
        newVersion = doc.getElementsByClass("htlgb").get(6).text()

            }catch ( e:Exception){
                e.printStackTrace();

            }

        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        //Toast.makeText(context,newVersion,Toast.LENGTH_LONG).show()
        var oldVersionName = context.getPackageManager()
                .getPackageInfo( context.getPackageName(), 0).versionName
        if (!oldVersionName.equals(newVersion)){
           // showUpdateAppDialog(newVersion,context)
        }

    }

     private fun showUpdateAppDialog(mPlayStoreVersion: String, context: Context) {
         val dialog = Dialog(context)
         dialog.setContentView(R.layout.version_layout)
         dialog.setCancelable(false)
         //  dialog.setTitle("New App Update Available");
         val tittle = dialog.findViewById(R.id.tittle) as TextView
         val textMessage = dialog.findViewById(R.id.textMessage) as TextView
         val upgrade = dialog.findViewById(R.id.upgradeNow) as TextView
         val remind = dialog.findViewById(R.id.remind) as TextView
         val cancelText = dialog.findViewById(R.id.cancel) as TextView
         tittle.text = "New App Update Available"
         textMessage.text = "A new update of PlacePoint App (Version #$mPlayStoreVersion) is available. Please Upgrade."
         upgrade.setOnClickListener {

             val appPackageName = "com.user24.placepoint" // getPackageName() from Context or Activity object
             try {
                 context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
             } catch (anfe: android.content.ActivityNotFoundException) {
                 context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
             }

             dialog.dismiss()
         }

         dialog.show()

     }

 }