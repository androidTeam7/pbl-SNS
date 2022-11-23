package com.example.pbl_sns.ui.profile

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseBottomDialogFragment
import com.example.pbl_sns.databinding.DialogDeleteFriendBinding
import com.example.pbl_sns.model.User
import com.example.pbl_sns.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

// 팔로워 삭제
class FollowerDeleteDialog(friendId:String, friendList:ArrayList<String>):BaseBottomDialogFragment<DialogDeleteFriendBinding>(R.layout.dialog_delete_friend) {
    private val db = Firebase.firestore
    private val mFriendId = friendId
    private val mFriendList = friendList

    override fun initDataBinding() {
        super.initDataBinding()

        binding.tvInfoDelete.text = mFriendId + getString(R.string.deleteInfo)
    }
    override fun initAfterBinding() {
        super.initAfterBinding()

        // follower 삭제시 FollowerDialog에 그 결과 넘겨서 ui 업데이트
        binding.btnDelete.setOnClickListener {
            mFriendList.remove(mFriendId)
            setFragmentResult("deleteFollower", bundleOf("followerList" to mFriendList))

            // 친구 목록에서 팔로잉 팔로우 눌렀을때도 팔로잉 팔로우 버튼 있게 만들시 사용
//            // 내 프로필에서 눌렀을 때
//            if(mFriendList.isNotEmpty()){
//                mFriendList.remove(mFriendId)
//                setFragmentResult("deleteFollower", bundleOf("followerList" to mFriendList))
//            }
//            // 친구 프로필의 팔로워 목록에서 눌렀을 때
//            else{
//                var myFriendList:ArrayList<String> = ArrayList()
//                // 내 친구목록 불러오기
//                db.collection("users").document(prefs.getString("email", "-1")).get()
//                    .addOnSuccessListener { documentSnapshot ->
//                        val data = documentSnapshot.toObject<User>()
//                        myFriendList = data!!.friends.following
//                    }
//                    .addOnFailureListener { exception ->
//                        Log.d(ContentValues.TAG, "get failed with ", exception)
//                    }
//                myFriendList.remove(mFriendId) // 내 친구에서 삭제하기
//                // 내 친구목록 업데이트
//                db.collection("users").document(prefs.getString("email", "-1"))
//                    .update("friends.follower", myFriendList)
//            }
            dismiss()
        }
        binding.btnNoDelete.setOnClickListener {
            dismiss()
        }
    }
}