package com.example.pbl_sns.repository

import android.app.Notification

data class PushDTO (
    var to : String? = null,
    var notification: Notification = Notification()
) {
    data class Notification(
        var title: String? = null,
        var body: String? = null
    )
}