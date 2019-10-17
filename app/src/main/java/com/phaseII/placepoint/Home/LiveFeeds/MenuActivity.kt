package com.phaseII.placepoint.Home.LiveFeeds

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.phaseII.placepoint.R

class MenuActivity : AppCompatActivity() {

    lateinit var adapter: MenuAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var textView: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var mText: TextView
    private lateinit var backButton: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setToolBar()
        recyclerView = findViewById(R.id.recyclerViewMenu)
        textView = findViewById(R.id.noData)


        if (intent.hasExtra("menuImages")) {
            val imageList = intent.getStringArrayListExtra("menuImages")
            if (imageList.size != 0) {
                for (i in 0 until imageList.size) {

                    Log.e("menuImages", imageList[i])
                    adapter = MenuAdapter(this, imageList)
                    val linear = LinearLayoutManager(this)
                    recyclerView.layoutManager = linear
                    recyclerView.adapter = adapter
                }
            } else {
                textView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE

            }


        } else {
            textView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

        }


    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        // title = ""
        //supportActionBar!!.setDisplayShowTitleEnabled(false)
        mText = toolbar.findViewById(R.id.toolbar_title) as TextView
        mText.text ="Menu"


        // mText.text = "Name"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
