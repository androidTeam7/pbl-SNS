package com.example.pbl_sns.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pbl_sns.model.Privacy
import com.example.pbl_sns.model.User
import com.example.pbl_sns.repository.UserRepository

class UserViewModel :ViewModel(){
    private val _userLiveData: MutableLiveData<Privacy>
            = MutableLiveData()
    val userLiveData: LiveData<Privacy>
        get() = _userLiveData
    private val repo = UserRepository()

    fun getUserData() {
        repo.getData().observeForever{
            _userLiveData.postValue(it)
            Log.d("vm", _userLiveData.value.toString())
        }
    }
}