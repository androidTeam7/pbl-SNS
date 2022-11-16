package com.example.pbl_sns

import android.app.Application

class MyApplication: Application() {
    companion object {
        lateinit var prefs: MySharedPreferences
    }

    override fun onCreate() {
        prefs = MySharedPreferences(applicationContext)
        super.onCreate()
        MyApplication.prefs.removeAll()
    }
}