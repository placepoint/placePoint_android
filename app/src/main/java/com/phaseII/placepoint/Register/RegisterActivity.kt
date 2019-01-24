package com.phaseII.placepoint.Register

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import org.json.JSONObject
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.content.DialogInterface
import android.widget.RadioGroup
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.MultichoiceCategories.ModelCategoryData
import com.phaseII.placepoint.MultichoiceCategories.MultipleCategories
import com.phaseII.placepoint.Payment.PaymentActivity
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Town.ModelTown
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), RegisterHelper {
    lateinit var mPresenter: RegisterPresenter
    private lateinit var selectedLocation: String
    private lateinit var selectedCategory: String
    var cat_list: ArrayList<ModelCategoryData> = arrayListOf()
    var loc_list: ArrayList<ModelTown> = arrayListOf()
    var cat_name: ArrayList<String> = arrayListOf()
    var loc_names: ArrayList<String> = arrayListOf()
    private var cat_id: String = ""
    private var town_id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Constants.getSSlCertificate(this)
        mPresenter = RegisterPresenter(this)
        Constants.getPrefs(this)!!.edit().remove(Constants.USERTYPE).apply()
        choosedPlan.text = "Premium  € 300/year"
        Constants.getPrefs(this)!!.edit().putString(Constants.USERTYPE, "1").apply()
    }

    override fun onResume() {
        super.onResume()

        //------Service Called for Getting Updated Town and Categories-------------------

        Constants.getAppDataService(this@RegisterActivity)
        val cat = Constants.getPrefs(this@RegisterActivity)?.getString(Constants.CATEGORY_LIST, "")!!
        val loc = Constants.getPrefs(this@RegisterActivity)?.getString(Constants.LOCATION_LIST, "")!!

        try {
            cat_list = Constants.getCategoryData(cat)!!
            loc_list = Constants.getTownData(loc)!!
            val hashSet = HashSet<ModelCategoryData>()
            hashSet.addAll(cat_list)
            cat_list.clear()
            cat_list.addAll(hashSet)
            loc_list = Constants.getTownData(loc)!!
            val hashSet2 = HashSet<ModelTown>()
            hashSet2.addAll(loc_list)
            loc_list.clear()
            loc_list.addAll(hashSet2)
            for (i in 0 until cat_list.size) {
                cat_name.add(cat_list[i].name)
            }
            for (i in 0 until loc_list.size) {
                loc_names.add(loc_list[i].townname)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        emailId.error = null
        pass.error = null
        bus_name.error = null
        bus_loc.error = null
        bus_cat.error = null

        register.setOnClickListener {

            /////// //here user set to free user by default on registration//////////////////
            Constants.getPrefs(this)!!.edit().putString(Constants.USERTYPE, "3").apply()
            //////////////////////////////////////////////////////
            val userType = Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "")
            if (userType.isEmpty()) {

                Toast.makeText(this, "Select Subscription Plan", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            mPresenter.getRegisterData(userType, couponId.text.trim().toString())
        }
        back_arrow.setOnClickListener {
            onBackPressed()
        }
        loginBtn.setOnClickListener {
            mPresenter.getLoginScreen()
        }

        bus_loc.setOnClickListener {
            showLocationChooser()
        }
        bus_cat.setOnClickListener {
            //showCategoryChooser()
            val intent = Intent(this, MultipleCategories::class.java)
            intent.putExtra("from", "register")
            startActivityForResult(intent, 9)
        }
        overLay.setOnClickListener {
            Toast.makeText(this, "Select Subscription Plan", Toast.LENGTH_LONG).show()
        }
        linearLayout.setOnClickListener {
            showPopup()
        }

        if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "3") {
            choosedPlan.text = "Free  € 0/year"
        } else if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "2") {
            choosedPlan.text = "Standard  € 150/year"
        } else if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "1") {
            choosedPlan.text = "Premium  € 300/year"
        } else if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "4") {
            choosedPlan.text = "Admin"
        }

    }

    private fun showPopup() {
        val dialogBuilder = AlertDialog.Builder(this)
// ...Irrelevant code for customizing the buttons and title
        dialogBuilder.setTitle("Choose Subscription Plan")
        val inflater = this.layoutInflater

        val dialogView = inflater.inflate(R.layout.user_type, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        var value = 0
        val radioGroup = dialogView.findViewById(R.id.radioGroup) as RadioGroup
        val checkedIdIs = Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "1")
        if (checkedIdIs == "3") {
            radioGroup.check(R.id.free)
        } else if (checkedIdIs == "2") {
            radioGroup.check(R.id.standard)
        } else if (checkedIdIs == "4") {
            radioGroup.check(R.id.admin)
        } else {
            radioGroup.check(R.id.premium)
        }


        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            overLay.visibility = View.GONE
            bus_cat.text = ""
            Constants.getPrefs(this)!!.edit().remove(Constants.MAIN_CATEGORY).apply()
            Constants.getPrefs(this)?.edit()?.remove(Constants.USERTYPE)?.apply()
            if (checkedId == R.id.free) {
                value = 3
            } else if (checkedId == R.id.standard) {
                value = 2
            } else if (checkedId == R.id.admin) {
                value = 4
            } else {
                value = 1
            }

            Constants.getPrefs(this)!!.edit().putString(Constants.USERTYPE, value.toString()).apply()
        })
        dialogBuilder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
            if (value == 0) {
                Constants.getPrefs(this)!!.edit().putString(Constants.USERTYPE, "1").apply()
                choosedPlan.text = "Premium  € 300/year"
                overLay.visibility = View.GONE
            }
            if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "3") {
                choosedPlan.text = "Free  € 0/year"
                couponId.setText("")
                coupon_layout.visibility = View.GONE
            } else if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "2") {
                choosedPlan.text = "Standard  € 150/year"
                couponId.setText("")
                coupon_layout.visibility = View.VISIBLE
            } else if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "1") {
                choosedPlan.text = "Premium  € 300/year"
                couponId.setText("")
                coupon_layout.visibility = View.VISIBLE
            } else if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "") == "4") {
                choosedPlan.text = "Admin"
                couponId.setText("")
                coupon_layout.visibility = View.GONE
            }
            dialog.dismiss()
        })

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 9) {
            if (data != null) {
                bus_cat.text = data.getStringExtra("result")
            }
        }
    }

    override fun getEmail(): String {
        return emailId.text.toString().trim()
    }

    override fun getPassword(): String {
        return pass.text.toString().trim()
    }

    override fun getBusinessName(): String {
        return bus_name.text.toString().trim()
    }

    override fun getBusinessLocation(): String {
        return town_id
    }

    override fun getBusinessCategory(): String {
        return Constants.getPrefs(this)!!.getString(Constants.MAIN_CATEGORY, "")
    }

    override fun setEmailError() {
        emailId.error = getString(R.string.error_field_required)
    }

    override fun setInvalidEmailError() {
        emailId.error = getString(R.string.error_invalid_email)
    }

    override fun setPasswordError() {
        pass.error = getString(R.string.error_invalid_password)
    }

    override fun setBNameError() {
        bus_name.error = getString(R.string.bname_err)
    }

    override fun setBLocError() {
        bus_loc.error = getString(R.string.bloc_error)
    }

    override fun setBCatError() {
        bus_cat.error = getString(R.string.bcat_err)
    }

    override fun showLoginScreen() {
        onBackPressed()
    }


    private fun getAllUniqueValues(list: ArrayList<ModelCategoryData>): ArrayList<ModelCategoryData> {
        val uniqueList = ArrayList<ModelCategoryData>()
        val enemyIds = ArrayList<String>()
        for (entry in list) {
            if (!enemyIds.contains(entry.id)) {
                enemyIds.add(entry.id)
                uniqueList.add(entry)
            }
        }
        return uniqueList
    }

    private fun showCategoryChooser() {

        val mRecyclerView: RecyclerView
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val content = inflater.inflate(R.layout.custom_dialog, null)
        builder.setView(content)
        mRecyclerView = content.findViewById<View>(R.id.my_recycler_view) as RecyclerView
        val array = getAllUniqueValues((cat_list))
        var sortedList = array.sortedWith(compareBy { it.name })
        val adapter = MultipleCatagoryAdapter(this, sortedList, "register")
        val linear = LinearLayoutManager(this)
        mRecyclerView.layoutManager = linear
        mRecyclerView.adapter = adapter
        builder.setNegativeButton("CANCEL", DialogInterface.OnClickListener { d, arg1 ->
            d.dismiss()
        })
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { d, arg1 ->
            cat_id = adapter.getSelected()
            bus_cat.text = adapter.getFirstSelectedName()
            d.dismiss()
        })


        builder.show()

    }


    private fun showLocationChooser() {
        val cat = Constants.getPrefs(this@RegisterActivity)?.getString(Constants.CATEGORY_LIST, "")!!
        val loc = Constants.getPrefs(this@RegisterActivity)?.getString(Constants.LOCATION_LIST, "")!!

        try {
            cat_list = Constants.getCategoryData(cat)!!
            loc_list = Constants.getTownData(loc)!!
            val hashSet = HashSet<ModelCategoryData>()
            hashSet.addAll(cat_list)
            cat_list.clear()
            cat_list.addAll(hashSet)
            loc_list = Constants.getTownData(loc)!!
            val hashSet2 = HashSet<ModelTown>()
            hashSet2.addAll(loc_list)
            loc_list.clear()
            loc_list.addAll(hashSet2)
            for (i in 0 until cat_list.size) {
                cat_name.add(cat_list[i].name)
            }
            for (i in 0 until loc_list.size) {
                loc_names.add(loc_list[i].townname)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val items1 = loc_names.toTypedArray()
        val items = items1.distinct().toTypedArray()
        val value = arrayOfNulls<String>(1)
        val sort = items.sortedArray()
        val builder = AlertDialog.Builder(this@RegisterActivity)
                .setSingleChoiceItems(sort, -1) { dialog, which -> value[0] = sort[which] }
                .setPositiveButton("Ok") { dialog, which ->
                    try {
                        val lw = (dialog as AlertDialog).listView
                        val checkedItem = lw.adapter.getItem(lw.checkedItemPosition)
                        selectedLocation = checkedItem.toString()
                        bus_loc.text = selectedLocation
                        val pos = lw.checkedItemPosition
                        var sortedList = loc_list.sortedWith(compareBy({ it.townname }))

                        town_id = sortedList[pos].id
                        Constants.getPrefs(this)!!.edit().putString(Constants.TOWN_ID2, town_id).apply()
                        dialog.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    // dialog.dismiss()
                }
                .setNegativeButton("Cancel"
                ) { dialog, which -> dialog.dismiss() }.create()
        builder.show()
    }

    override fun getAuthCode(): String {
        return Constants.getPrefs(this@RegisterActivity)!!.getString(Constants.AUTH_CODE, "")
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun showNetworkError(resId: Int) {
        Toast.makeText(this@RegisterActivity, getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun saveRegisterDataToPrefs(data: JSONObject, pass: String, category: String) {
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putString("registers", "yes")?.apply()
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putString(Constants.PASSWORD, pass)?.apply()
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putString(Constants.CATEGORY, data.optString("category"))?.apply()
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putString(Constants.MY_BUSINESS_NAME, data.optString("business_name"))?.apply()
        // Constants.getPrefs(this@RegisterActivity)?.edit()?.putString(Constants.TOWN_ID, data.optString("location"))?.apply()
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putString(Constants.TOWN_ID2, data.optString("location"))?.apply()
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putString(Constants.AUTH_CODE, data.optString("auth_code"))?.apply()
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putString(Constants.BUSINESS_ID_MAIN, data.optString("business_id"))?.apply()
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putBoolean(Constants.LOGIN, true)?.apply()
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putBoolean(Constants.LOGGED, true)?.apply()
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putString(Constants.LOGGED_IN, "true")?.apply()
        Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
        Constants.getPrefs(this@RegisterActivity)?.edit()!!.putString(Constants.MAIN_CATEGORY, category).apply()
        mPresenter.openBusinessProfileFragment()
    }

    override fun openBusinessProfile() {
        Constants.getPrefs(this)?.edit()?.putString("firstTime", "login")?.apply()
        Constants.getPrefs(this@RegisterActivity)?.edit()?.putBoolean(Constants.LOGIN, true)?.apply()
        val intent = Intent(this@RegisterActivity, DashBoardActivity::class.java)
        intent.putExtra("goto", "businessProfile")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun saveEmail(email: String) {
        Constants.getPrefs(this)?.edit()?.putString(Constants.EMAIL, email)?.apply()

    }

    override fun getUserType(): String {

        return Constants.getPrefs(this@RegisterActivity)!!.getString(Constants.USERTYPE, "")
    }

    override fun openPayment(email: String, pass: String, bName: String, bLoc: String, bCat: String, auth_code: String, type: String, coupon: String) {
        if (type == "3" || type == "4") {
            val userType = Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "")
            mPresenter.getRegisterService(userType, couponId.text.trim().toString())
        } else {
            if (coupon.isEmpty()) {
                if (pass.length > 4) {
                    var amount = "300"
                    if (Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "1") == "1") {
                        amount = "300"
                    } else {
                        amount = "150"
                    }
                    var intent = Intent(this, PaymentActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("pass", pass)
                    intent.putExtra("bName", bName)
                    intent.putExtra("bLoc", bLoc)
                    intent.putExtra("bCat", bCat)
                    intent.putExtra("auth_code", auth_code)
                    intent.putExtra("type", type)
                    intent.putExtra("catId", cat_id)
                    intent.putExtra("from", "Register")
                    intent.putExtra("amount_payable", amount)
                    intent.putExtra("coupon", couponId.text.toString().trim())
                    startActivity(intent)
                } else {

                    Toast.makeText(this, R.string.error_invalid_password, Toast.LENGTH_LONG).show()
                }
            } else {
                val userType = Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "")
                mPresenter.CheckCouponValidity(couponId.text.trim().toString(), userType, "1")

            }
        }


    }

    override fun hitRegisterApi(email: String, pass: String, bName: String, bCat: String, bLoc: String, type: String, from: String, amount: String, auth_code: String) {

        if (pass.length > 4) {

            var intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("pass", pass)
            intent.putExtra("bName", bName)
            intent.putExtra("bLoc", bLoc)
            intent.putExtra("bCat", bCat)
            intent.putExtra("auth_code", auth_code)
            intent.putExtra("type", type)
            intent.putExtra("catId", cat_id)
            intent.putExtra("from", "Register")
            intent.putExtra("amount_payable", amount)
            intent.putExtra("coupon", couponId.text.toString().trim())
            startActivity(intent)
        } else {

            Toast.makeText(this, R.string.error_invalid_password, Toast.LENGTH_LONG).show()
        }

    }

    override fun showInvalidMessage(email: String, pass: String, bName: String, bCat: String, bLoc: String, type: String, from: String, amount: String, auth_code: String) {
        showInvalidCoupon(email, pass, bName, bCat, bLoc, type, from, amount, auth_code)
    }

    private fun showInvalidCoupon(email: String, pass: String, bName: String, bCat: String, bLoc: String, type: String, from: String, amount: String, auth_code: String) {

        AlertDialog.Builder(this)
                .setTitle("Alert!")
                .setMessage("The Coupon you have entered is Invalid. Do you want to proceed without applying coupon?")
                .setPositiveButton("Yes") { dialog, which ->
                    dialog.dismiss()
                    val userType = Constants.getPrefs(this)!!.getString(Constants.USERTYPE, "")

                    if (pass.length > 4) {

                        var intent = Intent(this, PaymentActivity::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("pass", pass)
                        intent.putExtra("bName", bName)
                        intent.putExtra("bLoc", bLoc)
                        intent.putExtra("bCat", bCat)
                        intent.putExtra("auth_code", auth_code)
                        intent.putExtra("type", type)
                        intent.putExtra("catId", cat_id)
                        intent.putExtra("from", "Register")
                        intent.putExtra("amount_payable", amount)
                        intent.putExtra("coupon", couponId.text.toString().trim())
                        startActivity(intent)
                    } else {

                        Toast.makeText(this, R.string.error_invalid_password, Toast.LENGTH_LONG).show()
                    }

                }.setNegativeButton("No", null).show()
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_SHORT).show()
    }


}
