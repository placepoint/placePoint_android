package com.phaseII.placepoint.Town.MultiPatentCategories

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Service
import com.phaseII.placepoint.Town.MultipleTown.ModelMultiTown
import com.phaseII.placepoint.Town.MultipleTown.MultipleTownActivity
import com.phaseII.placepoint.Town.MultipleTown.MultipleTownAdapter
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class MultipleParentCategories : AppCompatActivity() {
    lateinit var adapter: MultipleCategoryAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: Toolbar
    lateinit var back: ImageView
    lateinit var done: TextView
    private var list: ArrayList<ModelCategoryData> = arrayListOf()
    private var list2: ArrayList<ModelCategoryData> = arrayListOf()
    var id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_parent_categories)
        Constants.getPrefs(this)!!.getString(Constants.CATEGORY_LIST, "")
        setToolBar()
    }

    private fun setToolBar() {

        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        back = toolbar.findViewById(R.id.back) as ImageView
        done = toolbar.findViewById(R.id.done) as TextView
        done.visibility= View.VISIBLE
        back.visibility= View.VISIBLE
        back.setOnClickListener{
            finish()
        }
        setSupportActionBar(toolbar)
        title = ""
        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        mTitle.text = "Choose Categories for Flash Deals"
        var categories= Constants.getPrefs(this@MultipleParentCategories)!!.getString(Constants.CATEGORY_LIST, "")
        list = Constants.getCategoryData(categories)!!
        if (list!!.size > 0) {
            val sortedList = list!!.sortedWith(compareBy { it.name })
            for (i in 0 until sortedList.size){
                if (sortedList[i].parent_category=="0"){
                    sortedList[i].checked=true
                    list2.add(sortedList[i])
                }
            }


            val pprefs:SharedPreferences =getSharedPreferences("MultiCategory",0);
            val shaid=pprefs.getString("MultiCategoryIds","")
            val animalsArray = shaid.split(",")
            if (!shaid.isEmpty()) {
                for (i in 0 until list2.size) {
                    var exist = 0
                    for (j in 0 until animalsArray.size) {
                        if (list2[i].id == animalsArray[j]) {
                            exist = 1
                        }
                    }
                    list2[i].checked = exist == 1

                }
            }
//            else{
//
//                var id1=""
//
//                for (u in 0 until list2.size){
//                    list2[u].checked=true
//                    if (id1.isEmpty()){
//                        id1=list2[u].id
//                    }else{
//                        id1=id1+","+list2[u].id
//                    }
//                }
//
//                val editor=pprefs.edit()
//                editor.putString("MultiCategoryIds",id1)
//                editor.apply()
//            }
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = MultipleCategoryAdapter(this, list2 )
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }
        done.setOnClickListener{
            Constants.getPrefs(this)?.edit()?.remove("changeMultiTown")?.apply()
            id=adapter.getSelectedCatetegoriesIds()
            if (id.isEmpty()){
                Toast.makeText(this@MultipleParentCategories,"Please Select at least one Category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var pprefs: SharedPreferences =getSharedPreferences("MultiCategory",0);
            var editor=pprefs.edit()
            editor.putString("MultiCategoryIds",id)
            editor.apply()
            if (Constants.getPrefs(this@MultipleParentCategories)!!.getString("getAuthCode", "") == "") {
                Constants.getPrefs(this@MultipleParentCategories)?.edit()?.putString(Constants.OPEN_TOWN, "no")?.apply()
                runAppAuthService(getIntent().getStringExtra("townId"), getIntent().getStringExtra("townName"))

            }else{
                var intent= Intent(this@MultipleParentCategories, DashBoardActivity::class.java)
//                intent.putExtra("from","cat")
//                intent.putExtra("from1",getIntent().getStringExtra("from1"))
//                intent.putExtra("townId",getIntent().getStringExtra("townId"))
//                intent.putExtra("townName",getIntent().getStringExtra("townName"))
                startActivity(intent)
            }

        }
    }

    private fun runAppAuthService(townId: String, townname: String) {
        val town_id = townId
        // val town_id = Constants.getPrefs(context)?.getString(Constants.TOWN_ID, "")
        val device_id = Constants.getDeviceId(this@MultipleParentCategories)
       // Toast.makeText(this,device_id,Toast.LENGTH_LONG).show()
        val device_type = Constants.DEVICE_TYPE
        appAuthService(device_id, town_id, device_type,townname)
    }
    private fun appAuthService(device_id: String, town_id: String?, device_type: String, townname: String) {

        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getAppAuth(device_id, town_id, device_type)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            val data = `object`.optJSONArray("data")

                            if (town_id != null) {
                                runAppService(setData(data),town_id,townname)
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }
    private fun setData(data: JSONArray?): JSONObject {
        var obj: JSONObject? = null
        if (data != null) {
            for (i in 0 until data.length()) {
                obj = data.optJSONObject(i)
            }
        }
        return obj!!
    }
    private fun runAppService(data: JSONObject, town_id1: String, townname: String) {

        val device_id = Constants.getDeviceId(this@MultipleParentCategories)
        val device_type = Constants.DEVICE_TYPE
        val retrofit = Constants.getWebClient()
        val service = retrofit!!.create(Service::class.java)
        val call: Call<ResponseBody> = service.getAppData()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    try {
                        val res = response.body()!!.string()
                        val `object` = JSONObject(res)
                        val status = `object`.optString("status")
                        if (status.equals("true", ignoreCase = true)) {
                            val location = `object`.optJSONArray("location")
                            val category     = `object`.optJSONArray("category")
                            saveCategories(category.toString())
                            saveTowns(location.toString())
                            saveAuthDataToPrefs(data,town_id1,townname)
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                // view.showNetworkError(R.string.network_error)
            }
        })
    }

    private fun saveTowns(toString: String) {
        try {
            Constants.getPrefs(this@MultipleParentCategories)?.edit()?.putString(Constants.LOCATION_LIST, toString)?.apply()

        } catch (e: Exception) {
        }


    }

    private fun saveCategories(toString: String) {
        try {
            Constants.getPrefs(this@MultipleParentCategories)?.edit()?.putString(Constants.CATEGORY_LIST, toString)?.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun saveAuthDataToPrefs(data: JSONObject, town_id: String?, townname: String) {
        try {
            if (data != null) {
                Constants.getPrefs(this@MultipleParentCategories)?.edit()?.putString(Constants.ID, data.optString("id"))?.apply()
                Constants.getPrefs(this@MultipleParentCategories)?.edit()?.putString(Constants.AUTH_CODE, data.optString("auth_code"))?.apply()
                Constants.getPrefs(this@MultipleParentCategories)?.edit()?.putString(Constants.DEVICE_ID, data.optString("device_id"))?.apply()
                Constants.getPrefs(this@MultipleParentCategories)?.edit()?.putString(Constants.DEVICE_TYPE, data.optString("device_type"))?.apply()
                Constants.getPrefs(this@MultipleParentCategories)?.edit()?.putString(Constants.BUSINESS_ID, data.optString("business_id"))?.apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Constants.getPrefs(this@MultipleParentCategories)?.edit()?.putString("getAuthCode", "no")?.apply()
        Constants.getPrefs(this@MultipleParentCategories)!!.edit().putString(Constants.TOWN_NAME, townname).apply()
        Constants.getPrefs(this@MultipleParentCategories)!!.edit().putString(Constants.TOWN_ID, town_id).apply()


        var intent= Intent(this@MultipleParentCategories, DashBoardActivity::class.java)
        intent.putExtra("from","cat")
        intent.putExtra("from1",getIntent().getStringExtra("from1"))
        intent.putExtra("townId",getIntent().getStringExtra("townId"))
        intent.putExtra("townName",getIntent().getStringExtra("townName"))
        startActivity(intent)
    }

}
