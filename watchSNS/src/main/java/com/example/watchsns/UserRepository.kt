package com.example.watchsns

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.watchsns.MyApplication.Companion.prefs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class UserRepository {
    private val db = Firebase.firestore
    private val user = prefs.getString("email","-1")

    fun getFollowerData(email:String): LiveData<ArrayList<String>>{
        val mutableData = MutableLiveData<ArrayList<String>>()

        if(email != "-1"){
            db.collection("users").document(email).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObject<User>()
                    Log.d("userRepoooo", data.toString())
                    mutableData.value = data!!.friends.follower
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
        return mutableData
    }

    private val followingMutableData = MutableLiveData<ArrayList<String>>()
    fun getFollowingData(email:String): LiveData<ArrayList<String>>{
        if(email != "-1"){
            db.collection("users").document(email).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObject<User>()
                    Log.d("userRepoo", data.toString())
                    followingMutableData.value = data!!.friends.following
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
        return followingMutableData
    }
}