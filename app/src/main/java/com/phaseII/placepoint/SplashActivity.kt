package com.phaseII.placepoint

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.Town.MultipleTown.MultipleTownActivity
import com.phaseII.placepoint.Town.TownActivity
import com.phaseII.placepoint.sdk.JivoActivity


class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        var i: Intent
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Constants.getSSlCertificate(this)
        Handler().postDelayed({
            Constants.getPrefs(this)!!.edit().putString("cog","0").apply()
            Constants.getPrefs(this)!!.edit().putString("showBack", "no").apply()
            Constants.getPrefs(this)!!.edit().putString("subcategory", "0").apply()
            Constants.getPrefs(this)!!.edit().putString("showHomeBackButton", "no").apply()
            Constants.getPrefs(this)!!.edit().putString("showBackYesOrNo", "category").apply()
            Constants.getPrefs(this)!!.edit().putString("comingFrom", "").apply()

            val loggedIn = Constants.getPrefs(this@SplashActivity)?.getBoolean(Constants.LOGGED, false)
//
            if (loggedIn!!) {
                val pprefs: SharedPreferences =getSharedPreferences("MultiCategory",0)
                if (pprefs.getString("MultiCategoryIds","").isEmpty()){
                    Constants.getPrefs(this)!!.edit().putString("firstTime","splash").apply()
                    i = Intent(this@SplashActivity, MultipleTownActivity::class.java)
                    startActivity(i)
                }else {

                    Constants.getPrefs(this)!!.edit().putString("firstTime", "splash").apply()
                    i = Intent(this@SplashActivity, DashBoardActivity::class.java)
                    startActivity(i)
                }
            } else {
                val pprefs: SharedPreferences =getSharedPreferences("MultiCategory",0)
                if (pprefs.getString("MultiCategoryIds","").isEmpty()){
                    i = Intent(this@SplashActivity, TownActivity::class.java)
                    i.putExtra("from","false")
                    startActivity(i)
                }else{
                    val pprefs: SharedPreferences =getSharedPreferences("MultiCategory",0)
                    if (pprefs.getString("MultiCategoryIds","").isEmpty()){
                        Constants.getPrefs(this)!!.edit().putString("firstTime","splash").apply()
                        i = Intent(this@SplashActivity, MultipleTownActivity::class.java)
                        startActivity(i)
                    }else {

                        Constants.getPrefs(this)!!.edit().putString("firstTime", "splash").apply()
                        i = Intent(this@SplashActivity, DashBoardActivity::class.java)
                        startActivity(i)
                    }

                }

            }

            finish()
        }, 3000)
    }
}
