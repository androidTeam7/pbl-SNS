package com.example.pbl_sns.model

data class User (
    val email: String = "",
    val id:String = "",
    val privacy:Privacy = Privacy(),
    val postArray:ArrayList<Post> = ArrayList()
)