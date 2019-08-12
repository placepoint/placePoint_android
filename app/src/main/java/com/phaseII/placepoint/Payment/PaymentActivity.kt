package com.phaseII.placepoint.Payment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.R
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import kotlinx.android.synthetic.main.activity_payment.*

import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentContract {

    private lateinit var mPresenter: PaymentPresenter
    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView
    lateinit var amountToPayed: TextView
    lateinit var emailIntent: String
    lateinit var passIntent: String
    lateinit var getBNameIntent: String
    lateinit var getBLocIntent: String
    lateinit var getBCatIntent: String
    lateinit var gettypeIntent: String
    lateinit var catIdIntent: String
    lateinit var auth_code: String
    lateinit var from: String
    lateinit var coupon: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        Constants.getSSlCertificate(this)
        amountToPayed = findViewById(R.id.amountToPayed)
        mPresenter = PaymentPresenter(this)
        setToolBar()

        from = intent.getStringExtra("from")
        if (from == "payment") {
            gettypeIntent = intent.getStringExtra("type")
            amountToPayed.visibility = View.VISIBLE
            amountToPayed.text = "Amount to be paid €" + intent.getStringExtra("amount_payable")
        } else {
            amountToPayed.visibility = View.VISIBLE

            amountToPayed.text = "Amount to be paid €" + intent.getStringExtra("amount_payable")
            emailIntent = intent.getStringExtra("email")
            passIntent = intent.getStringExtra("pass")
            getBNameIntent = intent.getStringExtra("bName")
            getBLocIntent = intent.getStringExtra("bLoc")
            getBCatIntent = intent.getStringExtra("bCat")
            gettypeIntent = intent.getStringExtra("type")
            catIdIntent = intent.getStringExtra("catId")
            coupon = intent.getStringExtra("coupon")

        }
        auth_code = Constants.getPrefs(this@PaymentActivity)!!.getString(Constants.AUTH_CODE, "")

        if (gettypeIntent == "1") {
            layStandard.visibility = View.GONE
            premiumText.visibility = View.VISIBLE
      }

        else if (gettypeIntent == "2") {
            layStandard.visibility = View.VISIBLE
            premiumText.visibility = View.GONE
        }
        expiry.setOnClickListener {
            val pd = MonthYearPickerDialog()
            pd.setListener(DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                expiry.setTextColor(Color.parseColor("#000000"))
                expiry.text = "${month.toString()}/${year.toString()}"
            })
            pd.show(fragmentManager, "MonthYearPickerDialog")
        }
        buyNow.setOnClickListener {
            Constants.hideKeyBoard(this, buyNow)

            val cardNumber = card_no.text.toString()
            val name = name.text.toString()
            val cvv = cvv.text.toString()
            val expiry = expiry.text.toString()
            if (cardNumber.isEmpty()) {
                Toast.makeText(this, "Enter Card Number", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (name.isEmpty()) {
                Toast.makeText(this, "Enter Name", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (expiry.isEmpty()) {
                Toast.makeText(this, "Select Expiry date", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (cvv.isEmpty()) {
                Toast.makeText(this, "Enter CVV", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            sendData(cardNumber, name, expiry, cvv)

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar)
        mTitle = toolbar.findViewById(R.id.toolbar_title)
        mTitle.text = "Payment"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendData(cardNumber: String, name: String, expiry: String, cvv: String) {
        val result: List<String> = expiry.split("/").map { it.trim() }
        progressBar.visibility = View.VISIBLE
        val stripe = Stripe(this, Constants.STRIPE_KEY)
        val card = Card(
                card_no.text.toString(),
                result[0].toInt(),
                result[1].toInt(),
                cvv
        )

        stripe.createToken(
                card,
                object : TokenCallback {
                    override fun onSuccess(token: Token) {
                        // Send token to your own web service
                        progressBar.visibility = View.GONE
                        if (from == "payment") {
                            mPresenter.paymentService(auth_code, intent.getStringExtra("currenttype"), intent.getStringExtra("upgrade_type"), token.id.toString(), intent.getStringExtra("amount_payable"))
                        } else {
                            mPresenter.registerWebService(emailIntent, passIntent, getBNameIntent,
                                    getBLocIntent, getBCatIntent, auth_code, gettypeIntent, token.id.toString(),coupon)
                        }
                    }

                    override fun onError(error: Exception) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@PaymentActivity,
                                error.localizedMessage,
                                Toast.LENGTH_LONG).show()
                    }
                }
        )
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE

    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun saveRegisterDataToPrefs(data: JSONObject, pass: String, category: String) {
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putString("registers", "yes")?.apply()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putString(Constants.PASSWORD, pass)?.apply()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putString(Constants.CATEGORY, data.optString("category"))?.apply()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putString(Constants.MY_BUSINESS_NAME, data.optString("business_name"))?.apply()
        // Constants.getPrefs(this@RegisterActivity)?.edit()?.putString(Constants.TOWN_ID, data.optString("location"))?.apply()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putString(Constants.TOWN_ID2, data.optString("location"))?.apply()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putString(Constants.AUTH_CODE, data.optString("auth_code"))?.apply()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putString(Constants.BUSINESS_ID_MAIN, data.optString("business_id"))?.apply()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putBoolean(Constants.LOGIN, true)?.apply()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putBoolean(Constants.LOGGED, true)?.apply()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putString(Constants.LOGGED_IN, "true")?.apply()
        Toast.makeText(this@PaymentActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putString(Constants.MAIN_CATEGORY, category)?.apply()


        Constants.getPrefs(this)?.edit()?.putString("firstTime", "login")?.apply()
        Constants.getPrefs(this@PaymentActivity)?.edit()?.putBoolean(Constants.LOGIN, true)?.apply()
        val intent = Intent(this@PaymentActivity, DashBoardActivity::class.java)
        intent.putExtra("goto", "businessProfile")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun saveEmail(email: String?) {

        Constants.getPrefs(this)?.edit()?.putString(Constants.EMAIL, email)?.apply()
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(this@PaymentActivity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showNetworkError(resId: Int) {
        Toast.makeText(this@PaymentActivity, getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun showPaymentDone(upgrade_type: String) {
        Toast.makeText(this@PaymentActivity, "Payment Successfull", Toast.LENGTH_SHORT).show()
        Constants.getPrefs(this)!!.edit().putString(Constants.USERTYPE, upgrade_type).apply()
        val intent = Intent(this, DashBoardActivity::class.java)
        intent.putExtra("goto", "businessProfile")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }
}
