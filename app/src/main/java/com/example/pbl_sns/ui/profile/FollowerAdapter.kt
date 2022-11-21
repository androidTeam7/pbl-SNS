package com.example.pbl_sns.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.databinding.ItemFollowerBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FollowerAdapter (itemList: ArrayList<String>)
    : RecyclerView.Adapter<FollowerAdapter.ViewHolder>(){
    private val db = Firebase.firestore
    private var friendsData = mutableListOf<String>()

    var itemList: ArrayList<String> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemViewBinding: ItemFollowerBinding)
        : RecyclerView.ViewHolder(itemViewBinding.root){
        val img = itemViewBinding.imgFriendProfile
        val id = itemViewBinding.tvIdFriend
        val btn = itemViewBinding.btnCheckbox
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            ItemFollowerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = itemList[position]

        friendsData = itemList

        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.btn.setOnClickListener {
            itemClickListener?.onClick(it, friendsData as ArrayList<String>, position)
        }
    }
    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, list:ArrayList<String>, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private var itemClickListener : OnItemClickListener? = null

    override fun getItemCount(): Int = itemList.size
}