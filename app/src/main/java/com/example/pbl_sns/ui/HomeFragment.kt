package com.example.pbl_sns.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentHomeBinding
import com.example.pbl_sns.ui.profile.ProfileFragment
import com.example.pbl_sns.ui.search.SearchFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    override fun initDataBinding() {
        super.initDataBinding()

        (activity as MainActivity).setBottomNavSetting("all")
    }


}