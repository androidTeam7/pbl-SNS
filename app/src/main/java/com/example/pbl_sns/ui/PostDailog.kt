package com.example.pbl_sns.ui

import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogPostBinding
import com.example.pbl_sns.model.Post

class PostDailog(post:Post): BaseDialogFragment<DialogPostBinding>(R.layout.dialog_post) {
    private val mPost:Post = post

    override fun initDataBinding() {
        super.initDataBinding()

        binding.tvContentPostDialog.text = mPost.content
    }
    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.btnClosePostDialog.setOnClickListener {
            dismiss()
        }
    }
}