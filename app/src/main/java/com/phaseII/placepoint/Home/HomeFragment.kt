package com.phaseII.placepoint.Home


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import com.phaseII.placepoint.R
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import com.phaseII.placepoint.Constants
import com.github.florent37.tutoshowcase.TutoShowcase
import com.phaseII.placepoint.BusEvents.*
import com.phaseII.placepoint.Home.BusinessListing.BusinessListingFragment
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.Town.TownActivity
import com.squareup.otto.Subscribe
import java.util.ArrayList
import android.content.DialogInterface
import com.phaseII.placepoint.AboutBusiness.BusinessDetails.DetailFragment.setTitle
import android.os.Build
import android.support.v7.app.AlertDialog


class HomeFragment : Fragment(), FlashContractHome.View {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var toolbar: Toolbar
    private lateinit var mText: TextView
    private lateinit var noFlashData: TextView
    private lateinit var dots: ImageView
    private lateinit var back: ImageView
    private lateinit var filter: ImageView
    private lateinit var progressFlashHome: ProgressBar
    private lateinit var layContainViewPager: LinearLayout
    private lateinit var staticLay: LinearLayout
    private lateinit var popbl: PopupShow
    private lateinit var flashPostList: RecyclerView
    private lateinit var pullToRefresh: SwipeRefreshLayout
    private lateinit var flashLish: ArrayList<ModelHome>
    private lateinit var flashAdapter: FlashHomeAdapter

    private lateinit var mPresenter: FlashPresenterHome

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_home, container, false)
        init(view)
        mPresenter = FlashPresenterHome(this)
        Constants.getSSlCertificate(activity!!)
        setToolBar(view)
        setHasOptionsMenu(true)
        setupViewPager(viewPager)
        setCustomFont(tabLayout)
        val cc = Constants.getPrefs(activity!!)!!.getString("cog", "0").toInt()
        val dd = cc + 1
        Constants.getPrefs(activity!!)!!.edit().putString("cog", dd.toString()).apply()
        Constants.getPrefs(activity!!)!!.edit().putString("cob", "0").apply()

        return view
    }


    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        if (!Constants.isAppOpenedFirstTime){
            Constants.isAppOpenedFirstTime=true
           // showAlert()
        }

        Constants.getBus().register(this)
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.SHOW_ALL_POST, "yes").apply()
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.FROMINTENT, "home").apply()
        Constants.getPrefs(activity!!)!!.edit().putString(Constants.STOPCLICK, "no").apply()
        if (Constants.getPrefs(activity!!)!!.getString("showTaxiAtHome", "no") == "yes") {
            taxiWorking()
        }

        val prefs = Constants.getPrefs(activity!!)
        val showLay = prefs!!.getString("showLay", "static")
        if (showLay == "static") {
            filter.visibility = View.GONE
            layContainViewPager.visibility = View.GONE
            staticLay.visibility = View.VISIBLE

            //----------------------------Getting FlashPosts---------------------------------
            val auth_code = Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE, "")
            val town_id = Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_ID, "")
            val category_id = Constants.getPrefs(activity!!)!!.getString(Constants.CATEGORY_IDS, "")
            mPresenter.getFlashPost(auth_code, town_id, category_id, "20", "0")
            pullToRefresh.setOnRefreshListener {
                mPresenter.getFlashPost(auth_code, town_id, category_id, "20", "0")
                pullToRefresh.isRefreshing = false
            }
            //-------------------------------------------------------------------------------
            mText.text = "Flash Sales"
            if (Constants.getPrefs(activity!!)!!.getString("showHLiveYes", "no") == "no") {
                Constants.getPrefs(activity!!)!!.edit().putString("showHLiveYes", "yes").apply()
                showCase()
            }

        } else {

            filter.visibility = View.VISIBLE
            layContainViewPager.visibility = View.VISIBLE
            staticLay.visibility = View.GONE
        }

    }

    private fun showAlert() {

        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(activity!!)
        } else {
            builder = AlertDialog.Builder(activity!!)
        }

        builder.setTitle("Alert")
                .setMessage("PlacePoint is launching in Athlone on the 1st of February. Please note the current data being shown is only test data. Make sure you keep the app installed to ensure you get notifications of new special offers as we have over €1,000 in giveaways and promotions from February the 1st.")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    // continue with delete
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }


    override fun setAdapter(data: String) {

        if (!data.isEmpty()) {
            val list = Constants.getHomeFeedData(data)
            flashLish = list
            if (list.size > 0) {
                try {
                    flashAdapter = FlashHomeAdapter(activity!!, flashLish)
                    noFlashData.visibility = View.GONE
                    flashPostList.visibility = View.VISIBLE
                    flashPostList.layoutManager = LinearLayoutManager(activity!!) as RecyclerView.LayoutManager?
                    flashPostList.adapter = flashAdapter
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                flashPostList.visibility = View.GONE
                noFlashData.visibility = View.VISIBLE
            }

        } else {
            flashPostList.visibility = View.GONE
            noFlashData.visibility = View.VISIBLE
        }
    }

    private fun showCase() {
        TutoShowcase.from(activity!!)
                .setContentView(R.layout.tutorial_businesslist)
                .showOnce("yes")
                .show()
    }

    @SuppressLint("SetTextI18n")
    private fun setToolBar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        mText = toolbar.findViewById(R.id.toolbar_title)
        dots = toolbar.findViewById(R.id.dots)
        back = toolbar.findViewById(R.id.back) as ImageView
        filter = toolbar.findViewById(R.id.filter) as ImageView
        dots.visibility = View.GONE
        val cats = Constants.getPrefs(activity!!)!!.getString(Constants.CATEGORY_IDS, "")
        if (!cats.isEmpty()) {
            mText.text = Constants.getPrefs(activity!!)!!.getString(Constants.CATEGORY_NAMEO, "") +
                    " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")
        }
        dots.setOnClickListener {
            val intent = Intent(activity, TownActivity::class.java)
            intent.putExtra("from", "true")
            startActivity(intent)
        }
        val showBack = Constants.getPrefs(activity!!)!!.getString("showHomeBackButton", "no")
        if (showBack == "yes") {
            back.visibility = View.VISIBLE
        } else {
            if (Constants.getPrefs(activity!!)!!.getString("showBackYesOrNo", "category") != "category") {
                back.visibility = View.GONE
            }
        }
        back.setOnClickListener {
            try {
                Constants.getBus().post(DoBackActionInDashBoard("value"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun init(view: View) {
        noFlashData = view.findViewById(R.id.noFlashData)
        flashPostList = view.findViewById(R.id.flashPostList)
        progressFlashHome = view.findViewById(R.id.progressFlashHome)
        viewPager = view.findViewById(R.id.viewpager)
        tabLayout = view.findViewById(R.id.tabs) as TabLayout
        layContainViewPager = view.findViewById(R.id.layContainViewPager)
        staticLay = view.findViewById(R.id.staticLay)
        pullToRefresh = view.findViewById(R.id.pullToRefresh)
    }

    private fun setCustomFont(tabLayout: TabLayout) {
        try {
            val vg = tabLayout.getChildAt(0) as ViewGroup
            val tabsCount = vg.childCount

            for (j in 0 until tabsCount) {
                val vgTab = vg.getChildAt(j) as ViewGroup

                val tabChildCount = vgTab.childCount

                for (i in 0 until tabChildCount) {
                    val tabViewChild = vgTab.getChildAt(i)
                    if (tabViewChild is TextView) {
                        tabViewChild.typeface = Typeface.createFromAsset(activity!!.assets, "fonts/bold.ttf")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setupViewPager(viewPager: ViewPager) {

        var showFeed = "1"

        var counter = 2
        val cat = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_LIST, "")!!
        if (!cat.isEmpty()) {
            val catList: ArrayList<ModelCategoryData> = Constants.getCategoryData(cat)!!
            val selectedId = Constants.getPrefs(activity!!)?.getString(Constants.CATEGORY_IDSUB, "")!!
            for (i in 0 until catList.size) {
                if (selectedId == catList[i].id) {
                    showFeed = catList[i].show_on_live
                }
            }
            if (showFeed == "0") {
                counter = 1
                viewPager.adapter = ViewPagerAdapter(fragmentManager!!, counter)
                tabLayout.setupWithViewPager(viewPager)
                tabLayout.getTabAt(0)!!.text = "Business listings"
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4F5D81"))
            } else {

                viewPager.adapter = ViewPagerAdapter(fragmentManager!!, counter)
                tabLayout.setupWithViewPager(viewPager)
                tabLayout.getTabAt(0)!!.text = "Business listings"
                tabLayout.getTabAt(1)!!.text = "Live feed"
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#34b0f2"))


            }
        } else {
            viewPager.adapter = ViewPagerAdapter(fragmentManager!!, counter)
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.getTabAt(0)!!.text = "Business listings"
            tabLayout.getTabAt(1)!!.text = "Live feed"
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#34b0f2"))
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    filter.visibility = View.VISIBLE


                } else {
                    if (Constants.getPrefs(activity!!)!!.getString("showTLiveYes", "no") == "no") {
                        if (Constants.getPrefs(activity!!)!!.getString("showTLive", "no") == "yes") {
                            Constants.getPrefs(activity!!)!!.edit().putString("showTLiveYes", "yes").apply()
                            showCaseLiveFeed()
                        }
                    }

                    filter.visibility = View.GONE
                }

            }

        })

        filter.setOnClickListener {

            val fragment = viewPager.adapter!!.instantiateItem(viewPager, 0) as BusinessListingFragment
            if (fragment is BusinessListingFragment) {
                fragment.showData()
            }


        }
    }

    private fun showCaseLiveFeed() {
        TutoShowcase.from(activity!!)
                .setContentView(R.layout.tutorialthree)
                .showOnce("yes")
                .show()
    }


    override fun onPause() {
        super.onPause()
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @Subscribe
    fun getEventValue(event: TAXI_EVENT) {
        taxiWorking()
        back.visibility = View.VISIBLE
    }

    @Subscribe
    fun getEventValue(event: SetInitialValue) {
        try {
            Constants.getBus().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Subscribe
    fun getEventValue(event: HideBackButton) {

        if (Constants.getPrefs(activity!!)!!.getString("showBackYesOrNo", "category") != "category") {
            back.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun taxiWorking() {
        back.setOnClickListener {

            if (Constants.getPrefs(activity!!)!!.getString("showBackYesOrNo", "category") != "category") {
                back.visibility = View.GONE
            }
            Constants.getBus().post(DoBackActionInDashBoard("value"))
        }
        back.visibility = View.VISIBLE
        Constants.getPrefs(activity!!)!!.edit().putString("backPress", "1").apply()
        Constants.getPrefs(activity!!)!!.edit().putString("Title", mText.text.toString()).apply()
        Constants.getBus().post(BusiniessListingTaxiEvent("taxi"))
        Constants.getBus().post(LiveFeedTaxiEvent("LiveTaxi"))

        mText.text = "Taxi" +
                " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")


    }


    @SuppressLint("SetTextI18n")
    @Subscribe
    fun getTaxiEventValue(event: SeTaxitTitleEvent) {

        mText.text = "Taxi" +
                " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")
        back.visibility = View.VISIBLE
        Constants.getPrefs(activity!!)!!.edit().putString("backPress", "1").apply()
        Constants.getPrefs(activity!!)!!.edit().putString("Title", mText.text.toString()).apply()
    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    fun getTitleEventValue(event: SetTitleEvent) {

        if (event.value.status == "1") {
            mText.text = "All " + event.value.name +
                    " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")
        } else {
            mText.text = Constants.getPrefs(activity!!)!!.getString(Constants.CATEGORY_NAMEO, "") +
                    " in " + Constants.getPrefs(activity!!)!!.getString(Constants.TOWN_NAME, "")

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            popbl = context as PopupShow
        } catch (e: Exception) {

        }
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            popbl = activity as PopupShow
        } catch (e: Exception) {

        }
    }

    interface PopupShow {
        fun showPopUpBL()
    }

    override fun showProgress() {
        progressFlashHome.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressFlashHome.visibility = View.GONE
    }

    override fun noData() {
        flashPostList.visibility = View.GONE
        noFlashData.visibility = View.VISIBLE
    }

    override fun showNetworkError(server_error: Int) {
        try {
            Toast.makeText(activity, getString(server_error), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Subscribe
    fun getEventValue(event: ClaimPost) {

        val auth_code = Constants.getPrefs(activity!!)!!.getString(Constants.AUTH_CODE, "")
        mPresenter.claimPostCall(auth_code, event.model.postId, event.model.name, event.model.phoneNo, event.model.email, event.model.position)
    }

    override fun updateModeldata(position: String, claimed: String) {
       try {
           val rr = flashLish[position.toInt()]
           val countRe = rr.redeemed.toInt() + 1
           rr.redeemed = countRe.toString()
           rr.personRedeem = claimed
           flashLish.set(position.toInt(), rr)
           flashAdapter.notifyDataSetChanged()
       }catch (e:Exception){
           e.printStackTrace()
       }
    }

    override fun showToast(optString: String?) {
        Toast.makeText(activity, optString, Toast.LENGTH_LONG).show()
    }
}
