package com.example.pbl_sns.ui.home

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentHomeBinding
import com.example.pbl_sns.ui.MainActivity
import com.example.pbl_sns.ui.profile.ProfileAdapter
import com.example.pbl_sns.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    lateinit var homeAdapter: HomeAdapter
    private val userEmail= prefs.getString("email","-1")
    private val viewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun initDataBinding() {
        super.initDataBinding()

        (activity as MainActivity).setBottomNavSetting("all")

        homeAdapter = HomeAdapter(ArrayList())
        binding.homeRecyclerviewHome.adapter = homeAdapter

        Log.d("내 이메일", prefs.getString("email","-1"))
        viewModel.getUserFollowing(userEmail)
        viewModel.getAllPost()
        viewModel.userLiveFollowingData.observe(viewLifecycleOwner){
            viewModel.getAllPost()
        }
        viewModel.allLivePostData.observe(viewLifecycleOwner) {
            homeAdapter.itemList = it
            Log.d("으음?",it.toString())
        }
    }
}