package com.example.pbl_sns.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogFollowerBinding
import com.example.pbl_sns.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FollowerDialog(email:String, id:String, followerList:ArrayList<String>) :BaseDialogFragment<DialogFollowerBinding>(R.layout.dialog_follower){
    private val db = Firebase.firestore
    private lateinit var followerAdapter: FollowerAdapter
    private val list:ArrayList<String> = followerList
    private val mEmail = email
    private val mId = id
    //private val userEmail = prefs.getString("email","-1")
    private val userId = prefs.getString("id","-1")

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment())[UserViewModel::class.java]
    }

    override fun initDataBinding() {
        super.initDataBinding()

        binding.tvIdFollowing.text = mId

        followerAdapter = if(mId == userId)
            FollowerAdapter(true, ArrayList())
        else
            FollowerAdapter(false, ArrayList())
        binding.postRecyclerviewFollower.adapter = followerAdapter
        followerAdapter.itemList = list

        viewModel.userLiveFollowerData.observe(viewLifecycleOwner) {
            followerAdapter.itemList = it
        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        // 검색 기능
        binding.editTvSearchFollower.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searchString = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchItemList:ArrayList<String> = ArrayList()
                val searchString = binding.editTvSearchFollower.text

                if(viewModel.userLiveFollowingData.value != null){
                    for (item in viewModel.userLiveFollowerData.value!!) {
                        if (item.contains(searchString)) {
                            searchItemList.add(item)
                        }
                    }
                    followerAdapter.itemList = searchItemList
                }

                if (binding.editTvSearchFollower.text.isEmpty()) {
                    viewModel.getUserFollowing(mEmail)
                }
            }
        })

        // adapter에서 삭제 버튼 눌렸을시 다이얼로그
        followerAdapter.setItemClickListener(object: FollowerAdapter.OnItemClickListener {
            override fun onClick(v: View, list:ArrayList<String>,position: Int) {
                val friend = followerAdapter.itemList[position]
                val friendList = list
                FollowerDeleteDialog(friend,friendList).show(parentFragmentManager,"FollowerDeleteDialog")
            }
        })

        // follower 변경 있을시 fireStore에 업데이트
        setFragmentResultListener("deleteFollower") { _, bundle ->
            val result = bundle.get("followerList") as ArrayList<String>
            db.collection("users").document(MyApplication.prefs.getString("email", "-1"))
                .update("friends.follower", result).addOnSuccessListener {
                    viewModel.setUserFollower(mEmail, result)
                    setFragmentResult("changeFollower", bundleOf("followerList" to result))
                }
        }

        // back 아이콘 클릭시 다이얼로그 종료
        binding.btnCloseFollowerDialog.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        // dialog full Screen code
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        viewModel.getUserFollower(mEmail)
    }
}