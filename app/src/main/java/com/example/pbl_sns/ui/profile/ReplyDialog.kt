package com.example.pbl_sns.ui.profile

import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogReplyBinding


class ReplyDialog: BaseDialogFragment<DialogReplyBinding>(R.layout.item_reply) {
    lateinit var replyAdapter: ReplyAdapter
    private val userEmail = MyApplication.prefs.getString("email", "-1")

    override fun initDataBinding() {
        super.initDataBinding()

    }
    override fun initAfterBinding(){
        super.initAfterBinding()

    }

    override fun onResume(){
        super.onResume()

        context?.dialogFragmentResize(this@ReplyDialog, 0.9f, 0.7f)
    }
}