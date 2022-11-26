package com.example.pbl_sns.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.databinding.ActivityMainBinding
import com.example.pbl_sns.service.MyFirebaseMessagingService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    // private var bottomNavigationView: BottomNavigationView? = null
    private lateinit var binding:ActivityMainBinding
    private val db = Firebase.firestore
    private val userEmail = prefs.getString("email", "-1")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        setBottomNav()

        setToken()
    }

    private fun setToolbar(){
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        // Toolbar에 표시되는 제목의 표시 유무를 설정. false로 해야 custom한 툴바의 이름이 화면에 보인다.
        ab.setDisplayShowTitleEnabled(false)
        // 뒤로가기 버튼
        ab.setDisplayHomeAsUpEnabled(false)
        //왼쪽 버튼 아이콘 변경
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
    }

    private fun setBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainFrame) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .setupWithNavController(navController)
    }

    fun setBottomNavSetting(tag: String){
        when (tag) {
            "none" -> {
                binding.toolbar.visibility = View.GONE
                binding.bottomNavigationView.visibility = View.GONE
            }
            "all" ->{
                binding.toolbar.visibility = View.VISIBLE
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
            else -> {
                binding.toolbar.visibility = View.GONE
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "helloworld-follow", "helloworld-messaging channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "This is helloworld-messaging channel"
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setToken(){
        // 디바이스의 토큰 값 가져오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if(it.isSuccessful){
                val tokenData = it.result
                Log.d(MyFirebaseMessagingService.TAG, "FCM token: ${it.result}")
                val token = mutableMapOf<String,Any>()
                token["token"] = tokenData!!
                db.collection("users").document(userEmail).set(tokenData)
            }

        }
    }
}
