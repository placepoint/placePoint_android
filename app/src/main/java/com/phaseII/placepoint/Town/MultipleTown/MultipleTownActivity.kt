package com.phaseII.placepoint.Town.MultipleTown


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Town.MultiPatentCategories.MultipleParentCategories
import java.util.*
import kotlin.collections.ArrayList


class MultipleTownActivity : AppCompatActivity() {
    lateinit var adapter: MultipleTownAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: Toolbar
    //lateinit var toolbarArrow: ImageView
    lateinit var back: ImageView
    lateinit var done: TextView
    private var list: ArrayList<ModelMultiTown>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_town)
        setToolBar()
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId==android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
    private fun setToolBar() {

        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        back = toolbar.findViewById(R.id.back) as ImageView
        done = toolbar.findViewById(R.id.done) as TextView
        done.visibility = View.VISIBLE
        //toolbarArrow.visibility = View.GONE
        setSupportActionBar(toolbar)

        if(!Constants.getPrefs(this)!!.getString("changeMultiTown", "").isEmpty()){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }else{
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setDisplayShowHomeEnabled(false)
        }
        title = ""
        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        mTitle.text = "Choose Town for Flash Deals"
        var allTowns = Constants.getPrefs(this@MultipleTownActivity)!!.getString(Constants.LOCATION_LIST, "")
        list = Constants.getTownData2(allTowns)
        if (list!!.size > 0) {
            val sortedList = list!!.sortedWith(compareBy { it.townname })
            var pprefs: SharedPreferences = getSharedPreferences("MultiTown", 0);

            //val elephantList = Arrays.asList(shaid.split(","))

            var shaid = pprefs.getString("MultiTownIds", "")
            val animalsArray = shaid.split(",")
            //val elephantList = Arrays.asList(shaid.split(",")) as ArrayList<String>
            if (!shaid.isEmpty()) {


                for (i in 0 until sortedList.size) {
                    var exist = 0
                    for (j in 0 until animalsArray.size) {
                        if (sortedList[i].id == animalsArray[j]) {
                            exist = 1
                        }
                    }
                    sortedList[i].checked = exist == 1

                }
            }else{
                for (i in 0 until sortedList.size){
                    sortedList[i].checked = sortedList[i].id=="9"
                }
            }
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = MultipleTownAdapter(this@MultipleTownActivity, sortedList)
            recyclerView.adapter = adapter
        }
        done.setOnClickListener {
            var id = ""
            id = adapter.getSelectedTownIds()
            if (id.isEmpty()) {
                Toast.makeText(this@MultipleTownActivity, "Please Select at least one Town", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var pprefs: SharedPreferences = getSharedPreferences("MultiTown", 0);
            var editor = pprefs.edit()
            editor.putString("MultiTownIds", id)
            editor.apply()
            var intent = Intent(this@MultipleTownActivity, MultipleParentCategories::class.java)
            intent.putExtra("from", "cat")
            intent.putExtra("from1", getIntent().getStringExtra("from1"))
            intent.putExtra("townId", getIntent().getStringExtra("townId"))
            intent.putExtra("townName", getIntent().getStringExtra("townName"))
            startActivity(intent)

        }
    }
}
