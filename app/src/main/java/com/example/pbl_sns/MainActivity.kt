package com.example.pbl_sns

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pbl_sns.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseUser 객체가 null인 경우
        if(Firebase.auth.currentUser == null) {
            startActivity(   // LoginActivity로 이동
                Intent(this, LoginActivity::class.java))
            finish()    // 현재 화면 지우기
        }

        // bottomNavigationView 가져오기
        bottomNavigationView = binding.bottomNavigationView

        // HomeFragment로 시작
        supportFragmentManager.beginTransaction().add(R.id.mainFrame, HomeFragment())
            .commit()

        // bottomNavigation 선택에 따라 처리
        bottomNavigationView?.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, HomeFragment()).commit()   // 화면을 HomeFragment로
                R.id.searchFragment -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, SearchFragment()).commit()  // 화면을 SearchFragment로
                R.id.profileFragment -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, ProfileFragment()).commit()   // 화면을 ProfileFragment로
            }
            true
        })
    }
}
