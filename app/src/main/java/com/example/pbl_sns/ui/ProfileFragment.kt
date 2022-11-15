package com.example.pbl_sns.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentProfileBinding
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.model.Privacy
import com.example.pbl_sns.model.User
import com.example.pbl_sns.viewmodel.UserViewModel

class ProfileFragment: BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private var result : Boolean = false
    lateinit var profileAdapter: ProfileAdapter
    private var following:String = ""
    private var follower:String = ""

    private val viewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun initDataBinding() {
        super.initDataBinding()
        Log.d("lifee", "FragInit")

        (activity as MainActivity).setBottomNavSetting("")

        profileAdapter = ProfileAdapter(ArrayList())
        binding.postRecyclerviewProfile.adapter = profileAdapter

        viewModel.getUserData()
        viewModel.userLiveData.observe(viewLifecycleOwner) {
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

        viewModel.getUserPost()
        viewModel.userLivePostData.observe(viewLifecycleOwner){
            val itemList:ArrayList<Post> = it
            profileAdapter.itemList = itemList
        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        Log.d("lifee", "FragAfter")

        // 사용자 변경 정보 받아오기
        setFragmentResultListener("editPrivacy") { _, bundle ->
            result = bundle.get("resultPrivacy") as Boolean
            viewModel.getUserData()
        }
        // 로그아웃
        setFragmentResultListener("requestLogout") { _, bundle ->
            val isLogout:Boolean = bundle.get("resultLogout") as Boolean
            if(isLogout)
                navController.navigate(R.id.action_profileFragment_to_loginFragment)
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
                PostDailog(post).show(parentFragmentManager,"PostDialog")
            }
        })
    }

    private fun initPostArray() {
        binding.postRecyclerviewProfile.apply {
            adapter = profileAdapter
            layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }
}