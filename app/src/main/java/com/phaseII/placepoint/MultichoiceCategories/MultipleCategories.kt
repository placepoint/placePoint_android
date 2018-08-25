package com.phaseII.placepoint.MultichoiceCategories

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.R
import org.json.JSONObject


class MultipleCategories : AppCompatActivity(), CategoryHelper,MultiChoiceRadioAdapter.notifyUpdate {
    override fun saveCategories(categories: String) {
        Constants.getPrefs(this)?.edit()!!.remove(Constants.CATEGORY_LIST).apply()
        Constants.getPrefs(this)?.edit()?.putString(Constants.CATEGORY_LIST, categories)?.apply()
    }

    override fun saveTowns(towns: String) {
        Constants.getPrefs(this)?.edit()?.putString(Constants.LOCATION_LIST, towns)?.apply()
    }

    private lateinit var mPresenter: CategoryPresenter
    private lateinit var expandableList: ExpandableListView
    private lateinit var mAdapter: ExpandableAdapter
    lateinit var noData: TextView
    lateinit var progressBar: ProgressBar
    private var from: String = ""
    lateinit var toolbar: Toolbar
    lateinit var toolbarArrow: ImageView
    private var lastExpandedPosition = -1
    private lateinit var mList: ArrayList<ModelCategoryData>
    private lateinit var parentList: ArrayList<ModelCategoryData>
    private lateinit var childList: ArrayList<ModelCategoryData>
    lateinit var  list2:  List<ModelCategoryData>
    lateinit var ids:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_categories)
        Constants.getSSlCertificate(this)
        ini()
        setToolBar()
    }

    private fun ini() {
        mPresenter = CategoryPresenter(this)
        expandableList = findViewById(R.id.lvExp)
        noData = findViewById(R.id.noData)
        progressBar = findViewById(R.id.progressBar)


    }

    @SuppressLint("SetTextI18n")
    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        toolbarArrow = toolbar.findViewById(R.id.arrow_down) as ImageView
        toolbarArrow.visibility = View.GONE
        setSupportActionBar(toolbar)
        title = ""
        val mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        val done = toolbar.findViewById(R.id.done) as TextView
        done.visibility=View.VISIBLE
        mTitle.text = "Choose a Category"
        try {
            from = intent.getStringExtra("from")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (from == "cat") {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setDisplayShowHomeEnabled(false)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            mPresenter.runAppService()
        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            if (from=="profile"||from=="register"){
                 ids= Constants.getPrefs(this)!!.getString(Constants.MAIN_CATEGORY, "")

            }else if(from=="addPost"){
//               val ihj= Constants.getPrefs(this)!!.getString(Constants.ADDPOST_CATEGORY, "")
//                if (ihj.isEmpty()) {
                    ids = Constants.getPrefs(this)!!.getString(Constants.MAIN_CATEGORY, "")
//                    Constants.getPrefs(this)?.edit()?.remove(Constants.ADDPOST_CATEGORY)?.apply()
//                    Constants.getPrefs(this)?.edit()?.putString(Constants.ADDPOST_CATEGORY, ids)?.apply()
//                }
//                 ids=Constants.getPrefs(this)!!.getString(Constants.ADDPOST_CATEGORY, "")

            }else{
                ids=Constants.getPrefs(this)!!.getString(Constants.CATEGORY_IDS, "")
            }

            mPresenter.prepareCategory(ids,from)
        }
        done.setOnClickListener{
            val stringBuilder = StringBuilder("")
            val stringBuilder2 = StringBuilder("")
            var prefix = ""
            var prefix2 = ""
            var  list3:  ArrayList<String> = arrayListOf()
            for (i in 0 until list2.size ){
                if (list2[i].checked){
                    if (list2[i].parent_category!="0") {
                        stringBuilder.append(prefix)
                        prefix = ","
                        stringBuilder.append(list2[i].id)
                    }
                }
            }
            for (i in 0 until list2.size ){
                if (list2[i].checked){
                    if (list2[i].parent_category!="0") {
                        list3.add(list2[i].name)
                        stringBuilder2.append(prefix2)
                        prefix2 = " ,"
                        stringBuilder2.append(list2[i].name)

                    }
                }
            }

            if (prefix.isEmpty()){
                Toast.makeText(this,"Please select at least one sub-category.",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val main = String(stringBuilder)
            val nameMain = String(stringBuilder2)
            if (from=="cat") {

                Constants.getPrefs(this)?.edit()?.putString(Constants.CATEGORY_IDS, main)?.apply()
                val intent = Intent(this, DashBoardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }else if (from=="register"){
                val returnIntent = Intent()
                returnIntent.putExtra("result", nameMain)
                setResult(9 ,returnIntent)
                Constants.getPrefs(this)?.edit()?.putString(Constants.MAIN_CATEGORY, main)?.apply()
                finish()
            }else if(from=="profile"){

                Constants.getPrefs(this)?.edit()?.putString(Constants.MAIN_CATEGORY, main)?.apply()
                Constants.getPrefs(this)?.edit()?.putString(Constants.CATEGORY_NAMES, nameMain)?.apply()
                finish()
            }else if (from=="addPost"){
                Constants.getPrefs(this)?.edit()?.putString(Constants.ADDPOST_CATEGORY, main)?.apply()
                Constants.getPrefs(this)?.edit()?.putString(Constants.CATEGORY_NAMES_ADDPOST, main)?.apply()
                val returnIntent = Intent()
                returnIntent.putExtra("result", nameMain)
                setResult(155 ,returnIntent)
                finish()
            }else{
                Constants.getPrefs(this)?.edit()?.putString(Constants.CATEGORY_IDS, main)?.apply()
                finish()
            }
        }
    }

    override fun getSelectedList(): String {
        return  Constants.getPrefs(this)!!.getString(Constants.ADDPOST_CATEGORY, "")
    }

    override fun setAdapterToList(list: ArrayList<ModelCategoryData>, highlightCat: ArrayList<String>) {
        mList = list
         list2= list.sortedWith(compareBy { it.name })
        parentList = arrayListOf()
        childList = arrayListOf()

        for (i in 0 until list2.size) {
            if (list2[i].parent_category == "0") {
                val model = ModelCategoryData()
                model.name = list2[i].name
                model.id = list2[i].id
                model.image_url = list2[i].image_url
                model.parent_category = list2[i].parent_category
                model.checked = list2[i].checked
                parentList.add(model)
            } else {
                val model = ModelCategoryData()
                model.name = list2[i].name
                model.id = list2[i].id
                model.image_url = list2[i].image_url
                model.parent_category = list2[i].parent_category
                model.checked = list2[i].checked
                childList.add(model)
            }
        }
        val taxiTownId = Constants.getPrefs(this)!!.getString(Constants.TAXI_TOWNID, "");
        val choosenTownId = Constants.getPrefs(this)!!.getString(Constants.TOWN_ID2, "");
        var idList = taxiTownId!!.split(",")
        var show = 0
        if (idList.contains(choosenTownId)) {
            show = 1
        }
        var listparent = ArrayList<ModelCategoryData>()
        var listparentchild = ArrayList<ModelCategoryData>()
        var mainList = ArrayList<ModelCategoryData>()
        if (show == 0) {

            for (k in 0 until parentList.size) {
                if (parentList[k].name != "Taxis") {
                    listparent.add(parentList[k])
                }
            }
            parentList = listparent
      for (k in 0 until childList.size) {
                if (childList[k].name != "Taxis") {
                    listparentchild.add(childList[k])
                }
            }
            childList = listparentchild
       for (k in 0 until list2.size) {
                if (list2[k].name != "Taxis") {
                    mainList.add(list2[k])
                }
            }
            list2 = mainList
        }
        mAdapter = ExpandableAdapter(this, parentList, childList,list2,highlightCat)
        expandableList.setAdapter(mAdapter)
//        expandableList.setOnGroupExpandListener { groupPosition ->
//            // listDataChild=services.get(groupPosition).getServices();
//            if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
//                expandableList.collapseGroup(lastExpandedPosition)
//            }
//            lastExpandedPosition = groupPosition
//        }
    }


//    private fun setSearchList(filteredList: ArrayList<ModelCategoryData>) {
//        mAdapter = ExpandableAdapter(this@MultipleCategories, filteredList, childList, list2)
//        mAdapter.notifyDataSetChanged()
//    }

    fun showNoData() {
        noData.visibility = View.VISIBLE
    }

    override fun hideNoData() {
        noData.visibility = View.GONE
    }


    override fun setSearchToView(mList: ArrayList<ModelCategoryData>, query: String) {
//        searchList = mList
//        mQuery = query
//        filteredList.clear()
//        if (mQuery.trim() != "") {
//            mQuery = query.toLowerCase()
//            for (i in 0 until searchList.size) {
//                val text = searchList[i].name.toLowerCase()
//                if (text.contains(query)) {
//                    filteredList.add(searchList[i])
//                }
//            }
//            if (filteredList.size == 0) {
//                showNoData()
//                for (i in 0 until searchList.size) {
//                }
//            } else {
//                hideNoData()
//            }
//            setSearchList(filteredList)
//        } else {
//            mPresenter.prepareCategory()
//        }
//
    }

    override fun getCategoriesFromPrefs(): String {
        return Constants.getPrefs(this@MultipleCategories)?.getString(Constants.CATEGORY_LIST, "")!!
    }

    override fun getTownId(): String? {
        return Constants.getPrefs(this@MultipleCategories)?.getString(Constants.TOWN_ID, "")
    }

    override fun getDeviceId(): String {
        return Constants.getDeviceId(this@MultipleCategories)
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun saveAuthDataToPrefs(data: JSONObject) {
        Constants.getPrefs(this@MultipleCategories)?.edit()?.putString(Constants.ID, data.optString("id"))?.apply()
        Constants.getPrefs(this@MultipleCategories)?.edit()?.putString(Constants.AUTH_CODE, data.optString("auth_code"))?.apply()
        Constants.getPrefs(this@MultipleCategories)?.edit()?.putString(Constants.DEVICE_ID, data.optString("device_id"))?.apply()
        Constants.getPrefs(this@MultipleCategories)?.edit()?.putString(Constants.DEVICE_TYPE, data.optString("device_type"))?.apply()
       // Constants.getPrefs(this@MultipleCategories)?.edit()?.putString(Constants.TOWN_ID, data.optString("town_id"))?.apply()
        Constants.getPrefs(this@MultipleCategories)?.edit()?.putString(Constants.BUSINESS_ID, data.optString("business_id"))?.apply()

    }

    override fun getCategories() {
        if (from=="profile"){
            ids= Constants.getPrefs(this)!!.getString(Constants.MAIN_CATEGORY, "")

        }else{
            ids=Constants.getPrefs(this)!!.getString(Constants.CATEGORY_IDS, "")

        }
        mPresenter.prepareCategory(ids, from)
    }

    override fun showNetworkError(resId: Int) {
        Toast.makeText(this@MultipleCategories, getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun notifyMainAdapter() {

        mAdapter.notifyDataSetChanged()
    }

    override fun getMaincatIDS(): String? {
        return Constants.getPrefs(this)!!.getString(Constants.MAIN_CATEGORY, "")
    }
}
