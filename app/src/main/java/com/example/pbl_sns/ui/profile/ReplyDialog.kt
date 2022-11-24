package com.example.pbl_sns.ui.profile

import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogReplyBinding
import com.example.pbl_sns.model.Reply

class ReplyDialog(reply: Reply): BaseDialogFragment<DialogReplyBinding>(R.layout.dialog_reply) {
    private val mReply: Reply = reply

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