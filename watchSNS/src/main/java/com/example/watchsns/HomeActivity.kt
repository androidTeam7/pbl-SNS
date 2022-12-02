package com.example.watchsns

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.watchsns.databinding.ActivityHomeBinding
import com.example.watchsns.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private val userEmail = MyApplication.prefs.getString("email","-1")
    private var following:ArrayList<String> = ArrayList()
    private var follower:ArrayList<String> = ArrayList()

    private val viewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUserFollower(userEmail)
        viewModel.getUserFollowing(userEmail)
        viewModel.userLiveFollowerData.observe(this){
            val itemList:ArrayList<String> = it
            follower = itemList
            binding.tvFollower.text = follower.size.toString()
            Log.d("follwerTest: ", it.toString())
        }
        viewModel.userLiveFollowingData.observe(this){
            val itemList:ArrayList<String> = it
            following = itemList
            binding.tvFollowing.text = following.size.toString()
            Log.d("followingTest: ", it.toString())
        }
    }
}