package com.example.pbl_sns.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.databinding.ItemFollowingBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.coroutineContext

class FollowingAdapter(user:Boolean, itemList: ArrayList<String>)
    : RecyclerView.Adapter<FollowingAdapter.ViewHolder>(){
    private val db = Firebase.firestore
    var friendsData = mutableListOf<String>()
    lateinit var context:Context
    private val isUser:Boolean = user

    var itemList: ArrayList<String> = itemList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemViewBinding: ItemFollowingBinding)
        : RecyclerView.ViewHolder(itemViewBinding.root){
        val img = itemViewBinding.imgFriendProfile
        val id = itemViewBinding.tvIdFriend
        val btn = itemViewBinding.btnCheckbox
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemFollowingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = itemList[position]

        if(isUser){
            friendsData = itemList

            // 팔로우 팔로잉
            holder.btn.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    friendsData.add(holder.id.text.toString())
                    holder.btn.text = "팔로잉"
                    holder.btn.setTextColor(ContextCompat.getColor(context, R.color.black))
                } else {
                    friendsData.remove(holder.id.text.toString())
                    holder.btn.text = "팔로우"
                    holder.btn.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                db.collection("users").document(MyApplication.prefs.getString("email", "-1"))
                    .update("friends.following", friendsData)
            }
        }
        else{
            holder.btn.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = itemList.size
}