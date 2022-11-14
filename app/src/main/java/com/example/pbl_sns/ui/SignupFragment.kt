package com.example.pbl_sns.ui

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupFragment : BaseFragment<FragmentSignupBinding>(R.layout.fragment_signup) {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setBottomNavSetting("none")
        auth = Firebase.auth
    }

    override fun initDataBinding() {
        super.initDataBinding()

        (activity as MainActivity).setBottomNavSetting("none")
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.btnBack.setOnClickListener {
            navController.navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.btnSignup.setOnClickListener {
            var isGoToJoin = true
            var EmptyCheck = true
            val EmptyString  = emptyList<String>().toMutableList()

            val email = binding.editTvEmail.text.toString()
            val name = binding.editTvName.text.toString()
            val id = binding.editTvId.text.toString()
            val password = binding.editTvPassword.text.toString()
            val passwordCheck=binding.editTvPasswordCheck.text.toString()


            // 유효성 검사
            if(email.isEmpty())
                EmptyString.add("이메일")
            if(name.isEmpty())
                EmptyString.add("이름")
            if(id.isEmpty())
                EmptyString.add("아이디")
            if(password.isEmpty())
                EmptyString.add("비밀번호")
            if(passwordCheck.isEmpty())
                EmptyString.add("비밀번호 확인")

            if(email.isEmpty() or name.isEmpty() or id.isEmpty() or password.isEmpty() or passwordCheck.isEmpty()){
                Toast.makeText(context, "$EmptyString 을(를) 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
                EmptyCheck = false
            }

            if (EmptyCheck and (password == passwordCheck) and !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(context, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show()
                binding.editTvEmail.setText("")
                isGoToJoin = false
            }

            if (EmptyCheck and (password != passwordCheck)) {
                Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            //아이디와 비밀번호로 user 생성
            if (isGoToJoin) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "${name}님 환영합니다.", Toast.LENGTH_LONG).show()

                            //firestore에 유저정보 저장
                            val data = hashMapOf(
                                "email" to email,
                                "name" to name,
                                "id" to id,
                                "info" to ""
                            )

                            MyApplication.prefs.setString("email", email)

                            //db.collection("users").document(email).set(data)

                            db.collection("users").document(MyApplication.prefs.getString("email","null"))
                                .update("privacy", FieldValue.arrayUnion(data)).addOnSuccessListener {
                                    navController.navigate(R.id.action_signupFragment_to_loginFragment)
                                }.addOnFailureListener(){
                                    Toast.makeText(context, "데이터 확인",Toast.LENGTH_LONG)
                                }

                        } else {
                            Toast.makeText(context, "회원가입 실패", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        binding.btnBack.setOnClickListener {
            navController.navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
}