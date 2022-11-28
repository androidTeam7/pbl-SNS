package com.example.pbl_sns.ui.profile

import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogPostBinding
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.model.User
import com.example.pbl_sns.repository.ContentDTO
import com.google.firebase.auth.FirebaseAuth
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
        Glide.with(requireContext()).load(mPost.image).into(binding.imgPostDialog)
        binding.tvLikecounter.text = "Like " + mPost.likeCount.toString()
        binding.tvTime.text = mPost.date
    }
    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.btnClosePostDialog.setOnClickListener {
            dismiss()
        }

        // 댓글 모두 보기를 눌렀을 때
        binding.btnViewAllReply.setOnClickListener{

        }

        // 게시 버튼을 눌렀을 때
        binding.btnReply.setOnClickListener{
            var reply = ContentDTO.Comment()
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            reply.uid = uid
            reply.userId = userEmail
            reply.comment = binding.editTvReply?.text.toString()
            reply.timestamp = System.currentTimeMillis()

            if(userEmail != "-1"){
                val reply = binding.editTvReply?.text.toString()
                if(reply != ""){
                    if (uid != null) {
                        Firebase.firestore.collection("uid").document(uid).get()
                            .addOnSuccessListener { documentSnapshot ->
                                val data = documentSnapshot.toObject<User>()
                                val post = data!!.postArray
                                Log.d("postArray", post.toString())
                            }
                            .addOnFailureListener { exception ->
                                Log.d(ContentValues.TAG, "get failed with ", exception)
                            }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        context?.dialogFragmentResize(this@PostDialog, 0.9f, 0.8f)
    }
}