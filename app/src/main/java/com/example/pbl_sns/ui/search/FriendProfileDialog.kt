package com.example.pbl_sns.ui.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.FragmentProfileBinding
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.repository.AlarmDTO
import com.example.pbl_sns.service.FcmPush
import com.example.pbl_sns.ui.MainActivity
import com.example.pbl_sns.ui.profile.*
import com.example.pbl_sns.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendProfileDialog(email: String) : BaseDialogFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private val db = Firebase.firestore
    lateinit var profileAdapter: ProfileAdapter
    private var following:ArrayList<String> = ArrayList()
    private lateinit var id:String
    private val mEmail = email  // 친구 이메일
    private val userEmail = prefs.getString("email","-1")
    private var follower:ArrayList<String>
    private var isFollowing:Boolean
    private var firstStatus:Boolean
    private var secondStatus:Boolean
    init {
        isFollowing = false
        firstStatus = false
        secondStatus = false
        follower = ArrayList()
    }

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment())[UserViewModel::class.java]
    }

    override fun initDataBinding() {
        super.initDataBinding()

        (activity as MainActivity).setBottomNavSetting("")

        binding.btnCloseFriendProfile.visibility = View.VISIBLE
        binding.tvIdFriendProfile.visibility = View.VISIBLE
        binding.btnFollowerFollowing.visibility = View.VISIBLE

        binding.tvIdProfile.visibility = View.GONE
        binding.btnSettingProfile.visibility = View.GONE
        binding.btnEditProfile.visibility = View.INVISIBLE
        profileAdapter = ProfileAdapter(ArrayList())
        initPostArray()

        viewModel.getUserData(mEmail) // 친구의 privacy 데이터
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            id = it.id
            binding.tvIdFriendProfile.text = it.id
            binding.tvNameProfile.text = it.name

            if(it.info.isNotEmpty()){
                binding.tvInfoProfile.text = it.info
                binding.tvInfoProfile.visibility = View.VISIBLE
            }
            else{
                binding.tvInfoProfile.visibility = View.GONE
            }
        }

        viewModel.getUserPost(mEmail) // 친구의 post 데이터
        viewModel.userLivePostData.observe(viewLifecycleOwner){
            val itemList:ArrayList<Post> = it
            binding.tvPost.text = itemList.size.toString()
            profileAdapter.itemList = itemList
        }

        viewModel.getUserFollower(mEmail)
        viewModel.getUserFollowing(mEmail)
        viewModel.userLiveFollowerData.observe(viewLifecycleOwner){
            val itemList:ArrayList<String> = it
            follower = itemList
            Log.d("followingCheck","$itemList")
            binding.tvFollower.text = follower.size.toString()
            isFollowingFunc()
        }
        viewModel.userLiveFollowingData.observe(viewLifecycleOwner){
            val itemList:ArrayList<String> = it
            following = itemList
            binding.tvFollowing.text = following.size.toString()
        }

    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        profileAdapter.setItemClickListener(object: ProfileAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val post = profileAdapter.itemList[position]
                PostDialog(mEmail, post).show(parentFragmentManager,"PostDialog")
            }
        })

        binding.btnCloseFriendProfile.setOnClickListener {
            secondStatus = isFollowing
            // 내 친구목록 업데이트
            if(firstStatus != secondStatus){
                if(isFollowing)
                    setFragmentResult("addFollowingFPD", bundleOf("friendEmail" to mEmail))
                else
                    setFragmentResult("deleteFollowingFPD", bundleOf("friendEmail" to mEmail))
            }

            dismiss()
        }
        binding.btnFollowing.setOnClickListener {
            FollowingDialog(mEmail, id, following).show(parentFragmentManager,"following")
        }
        binding.btnFollower.setOnClickListener {
            FollowerDialog(mEmail, id, follower).show(parentFragmentManager,"follower")
        }
        binding.btnFollowerFollowing.setOnClickListener {
            if(isFollowing){ // 내가 팔로잉하는 상태
                follower.remove(userEmail) // 친구 팔로워에서 나 삭제하기
                binding.btnFollowerFollowing.background = ContextCompat.getDrawable(requireContext(), R.drawable.radius10_solid_blue)
                binding.btnFollowerFollowing.text = "팔로우"
                binding.btnFollowerFollowing.setTextColor(Color.WHITE)
                isFollowing = false
                viewModel.setUserFollower(mEmail, follower)
            } else{ // 팔로잉 안하는 상태
                follower.add(userEmail) // 친구 팔로워에 나 추가하기
                binding.btnFollowerFollowing.background = ContextCompat.getDrawable(requireContext(), R.drawable.radius10_solid)
                binding.btnFollowerFollowing.text = "팔로잉"
                binding.btnFollowerFollowing.setTextColor(Color.BLACK)
                isFollowing = true
                viewModel.setUserFollower(mEmail, follower)
                followAlarm(mEmail)
            }
            binding.btnFollowerFollowing.backgroundTintList

            // 비공개 계정 만들시 사용
//            var isFollower:Boolean = false
//            for(f in follower){
//                if(f == mEmail) {
//                    isFollower = true
//                    break
//                }
//            }
//            // 내가 팔로잉하는 상태라면 물어보기
//            if(isFollower){
//                FollowerDeleteDialog(id, ArrayList())
//            } else{
//                var myFriendList:ArrayList<String> = ArrayList()
//                myFriendList.add(id) // 내 친구에 추가하기
//                // 내 친구목록 업데이트
//                db.collection("users").document(prefs.getString("email", "-1"))
//                    .update("friends.following", myFriendList)
//            }
        }
    }

    private fun isFollowingFunc(){
        // 내가 팔로잉 한 상태라면 회색버튼, 아니라면 파란 버튼
        isFollowing = false
        Log.d("followingCheck1","$follower")
        for(f in follower){
            if(f == userEmail) {
                Log.d("followingCheck2","true${f}")
                isFollowing = true
                firstStatus = true
                break
            }
        }

        Log.d("followingCheck2.1","$isFollowing")

        if(isFollowing){
            binding.btnFollowerFollowing.background = ContextCompat.getDrawable(requireContext(), R.drawable.radius10_solid)
            binding.btnFollowerFollowing.text = "팔로잉"
            binding.btnFollowerFollowing.setTextColor(Color.BLACK)
        } else{
            binding.btnFollowerFollowing.background = ContextCompat.getDrawable(requireContext(), R.drawable.radius10_solid_blue)
            binding.btnFollowerFollowing.text = "팔로우"
            binding.btnFollowerFollowing.setTextColor(Color.WHITE)
        }
    }

    private fun initPostArray() {
        binding.postRecyclerviewProfile.apply {
            adapter = profileAdapter
            layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()

        // dialog full Screen code
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    private fun followAlarm(destinationUid: String){
        var alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = userEmail
        alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        alarmDTO.kind = 0
        alarmDTO.timestamp = System.currentTimeMillis()
        alarmDTO.message = "님이 회원님을 팔로우하였습니다."
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)
    }
}