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

    // 나+친구 포스팅 가져오기
    fun getAllPost(): LiveData<ArrayList<Post>> {
        val mutableData = MutableLiveData<ArrayList<Post>>()
        val tempData = ArrayList<Post>()

        // 내 이메일과 친구들 이메일 넣기
        var followingData: ArrayList<String> = ArrayList<String>()
        if(followingMutableData.value != null)
            followingData = followingMutableData.value!!
        followingData.add(user)

        var tempPostArray: ArrayList<Post> = ArrayList()
        Log.d("다큐먼트2", followingData.toString())
        val comparator : Comparator<Post> = compareBy { -it.time }
        if (followingData != null) {
            Log.d("다큐먼트3", "")
            for (i in 0 until followingData.size) {
                db.collection("users").document(followingData[i]).get()
                    .addOnSuccessListener {
                        Log.d("다큐먼트1", it.data.toString())
                        // 프로필 사진 구하는 로직
                        val tempProfile = it.data?.get("privacy") as HashMap<String, String>
                        // 포스트 정보 구하는 로직
                        val data = it.toObject<User>()
                        val mTempPost: ArrayList<Post> = data!!.postArray
                        for (i in 0 until mTempPost.size) {
                            val tempPost: Post = Post()
                            tempPost.profile = tempProfile["image"].toString()
                            tempPost.id = tempProfile["id"].toString()
                            tempPost.content = mTempPost[i].content
                            tempPost.image = mTempPost[i].image
                            tempPost.time = mTempPost[i].time
                            tempPost.date = mTempPost[i].date
                            tempPostArray.add(tempPost)
                            Log.d("잘됐나${i}", tempPost.content.toString())
                        }
                        mutableData.value = tempPostArray
                    }.addOnSuccessListener {
                        mutableData.value?.sortWith(comparator)
                        Log.d("잘됐나아아아아아${i}", mutableData.value.toString())
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
            }

        }
        return mutableData
    }
            /*
        db.collection("users")
            .whereNotEqualTo("postArray", null).get()
            .addOnSuccessListener { querySnapshot ->
                val documents:MutableList<DocumentSnapshot> = querySnapshot.documents
                val tempPostArray:ArrayList<Post> = ArrayList()
                Log.d("getAll 1", documents.toString())
                for (document in documents) {
                    // 프로필 사진 구하는 로직
                    val tempProfile = document.data?.get("privacy") as HashMap<String,String>
                    val tempPost:Post = Post()
                    tempPost.profile = tempProfile["image"].toString()

                    // 포스트 정보 구하는 로직
                    val mTempPost:ArrayList<HashMap<String,String>> = document.data?.get("postArray") as ArrayList<HashMap<String, String>>
                    for(i in 0 until mTempPost.size){
                        tempPost.profile = tempProfile["image"].toString()
                        tempPost.id = tempProfile["id"].toString()
                        tempPost.content = mTempPost[i]["content"].toString()
                        tempPost.image = mTempPost[i]["image"].toString()
                        tempPost.time = mTempPost[i]["time"].toString()
                        tempPostArray.add(tempPost)
                    }
                    mutableData.value = tempPostArray
                    Log.d("잘됐나",mutableData.value.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        return mutableData
            */


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