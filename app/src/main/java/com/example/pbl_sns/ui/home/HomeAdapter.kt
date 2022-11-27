package com.example.pbl_sns.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pbl_sns.R
import com.example.pbl_sns.databinding.ItemHomeBinding
import com.example.pbl_sns.databinding.ItemSearchBinding
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.model.Privacy
import com.example.pbl_sns.ui.search.SearchAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeAdapter (itemList: ArrayList<Post>)
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>(){
    private val db = Firebase.firestore
    lateinit var context:Context

    var itemList: ArrayList<Post> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemViewBinding: ItemHomeBinding)
        : RecyclerView.ViewHolder(itemViewBinding.root){
        val profileImg = itemViewBinding.imgPostProfile
        val profileId = itemViewBinding.tvIdPostProfile
        val img = itemViewBinding.imgPost
        val id = itemViewBinding.tvIdPost
        val content = itemViewBinding.tvContentPost
        val btnAllReply = itemViewBinding.btnViewAllReply
        val time = itemViewBinding.tvTime
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemHomeBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //프로필 이미지
        if(itemList[position].profile.isEmpty())
            holder.profileImg.setImageResource(R.drawable.user)
        else{
            Glide.with(context).load(itemList[position].profile).into(holder.profileImg)
        }
        // 아이디
        holder.profileId.text = itemList[position].id
        // 게시물 사진
        Glide.with(context).load(itemList[position].image).into(holder.img)
        holder.id.text = itemList[position].id
        holder.content.text = itemList[position].content
        Log.d("잘됐나요",itemList.toString())

        holder.time.text = itemList[position].date

        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.btnAllReply.setOnClickListener {
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