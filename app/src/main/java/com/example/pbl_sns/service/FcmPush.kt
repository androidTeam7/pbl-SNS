package com.example.pbl_sns.service

import android.util.Log
import com.example.pbl_sns.model.User
import com.example.pbl_sns.repository.PushDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

import java.io.IOException

class FcmPush {
    var JSON = "application/json; charset=utf-8".toMediaTypeOrNull()


    var url = "https://fcm.googleapis.com/fcm/send"
    var serverKey = "AIzaSyB6rmsEfUnqSpSCRLyKM7Qal_qDiUJFc30"
    var gson : Gson? = null
    private var okHttpClient: OkHttpClient? = null
    companion object{
        var instance = FcmPush()
    }
    init{
        gson = Gson()
        okHttpClient = OkHttpClient()
    }
    fun sendMessage(destinationEmail : String, title: String, message: String){
        FirebaseFirestore.getInstance().collection("users").document(destinationEmail).get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.toObject<User>()
                Log.d("userRepoo", data.toString())
                val token = data!!.token
                println("token:" + token)

                var pushDTO = PushDTO()
                pushDTO.to = token
                pushDTO.notification.title = title
                pushDTO.notification.body = message

                var body = gson?.toJson(pushDTO)!!.toRequestBody(JSON)
                var request = Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "key="+serverKey)
                    .url(url)
                    .post(body)
                    .build()

                okHttpClient?.newCall(request)?.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        println(response.body?.string())
                    }

                })
            }
    }
}
