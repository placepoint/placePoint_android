package com.phaseII.placepoint.DashBoard

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.BusEvents.*
import com.phaseII.placepoint.Business.BusinessFragment
import com.phaseII.placepoint.Categories.CategoriesFragment
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.Home.HomeFragment
import com.phaseII.placepoint.Login.LoginActivity
import com.phaseII.placepoint.More.MoreFragment
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Service
import com.squareup.otto.Subscribe
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class DashBoardActivity : AppCompatActivity() {

    lateinit var bottomNavigation: BottomNavigationView
    //private lateinit var viewPager: ViewPagerUsedInXml
    lateinit var ft: FragmentTransaction
    var positon = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        bottomNavigation = findViewById(R.id.navigationView)
        // viewPager = findViewById(R.id.pager)
        //  setupViewPager(viewPager)
        addHomeFragment()
        onBottomNavigationClicks()
    }

    private fun addHomeFragment() {
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

    override fun onResume() {
        super.onResume()
        try {
        Constants.getBus().register(this)
        }catch (e:Exception){

        }
        addFragment()

    }

    override fun onPause() {
        super.onPause()
        try {
            Constants.getBus().unregister(this)
        }catch (e:Exception){

        }
    }

    @SuppressLint("ResourceType")
    private fun addFragment() {
        val fregister=Constants.getPrefs(this)?.getString("registers", "no")

        val firstTime = Constants.getPrefs(this)!!.getString("firstTime", "no")
//        val catIds=Constants.getPrefs(this)!!.getString(Constants.CATEGORY_IDS,"")
//        val logged=Constants.getPrefs(this)!!.getBoolean(Constants.LOGGED,false)
      if (fregister=="yes"){
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
      }else if (firstTime == "Town") {
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
//            else if(firstTime=="no") {
//        }else{
//
////            if (logged) {
////                Constants.getPrefs(this)?.edit()?.putString("firstTime", "no")?.apply()
////
////                ft = supportFragmentManager.beginTransaction()
////                ft.add(R.id.pager, BusinessFragment())
////                ft.commit()
////                val menu = bottomNavigation.getMenu()
////                var i = 0
////                val size = menu.size()
////                our@while (i < size) {
////                    val item = menu.getItem(i)
////                    if (item.itemId=== R.id.nav_business) {
////                        item.isChecked = item.itemId === R.id.nav_business
////                        break@our
////                    }
////                    i++
////                }
////
////            }else{
//
//                ft = supportFragmentManager.beginTransaction()
//                ft.add(R.id.pager, HomeFragment())
//                ft.commit()
//                val menu = bottomNavigation.getMenu()
//                var i = 0
//                val size = menu.size()
//                our@while (i < size) {
//                    val item = menu.getItem(i)
//                    if (item.itemId=== R.id.nav_home) {
//                        item.isChecked = item.itemId === R.id.nav_home
//                        break@our
//                    }
//                    i++
//                }
////            }
//        }
    }

    @SuppressLint("CommitTransaction", "ResourceType")
    private fun onBottomNavigationClicks() {
        //----Disable the scrolling BottomNavigation View----------
        Constants.disableShiftMode(bottomNavigation)
        //--------BottomNavigation Clicks
        bottomNavigation.setOnNavigationItemSelectedListener {
            Constants.getPrefs(this)!!.edit().remove("backPress").apply()
            when (it.itemId) {
                R.id.nav_home -> {
//                    val catIds=Constants.getPrefs(this)!!.getString(Constants.CATEGORY_IDS,"")
//                    if (catIds.isEmpty()) {
//
//                        positon=1
//                        //
//                        // viewPager.currentItem = 1
//                        Toast.makeText(this,"Please select category",Toast.LENGTH_LONG).show()
//
//
//                        ft = supportFragmentManager.beginTransaction()
//                        ft.replace(R.id.pager, CategoriesFragment())
//                        ft.commit()
//                        val menu = bottomNavigation.getMenu()
//                        val item=menu.findItem(R.id.nav_home)
//
//
//
//                    }else {
                    if (positon != 0) {
                        positon = 0
                        //viewPager.currentItem = 0
                        //it.itemId=R.id.home
                        ft = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.pager, HomeFragment())
                        ft.commit()
                    }
                }
                R.id.nav_category -> {
                    if (positon != 1) {
                        positon = 1
                        // viewPager.currentItem = 1
                        ft = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.pager, CategoriesFragment())
                        ft.commit()
                    }
                }
                R.id.nav_business -> {
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



    override fun onBackPressed() {
        if (Constants.getPrefs(this)!!.getString("backPress", "0") == "0") {
            finish()
        } else {

            Constants.getPrefs(this)!!.edit().remove("backPress").apply()
            Constants.getBus().post(BusiniessListingBackEvent("TaxiToHome"))
            Constants.getBus().post(LiveListingBackEvent("TaxiToHome"))
        }
    }
    @Subscribe
    fun getMessage(event: SetPagerToHome) {

        positon=0
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
}
