package com.phaseII.placepoint.Categories.SubCategories

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import com.phaseII.placepoint.AlphabetiComparator
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import java.util.*

class SubCategoriesActivity : AppCompatActivity() {
    var mainList: ArrayList<ModelCategoryData> = arrayListOf()
    lateinit var noData: TextView
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var catId: String
    lateinit var adapter: SubCategoryAdapter
    lateinit var toolbar: Toolbar
    private lateinit var mTitle: TextView
    var townName:String=""
    var townId:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_categoryies)
        setToolBar()
        setAdapter()
        Constants.getSSlCertificate(this)
    }

    override fun onResume() {
        super.onResume()
        Constants.getBus().register(this)
    }

    private fun setToolBar() {

        toolbar = findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        townName=intent.getStringExtra("townName")
       townId=intent.getStringExtra("townId")
        mTitle.text = intent.getStringExtra("name") + " (" + townName + ")"
    }

    private fun setAdapter() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        noData = findViewById(R.id.noData)
        mainList = intent.getParcelableArrayListExtra("List")
       var  modelData = intent.getStringExtra("model")
        catId = intent.getStringExtra("catId")
        val childList1: ArrayList<ModelCategoryData> = arrayListOf()
        for (i in 0 until mainList.size) {
            if (catId == mainList[i].parent_category) {
                childList1.add(mainList[i])
            }
        }
        if (childList1.size > 0) {
            noData.visibility = View.GONE
            Collections.sort(childList1, AlphabetiComparator())
            if(intent.getStringExtra("name")!="Taxi") {
                var model = ModelCategoryData()
                model.name = "All"
                model.image_url = modelData
                childList1.add(0, model)
            }
            val grid = GridLayoutManager(this, 2)
            recyclerView.layoutManager = grid as RecyclerView.LayoutManager?
            adapter = SubCategoryAdapter(this, childList1, intent.getStringExtra("froms")
                    , intent.getStringExtra("name"),townName,townId)
            recyclerView.adapter = adapter
        } else {
            noData.visibility = View.VISIBLE
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
