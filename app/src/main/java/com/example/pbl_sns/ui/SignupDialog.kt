package com.example.pbl_sns.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Patterns
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogSignupBinding
import com.example.pbl_sns.model.Privacy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupDialog : BaseDialogFragment<DialogSignupBinding>(R.layout.dialog_signup) {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setBottomNavSetting("none")
        auth = Firebase.auth
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.btnBack.setOnClickListener {
            dismiss()
        }

        binding.btnSignup.setOnClickListener {
            var isPossibleSignup = true
            var isEmpty = true
            val empty  = emptyList<String>().toMutableList()

            val email = binding.editTvEmail.text.toString()
            val name = binding.editTvName.text.toString()
            val id = binding.editTvId.text.toString()
            val password = binding.editTvPassword.text.toString()
            val passwordCheck=binding.editTvPasswordCheck.text.toString()


            // 유효성 검사
            if(email.isEmpty())
                empty.add("이메일")
            if(name.isEmpty())
                empty.add("이름")
            if(id.isEmpty())
                empty.add("아이디")
            if(password.isEmpty())
                empty.add("비밀번호")
            if(passwordCheck.isEmpty())
                empty.add("비밀번호 확인")

            if(email.isEmpty() or name.isEmpty() or id.isEmpty() or password.isEmpty() or passwordCheck.isEmpty()){
                Toast.makeText(context, "${empty}을(를) 입력해주세요", Toast.LENGTH_LONG).show()
                isPossibleSignup = false
                isEmpty = false
            }

            if(id == "-1")
                Toast.makeText(context, "${id}은(는) 사용할 수 없는 아이디 입니다.", Toast.LENGTH_LONG).show()

            if (isEmpty and (password == passwordCheck) and !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(context, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show()
                binding.editTvEmail.setText("")
                isPossibleSignup = false
            }

            if (isEmpty and (password != passwordCheck)) {
                Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                isPossibleSignup = false
            }

            //아이디와 비밀번호로 user 생성
            if (isPossibleSignup) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            //firestore에 유저정보 저장
                            val data = hashMapOf(
                                "email" to email,
                                "id" to id,
                                "privacy" to Privacy()
                            )

                            val privacyData = hashMapOf(
                                "image" to "",
                                "name" to name,
                                "id" to id,
                                "info" to ""
                            )

                            db.collection("users").document(email).set(data).addOnSuccessListener {
                                db.collection("users").document(email)
                                    .update("privacy", privacyData).addOnSuccessListener {
                                        Toast.makeText(context, "회원가입 완료. ${name}님 환영합니다.", Toast.LENGTH_LONG).show()
                                        dismiss()
                                    }.addOnFailureListener(){
                                        Toast.makeText(context, "데이터 확인 필요",Toast.LENGTH_LONG)
                                    }
                            }.addOnFailureListener{
                                Toast.makeText(context, "이메일 데이터 확인 필요",Toast.LENGTH_LONG)
                            }

                        } else {
                            Toast.makeText(context, "회원가입 실패", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // dialog full Screen code
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }
}