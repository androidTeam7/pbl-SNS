package com.example.pbl_sns.model

data class Post(
    var profile:String="",
    var email: String = "",
    var id:String = "",      // 게시물 업로드한 사람의 id
    var content:String = "",
    var image:String = "",
    var date:String = "",
    var time:Long=0,
    val replyArray:ArrayList<Reply> = ArrayList(),
    var likeCount: Int? = null,
    var likes: MutableMap<String, Boolean> = HashMap()
)