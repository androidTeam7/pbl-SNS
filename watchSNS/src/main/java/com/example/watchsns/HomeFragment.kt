package com.example.watchsns

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.watchsns.MyApplication.Companion.prefs
import com.example.watchsns.databinding.FragmentHomeBinding

class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val userEmail = prefs.getString("email","-1")
    private var following:ArrayList<String> = ArrayList()
    private var follower:ArrayList<String> = ArrayList()

    private val viewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun initDataBinding() {
        super.initDataBinding()

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
}