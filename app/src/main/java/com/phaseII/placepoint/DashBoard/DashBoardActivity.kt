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
        BusinessFragment.BackToMoreFragment, BusinessListAdapter.callPermissionDialog,
        CategoriesFragment.BackShow {

    private var flag: Int = 0
    lateinit var bottomNavigation: BottomNavigationView
    private lateinit var currentFragment: Fragment
    lateinit var ft: FragmentTransaction
    var positon = -1
    var firstexe = 0
    lateinit var jpp: showpp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        Constants.getPrefs(this)!!.edit().putString("switchTab", "").apply()
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
//        if (intent.hasExtra("noti")) {
//            showDealsFragment()
//        } else {
//            addHomeFragment()
//        }


        when {
            intent.hasExtra("noti1") -> showCategoryFragment()
            intent.hasExtra("noti") -> showDealsFragment()
            else -> addHomeFragment()
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
        GetPlayStoreVersion(this, "https://play.google.com/store/apps/details?id=com.user24.placepoint").execute()
        val showback = Constants.getPrefs(this)!!.getString("showHomeBackButton", "no")
        val doNothing = Constants.getPrefs(this)?.getString("doNothing", "no")

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
        } else {
            if (Constants.getPrefs(this)?.getString("doNothing", "no").equals("yes")) {
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
        Constants.getPrefs(this)!!.edit().putString("switchTab", "").apply()
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

    override fun showBacdk() {
        onBackPressed()
    }

    override fun showBacdCategory() {
        onBackPressed()
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

    fun showCategoryFragment() {

        positon = 2

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
        Constants.getPrefs(this)!!.edit().putString("MainCatName", "Food & Drink").apply()
        Constants.getPrefs(this)!!.edit().putString("position", 4.toString()).apply()
        Constants.getBus().post(ShowHomeButton("value"))

        Constants.getPrefs(this)!!.edit().putString("subcategory", "2").apply()
        Constants.getPrefs(this)?.edit()!!.putString("doNothing", "dddd").apply()
        Constants.getPrefs(this)!!.edit().putString("comingFrom", "addPost").apply()
        Constants.getPrefs(this)!!.edit().putString("switchTab", "yes").apply()
        Constants.getPrefs(this)!!.edit().putString(Constants.CATEGORY_IDS, "166").apply()
        Constants.getPrefs(this)!!.edit().putString("firstTime", "sub").apply()
        Constants.getPrefs(this)!!.edit().putString(Constants.CATEGORY_NAMEO, "Daily Lunch Specials").apply()
        Constants.getPrefs(this)!!.edit().putString(Constants.CATEGORY_IDSUB, "166").apply()
        Constants.getPrefs(this)!!.edit().putString(Constants.TOWN_ID, "9").apply()
        Constants.getPrefs(this)!!.edit().putString(Constants.TOWN_NAME, "Athlone").apply()
        //   (context as Activity).finish()
        Constants.getPrefs(this)?.edit()!!.putString(Constants.CATEGORY_LIST, "[{\"id\":\"1\",\"name\":\"Entertainment\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533919446_792502.png\",\"slug\":\"entertainment\",\"show_on_live\":\"1\",\"parent_category\":\"0\",\"hashtags\":null,\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-05-29 13:11:57\",\"updated_at\":\"2018-08-10 16:44:06\"},{\"id\":\"18\",\"name\":\"NightClubs\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1532644197_432170.jpg\",\"slug\":\"nightclubs\",\"show_on_live\":\"1\",\"parent_category\":\"1\",\"hashtags\":\"#nightlife #party #bar #dj #music\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-07-20 05:08:23\",\"updated_at\":\"2018-07-31 06:59:44\"},{\"id\":\"26\",\"name\":\"Food & Drink\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1532446929_888680.jpg\",\"slug\":\"food-drink\",\"show_on_live\":\"1\",\"parent_category\":\"0\",\"hashtags\":null,\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-07-24 15:42:09\",\"updated_at\":\"2018-08-17 13:57:54\"},{\"id\":\"29\",\"name\":\"Pubs\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1534509334_424042.png\",\"slug\":\"pubs\",\"show_on_live\":\"1\",\"parent_category\":\"1\",\"hashtags\":\"#pubs #livemusic #citylife #bars #irishpub\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-07-25 06:12:38\",\"updated_at\":\"2018-08-17 12:35:34\"},{\"id\":\"30\",\"name\":\"Live Music\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1532644274_798967.jpg\",\"slug\":\"live-music\",\"show_on_live\":\"1\",\"parent_category\":\"1\",\"hashtags\":\"#livemusic #musician #gig #irishmusic #singer \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-07-25 06:12:57\",\"updated_at\":\"2019-02-08 01:47:45\"},{\"id\":\"33\",\"name\":\"Beauty\\/Wellness\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533923322_795176.png\",\"slug\":\"beauty-wellness-1\",\"show_on_live\":\"1\",\"parent_category\":\"0\",\"hashtags\":null,\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 17:24:09\",\"updated_at\":\"2018-08-10 17:48:42\"},{\"id\":\"37\",\"name\":\"Activities\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533924725_75540.png\",\"slug\":\"activities\",\"show_on_live\":\"1\",\"parent_category\":\"153\",\"hashtags\":\"#activities #outdooractivities  #thingstodo  #familyactivities #indooractivities\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 17:30:02\",\"updated_at\":\"2018-08-10 18:12:05\"},{\"id\":\"39\",\"name\":\"Places of Interest\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533924780_554615.png\",\"slug\":\"places-of-interest\",\"show_on_live\":\"1\",\"parent_category\":\"153\",\"hashtags\":\"#wonderful_places #beautifulplaces #picoftheday  #pinterestinspired #wonderfulplaces \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 17:31:33\",\"updated_at\":\"2018-08-10 18:13:00\"},{\"id\":\"42\",\"name\":\"Restaurants\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533924991_275230.png\",\"slug\":\"restaurants\",\"show_on_live\":\"1\",\"parent_category\":\"26\",\"hashtags\":\"#foodie #instafood #dinner #lunch #delicious \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 17:42:45\",\"updated_at\":\"2018-08-10 18:16:31\"},{\"id\":\"43\",\"name\":\"Cafe's\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533924933_675238.png\",\"slug\":\"cafe-s\",\"show_on_live\":\"1\",\"parent_category\":\"26\",\"hashtags\":\"#cafe #lunch #breakfast #coffeeshop #coffeetime \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 17:43:24\",\"updated_at\":\"2018-08-10 18:15:33\"},{\"id\":\"44\",\"name\":\"Takeaways\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533925014_447981.png\",\"slug\":\"takeaways\",\"show_on_live\":\"1\",\"parent_category\":\"26\",\"hashtags\":\"#takeaway #pizza #chipper  #fishandchips \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 17:44:28\",\"updated_at\":\"2018-08-10 18:16:54\"},{\"id\":\"45\",\"name\":\"Caterer's\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533924944_438901.png\",\"slug\":\"caterer-s\",\"show_on_live\":\"1\",\"parent_category\":\"26\",\"hashtags\":\"#mykitchen #irishfood #desserts #caterers \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 17:45:15\",\"updated_at\":\"2018-08-10 18:15:44\"},{\"id\":\"48\",\"name\":\"Groceries\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533924957_286216.png\",\"slug\":\"groceries\",\"show_on_live\":\"1\",\"parent_category\":\"26\",\"hashtags\":\"#groceries #healthygroceries  #healthy #freshfruit #healthyfood\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 17:54:21\",\"updated_at\":\"2018-08-10 18:15:57\"},{\"id\":\"49\",\"name\":\"Off-License\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533925128_100751.png\",\"slug\":\"off-license\",\"show_on_live\":\"1\",\"parent_category\":\"26\",\"hashtags\":\"#offlicense #irishwhiskey #whiskeyshop #singlemalt #whiskeygram\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 17:54:49\",\"updated_at\":\"2018-08-10 18:18:48\"},{\"id\":\"70\",\"name\":\"Mens Barbers\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533923966_427177.png\",\"slug\":\"mens-barbers\",\"show_on_live\":\"1\",\"parent_category\":\"33\",\"hashtags\":\"#barber #barbershop #barberlife #irishbarber #athlonebarber\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 18:46:55\",\"updated_at\":\"2018-08-10 17:59:26\"},{\"id\":\"71\",\"name\":\"Hairdressers\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533923956_294123.png\",\"slug\":\"hairdressers\",\"show_on_live\":\"1\",\"parent_category\":\"33\",\"hashtags\":\"#hairdresser #irishhairdresser  #hairstylist #hairstyles #hairdresserlife\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 18:47:16\",\"updated_at\":\"2018-08-10 17:59:16\"},{\"id\":\"73\",\"name\":\"Beauty Salon\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533923930_526203.png\",\"slug\":\"beauty-salon\",\"show_on_live\":\"1\",\"parent_category\":\"33\",\"hashtags\":\"#beautysalon #instabeauty #beautyblogger #beautiful #gelnails\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 18:48:07\",\"updated_at\":\"2018-08-10 17:58:50\"},{\"id\":\"76\",\"name\":\"Gyms\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533923941_470032.png\",\"slug\":\"gyms\",\"show_on_live\":\"1\",\"parent_category\":\"33\",\"hashtags\":\"#gymlife #fitfam #gymmotivation #irishfitfam #bodybuilding\\r\\n\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 18:48:59\",\"updated_at\":\"2018-08-10 17:59:01\"},{\"id\":\"78\",\"name\":\"Weight Loss\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533924046_222742.png\",\"slug\":\"weight-loss\",\"show_on_live\":\"1\",\"parent_category\":\"33\",\"hashtags\":\"#weightloss #fitness #workout #healthy #fitfam\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-09 18:49:26\",\"updated_at\":\"2018-08-10 18:00:46\"},{\"id\":\"82\",\"name\":\"Taxis\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1533933062_228368.png\",\"slug\":\"taxis\",\"show_on_live\":\"0\",\"parent_category\":\"1\",\"hashtags\":\"#taxi #taxidriver\",\"town_id\":\",9,35,37,38,39,43,44,45,46,47,48,49,50,51,52,53,54,\",\"status\":\"0\",\"created_at\":\"2018-08-10 17:54:28\",\"updated_at\":\"2019-02-08 01:50:43\"},{\"id\":\"129\",\"name\":\"Kids\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1535584067_223055.png\",\"slug\":\"kids\",\"show_on_live\":\"1\",\"parent_category\":\"153\",\"hashtags\":\"#instakids #kidsofinstagram  #kidsactivities #toddleractivities #familyactivities\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-08-11 08:07:30\",\"updated_at\":\"2018-08-29 23:07:47\"},{\"id\":\"153\",\"name\":\"Activities\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1536638537_307925.jpg\",\"slug\":\"activities-1\",\"show_on_live\":\"1\",\"parent_category\":\"0\",\"hashtags\":null,\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2018-09-11 05:02:17\",\"updated_at\":\"2018-09-11 05:02:17\"},{\"id\":\"154\",\"name\":\"Parties\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1549591031_540811.png\",\"slug\":\"parties\",\"show_on_live\":\"1\",\"parent_category\":\"1\",\"hashtags\":\"#parties #birthdays #weddings #privateparty #stag #hens \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-02-08 01:57:11\",\"updated_at\":\"2019-02-08 01:57:11\"},{\"id\":\"155\",\"name\":\"Sports\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1553366520_85985.jpg\",\"slug\":\"sports\",\"show_on_live\":\"1\",\"parent_category\":\"153\",\"hashtags\":\"#sports #watersports #sportsphotography #sportsnutrition #sportsphotographer\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-03-23 18:42:00\",\"updated_at\":\"2019-03-23 18:42:00\"},{\"id\":\"156\",\"name\":\"Events\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1553366716_133229.jpg\",\"slug\":\"events\",\"show_on_live\":\"1\",\"parent_category\":\"1\",\"hashtags\":\"#events #venue #event \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-03-23 18:45:16\",\"updated_at\":\"2019-03-23 18:45:16\"},{\"id\":\"157\",\"name\":\"Live Sports\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1553366755_281187.png\",\"slug\":\"live-sports\",\"show_on_live\":\"1\",\"parent_category\":\"1\",\"hashtags\":\"#gaa #soccer #hurling #livesports #rubgy\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-03-23 18:45:55\",\"updated_at\":\"2019-03-23 18:45:55\"},{\"id\":\"158\",\"name\":\"Retail\\/Shopping\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1565082636_415665.jpg\",\"slug\":\"retail-shopping\",\"show_on_live\":\"1\",\"parent_category\":\"0\",\"hashtags\":null,\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-08-06 10:10:36\",\"updated_at\":\"2019-08-06 10:10:36\"},{\"id\":\"159\",\"name\":\"Accommodation\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1565082657_282041.jpg\",\"slug\":\"accommodation\",\"show_on_live\":\"1\",\"parent_category\":\"0\",\"hashtags\":null,\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-08-06 10:10:57\",\"updated_at\":\"2019-08-06 10:11:51\"},{\"id\":\"160\",\"name\":\"Other\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1565083353_881169.jpg\",\"slug\":\"other\",\"show_on_live\":\"1\",\"parent_category\":\"159\",\"hashtags\":\"#accommodation #bedandbreakfast #selfcateringaccommodation #nearestaccommodation #countryhouseaccommodation \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-08-06 10:22:33\",\"updated_at\":\"2019-08-07 00:19:23\"},{\"id\":\"161\",\"name\":\"Other Retail\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1565083405_469422.jpg\",\"slug\":\"other-retail\",\"show_on_live\":\"1\",\"parent_category\":\"158\",\"hashtags\":\"#shopping #fashion #christmasshopping  #instagood #shoppingcentre \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-08-06 10:23:25\",\"updated_at\":\"2019-08-07 00:19:51\"},{\"id\":\"162\",\"name\":\"Hotels\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1565132780_597098.png\",\"slug\":\"hotels\",\"show_on_live\":\"1\",\"parent_category\":\"159\",\"hashtags\":\"#hotels #irishhotels #tophotels #accommodation #accommodationinireland \",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-08-07 00:06:20\",\"updated_at\":\"2019-08-07 00:06:20\"},{\"id\":\"163\",\"name\":\"Bed & Breakfast\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1565132807_612894.png\",\"slug\":\"bed-breakfast\",\"show_on_live\":\"1\",\"parent_category\":\"159\",\"hashtags\":\"#bedandbreakfast #irishbreakfast #discoverireland #fullirishbreakfast #nearestaccommodation\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-08-07 00:06:47\",\"updated_at\":\"2019-08-07 00:06:47\"},{\"id\":\"164\",\"name\":\"Clothes\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1565133015_113508.png\",\"slug\":\"clothes\",\"show_on_live\":\"1\",\"parent_category\":\"158\",\"hashtags\":\"#shopping #fashion #christmasshopping #style #instagood #shoppingspree #shoppingcentre #shoppingcenter #giftideas #gift #stressfreeshopping\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-08-07 00:10:15\",\"updated_at\":\"2019-08-07 00:10:15\"},{\"id\":\"165\",\"name\":\"Groceries\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1565133028_195801.png\",\"slug\":\"groceries-1\",\"show_on_live\":\"1\",\"parent_category\":\"158\",\"hashtags\":\"#groceries #healthygroceries  #healthy #freshfruit #healthyfood\",\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-08-07 00:10:28\",\"updated_at\":\"2019-08-07 00:10:28\"},{\"id\":\"166\",\"name\":\"Daily Lunch Specials\",\"image_url\":\"https:\\/\\/www.placepoint.ie\\/upload\\/category\\/1569517346_331798.png\",\"slug\":\"daily-lunch-specials\",\"show_on_live\":\"1\",\"parent_category\":\"26\",\"hashtags\":null,\"town_id\":\"\",\"status\":\"0\",\"created_at\":\"2019-09-26 18:02:26\",\"updated_at\":\"2019-09-26 18:02:26\"}]").apply()

        val editor = Constants.getPrefs(this)!!.edit()
        editor.putString("showLay", "main")
        editor.apply()

        Constants.getPrefs(this)!!.edit().putString("showLay", "yes").apply()
        Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "yes").apply()
        //  Constants.getBus().post(SwitchToMainCategory("value"))
        replaceFragment(DealsFragment())
        val value = Constants.getPrefs(this)!!.getString("cc", "0").toInt()
        val dd = value + 1
        Constants.getPrefs(this)!!.edit().putString("cc", dd.toString()).apply()

    }

    private lateinit var callPhoneNO: String

    override fun callPhone(phoneNo: String) {
        callPhoneNO = phoneNo
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this as Activity, arrayOf(Manifest.permission.CALL_PHONE), 167)

        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNo")
            startActivity(callIntent)
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        currentFragment = fragment
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
        if (currentFragment is BusinessFragment) {
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
private fun getAppVersion(patternString: String, inputString: String): String {
    try {
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

    } catch (ex: PatternSyntaxException) {

        ex.printStackTrace();
    }

    return "";
}

class GetPlayStoreVersion(
        var context: DashBoardActivity,
        var commingUrl: String
//        var listener : Constants.ClickListener
) : AsyncTask<Void, Void, Void>() {
    var playStoreAppVersion: String = "";
    var newVersion = "";

    override fun doInBackground(vararg params: Void?): Void? {

        try {
//It retrieves the latest version by scraping the content of current version from play store at runtime
            var doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.user24.placepoint").get()
            newVersion = doc.getElementsByClass("htlgb").get(6).text()

        } catch (e: Exception) {
            e.printStackTrace();

        }

        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        //Toast.makeText(context,newVersion,Toast.LENGTH_LONG).show()
        var oldVersionName = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0).versionName
        if (!oldVersionName.equals(newVersion)) {
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