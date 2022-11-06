package com.example.pbl_sns

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

      
        bottomNavigationView = binding.bottomNavigationView

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
