package com.example.pbl_sns.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
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
        val comparator : Comparator<Post> = compareBy { -it.time }
        if (followingData != null) {
            for (i in 0 until followingData.size) {
                db.collection("users").document(followingData[i]).get()
                    .addOnSuccessListener {
                        // 프로필 사진 구하는 로직
                        val tempProfile = it.data?.get("privacy") as HashMap<String, String>
                        // 포스트 정보 구하는 로직
                        val data = it.toObject<User>()
                        val mTempPost: ArrayList<Post> = data!!.postArray
                        for (i in 0 until mTempPost.size) {
                            val tempPost: Post = Post()
                            tempPost.email = mTempPost[i].email
                            tempPost.profile = tempProfile["image"].toString()
                            tempPost.id = tempProfile["id"].toString()
                            tempPost.content = mTempPost[i].content
                            tempPost.image = mTempPost[i].image
                            tempPost.time = mTempPost[i].time
                            tempPost.date = mTempPost[i].date
                            tempPostArray.add(tempPost)
                        }
                        mutableData.value = tempPostArray
                    }.addOnSuccessListener {
                        mutableData.value?.sortWith(comparator)
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
            }

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
        val comparator : Comparator<Post> = compareBy { -it.time }
        if(email != "-1"){
            db.collection("users").document(email).get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObject<User>()
                    Log.d("userRepoo", data.toString())
                    mutableData.value = data!!.postArray
                }.addOnSuccessListener {
                    mutableData.value?.sortWith(comparator)
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

    fun getUserAlarmData(id: String): LiveData<ArrayList<AlarmDTO>>{
        val mutableData = MutableLiveData<ArrayList<AlarmDTO>>()

        if(id != "-1"){
            db.collection("alarms").whereEqualTo("destinationUid", id)
                .addSnapshotListener { querySnapshot, error ->
                    Log.d("알람 query",querySnapshot.toString())

                    if(querySnapshot == null) return@addSnapshotListener
                    else Log.d("알람 query.docu",querySnapshot!!.documents.toString())
                    val tempAlarmArray = ArrayList<AlarmDTO>()
                    for(snapshot in querySnapshot.documents){
                        Log.d("알람 snap",snapshot.data.toString())
                        val tempAlarm:AlarmDTO = AlarmDTO()
                        tempAlarm.uid = snapshot.data?.get("uid").toString()
                        tempAlarm.kind = (snapshot.data?.get("kind") as Long).toInt()
                        tempAlarm.userId = snapshot.data?.get("userId").toString()
                        tempAlarm.message = snapshot.data?.get("message").toString()
                        tempAlarm.destinationUid = snapshot.data?.get("destinationUid").toString()
                        tempAlarm.profile = snapshot.data?.get("profile").toString()
                        tempAlarm.timestamp = snapshot.data?.get("timestamp") as Long
                        Log.d("알람 snap.uid",snapshot.data?.get("uid").toString())
                        tempAlarmArray.add(tempAlarm)
                    }
                    mutableData.value = tempAlarmArray
                }
            Log.d("알람","if문")
        } else{
            Log.d("알람","else문")
        }
        return mutableData
    }


    fun getAllReply(email:String, time: String): MutableLiveData<ArrayList<Reply>> {
        val mutableData = MutableLiveData<ArrayList<Reply>>()

        if(email != "-1"){
            db.collection("users").document(email).collection("postArray")
                .document(time).get().addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObject<PostDetail>()
                    Log.d("userRepo PostDetail:", data.toString())
                    mutableData.value = data!!.reply
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
        return mutableData
    }


    fun getLikePost(email:String): LiveData<HashMap<String,ArrayList<String>>>{
        val mutableData = MutableLiveData<HashMap<String,ArrayList<String>>>()
        db.collection("users").document(email).collection("postArray")
            .whereNotEqualTo("like",null).get()
                .addOnSuccessListener {
                    val documents:MutableList<DocumentSnapshot> = it.documents
                    val tempLikeArray:HashMap<String,ArrayList<String>> = HashMap()
                    for (document in documents) {
                        val tempArray:ArrayList<String> = document.data?.get("like") as ArrayList<String>
                        tempLikeArray[document.id] = tempArray
                    }
                    mutableData.value = tempLikeArray
                }
        return mutableData
    }
}