package com.example.pbl_sns.ui.profile

import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseBottomDialogFragment
import com.example.pbl_sns.databinding.DialogDeleteFriendBinding
import com.example.pbl_sns.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendDeleteDialog(friendId:String, friendList:ArrayList<String>):BaseBottomDialogFragment<DialogDeleteFriendBinding>(R.layout.dialog_delete_friend) {

    private val mFriendId = friendId
    private val mFriendList = friendList

    override fun initDataBinding() {
        super.initDataBinding()

        binding.tvInfoDelete.text = mFriendId + R.string.deleteInfo
    }
    override fun initAfterBinding() {
        super.initAfterBinding()

        // follower 삭제시 FollowerDialog에 그 결과 넘겨서 ui 업데이트
        binding.btnDelete.setOnClickListener {
            mFriendList.remove(mFriendId)
            setFragmentResult("deleteFollower", bundleOf("followerList" to mFriendList))

            Log.d("lifecyclee","4")
            dismiss()
        }
        binding.btnNoDelete.setOnClickListener {
            dismiss()
        }
    }
}