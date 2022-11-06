package com.example.pbl_sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pbl_sns.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonLogin.setOnClickListener {
            val userEmail = binding.editTextId.text.toString()
            val password = binding.editTextPasswd.text.toString()
            doLogin(userEmail, password)
        }

        binding.goSignUp.setOnClickListener {
            startActivity(
                Intent(this, SignupActivity::class.java))

        }
    }

    private fun doLogin(userEmail: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    startActivity(
                        Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w("LoginAcitivy", "signInWIthEmail", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}