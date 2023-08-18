package com.example.htseafood.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import com.example.htseafood.R
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_login.tvSignIn

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etPassword.setOnTouchListener { v, event ->
            if ((event.action == MotionEvent.ACTION_UP) && event.rawX >= (etPassword.right - etPassword.compoundDrawables[2].bounds.width())) {
                if (etPassword.inputType == InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ) {
                    etPassword.inputType = InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0)
                } else {
                    etPassword.inputType = InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_eye, 0)
                }
                etPassword.setSelection(etPassword.text.length)
                true
            } else false
        }

        tvSignIn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}