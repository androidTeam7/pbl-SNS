package com.example.watchsns

data class User (
    val email: String = "",
    val id:String = "",
    val privacy:Privacy = Privacy(),
    val friends:Friends = Friends()
)