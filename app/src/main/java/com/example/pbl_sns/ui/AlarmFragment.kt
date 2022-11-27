package com.example.pbl_sns.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentAlarmBinding

class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm) {
    var alarmAdapter: AlarmAdapter? = null

    override fun initDataBinding() {
        super.initDataBinding()
        (activity as MainActivity).setBottomNavSetting("all")
        alarmAdapter = AlarmAdapter()
        binding.alarmfragmentRecyclerview.adapter = alarmAdapter
        binding.alarmfragmentRecyclerview.layoutManager = LinearLayoutManager(context)
    }
    override fun initAfterBinding() {
        super.initAfterBinding()
    }
}