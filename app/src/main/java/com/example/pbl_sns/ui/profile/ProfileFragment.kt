package com.example.pbl_sns.ui.profile

import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentProfileBinding
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.ui.MainActivity
import com.example.pbl_sns.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment: BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private var result : Boolean = false
    lateinit var profileAdapter: ProfileAdapter
    private var following:ArrayList<String> = ArrayList()
    private var follower:ArrayList<String> = ArrayList()
    private lateinit var auth: FirebaseAuth
    private lateinit var id:String
    private val userEmail = prefs.getString("email","-1")

    private val viewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).setBottomNavSetting("")
    }

    override fun initDataBinding() {
        super.initDataBinding()
        Log.d("lifee", "FragInit")

        (activity as MainActivity).setBottomNavSetting("")

        binding.btnCloseFriendProfile.visibility = View.GONE
        binding.tvIdFriendProfile.visibility = View.GONE
        binding.btnFollowerFollowing.visibility = View.GONE

        binding.tvIdProfile.visibility = View.VISIBLE
        binding.btnSettingProfile.visibility = View.VISIBLE
        binding.btnEditProfile.visibility = View.VISIBLE

        auth=Firebase.auth

        profileAdapter = ProfileAdapter(ArrayList())
        initPostArray()
        //binding.postRecyclerviewProfile.adapter = profileAdapter

        viewModel.getUserData(userEmail)
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            id = it.id
            binding.tvIdProfile.text = it.id
            binding.tvNameProfile.text = it.name

            if(it.info.isNotEmpty()){
                binding.tvInfoProfile.text = it.info
                binding.tvInfoProfile.visibility = View.VISIBLE
            }
            else{
                binding.tvInfoProfile.visibility = View.GONE
            }
        }

        viewModel.getUserPost(userEmail)
        viewModel.userLivePostData.observe(viewLifecycleOwner){
            val itemList:ArrayList<Post> = it
            binding.tvPost.text = itemList.size.toString()
            profileAdapter.itemList = itemList
        }

        viewModel.getUserFollower(userEmail)
        viewModel.getUserFollowing(userEmail)
        viewModel.userLiveFollowerData.observe(viewLifecycleOwner){
            val itemList:ArrayList<String> = it
            follower = itemList
            binding.tvFollower.text = follower.size.toString()
        }
        viewModel.userLiveFollowingData.observe(viewLifecycleOwner){
            val itemList:ArrayList<String> = it
            following = itemList
            binding.tvFollowing.text = following.size.toString()
        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        Log.d("lifee", "FragAfter")

        // 사용자 변경 정보 받아오기
        setFragmentResultListener("editPrivacy") { _, bundle ->
            result = bundle.get("resultPrivacy") as Boolean
            viewModel.getUserData(userEmail)
        }
        // 로그아웃
        setFragmentResultListener("requestLogout") { _, bundle ->
            val isLogout:Boolean = bundle.get("resultLogout") as Boolean
            if(isLogout){
                auth.signOut()
                prefs.removeAll()
                navController.navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }

        binding.btnSettingProfile.setOnClickListener {
            LogoutDialog().show(parentFragmentManager,"LogoutDialog")
        }
        binding.btnEditProfile.setOnClickListener {
            ProfileEditDialog().show(parentFragmentManager, "ProfileEditDialog")
        }

        profileAdapter.setItemClickListener(object: ProfileAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val post = profileAdapter.itemList[position]
                //PostDailog(userEmail, post).show(parentFragmentManager,"PostDialog")
            }
        })

        binding.btnFollowing.setOnClickListener {
            Log.d("clickk", "btnFollowing")
            FollowingDialog(userEmail, id, following).show(parentFragmentManager,"following")
        }
        binding.btnFollower.setOnClickListener {
            Log.d("clickk", "btnFollower")
            FollowerDialog(userEmail, id, follower).show(parentFragmentManager,"follower")
        }

        setFragmentResultListener("changeFollower") { _, _ ->
            viewModel.getUserFollower(userEmail)
        }
        setFragmentResultListener("changeFollowing") { _, _ ->
            viewModel.getUserFollowing(userEmail)
        }
    }

    private fun initPostArray() {
        binding.postRecyclerviewProfile.apply {
            adapter = profileAdapter
            layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }
}