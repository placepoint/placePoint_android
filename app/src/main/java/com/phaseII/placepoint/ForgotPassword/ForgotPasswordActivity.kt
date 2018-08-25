package com.phaseII.placepoint.ForgotPassword

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity(), ForgotPasswordView {

    private lateinit var mPresenter: ForgotPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        Constants.getSSlCertificate(this)
        mPresenter=ForgotPasswordPresenter(this)
        submitButton.setOnClickListener{
            val authcode= Constants.getPrefs(this)!!.getString(Constants.AUTH_CODE,"")
            mPresenter.sendPassword(emailId.text.toString(),authcode)
        }
        back.setOnClickListener{
            finish()
        }
    }

    override fun showLoader() {
        progressBar.visibility= View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility= View.GONE
    }

    override fun savePassWord(password: String) {
        Constants.getPrefs(this@ForgotPasswordActivity)?.edit()?.putString(Constants.PASSWORD, password)?.apply()
        Toast.makeText(this,"Password sent to your Email ID",Toast.LENGTH_LONG).show()
        finish()
    }

    override fun showError(optString: String?) {
        Toast.makeText(this,optString,Toast.LENGTH_LONG).show()
    }
}
