package com.example.pbl_sns.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentAlarmBinding
import com.example.pbl_sns.viewmodel.UserViewModel

class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {
    lateinit var alarmAdapter: AlarmAdapter
    private val userEmail = prefs.getString("email", "-1")
    private val viewModel by lazy{
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun initDataBinding() {
        super.initDataBinding()
        (activity as MainActivity).setBottomNavSetting("")
        alarmAdapter = AlarmAdapter(ArrayList())
        binding.alarmfragmentRecyclerview.adapter = alarmAdapter

        viewModel.getUserAlarmData(userEmail)
        viewModel.userLiveAlarmData.observe(viewLifecycleOwner){
            alarmAdapter.itemList = it
            Log.d("알람",it.toString())
        }
    }
    override fun initAfterBinding() {
        super.initAfterBinding()
    }
}