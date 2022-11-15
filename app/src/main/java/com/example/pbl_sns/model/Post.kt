package com.example.pbl_sns.model

data class Post(
    val content:String = "",
    val image:String = "",
    val replyArray:ArrayList<Reply> = ArrayList()
)