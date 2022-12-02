package com.example.watchsns

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel :ViewModel(){
    private val _userLiveFollowerData : MutableLiveData<ArrayList<String>>
            = MutableLiveData()
    val userLiveFollowerData: LiveData<ArrayList<String>>
        get() = _userLiveFollowerData
    private val _userLiveFollowingData : MutableLiveData<ArrayList<String>>
            = MutableLiveData()
    val userLiveFollowingData: LiveData<ArrayList<String>>
        get() = _userLiveFollowingData


    private val repo = UserRepository()

    fun getUserFollower(email:String){
        repo.getFollowerData(email).observeForever{
            _userLiveFollowerData.postValue(it)
            Log.d("vm", _userLiveFollowerData.value.toString())
        }
    }
    fun getUserFollowing(email:String){
        repo.getFollowingData(email).observeForever{
            _userLiveFollowingData.postValue(it)
            Log.d("vm", _userLiveFollowingData.value.toString())
        }
    }
}