package com.phaseII.placepoint.PrivacyPolicy

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.phaseII.placepoint.R
import android.webkit.WebView
import android.widget.TextView
import com.phaseII.placepoint.Constants
import android.widget.Toast
import android.webkit.WebViewClient
import android.app.Activity
import android.webkit.WebSettings




class PrivacyPolicyActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        Constants.getSSlCertificate(this)
       setToolBar()
        //title="Privacy Policy"
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setToolBar() {

    toolbar=findViewById(R.id.toolbar)
        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        mTitle.text = "Privacy Policy"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        webView=findViewById(R.id.webView)
       // webView.loadUrl(Constants.PRIVACY_URL)
        val webSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true


        val activity = this

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
            }


        }

        webView.loadUrl(Constants.PRIVACY_URL)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId==android.R.id.home){
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
