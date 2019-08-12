package com.phaseII.placepoint.Business.Profile

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.phaseII.placepoint.ImagePreview
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.bus_horz_recycler_item.view.*

class ViewImageActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView
    lateinit var back: ImageView
    lateinit var viewPagerItem: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewimgae)
        setToolBar()
        viewPagerItem=findViewById(R.id.viewPagerItem)
        if (intent.hasExtra("model")){
            Glide.with(this)
                    .load(intent.getStringExtra("model"))
                    .apply(RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher))
                    .into(viewPagerItem)
        }else{
            Glide.with(this!!)
                        .load(intent.getParcelableExtra("Uri") as Uri?)
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.placeholder))
                        .into(viewPagerItem)
        }
    }
    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        back = toolbar.findViewById(R.id.back) as ImageView
        back.visibility = View.GONE
        mTitle.text = "View Image"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
