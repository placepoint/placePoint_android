package com.phaseII.placepoint.DashBoard

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import com.phaseII.placepoint.BusEvents.*
import com.phaseII.placepoint.Business.BusinessFragment
import com.phaseII.placepoint.Categories.CategoriesFragment
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.HomeFragment
import com.phaseII.placepoint.Login.LoginActivity
import com.phaseII.placepoint.More.MoreFragment
import com.phaseII.placepoint.R
import com.squareup.otto.Subscribe
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.onesignal.OneSignal
import com.phaseII.placepoint.Service
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class DashBoardActivity : AppCompatActivity(),HomeFragment.PopupShow {

    lateinit var bottomNavigation: BottomNavigationView
    //private lateinit var viewPager: ViewPagerUsedInXml
    lateinit var ft: FragmentTransaction
    var positon = -1
    var firstexe = 0
    lateinit var jpp:showpp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        OneSignal.idsAvailable { userId, registrationId ->
            //Log.d("debug", "User:$userId")
            System.out.println("User Id: "+userId)
            if (registrationId != null)
            //    Log.d("debug", "registrationId:$registrationId")
            System.out.println("registrationId Id: "+registrationId)
        }
//         val android_id = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.ANDROID_ID)
//        System.out.println("Device Id: "+android_id)
        bottomNavigation = findViewById(R.id.navigationView)
        // viewPager = findViewById(R.id.pager)
        //  setupViewPager(viewPager)
        addHomeFragment()
        onBottomNavigationClicks()
        Constants.getPrefs(this)!!.edit().putString("showBackYesOrNo", "home").apply()
        try {
            // Constants.getBus().unregister(this)
            Constants.getBus().register(this)
        } catch (e: Exception) {

        }
        var userId=""
        OneSignal.idsAvailable { userId1, registrationId ->
            //Log.d("debug", "User:$userId")
            userId=userId1
            System.out.println("User Id: "+userId)
            if (registrationId != null)
            //    Log.d("debug", "registrationId:$registrationId")
                System.out.println("registrationId Id: "+registrationId)
        }

        updateOnesignalidService(Constants.getPrefs(this)!!.getString(Constants.AUTH_CODE,""),userId,Constants.getPrefs(this)!!.getString(Constants.TOWN_ID,""))
    }

    private fun updateOnesignalidService(auth_code1: String, userId: String, townId: String) {

        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.updateOnesignalid(auth_code1, userId,townId)
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
        firstexe = 1
        positon = 0
        ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.pager, HomeFragment())
        ft.commit()
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
        Constants.getPrefs(this)!!.edit().putString("showLay", "static").apply()
    }

    override fun onResume() {
        super.onResume()
        Constants.getSSlCertificate(this)

        val showback = Constants.getPrefs(this)!!.getString("showHomeBackButton", "no")
        if (showback == "no") {
            firstexe = 0
        }
        if (firstexe == 0) {
            if (showback == "no") {
                var fromWhere=Constants.getPrefs(this)!!.getString("comingFrom", "")
                if (fromWhere!="addPost") {
                    addFragment()
                }else{
                    Constants.getPrefs(this)?.edit()?.putString("comingFrom", "")?.apply()
                }
            }
        } else {
            var fromWhere=Constants.getPrefs(this)!!.getString("comingFrom", "")
            firstexe = 0
            if (fromWhere=="more") {
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
            }else{
                if (fromWhere=="addPost"){

                }else{
                    positon = 0
                    Constants.getPrefs(this)!!.edit().putString("showLay", "static").apply()
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
                Constants.getPrefs(this)?.edit()?.putString("comingFrom", "")?.apply()
            }

        }
    }

//    override fun onPause() {
//        super.onPause()
//        try {
//            Constants.getBus().unregister(this)
//        } catch (e: Exception) {
//
//        }
//    }
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
            positon = 2
            ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.pager, BusinessFragment())
            ft.commit()
            val menu = bottomNavigation.getMenu()
            var i = 0
            val size = menu.size()
            our@ while (i < size) {
                val item = menu.getItem(i)
                if (item.itemId === R.id.nav_business) {
                    item.isChecked = item.itemId === R.id.nav_business
                    break@our
                }
                i++
            }
        } else if (firstTime == "Town") {
            Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
            Constants.getPrefs(this)!!.edit().putString("showLay", "static").apply()
            positon = 0
            Constants.getPrefs(this)?.edit()?.putString("firstTime", "no")?.apply()
            ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.pager, HomeFragment())
            ft.commit()
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
        if (firstTime == "sub") {
            try {
                Constants.getPrefs(this)!!.edit().putString("showLay", "static").apply()
                Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
                positon = 0
                Constants.getPrefs(this)?.edit()?.putString("firstTime", "no")?.apply()
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.pager, HomeFragment())
                ft.commit()
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
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

    }

    @SuppressLint("CommitTransaction", "ResourceType")
    private fun onBottomNavigationClicks() {
        //----Disable the scrolling BottomNavigation View----------
        Constants.disableShiftMode(bottomNavigation)
        //--------BottomNavigation Clicks
        bottomNavigation.setOnNavigationItemSelectedListener {
            Constants.getPrefs(this)!!.edit().remove("backPress").apply()
            Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
            Constants.getPrefs(this)!!.edit().putString("comingFrom", "static").apply()

            when (it.itemId) {
                R.id.nav_home -> {
                    if (positon != 0) {
                        positon = 0
                        Constants.getPrefs(this)!!.edit().putString("showBackYesOrNo", "home").apply()
                        Constants.getPrefs(this)!!.edit().putString("showLay", "static").apply()

                        //viewPager.currentItem = 0
                        //it.itemId=R.id.home
                        ft = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.pager, HomeFragment())
                        ft.commit()
                    }
                }
                R.id.nav_category -> {
                    Constants.getPrefs(this)!!.edit().putString("showTaxiAtHome", "no").apply()
                   // if (positon != 1) {
                        positon = 1
                        var inSubCategory = Constants.getPrefs(this)!!.getString("subcategory", "0")

                        Constants.getPrefs(this)!!.edit().putString("showBack", "no").apply()
                        Constants.getPrefs(this)!!.edit().putString("subcategory", "0").apply()
                        Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
                        Constants.getPrefs(this)!!.edit().putString("showBackYesOrNo", "category").apply()

                        // viewPager.currentItem = 1
                        ft = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.pager, CategoriesFragment())
                        ft.commit()

                }
                R.id.nav_business -> {
                    Constants.getPrefs(this)!!.edit().putString("showTaxiAtHome", "no").apply()
                    // viewPager.currentItem = 2
                    val loggedIn = Constants.getPrefs(this@DashBoardActivity)!!.getBoolean(Constants.LOGGED, false)
                    if (loggedIn) {
                        if (positon != 2) {
                            positon = 2
                            ft = supportFragmentManager.beginTransaction()
                            ft.replace(R.id.pager, BusinessFragment())
                            ft.commit()
                        }
                    } else {

                        Constants.getPrefs(this)?.edit()?.putString("firstTime", "Town")?.apply()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.nav_more -> {
                    Constants.getPrefs(this)!!.edit().putString("showTaxiAtHome", "no").apply()
                    if (positon != 3) {
                        positon = 3
                        //viewPager.currentItem = 3
                        ft = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.pager, MoreFragment())
                        ft.commit()
                    }
                }
            }
            true
        }
    }

    //----Adding Fragments with viewPager in DashBoard------
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = DashBoardViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(), "Home")
        adapter.addFragment(CategoriesFragment(), "category")
        adapter.addFragment(BusinessFragment(), "business")
        adapter.addFragment(MoreFragment(), "more")
        viewPager.adapter = adapter
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

            } else if(positon==3){
                finish()
            }

            else {
                var inSubCategory = Constants.getPrefs(this)!!.getString("subcategory", "0");
                if (positon == 1) {
                    if (inSubCategory == "0") {
                        positon = 0
                        Constants.getPrefs(this)!!.edit().putString("showBack", "no").apply()
                        ft = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.pager, HomeFragment())
                        ft.commit()
                        val menu = bottomNavigation.menu
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
                    } else if (inSubCategory == "1") {
                        Constants.getPrefs(this)!!.edit().putString("showBack", "no").apply()
                        Constants.getPrefs(this)!!.edit().putString("subcategory", "0").apply()
                        positon = 1
                        ft = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.pager, CategoriesFragment())
                        ft.commit()
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
                        ft = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.pager, CategoriesFragment())
                        ft.commit()
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
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }

                } else {
                    positon = 0
                    ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.pager, HomeFragment())
                    ft.commit()
                    val menu = bottomNavigation.menu
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
        positon = 0
        ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.pager, HomeFragment())
        ft.commit()
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

    @Subscribe
    fun getMessage(event: SwitchToMainCategory) {
try {
    val editor = Constants.getPrefs(this)!!.edit()
    editor.putString("showLay", "main")
    editor.apply()
    positon = 1
    ft = supportFragmentManager.beginTransaction()
    ft.replace(R.id.pager, HomeFragment())
    ft.commit()
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
}catch (e:Exception){

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
    interface showpp{
        fun ppshow()
    }
}
