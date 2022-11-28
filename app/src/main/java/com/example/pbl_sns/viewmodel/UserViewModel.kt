package com.example.pbl_sns.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pbl_sns.model.Friends
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.model.Privacy
import com.example.pbl_sns.model.Reply
import com.example.pbl_sns.repository.AlarmDTO
import com.example.pbl_sns.repository.UserRepository

class UserViewModel :ViewModel(){
    private val _allUsersLiveData:MutableLiveData<ArrayList<Privacy>>
            = MutableLiveData()
    val allUsersLiveData : LiveData<ArrayList<Privacy>>
        get() = _allUsersLiveData

    private val _allLivePostData: MutableLiveData<ArrayList<Post>>
            = MutableLiveData()
    val allLivePostData: LiveData<ArrayList<Post>>
        get() = _allLivePostData

    private val _userLiveData: MutableLiveData<Privacy>
            = MutableLiveData()
    val userLiveData: LiveData<Privacy>
        get() = _userLiveData

    private val _userLivePostData: MutableLiveData<ArrayList<Post>>
            = MutableLiveData()
    val userLivePostData: LiveData<ArrayList<Post>>
        get() = _userLivePostData

    private val _userLiveFollowerData : MutableLiveData<ArrayList<String>>
            = MutableLiveData()
    val userLiveFollowerData: LiveData<ArrayList<String>>
        get() = _userLiveFollowerData
    private val _userLiveFollowingData : MutableLiveData<ArrayList<String>>
            = MutableLiveData()
    val userLiveFollowingData: LiveData<ArrayList<String>>
        get() = _userLiveFollowingData

    private val _userLiveAlarmData: MutableLiveData<ArrayList<AlarmDTO>>
            = MutableLiveData()
    val userLiveAlarmData: LiveData<ArrayList<AlarmDTO>>
        get() = _userLiveAlarmData

    private val _friendLiveEmailData : MutableLiveData<String>
            = MutableLiveData()
    val friendLiveEmailData: LiveData<String>
        get() = _friendLiveEmailData

    private val _postLiveReplyData: MutableLiveData<ArrayList<Reply>>
            = MutableLiveData()
    val postLiveReplyData: LiveData<ArrayList<Reply>>
        get() = _postLiveReplyData

    private val repo = UserRepository()

    fun getAllUsersData(){
        repo.getAllUsers().observeForever{
            _allUsersLiveData.postValue(it)
        }
    }

    fun getAllPost(){
        repo.getAllPost().observeForever{
            _allLivePostData.postValue(it)
        }
    }

    fun getUserData(email:String) {
        repo.getData(email).observeForever{
            _userLiveData.postValue(it)
            Log.d("vm", _userLiveData.value.toString())
        }
    }
    fun setUserData(result:Privacy){
        _userLiveData.value = result
    }

    fun getUserPost(email:String){
        repo.getPostData(email).observeForever{
            _userLivePostData.postValue(it)
            Log.d("vm", _userLivePostData.value.toString())
        }
    }

    fun getUserFollower(email:String){
        repo.getFollowerData(email).observeForever{
            _userLiveFollowerData.postValue(it)
            Log.d("vm", _userLiveFollowerData.value.toString())
        }
    }
    fun setUserFollower(email: String, result:ArrayList<String>){
        _userLiveFollowerData.value = result
        repo.setFollowerData(email, result)
    }
    fun getUserFollowing(email:String){
        repo.getFollowingData(email).observeForever{
            _userLiveFollowingData.postValue(it)
            Log.d("vm", _userLiveFollowingData.value.toString())
        }
    }

    fun setUserFollowing(result:ArrayList<String>){
        _userLiveFollowingData.value = result
        repo.setFollowingData(result)
    }

    // 유저 이메일 받아오기
    fun getUserEmail(id:String){
        repo.getUserEmail(id).observeForever{
            _friendLiveEmailData.postValue(it)
        }
    }

    fun getUserAlarmData(id: String){
        repo.getUserAlarmData(id).observeForever{
            _userLiveAlarmData.postValue(it)
        }
    }

    fun getPostReplyData(email: String, time: String){
        repo.getAllReply(email, time).observeForever{
            _postLiveReplyData.postValue(it)
        }
    }
}