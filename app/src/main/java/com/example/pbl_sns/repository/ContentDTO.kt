package com.example.pbl_sns.repository

data class ContentDTO(
    var explain: String? = null,
    var imageUrl: String? = null,
    var uid: String? = null,
    var userId: String? = null,
    var timestamp: Long? = null,
    var likeCount: Int? = null,
    var likes: MutableMap<String, Boolean> = HashMap()
) {
    data class Comment(
        var uid: String? = null,
        var userId: String? = null,
        var comment: String? = null,
        var timestamp: Long? = null
    )
}


