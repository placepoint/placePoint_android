package com.phaseII.placepoint.Categories

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.AlphabetiComparator
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.MultichoiceCategories.CategoryHelper
import com.phaseII.placepoint.MultichoiceCategories.CategoryPresenter
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import org.json.JSONObject
import java.util.*

class CategoryActivity : AppCompatActivity() , CategoryHelper {

    override fun saveCategories(categories: String) {

    }

    override fun saveTowns(towns: String) {
    }

    private lateinit var mPresenter: CategoryPresenter
    lateinit var noData: TextView
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CategoriesAdapter
    private lateinit var parentList: ArrayList<ModelCategoryData>
    private lateinit var childList: ArrayList<ModelCategoryData>
    lateinit var toolbar: Toolbar
    private lateinit var mTitle: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories_fragment)
        setToolBar()
        initialize()
    }
    private fun setToolBar() {

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mTitle.text="Categories "+" (" +Constants.getPrefs(this)?.getString(Constants.TOWN_NAME, "")+")"
    }

    private fun initialize() {
        mPresenter = CategoryPresenter(this)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        noData = findViewById(R.id.noData)


    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        //if (intent.getStringExtra("from1")=="true"){
            getCategories()
//        }else{
//            mPresenter.runAppAuthService()
//        }

      //  mPresenter.setDataFromPrefs()
    }

    override fun setAdapterToList(list: ArrayList<ModelCategoryData>, highlightCat: ArrayList<String>) {
        parentList = arrayListOf()
        childList = arrayListOf()
        for (i in 0 until list.size) {
            if (list[i].parent_category == "0") {
                val model = ModelCategoryData()
                model.name = list[i].name
                model.id = list[i].id
                model.image_url = list[i].image_url
                model.parent_category = list[i].parent_category
                model.checked = list[i].checked
                parentList.add(model)
            } else {
                val model = ModelCategoryData()
                model.name = list[i].name
                model.id = list[i].id
                model.image_url = list[i].image_url
                model.parent_category = list[i].parent_category
                model.checked = list[i].checked
                childList.add(model)
            }
        }
        Collections.sort(list, AlphabetiComparator())
        Collections.sort(parentList, AlphabetiComparator())
        val grid = GridLayoutManager(this, 2)
        recyclerView.layoutManager = grid
        adapter = CategoriesAdapter(this, list, parentList,"cat")
        recyclerView.adapter = adapter
    }

    override fun hideNoData() {
        noData.visibility = View.GONE
    }

    override fun setSearchToView(mList: ArrayList<ModelCategoryData>, query: String) {

    }

    override fun getCategoriesFromPrefs(): String {
        try {
            return Constants.getPrefs(this)?.getString(Constants.CATEGORY_LIST, "")!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun getTownId(): String? {
        try {
            return Constants.getPrefs(this)?.getString(Constants.TOWN_ID, "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun getDeviceId(): String {
        try {
            return Constants.getDeviceId(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun saveAuthDataToPrefs(data: JSONObject) {
        try {
            if (data != null) {
                Constants.getPrefs(this)?.edit()?.putString(Constants.ID, data.optString("id"))?.apply()
                Constants.getPrefs(this)?.edit()?.putString(Constants.AUTH_CODE, data.optString("auth_code"))?.apply()
                Constants.getPrefs(this)?.edit()?.putString(Constants.DEVICE_ID, data.optString("device_id"))?.apply()
                Constants.getPrefs(this)?.edit()?.putString(Constants.DEVICE_TYPE, data.optString("device_type"))?.apply()
                //Constants.getPrefs(this)?.edit()?.putString(Constants.TOWN_ID, data.optString("town_id"))?.apply()
                Constants.getPrefs(this)?.edit()?.putString(Constants.BUSINESS_ID, data.optString("business_id"))?.apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun showNetworkError(resId: Int) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun getCategories() {
        val ids=Constants.getPrefs(this)?.getString(Constants.CATEGORY_IDS, "")

        mPresenter.prepareCategory(ids, "profile")
    }

    override fun getMaincatIDS(): String? {
        return ""
    }
    override fun getSelectedList(): String {

        return ""
    }
}
