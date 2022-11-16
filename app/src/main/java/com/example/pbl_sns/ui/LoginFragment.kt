package com.example.pbl_sns.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentLoginBinding
import com.example.pbl_sns.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private lateinit var auth: FirebaseAuth

    override fun initStartView() {
        super.initStartView()

        auth = Firebase.auth

        //로그인 되어있는지 확인
        val currentUser = auth.currentUser
        if(currentUser != null) {
            if(prefs.getString("email","-1") != "-1")
                navController.navigate(R.id.action_loginFragment_to_homeFragment)
            else{
                auth.signOut()
            }
        }
    }

    override fun initDataBinding() {
        super.initDataBinding()

        (activity as MainActivity).setBottomNavSetting("none")
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.buttonLogin.setOnClickListener {     // 로그인 버튼 클릭했을 때
            val userEmail = binding.editTextId.text.toString()   // ID EditText의 문자열을 userEmail에 저장
            val password = binding.editTextPasswd.text.toString()   // Passwd EditText의 문자열을 password에 저장
            doLogin(userEmail, password)   // userEmail과 password를 통해 로그인 시도하는 함수 호출
        }

        binding.goSignUp.setOnClickListener {    // 가입하기 텍스트를 클릭했을 경우
            navController.navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    // 매개변수로 받은 userEmail과 password를 통해 로그인을 시도하는 함수
    private fun doLogin(userEmail: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)   // userEmail과 password로 로그인 시도
            .addOnCompleteListener {
                if(it.isSuccessful) {  // 로그인 성공했을 경우(Firebase의 Users에 계정이 존재할 경우)
                    prefs.removeAll()
                    prefs.setString("email", userEmail)
                    setUserId(userEmail)
                    navController.navigate(R.id.action_loginFragment_to_homeFragment)
                } else {   // 로그인 실패했을 경우(Firebase의 Users에 계정이 존재하지 않을 경우)
                    Log.w("LoginAcitivy", "signInWIthEmail", it.exception)  // Log에 에러 입력
                    Toast.makeText(context, "Login failed.", Toast.LENGTH_SHORT).show()  // Login failed 스낵바 띄우기
                }
            }
    }

    private fun setUserId(email:String){
        Firebase.firestore.collection("users").document(email).get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObject<User>()
                val id = data!!.id
                prefs.setString("id", id)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }
}