package com.example.pbl_sns.ui.profile

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogPostBinding
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PostDialog(email:String, post:Post): BaseDialogFragment<DialogPostBinding>(R.layout.dialog_post) {
    private val mPost:Post = post
    private val userEmail = prefs.getString("email","-1")

    override fun initDataBinding() {
        super.initDataBinding()

        binding.tvIdPostDialog.text = prefs.getString("id","-1")
        binding.tvContentPostDialog.text = mPost.content
    }
    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.btnClosePostDialog.setOnClickListener {
            dismiss()
        }

        binding.btnReply.setOnClickListener{
            val reply = binding.editTvReply?.text.toString()
            if(reply != ""){
                Firebase.firestore.collection("users").document(userEmail).get()
                    .addOnSuccessListener { documentSnapshot ->
                        val data = documentSnapshot.toObject<User>()
                        val email = data!!.id

                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "get failed with ", exception)
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        context?.dialogFragmentResize(this@PostDialog, 0.9f, 0.7f)
    }
}