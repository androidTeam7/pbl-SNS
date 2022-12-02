package com.example.watchsns

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.watchsns.databinding.ActivityLoginBinding
import com.example.watchsns.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        //로그인 되어있는지 확인
        val currentUser = auth.currentUser
        if(currentUser != null && MyApplication.prefs.getString("email","-1") != "-1") {
            val intent = Intent(application, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {     // 로그인 버튼 클릭했을 때
            val userEmail = binding.editTvName.text.toString()   // ID EditText의 문자열을 userEmail에 저장
            val password = binding.editTvPassword.text.toString()   // Passwd EditText의 문자열을 password에 저장
            if(userEmail.isEmpty() || password.isEmpty())
                Toast.makeText(this,"아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT)
            else
                doLogin(userEmail, password)   // userEmail과 password를 통해 로그인 시도하는 함수 호출
        }
    }

    // 매개변수로 받은 userEmail과 password를 통해 로그인을 시도하는 함수
    private fun doLogin(userEmail: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)   // userEmail과 password로 로그인 시도
            .addOnCompleteListener {
                if(it.isSuccessful) {  // 로그인 성공했을 경우(Firebase의 Users에 계정이 존재할 경우)
                    MyApplication.prefs.removeAll()
                    MyApplication.prefs.setString("email", userEmail)
                    setUserId(userEmail)
                    val intent = Intent(application, HomeActivity::class.java)
                    startActivity(intent)
                } else {   // 로그인 실패했을 경우(Firebase의 Users에 계정이 존재하지 않을 경우)
                    Log.w("LoginAcitivy", "signInWIthEmail", it.exception)  // Log에 에러 입력
                    Toast.makeText(this, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()  // Login failed 스낵바 띄우기
                }
            }
    }

    private fun setUserId(email:String){
        Firebase.firestore.collection("users").document(email).get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObject<User>()
                val id = data!!.id
                MyApplication.prefs.setString("id", id)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }
}