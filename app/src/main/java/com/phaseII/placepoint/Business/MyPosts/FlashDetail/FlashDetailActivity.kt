package com.phaseII.placepoint.Business.MyPosts.FlashDetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import android.support.v7.app.AlertDialog
import android.widget.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


class FlashDetailActivity : AppCompatActivity(), FlashDetailContract.View {

    lateinit var msearchView: SearchView
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewReedeem: RecyclerView
    lateinit var Claimed: LinearLayout
    lateinit var ReedeemedLay: LinearLayout
    lateinit var redeemButton: TextView
    lateinit var noDataFlash: TextView
    lateinit var progressBarFlash: ProgressBar
    lateinit var toolbar: Toolbar
    private lateinit var mTitle: TextView
    private lateinit var sendMail: ImageView
    private lateinit var mPresenter: FlashDetailPresenter
    private var claimedList = ArrayList<ModelFDetail>()
    private var claimedListRedeemed = ArrayList<ModelFDetail>()
    private var claimedListUnRedeemed = ArrayList<ModelFDetail>()
   // private lateinit var adapter: FlashDetailAdapter
    private lateinit var adapterC: FlashDetailAdapterClaim

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_detail)
        mPresenter = FlashDetailPresenter(this)
        setToolBar()
        initilize()
        clicks()

        mPresenter.getDetails(Constants.getPrefs(this)!!.getString(Constants.AUTH_CODE, ""), intent.getStringExtra("busId"))
    }

    private fun setToolBar() {

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title) as TextView
        sendMail = toolbar.findViewById(R.id.sendMail) as ImageView
        sendMail.visibility = View.VISIBLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mTitle.text = "Redeem"
        sendMail.setOnClickListener {
            showAlert()
        }
    }

    private fun showAlert() {

        AlertDialog.Builder(this@FlashDetailActivity)
                .setTitle("Alert!")
                .setMessage("You can send a list of redeemed and unredeemed offers to your registered email to print out. Click send to send the email now.")
                .setCancelable(false)
                .setPositiveButton("Send")
                { dialog, which ->
                    mPresenter.sendEmail(Constants.getPrefs(this@FlashDetailActivity)!!.getString(Constants.AUTH_CODE, ""), intent.getStringExtra("busId"))
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel")
                { dialog, which ->
                    dialog.dismiss()
                }.show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clicks() {
        redeemButton.setOnClickListener {
            if (claimedListUnRedeemed.size > 0) {
                if (adapterC != null) {
                    val ids: String = adapterC.getSectedIds()
                    if (ids.isEmpty()) {
                        Toast.makeText(this, "Please select to redeem", Toast.LENGTH_LONG).show()
                    } else {
                        mPresenter.deleteEmails(Constants.getPrefs(this)!!.getString(Constants.AUTH_CODE, ""), ids, intent.getStringExtra("busId"))
                    }
                }

            } else {
                Toast.makeText(this, "No Data to Redeem", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initilize() {

        noDataFlash = findViewById(R.id.noDataFlash)
        progressBarFlash = findViewById(R.id.progressBarFlash)
        msearchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)
        redeemButton = findViewById(R.id.redeemButtonF)
        recyclerViewReedeem = findViewById(R.id.recyclerViewReedeem)
        Claimed = findViewById(R.id.Claimed)
        ReedeemedLay = findViewById(R.id.ReedeemedLay)
        //recyclerView.isNestedScrollingEnabled = false;
        //recyclerViewReedeem.isNestedScrollingEnabled = false;
        setSearchView()
    }

    private fun setSearchView() {
        msearchView.setOnClickListener(View.OnClickListener { msearchView.isIconified = false })
        msearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //list.clear();
                //  adapter = null;
                //  recyclerView.getRecycledViewPool().clear();
                //  recyclerView.removeAllViews();
                /* if (!query.trim().equals("")) {
                    query = query.toLowerCase();
                    filteredList = new ArrayList<>();
                    for (int i = 0; i < mData.size(); i++) {
                        final String text = mData.get(i).getUsername().toLowerCase();
                        if (text.contains(query)) {
                            filteredList.add(mData.get(i));
                        }
                    }
                }
                if (filteredList.size() > 0) {
                    adapter = new InviteAdapter(InviteUserActivity.this, filteredList);
                    recyclerView.setAdapter(adapter);
                }*/
                // closeButton.setVisibility(View.VISIBLE)
                // mPresenter.sendSearchedData(query, 0, 1);
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //closeButton.setVisibility(View.VISIBLE);
                var newList = ArrayList<ModelFDetail>()
                var newList2 = ArrayList<ModelFDetail>()
                var newList3 = ArrayList<ModelFDetail>()
                var newList4 = ArrayList<ModelFDetail>()
               // if (newText.trim { it <= ' ' } == "") {
                   // newList2=getSelectedItems(claimedListUnRedeemed)
//                    if (claimedListRedeemed.size > 0) {
//                        //noDataFlash.visibility = View.GONE
//                        ReedeemedLay.visibility = View.VISIBLE
//                        adapter = FlashDetailAdapter(applicationContext, claimedListRedeemed)
//                        val linear = LinearLayoutManager(applicationContext)
//                        recyclerViewReedeem.adapter = adapter
//                        recyclerViewReedeem.layoutManager = linear
//                    } else {
//                       // noDataFlash.visibility = View.VISIBLE
//                        ReedeemedLay.visibility = View.GONE
//                    }
//                 if (newList2.size > 0) {
//                       noDataFlash.visibility = View.GONE
//                        Claimed.visibility = View.VISIBLE
//                        adapterC = FlashDetailAdapterClaim(applicationContext, claimedListUnRedeemed)
//                        val linear = LinearLayoutManager(applicationContext)
//                     recyclerView.adapter = adapterC
//                     recyclerView.layoutManager = linear
//                    } else {
//                     noDataFlash.visibility = View.VISIBLE
//                     Claimed.visibility = View.GONE
//                    }
//                } else {
//                    if (adapter!=null) {
//                        newList = adapter.getSelectedItems(newText)
//                        if (newList.size > 0) {
//                            ReedeemedLay.visibility = View.VISIBLE
//
//                            adapter = FlashDetailAdapter(applicationContext, newList)
//                            val linear = LinearLayoutManager(applicationContext)
//                            recyclerViewReedeem.adapter = adapter
//                            recyclerViewReedeem.layoutManager = linear
//
//                        } else {
//
//                            ReedeemedLay.visibility = View.GONE
//
//                        }
//                    }
                    if (adapterC!=null) {
                      newList2 =getSelectedItems(claimedListUnRedeemed,newText)
                        for (m in 0 until newList2.size){
                            if (newList2[m].status!="header"){
                                newList4.add(newList2[m])
                            }
                        }
                        for(i in 0 until newList4.size){
                            if (newList4[i].status=="0"){
                                newList.add(newList4[i])

                            }else{
                                newList3.add(newList4[i])
                            }
                        }
                        if (newList.size>0) {
                            val m = ModelFDetail()
                            m.name = "Claimed"
                            m.status = "header"
                            m.post_id= ""
                            m.email= ""
                            m.phone_no= ""
                            m.created_at= ""
                            m.updated_at= ""
                            newList.add(0, m)
                        }
                        if (newList3.size>0){
                            val m1=ModelFDetail()
                            m1.name="Redeemed"
                            m1.status="header"
                            m1.post_id= ""
                            m1.email= ""
                            m1.phone_no= ""
                            m1.created_at= ""
                            m1.updated_at= ""
                            newList3.add(0,m1)
                        }
                        newList.addAll(newList3)
                        if (newList.size > 0) {
                            Claimed.visibility = View.VISIBLE

                            adapterC = FlashDetailAdapterClaim(applicationContext, newList)
                            val linear = LinearLayoutManager(applicationContext)
                            recyclerView.adapter = adapterC
                            recyclerView.layoutManager = linear
                        } else {

                            Claimed.visibility = View.GONE

                        }
                 //   }

                    if (newList.size==0){
                        noDataFlash.visibility=View.VISIBLE
                    }else{
                        noDataFlash.visibility=View.GONE
                    }
                }
                return false
            }
        })
    }

    private fun getSelectedItems(claimedListUnRedeemed: ArrayList<ModelFDetail>, newText: String): ArrayList<ModelFDetail> {

        var newList= java.util.ArrayList<ModelFDetail>()
        for (i in 0 until claimedListUnRedeemed.size){

            if (claimedListUnRedeemed[i].email.contains(newText)) {
                newList.add(claimedListUnRedeemed[i])
            }

        }
        return newList
    }

    fun onClickS(v: View) {
        val im = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        im.showSoftInput(v, 0)
    }

    override fun showProgress() {
        progressBarFlash.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBarFlash.visibility = View.GONE
    }

    override fun showNetworkError(server_error: Int) {
        Toast.makeText(this, getString(server_error), Toast.LENGTH_LONG).show()
    }

    override fun noData() {
        noDataFlash.visibility = View.VISIBLE
        //recyclerView.visibility=View.GONE
    }

    override fun showToast(optString: String) {

        Toast.makeText(this, optString, Toast.LENGTH_LONG).show()
    }

    override fun setAdapter(list: ArrayList<ModelFDetail>) {
        claimedList = list
        claimedListUnRedeemed.clear()
        claimedListRedeemed.clear()
        for(i in 0 until list.size){
            if (list[i].status=="0"){
                claimedListUnRedeemed.add(list[i])

            }else{
                claimedListRedeemed.add(list[i])
            }
        }
        if (claimedListUnRedeemed.size>0) {
            val m = ModelFDetail()
            m.name = "Claimed"
            m.status = "header"
            m.post_id= ""
            m.email= ""
            m.phone_no= ""
            m.created_at= ""
            m.updated_at= ""
            claimedListUnRedeemed.add(0, m)
        }
        if (claimedListRedeemed.size>0){
            val m1=ModelFDetail()
            m1.name="Redeemed"
            m1.status="header"
            m1.post_id= ""
            m1.email= ""
            m1.phone_no= ""
            m1.created_at= ""
            m1.updated_at= ""
            claimedListRedeemed.add(0,m1)
        }
        claimedListUnRedeemed.addAll(claimedListRedeemed)
        adapterC = FlashDetailAdapterClaim(this, claimedListUnRedeemed)
        Claimed.visibility=View.VISIBLE
        recyclerView.visibility=View.VISIBLE
        noDataFlash.visibility = View.GONE

        val linear = LinearLayoutManager(this)
        recyclerView.adapter = adapterC
        recyclerView.layoutManager = linear
       // adapter = FlashDetailAdapter(this, claimedListRedeemed)
        if (claimedListRedeemed.size > 0||claimedListUnRedeemed.size>0) {
            //if (claimedListUnRedeemed.size>0){
                Claimed.visibility=View.VISIBLE
                recyclerView.visibility=View.VISIBLE
                noDataFlash.visibility = View.GONE

                val linear = LinearLayoutManager(this)
                recyclerView.adapter = adapterC
                recyclerView.layoutManager = linear
//            }else{
//                Claimed.visibility=View.GONE
//            }
//            if (claimedListRedeemed.size>0){
//                ReedeemedLay.visibility=View.VISIBLE
//                recyclerViewReedeem.visibility=View.VISIBLE
//                noDataFlash.visibility = View.GONE
//
//                val linear = LinearLayoutManager(this)
//                recyclerViewReedeem.adapter = adapter
//                recyclerViewReedeem.layoutManager = linear
//            }else{
//                ReedeemedLay.visibility=View.GONE
//
//            }

        } else {
            Claimed.visibility=View.GONE
            ReedeemedLay.visibility=View.GONE
            noDataFlash.visibility = View.VISIBLE
        }

    }
}
