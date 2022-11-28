package com.example.pbl_sns.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.databinding.ItemSearchBinding
import com.example.pbl_sns.model.Privacy
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchAdapter  (itemList: ArrayList<Privacy>)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>(){
    private val db = Firebase.firestore
    private var friendsData = mutableListOf<String>()

    var itemList: ArrayList<Privacy> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemViewBinding: ItemSearchBinding)
        : RecyclerView.ViewHolder(itemViewBinding.root){
        val layout = itemViewBinding.constraintLayout
        val img = itemViewBinding.imgSearchProfile
        val id = itemViewBinding.tvIdSearch
        val name = itemViewBinding.tvNameSearch
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = itemList[position].id
        holder.name.text = itemList[position].name

        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.layout.setOnClickListener {
            itemClickListener?.onClick(position)
        }
    }
    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }


    // (4) setItemClickListener로 설정한 함수 실행
    private var itemClickListener : OnItemClickListener? = null

    override fun getItemCount(): Int = itemList.size
}