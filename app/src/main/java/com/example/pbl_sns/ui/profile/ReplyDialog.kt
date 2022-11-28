package com.example.pbl_sns.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogReplyBinding
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.ui.MainActivity
import com.example.pbl_sns.ui.alarm.AlarmAdapter
import com.example.pbl_sns.viewmodel.UserViewModel


class ReplyDialog(email: String, post: Post): BaseDialogFragment<DialogReplyBinding>(R.layout.dialog_reply) {
    lateinit var replyAdapter: ReplyAdapter
    private val userEmail = MyApplication.prefs.getString("email", "-1")
    private val email = email
    private val post = post
    private val viewModel by lazy{
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun initStartView() {
        super.initStartView()

    }
    override fun initDataBinding() {
        super.initDataBinding()
        replyAdapter = ReplyAdapter(ArrayList())
        binding.postRecyclerviewReply.adapter = replyAdapter

        viewModel.getPostReplyData(email, post.time.toString())
        viewModel.postLiveReplyData.observe(viewLifecycleOwner){
            if(it.size == 0)
                binding.tvNoReply.visibility = View.VISIBLE
            else
                binding.tvNoReply.visibility = View.GONE
            replyAdapter.itemList = it
            Log.d("알람",it.toString())
        }
    }
    override fun initAfterBinding(){
        super.initAfterBinding()

        binding.btnCloseReplyDialog.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume(){
        super.onResume()

        // dialog full Screen code
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }
}