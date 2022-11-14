package com.example.pbl_sns.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentProfileBinding
import com.example.pbl_sns.model.User
import com.example.pbl_sns.viewmodel.UserViewModel

class ProfileFragment: BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private val viewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun initDataBinding() {
        super.initDataBinding()
        Log.d("lifee", "FragInit")

        (activity as MainActivity).setBottomNavSetting("")

        viewModel.getUserData()
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.tvId.text = it.id
            binding.tvName.text = it.name

            if(it.info.isNotEmpty()){
                binding.tvInfo.text = it.info
                binding.tvInfo.visibility = View.VISIBLE
            }
            else{
                binding.tvInfo.visibility = View.GONE
            }
        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        Log.d("lifee", "FragAfter")

        binding.btnEditProfile.setOnClickListener {
            ProfileEditDialog().show(parentFragmentManager, "ProfileEditDialog")
        }
    }
}