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

        binding.buttonLogin.setOnClickListener {     // 로그인 버튼 클릭했을 때
            val userEmail = binding.editTextId.text.toString()   // ID EditText의 문자열을 userEmail에 저장
            val password = binding.editTextPasswd.text.toString()   // Passwd EditText의 문자열을 password에 저장
            doLogin(userEmail, password)   // userEmail과 password를 통해 로그인 시도하는 함수 호출
        }

        binding.goSignUp.setOnClickListener {    // 가입하기 텍스트를 클릭했을 경우
            startActivity(
                Intent(this, SignupActivity::class.java))   // SignupActivity로 이동
        }
    }

    // 매개변수로 받은 userEmail과 password를 통해 로그인을 시도하는 함수
    private fun doLogin(userEmail: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)   // userEmail과 password로 로그인 시도
            .addOnCompleteListener(this) {
                if(it.isSuccessful) {  // 로그인 성공했을 경우(Firebase의 Users에 계정이 존재할 경우)
                    startActivity(
                        Intent(this, MainActivity::class.java))  // MainActivity로 이동
                    finish()  // 현재 Activity 화면 지우기
                } else {   // 로그인 실패했을 경우(Firebase의 Users에 계정이 존재하지 않을 경우)
                    Log.w("LoginAcitivy", "signInWIthEmail", it.exception)  // Log에 에러 입력
                    Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show()  // Login failed 스낵바 띄우기
                }
            }
    }
}