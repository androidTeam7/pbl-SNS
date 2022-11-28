package com.example.pbl_sns.ui.profile

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogPostBinding
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.model.User
import com.example.pbl_sns.repository.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PostDialog(email:String, post:Post): BaseDialogFragment<DialogPostBinding>(R.layout.dialog_post) {
    private val mPost:Post = post
    private val userEmail = prefs.getString("email","-1")
    private val userId = prefs.getString("id", "-1")
    private val db = Firebase.firestore

    override fun initDataBinding() {
        super.initDataBinding()

        binding.tvIdPostDialog.text = prefs.getString("id","-1")
        binding.tvContentPostDialog.text = mPost.content
        Glide.with(requireContext()).load(mPost.image).into(binding.imgPostDialog)
        binding.tvLikecounter.text = mPost.content
        binding.tvTime.text = mPost.likeCount.toString()
    }
    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.btnClosePostDialog.setOnClickListener {
            dismiss()
        }

        // 댓글 모두 보기를 눌렀을 때
        binding.btnViewAllReply.setOnClickListener{
            ReplyDialog(mPost.email, mPost).show(parentFragmentManager,"ReplyDialog")
        }

        // 게시 버튼을 눌렀을 때
        binding.btnReply.setOnClickListener{
            addReply(mPost.email, mPost, binding.editTvReply.text.toString())
        }
    }
    fun addReply(email: String, post:Post, editReply: String) {

        if (userEmail != "-1") {
            val reply = hashMapOf(
                "profile" to "",
                "id" to userId,
                "reply" to editReply,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("users").document(email).collection("postArray").document(post.time.toString())
                .update("reply", FieldValue.arrayUnion(reply))
                .addOnSuccessListener {
                    Toast.makeText(context, "댓글 업로드 성공", Toast.LENGTH_LONG).show()
                }

        }
    }

    override fun onResume() {
        super.onResume()

        context?.dialogFragmentResize(this@PostDialog, 0.9f, 0.8f)
    }
}