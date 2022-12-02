package com.example.watchsns
data class AlarmDTO(
    var profile:String="",
    var destinationUid: String? = null,
    var userId: String? = null,
    var uid : String? = null,
    var kind : Int? = null,   // 0: 팔로우, 1: 댓글, 2: 좋아요
    var message : String?= null,
    var timestamp : Long?=null,
) {
}