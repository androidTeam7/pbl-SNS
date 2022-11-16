package com.example.pbl_sns.ui

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogLogoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogoutDialog :BaseDialogFragment<DialogLogoutBinding>(R.layout.dialog_logout){
    private lateinit var auth: FirebaseAuth
    private var isLogout:Boolean = false

    override fun initStartView() {
        super.initStartView()

        auth= Firebase.auth
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.btnNoLogout.setOnClickListener {
            isLogout = false
            dismiss()
        }

        binding.btnLogout.setOnClickListener {
            isLogout = true

            setFragmentResult("requestLogout", bundleOf("resultLogout" to isLogout))
            dismiss()
        }
    }

}