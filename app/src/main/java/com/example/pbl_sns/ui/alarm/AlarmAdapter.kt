package com.example.pbl_sns.ui.alarm

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pbl_sns.R
import com.example.pbl_sns.databinding.ItemAlarmBinding
import com.example.pbl_sns.repository.AlarmDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AlarmAdapter (itemList: ArrayList<AlarmDTO>)
    : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    private val db = Firebase.firestore
    lateinit var context: Context

    var itemList: ArrayList<AlarmDTO> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemViewBinding: ItemAlarmBinding)
        : RecyclerView.ViewHolder(itemViewBinding.root) {
        val profileImg = itemViewBinding.profileImg
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
        if(itemList[position].profile.isEmpty())
            holder.profileImg.setImageResource(R.drawable.user)

        holder.id.text = itemList[position].userId

        when(itemList[position].kind){
            0 -> {
                holder.content.text = itemList[position].message
            }

        }
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
}