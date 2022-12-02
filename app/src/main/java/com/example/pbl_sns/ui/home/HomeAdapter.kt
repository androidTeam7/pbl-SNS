package com.example.pbl_sns.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.databinding.ItemHomeBinding
import com.example.pbl_sns.model.Post
import com.example.pbl_sns.repository.AlarmDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class HomeAdapter (itemList: ArrayList<Post>)
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>(){
    private val db = Firebase.firestore
    var firestore : FirebaseFirestore? = null
    val userEmail = prefs.getString("email","-1")
    lateinit var context:Context
    var likePostData:HashMap<String,ArrayList<String>> = HashMap<String,ArrayList<String>>()

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
        val like = itemViewBinding.tvLikecounter
        val likecount = itemViewBinding.likeImageview
        val btnReply = itemViewBinding.btnReply
        val editTvReply = itemViewBinding.editTvReply
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

        holder.time.text = itemList[position].date

        var tempData = mutableListOf<String>()

        if(likePostData[itemList[position].time.toString()] != null){
            for(i in 0 until likePostData[itemList[position].time.toString()]!!.size){
                tempData.add(likePostData[itemList[position].time.toString()]!![i])
            }
            Log.d("temppp0",tempData.toString())
        }

        holder.like.text = "Like${tempData!!.size}"

        Log.d("temppp1",tempData.toString())
        holder.likecount.isChecked = likePostData[itemList[position].time.toString()]?.contains(userEmail) == true

        holder.likecount.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tempData.add(userEmail)
                holder.like.text = "Like${tempData!!.size}"
                Log.d("temppp2",tempData.toString())
                likeAlarm(itemList[position].email);
            } else {
                tempData.remove(userEmail)
                holder.like.text = "Like${tempData!!.size}"
            }

            db.collection("users").document(userEmail).collection("postArray").document(itemList[position].time.toString())
                .update("like", tempData)
        }

        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.btnAllReply.setOnClickListener {
            val status = "btnAllReply"
            itemClickListener?.onClick(position, status, itemList[position], "")
        }
        holder.btnReply.setOnClickListener{
            val status="btnReply"
            itemClickListener?.onClick(position, status, itemList[position], holder.editTvReply.text.toString())
        }
    }
    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(position: Int, status: String, post: Post, reply: String)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private var itemClickListener : OnItemClickListener? = null

    override fun getItemCount(): Int = itemList.size

    private fun likeAlarm(destinationUid: String){
        var alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = userEmail
        alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        alarmDTO.kind = 2
        alarmDTO.timestamp = System.currentTimeMillis()
        alarmDTO.message = "님이 회원님의 게시물에 좋아요를 눌렀습니다."
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)
    }
}