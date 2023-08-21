package com.example.htseafood.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Toast
import com.example.htseafood.R
import com.example.htseafood.apis.ApiClient
import com.example.htseafood.model.request.LoginRequest
import com.example.htseafood.utils.Constants
import com.example.htseafood.utils.ProgressDialog
import com.example.htseafood.utils.SharedHelper
import com.example.htseafood.utils.Utils
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_login.etUsername
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_login.tvSignIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etPassword.setOnTouchListener { v, event ->
            if ((event.action == MotionEvent.ACTION_UP) && event.rawX >= (etPassword.right - etPassword.compoundDrawables[2].bounds.width())) {
                if (etPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    etPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0)
                } else {
                    etPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_eye, 0)
                }
                etPassword.setSelection(etPassword.text.length)
                true
            } else false
        }

        tvSignIn.setOnClickListener {
            if (etUsername.text.trim().toString().isEmpty()) {
                Toast.makeText(
                    this, getString(R.string.please_enter_username), Toast.LENGTH_SHORT
                ).show()
            } else if (etPassword.text.trim().toString().isEmpty()) {
                Toast.makeText(
                    this, getString(R.string.please_enter_password), Toast.LENGTH_SHORT
                ).show()
            } else {
                if (Utils.isOnline(this)) {
                    login(etUsername.text.toString(), etPassword.text.toString())
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.please_check_your_internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
//            startActivity(Intent(this, HomeActivity::class.java))
//            finish()
        }
    }

    fun login(username: String, password: String) {
        ProgressDialog.start(this@LoginActivity)
        ApiClient.getRestClient(
            Constants.BASE_URL, ""
        )!!.webservices.login(
            LoginRequest(
                username, password
            )
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                ProgressDialog.dismiss()
                if (response.isSuccessful) {
                    try {
                        if (!response.body()!!.get("status").asBoolean) {
                            Toast.makeText(
                                this@LoginActivity,
                                /*response.body()!!.get("msg").toString().replace('"', ' ').trim()*/"Login Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                response.body()!!.get("msg").toString().replace('"', ' ').trim(),
                                Toast.LENGTH_SHORT
                            ).show()
                            SharedHelper.putKey(
                                this@LoginActivity,
                                Constants.CustmerNo,
                                response.body()!!.getAsJsonObject("data").get("customerNo")
                                    .toString().replace('"', ' ').trim()
                            )
                            SharedHelper.putKey(
                                this@LoginActivity, Constants.IS_LOGIN, true
                            )

                            startActivity(
                                Intent(
                                    this@LoginActivity, HomeActivity::class.java
                                )
                            )
                            finishAffinity()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity, getString(R.string.api_fail_message), Toast.LENGTH_SHORT
                ).show()
                ProgressDialog.dismiss()
            }
        })
    }

}