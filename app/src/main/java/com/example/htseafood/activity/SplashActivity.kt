package com.example.htseafood.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.htseafood.R
import com.example.htseafood.utils.Constants
import com.example.htseafood.utils.SharedHelper

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