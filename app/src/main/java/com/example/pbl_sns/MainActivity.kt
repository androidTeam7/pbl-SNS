package com.example.pbl_sns

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        supportFragmentManager.beginTransaction().add(R.id.mainFrame, HomeFragment())
            .commit()

        bottomNavigationView?.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, HomeFragment()).commit()
                R.id.searchFragment -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, SearchFragment()).commit()
                R.id.profileFragment -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrame, ProfileFragment()).commit()
            }
            true
        })
    }
}
