package com.example.pbl_sns.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pbl_sns.databinding.ItemReplyBinding
import com.example.pbl_sns.model.Reply
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReplyAdapter(itemList: List<Reply>)
    : RecyclerView.Adapter<ReplyAdapter.ViewHolder>(){
        private val db = Firebase.firestore
    var itemList: List<Reply> = itemList
        set(value){
            field = value
            notifyDataSetChanged()
        }


    inner class ViewHolder(itemViewBinding: ItemReplyBinding)
        :RecyclerView.ViewHolder(itemViewBinding.root){
            val profileImg = itemViewBinding.profileImg
            val tvUserId = itemViewBinding.tvUserId
            val tvReply = itemViewBinding.tvReply
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(

            ItemReplyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        if(itemList[position].id.isEmpty())
            holder.tvUserId.text = "none"
        else{
            holder.tvUserId.text = itemList[position].id.toString()
            holder.tvReply.text = itemList[position].reply.toString()
        }

    }

    override fun getItemCount(): Int = itemList.size
}