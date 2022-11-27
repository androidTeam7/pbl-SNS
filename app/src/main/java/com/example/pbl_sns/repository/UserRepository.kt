package com.example.pbl_sns.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.model.Friends
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.model.Privacy
import com.example.pbl_sns.model.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class UserRepository {
    private val db = Firebase.firestore
    private val user = prefs.getString("email","-1")

    fun getAllUsers(): LiveData<ArrayList<Privacy>>{
        val mutableData = MutableLiveData<ArrayList<Privacy>>()
        val tempData = ArrayList<Privacy>()
        db.collection("users")
            .whereNotEqualTo("privacy", null).get()
            .addOnSuccessListener { querySnapshot ->
                val documents:MutableList<DocumentSnapshot> = querySnapshot.documents
                val tempPrivacyArray:ArrayList<Privacy> = ArrayList()
                Log.d("getAll 1", documents.toString())
                for (document in documents) {
                    Log.d("getAll", "${document.id} => ${document.data}")
                    Log.d("getAll 2",document.data?.get("privacy").toString())
                    val temp = document.data?.get("privacy") as HashMap<String,String>
                    val tempPrivacy:Privacy = Privacy()
                    tempPrivacy.id = temp["id"].toString()
                    tempPrivacy.name = temp["name"].toString()
                    tempPrivacy.image = temp["image"].toString()
                    tempPrivacyArray.add(tempPrivacy)
                }
                mutableData.value = tempPrivacyArray
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        return mutableData
    }

    //DB에서 유저정보 가져오기
    fun getData(email:String): LiveData<Privacy> {
        val mutableData = MutableLiveData<Privacy>()

        if(email != "-1"){
            db.collection("users").document(email).get()
                .addOnSuccessListener { documentSnapshot ->
                    Log.d("userRepoo2", email)
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

    fun getPostData(email:String): LiveData<ArrayList<Post>>{
        val mutableData = MutableLiveData<ArrayList<Post>>()

        if(email != "-1"){
            db.collection("users").document(email).get()
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

    fun getFollowerData(email:String): LiveData<ArrayList<String>>{
        val mutableData = MutableLiveData<ArrayList<String>>()

        if(email != "-1"){
            db.collection("users").document(email).get()
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
    fun getFollowingData(email:String): LiveData<ArrayList<String>>{
        val mutableData = MutableLiveData<ArrayList<String>>()

        if(email != "-1"){
            db.collection("users").document(email).get()
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

    fun setFollowerData(email:String, result:ArrayList<String>){
        if(email != "-1"){
            db.collection("users").document(email)
                .update("friends.follower", result).addOnSuccessListener {
                    Log.d("setFollowerCOMPLETE", result.toString())
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }

    fun setFollowingData(result:ArrayList<String>){
        if(user != "-1"){
            db.collection("users").document(user)
                .update("friends.following", result).addOnSuccessListener {
                    Log.d("setFollowerCOMPLETE", result.toString())
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }

    fun getUserEmail(id:String): LiveData<String>{
        val mutableData = MutableLiveData<String>()

        if(id != "-1"){
            db.collection("users")
                .whereEqualTo("id", id).get()
                .addOnSuccessListener { querySnapshot ->
                    val documents:MutableList<DocumentSnapshot> = querySnapshot.documents
                    mutableData.value = documents[0].data?.get("email") as String
                }
        }

        return mutableData
    }

}