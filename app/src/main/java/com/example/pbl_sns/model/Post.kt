package com.example.pbl_sns.model

data class Post(
    val id:String = "",      // 게시물 업로드한 사람의 id
    val content:String = "",
    val image:String = "",
    val replyArray:ArrayList<Reply> = ArrayList()
)