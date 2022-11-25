package com.example.pbl_sns.ui.profile


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pbl_sns.R
import com.example.pbl_sns.databinding.ItemPostBinding
import com.example.pbl_sns.model.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileAdapter(itemList: List<Post>)
    : RecyclerView.Adapter<ProfileAdapter.ViewHolder>(){
    private val db = Firebase.firestore
    lateinit var context:Context

    var itemList: List<Post> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemViewBinding: ItemPostBinding)
        :RecyclerView.ViewHolder(itemViewBinding.root){
        val img = itemViewBinding.imgPostProfile
        val betweenSpace = itemViewBinding.viewBetween
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(itemList[position].image.isEmpty())
            holder.img.setImageResource(R.drawable.loading)
        else{
            Glide.with(context).load(itemList[position].image).into(holder.img)
        }

        if((position+1)%3 == 0)
            holder.betweenSpace.visibility = View.GONE
        else
            holder.betweenSpace.visibility = View.VISIBLE




        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.img.setOnClickListener {
            itemClickListener?.onClick(it, position)
        }
    }
    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private var itemClickListener : OnItemClickListener? = null

    override fun getItemCount(): Int = itemList.size
}