package com.example.pbl_sns.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.model.Friends
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.model.Privacy
import com.example.pbl_sns.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class UserRepository {
    private val db = Firebase.firestore
    private val user = prefs.getString("email","-1")

    //DB에서 유저정보 가져오기
    fun getData(): LiveData<Privacy> {
        val mutableData = MutableLiveData<Privacy>()

        if(user != "-1"){
            db.collection("users").document(user).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObject<User>()
                    Log.d("userRepoo", data.toString())
                    mutableData.value = data!!.privacy
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }

        return mutableData
    }

    fun getPostData(): LiveData<ArrayList<Post>>{
        val mutableData = MutableLiveData<ArrayList<Post>>()

        if(user != "-1"){
            db.collection("users").document(user).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObject<User>()
                    Log.d("userRepoo", data.toString())
                    mutableData.value = data!!.postArray
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
        return mutableData
    }

    fun getFollowerData(): LiveData<ArrayList<String>>{
        val mutableData = MutableLiveData<ArrayList<String>>()

        if(user != "-1"){
            db.collection("users").document(user).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObject<User>()
                    Log.d("userRepoo", data.toString())
                    mutableData.value = data!!.friends.follower
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
        return mutableData
    }
    fun getFollowingData(): LiveData<ArrayList<String>>{
        val mutableData = MutableLiveData<ArrayList<String>>()

        if(user != "-1"){
            db.collection("users").document(user).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObject<User>()
                    Log.d("userRepoo", data.toString())
                    mutableData.value = data!!.friends.following
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
        return mutableData
    }
}