package com.htseafood.customer.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Toast
import com.google.gson.JsonObject
import com.htseafood.customer.R
import com.htseafood.customer.apis.ApiClient
import com.htseafood.customer.custom.BaseActivity
import com.htseafood.customer.databinding.ActivityLoginBinding
import com.htseafood.customer.model.request.LoginRequest
import com.htseafood.customer.utils.Constants
import com.htseafood.customer.utils.ProgressDialog
import com.htseafood.customer.utils.SharedHelper
import com.htseafood.customer.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun inflateBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.etPassword.setOnTouchListener { v, event ->
            if ((event.action == MotionEvent.ACTION_UP) && event.rawX >= (binding.etPassword.right - binding.etPassword.compoundDrawables[2].bounds.width())) {
                if (binding.etPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    binding.etPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.eye, 0
                    )
                } else {
                    binding.etPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.hide_eye, 0
                    )
                }
                binding.etPassword.setSelection(binding.etPassword.text.length)
                true
            } else false
        }

        binding.tvSignIn.setOnClickListener {
            if (binding.etUsername.text.trim().toString().isEmpty()) {
                Toast.makeText(
                    this, getString(R.string.please_enter_username), Toast.LENGTH_SHORT
                ).show()
            } else if (binding.etPassword.text.trim().toString().isEmpty()) {
                Toast.makeText(
                    this, getString(R.string.please_enter_password), Toast.LENGTH_SHORT
                ).show()
            } else {
                if (Utils.isOnline(this)) {
                    login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
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
            Constants.BASE_URL
        )!!.webservices.login(
            LoginRequest(
                password, username
            )
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                ProgressDialog.dismiss()
                if (response.isSuccessful) {
                    try {
                        if (!response.body()!!.get("status").asBoolean) {
                            Toast.makeText(
                                this@LoginActivity,/*response.body()!!.get("msg").toString().replace('"', ' ').trim()*/
                                "Login Failed", Toast.LENGTH_SHORT
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
                                this@LoginActivity,
                                Constants.CustmerName,
                                response.body()!!.getAsJsonObject("data").get("customerName")
                                    .toString().replace('"', ' ').trim()
                            )
                            SharedHelper.putKey(
                                this@LoginActivity,
                                Constants.CustmerEmail,
                                response.body()!!.getAsJsonObject("data").get("customerEmail")
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