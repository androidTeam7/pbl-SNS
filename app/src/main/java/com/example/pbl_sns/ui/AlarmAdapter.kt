package com.example.pbl_sns.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pbl_sns.R
import com.example.pbl_sns.databinding.ItemAlarmBinding
import com.example.pbl_sns.databinding.ItemFollowerBinding
import com.example.pbl_sns.repository.AlarmDTO
import com.example.pbl_sns.ui.profile.FollowerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    var alarmDTOList: ArrayList<AlarmDTO> = arrayListOf()

    init{
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        FirebaseFirestore.getInstance().collection("alarms").whereEqualTo("destinationUid", uid)
            .addSnapshotListener { querySnapshot, error ->
                alarmDTOList.clear()
                if(querySnapshot == null) return@addSnapshotListener

                for(snapshot in querySnapshot.documents){
                    alarmDTOList.add(snapshot.toObject(AlarmDTO::class.java)!!)
                }
                notifyDataSetChanged()
            }
    }

    inner class ViewHolder(itemViewBinding: ItemAlarmBinding)
        : RecyclerView.ViewHolder(itemViewBinding.root) {
        val img = itemViewBinding.profileImg
        val id = itemViewBinding.tvEmail
        val content = itemViewBinding.tvContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAlarmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(alarmDTOList[position].kind){
            0 -> {
                holder.id.text = alarmDTOList[position].uid
                holder.content.text = (R.string.followAlarm.toString())
            }

        }



    }
    override fun getItemCount(): Int {
        return alarmDTOList.size
    }
}