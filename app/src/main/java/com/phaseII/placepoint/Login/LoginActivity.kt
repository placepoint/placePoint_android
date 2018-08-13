package com.phaseII.placepoint.Login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.DashBoard.DashBoardActivity
import com.phaseII.placepoint.ForgotPassword.ForgotPasswordActivity
import com.phaseII.placepoint.R
import com.phaseII.placepoint.Register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginHelper {
    lateinit var mPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mPresenter = LoginPresenter(this)
        email.error = null
        password.error = null

        loginButton.setOnClickListener {
            mPresenter.attemptLogin()
        }

        Register.setOnClickListener {
            mPresenter.openRegisterScreen()
        }

        forgotPassword.setOnClickListener {
            mPresenter.openForgorPasswordScreen()
        }

        back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun clearPrefs() {

        Constants.getPrefs(this)!!.edit().remove(Constants.MAIN_CATEGORY).apply()
        Constants.getPrefs(this@LoginActivity)?.edit()?.remove(Constants.USERTYPE)?.apply()

        // Constants.getPrefs(this)!!.edit().remove(Constants.CATEGORY_IDS).apply()
    }

    override fun callResgister() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun callForgotPassword() {
        val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    override fun setEmailError() {
        email.error = getString(R.string.error_field_required)
    }

    override fun setInvalidEmailError() {
        email.error = getString(R.string.error_invalid_email)
    }

    override fun getEmail(): String {
        return email.text.toString().trim()
    }

    override fun getPassword(): String {
        return password.text.toString().trim()
    }

    override fun setPasswordEmptyError() {
        password.error = getString(R.string.error_field_required)
    }

    override fun getAuthCode(): String? {
        return Constants.getPrefs(this@LoginActivity)?.getString(Constants.AUTH_CODE, "")
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun showNetworkError(resId: Int) {
        Toast.makeText(this@LoginActivity, getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccessMessage(msg: String) {
//        ConstantVal.getPrefs(this@LoginActivity)?.edit()?.putBoolean(ConstantVal.LOGIN, true)?.apply()
        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
//        val resultIntent = Intent()
//      //  resultIntent.putExtra("some_key", "String data")
//        setResult(Activity.RESULT_OK, resultIntent)
//
        Constants.getPrefs(this)?.edit()?.putBoolean(Constants.LOGIN, true)?.apply()
        Constants.getPrefs(this)?.edit()?.putString("firstTime", "login")?.apply()

        val intent = Intent(this, DashBoardActivity::class.java)
        intent.putExtra("goto", "businessProfile")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
       // finish()
    }

    override fun saveBusId(business_id: String, business_name: String, cat_id: String, password: String) {
        Constants.getPrefs(this@LoginActivity)?.edit()?.putString(Constants.BUSINESS_ID_MAIN, business_id)?.apply()
        Constants.getPrefs(this@LoginActivity)?.edit()?.putString(Constants.MY_BUSINESS_NAME, business_name)?.apply()
        Constants.getPrefs(this@LoginActivity)?.edit()?.putString(Constants.MAIN_CATEGORY, cat_id)?.apply()
        Constants.getPrefs(this@LoginActivity)?.edit()?.putString(Constants.LOGGED_IN, "true")?.apply()
        Constants.getPrefs(this@LoginActivity)?.edit()?.putString(Constants.PASSWORD, password)?.apply()
        Constants.getPrefs(this@LoginActivity)?.edit()?.putBoolean(Constants.LOGGED, true)?.apply()

    }

    override fun saveEmail(email: String, townId: String, user_type: String) {
        Constants.getPrefs(this@LoginActivity)?.edit()?.putString(Constants.EMAIL, email)?.apply()
        Constants.getPrefs(this@LoginActivity)?.edit()?.putString(Constants.TOWN_ID2, townId)?.apply()
        Constants.getPrefs(this@LoginActivity)?.edit()?.putString(Constants.USERTYPE, user_type)?.apply()
    }

    override fun showErrorMessage(msg: String?) {
        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        clearPrefs()
    }
}
