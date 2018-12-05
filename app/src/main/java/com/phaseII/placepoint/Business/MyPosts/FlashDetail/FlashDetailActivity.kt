package com.phaseII.placepoint.Business.MyPosts.FlashDetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import android.widget.ImageView
import com.phaseII.placepoint.R.id.searchView
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.phaseII.placepoint.AboutBusiness.BusinessDetails.DetailFragment.setTitle


class FlashDetailActivity : AppCompatActivity(), FlashDetailContract.View {

    lateinit var msearchView: SearchView
    lateinit var recyclerView: RecyclerView
    lateinit var redeemButton: TextView
    lateinit var noDataFlash: TextView
    lateinit var progressBarFlash: ProgressBar
    lateinit var toolbar: Toolbar
    private lateinit var mTitle: TextView
    private lateinit var sendMail: ImageView
    private lateinit var mPresenter: FlashDetailPresenter
    private var claimedList = ArrayList<ModelFDetail>()
    private lateinit var adapter: FlashDetailAdapter

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
            if (claimedList.size > 0) {
                if (adapter != null) {
                    val ids: String = adapter.getSectedIds()
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
                if (newText.trim { it <= ' ' } == "") {

                    if (claimedList.size > 0) {
                        noDataFlash.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        adapter = FlashDetailAdapter(applicationContext, claimedList)
                        val linear = LinearLayoutManager(applicationContext)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = linear
                    } else {
                        noDataFlash.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                } else {
                    newList = adapter.getSelectedItems(newText)
                    if (newList.size > 0) {
                        noDataFlash.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        adapter = FlashDetailAdapter(applicationContext, newList)
                        val linear = LinearLayoutManager(applicationContext)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = linear
                    } else {

                        noDataFlash.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
                return false
            }
        })
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
        recyclerView.visibility=View.GONE
    }

    override fun showToast(optString: String) {

        Toast.makeText(this, optString, Toast.LENGTH_LONG).show()
    }

    override fun setAdapter(list: ArrayList<ModelFDetail>) {
        claimedList = list
        if (list.size > 0) {
            recyclerView.visibility=View.VISIBLE
            noDataFlash.visibility = View.GONE
            adapter = FlashDetailAdapter(this, list)
            val linear = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = linear
        } else {
            recyclerView.visibility=View.GONE
            noDataFlash.visibility = View.VISIBLE
        }

    }
}
