package com.example.pbl_sns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pbl_sns.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_signup)
    }
}