package com.htseafood.customer.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.htseafood.customer.R
import com.htseafood.customer.utils.Constants
import com.htseafood.customer.utils.SharedHelper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val isLogin = SharedHelper.getBoolKey(this, Constants.IS_LOGIN)
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }

        }, 3000)
    }
}