package com.phaseII.placepoint.Categories


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.AlphabetiComparator
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.MultichoiceCategories.CategoryHelper
import com.phaseII.placepoint.MultichoiceCategories.CategoryPresenter
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.business_item.view.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class CategoriesFragment : Fragment(), CategoryHelper {

    private lateinit var mPresenter: CategoryPresenter
    lateinit var noData: TextView
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CategoriesAdapter
    private lateinit var parentList: ArrayList<ModelCategoryData>
    private lateinit var childList: ArrayList<ModelCategoryData>
    lateinit var toolbar: Toolbar
    private lateinit var mTitle: TextView
    private var isRunning: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.categories_fragment, container, false)
        setToolBar(view)
        initialize(view)
        return view
    }

    override fun onResume() {
        super.onResume()

    }

    private fun setToolBar(view: View) {

        toolbar = view.findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        mTitle.text = "Categories " + " (" + Constants.getPrefs(activity!!)?.getString(Constants.TOWN_NAME, "") + ")"
    }

    private fun initialize(view: View) {
        mPresenter = CategoryPresenter(this)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        noData = view.findViewById(R.id.noData)
        //mPresenter.setDataFromPrefs()
        // mPresenter.runAppService()
//        if (Constants.getPrefs(activity!!)!!.getString("getAuthCode", "") == "yes") {
//            if (!isRunning) {
//                isRunning = true
//                mPresenter.runAppAuthService()
//            }
//
//        } else {
        mPresenter.setDataFromPrefs()
//        }
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

        val taxiTownId = Constants.getPrefs(activity!!)!!.getString(Constants.TAXI_TOWNID, "");
        val choosenTownId = Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_ID, "");
        var idList = taxiTownId!!.split(",")
        var show = 0
        if (idList.contains(choosenTownId)) {
            show = 1
        }
        var listparent = ArrayList<ModelCategoryData>()
        if (show == 0) {

            for (k in 0 until parentList.size) {
                if (parentList[k].name != "Taxi") {
                    listparent.add(parentList[k])
                }
            }
            parentList = listparent
        }


        val grid = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = grid as RecyclerView.LayoutManager?
        adapter = CategoriesAdapter(this.activity!!, list, parentList, "catfrag")
        recyclerView.adapter = adapter
    }

    override fun hideNoData() {
        noData.visibility = View.GONE
    }

    override fun setSearchToView(mList: ArrayList<ModelCategoryData>, query: String) {

    }

    override fun getCategoriesFromPrefs(): String {
        try {
            return Constants.getPrefs(this.activity!!)?.getString(Constants.CATEGORY_LIST, "")!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun getTownId(): String? {
        try {
            return Constants.getPrefs(this.activity!!)?.getString(Constants.TOWN_ID, "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun getDeviceId(): String {
        try {
            return Constants.getDeviceId(this.activity!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        isRunning = false
        progressBar.visibility = View.GONE
    }

    override fun saveAuthDataToPrefs(data: JSONObject) {
        try {
            if (data != null) {
                Constants.getPrefs(activity!!)?.edit()?.putString(Constants.ID, data.optString("id"))?.apply()
                Constants.getPrefs(activity!!)?.edit()?.putString(Constants.AUTH_CODE, data.optString("auth_code"))?.apply()
                Constants.getPrefs(activity!!)?.edit()?.putString(Constants.DEVICE_ID, data.optString("device_id"))?.apply()
                Constants.getPrefs(activity!!)?.edit()?.putString(Constants.DEVICE_TYPE, data.optString("device_type"))?.apply()
                //Constants.getPrefs(this)?.edit()?.putString(Constants.TOWN_ID, data.optString("town_id"))?.apply()
                Constants.getPrefs(activity!!)?.edit()?.putString(Constants.BUSINESS_ID, data.optString("business_id"))?.apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isRunning = false
        Constants.getPrefs(this.activity!!)?.edit()?.putString("getAuthCode", "no")?.apply()
        mPresenter.setDataFromPrefs()
    }

    override fun showNetworkError(resId: Int) {
        Toast.makeText(this.activity!!, getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun getCategories() {
        val ids = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_IDS, "")

        mPresenter.prepareCategory(ids, "")
    }

    override fun getMaincatIDS(): String? {
        return Constants.getPrefs(activity!!)?.getString(Constants.MAIN_CATEGORY, "")
    }

    override fun saveCategories(categories: String) {
        try {
            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.CATEGORY_LIST, categories)?.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun saveTowns(towns: String) {
        try {
            Constants.getPrefs(activity!!)?.edit()?.putString(Constants.LOCATION_LIST, towns)?.apply()
            mPresenter.setDataFromPrefs()
        } catch (e: Exception) {
            mPresenter.setDataFromPrefs()
        }

    }

    override fun getSelectedList(): String {

        return ""
    }
}