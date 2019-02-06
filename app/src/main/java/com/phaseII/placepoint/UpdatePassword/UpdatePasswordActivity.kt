package com.phaseII.placepoint.UpdatePassword

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.phaseII.placepoint.Constants
import com.phaseII.placepoint.R
import kotlinx.android.synthetic.main.activity_update_password.*

class UpdatePasswordActivity : AppCompatActivity(), UpdatePassView {
    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    override fun showMessage(optString: String?, i: Int) {
        if (i==1){
            Toast.makeText(this@UpdatePasswordActivity,optString,Toast.LENGTH_LONG).show()
            finish()
        }else{
            Toast.makeText(this@UpdatePasswordActivity,optString,Toast.LENGTH_LONG).show()

        }
    }

    private lateinit var mPresenter: UpdatePassPresenter
    private lateinit var submitButton: Button
    private lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        mPresenter = UpdatePassPresenter(this)
        back = findViewById(R.id.back)
        submitButton = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            if ( oldPassword.text.toString().trim().isEmpty()){
                Toast.makeText(this@UpdatePasswordActivity,"Old Password cannot be empty.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
           if ( newPassword.text.toString().trim().isEmpty()){
                Toast.makeText(this@UpdatePasswordActivity,"New Password cannot be empty.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
          if (newPassword.text.toString().trim() != confirmPassword.text.toString().trim()){
                Toast.makeText(this@UpdatePasswordActivity,"New Password and Confirm Password does not match.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mPresenter.updatePassword(Constants.getPrefs(this@UpdatePasswordActivity)!!.getString(Constants.AUTH_CODE, ""), oldPassword.text.toString().trim(), newPassword.text.toString().trim())
        }
        back.setOnClickListener {
            finish()
        }
    }

}
