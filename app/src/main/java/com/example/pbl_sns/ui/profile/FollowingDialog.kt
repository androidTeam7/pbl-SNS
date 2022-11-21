package com.example.pbl_sns.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogFollowingBinding
import com.example.pbl_sns.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FollowingDialog(id:String, followerList:ArrayList<String>):BaseDialogFragment<DialogFollowingBinding>(R.layout.dialog_following) {
    private val db = Firebase.firestore
    lateinit var followingAdapter: FollowingAdapter
    private val list:ArrayList<String> = followerList
    private val mId = id

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment())[UserViewModel::class.java]
    }

    override fun initDataBinding() {
        super.initDataBinding()

        binding.tvIdFollowing.text = mId

        followingAdapter = FollowingAdapter(ArrayList())
        binding.postRecyclerviewFollowing.adapter = followingAdapter
        followingAdapter.itemList = list
        viewModel.userLiveFollowingData.observe(viewLifecycleOwner) {
            followingAdapter.itemList = it
        }

    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        // 검색 기능
        binding.editTvSearchFollowing.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searchString = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchItemList:ArrayList<String> = ArrayList()
                val searchString = binding.editTvSearchFollowing.text

                for (item in viewModel.userLiveFollowingData.value!!) {
                    if (item.contains(searchString)) {
                        searchItemList.add(item)
                    }
                }
                followingAdapter.itemList = searchItemList
                if (binding.editTvSearchFollowing.text.isEmpty()) {
                    viewModel.getUserFollowing()
                }
            }
        })

        // back 아이콘 클릭시 다이얼로그 종료
        binding.btnCloseFollowingDialog.setOnClickListener {
            val result = followingAdapter.friendsData  as ArrayList<String>
            setFragmentResult("changeFollowing", bundleOf("followingList" to result))
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        // full Screen code
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        viewModel.getUserFollowing()
    }
}