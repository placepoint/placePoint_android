package com.phaseII.placepoint.Categories


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.AlphabetiComparator
import com.phaseII.placepoint.BusEvents.DoBackActionInDashBoard
import com.phaseII.placepoint.BusEvents.ShowHomeButton
import com.phaseII.placepoint.BusEvents.SwitchToMainCategory
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.MultichoiceCategories.CategoryHelper
import com.phaseII.placepoint.MultichoiceCategories.CategoryPresenter
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.R
import com.squareup.otto.Subscribe
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
    private lateinit var back: ImageView
    private var isRunning: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.categories_fragment, container, false)
        setToolBar(view)
        initialize(view)
        Constants.getSSlCertificate(activity!!)
        return view
    }

    override fun onResume() {
        super.onResume()
        try {
            Constants.getBus().register(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Constants.getPrefs(activity!!)!!.edit().putString("subcategory", "0").apply()
        val show = Constants.getPrefs(this.activity!!)?.getString("showBack", "no")
        if (show == "yes") {
            mTitle.text = Constants.getPrefs(activity!!)!!.getString("MainCatName", "") + " " + " (" + Constants.getPrefs(activity!!)?.getString(Constants.TOWN_NAME, "") + ")"
            back.visibility = View.VISIBLE
        } else {
            var inSubCategory = Constants.getPrefs(activity!!)!!.getString("subcategory", "0");
            if (inSubCategory == "0") {
                mTitle.text = "Categories " + " (" + Constants.getPrefs(activity!!)?.getString(Constants.TOWN_NAME, "") + ")"
//                val catListingPage = Constants.getPrefs(this.activity!!)?.getString("catListingPage", "no")
//                if(catListingPage=="yes"){
//                    back.visibility = View.VISIBLE
//                }else{
                    back.visibility = View.GONE
//                }
            }else{
                mTitle.text = Constants.getPrefs(activity!!)!!.getString("MainCatName", "") + " " + " (" + Constants.getPrefs(activity!!)?.getString(Constants.TOWN_NAME, "") + ")"
                back.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setToolBar(view: View) {

        toolbar = view.findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        back = toolbar.findViewById(R.id.back) as ImageView
        mTitle.text = "Categories " + " (" + Constants.getPrefs(activity!!)?.getString(Constants.TOWN_NAME, "") + ")"
        back.setOnClickListener {
            Constants.getBus().post(DoBackActionInDashBoard("value"))
        }
    }

    private fun initialize(view: View) {
        mPresenter = CategoryPresenter(this)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        noData = view.findViewById(R.id.noData)

        //mPresenter.setDataFromPrefs()

        var inSubCategory = Constants.getPrefs(activity!!)!!.getString("subcategory", "0");
        //if (inSubCategory == "0") {
            mPresenter.runAppService()
//        } else {
            mPresenter.setDataFromPrefs()
//        }
//        if (Constants.getPrefs(activity!!)!!.getString("getAuthCode", "") == "yes") {
//            if (!isRunning) {
//                isRunning = true
//                mPresenter.runAppAuthService()
//            }
//
//        } else {
        // mPresenter.setDataFromPrefs()
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
        var mainList = ArrayList<ModelCategoryData>()
        var copyList = list
        if (show == 0) {

            for (k in 0 until parentList.size) {
                if (parentList[k].name != "Taxis") {
                    listparent.add(parentList[k])
                }
            }
            parentList = listparent
            for (k in 0 until copyList.size) {
                if (copyList[k].name != "Taxis") {
                    mainList.add(copyList[k])
                }
            }
            copyList = mainList
        }

        var inSubCategory = Constants.getPrefs(activity!!)!!.getString("subcategory", "0");
        var position = Constants.getPrefs(activity!!)!!.getString("position", "0").toInt()
        if (inSubCategory == "1") {

            var childList1: java.util.ArrayList<ModelCategoryData> = arrayListOf()
            for (i in 0 until copyList.size) {
                if (parentList[position].id == copyList[i].parent_category) {
                    childList1.add(copyList[i])
                }
            }
            if (childList1.size > 0) {
                // noData.visibility = View.GONE
                Collections.sort(childList1, AlphabetiComparator())
                if (parentList[position].name != "Taxi") {
                    var model = ModelCategoryData()
                    model.name = "All"
                    model.image_url = parentList[position].image_url
                    childList1.add(0, model)
                }
                parentList = childList1

            }
            Constants.getPrefs(activity!!)!!.edit().putString("showHomeBackButton", "yes").apply()
//            Constants.getBus().post(ShowHomeButton("value"))
        } else if (inSubCategory == "2") {
            Constants.getPrefs(activity!!)!!.edit().putString("showHomeBackButton", "yes").apply()
            Constants.getBus().post(SwitchToMainCategory("value"))
        }


        val grid = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = grid as RecyclerView.LayoutManager?
        adapter = CategoriesAdapter(this.activity!!, copyList, parentList, "catfrag", Constants.getPrefs(activity!!)?.getString(Constants.TOWN_ID, "")!!, Constants.getPrefs(activity!!)?.getString(Constants.TOWN_NAME, "")!!)
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

        } catch (e: Exception) {
        }

    }

    override fun getSelectedList(): String {

        return ""
    }

    @Subscribe
    fun getMessage(event: ShowHomeButton) {
        back.visibility = View.VISIBLE
        mTitle.text = Constants.getPrefs(activity!!)!!.getString("MainCatName", "") + " " + " (" + Constants.getPrefs(activity!!)?.getString(Constants.TOWN_NAME, "") + ")"

        back.setOnClickListener {
            Constants.getBus().post(DoBackActionInDashBoard("value"))
        }
    }

}