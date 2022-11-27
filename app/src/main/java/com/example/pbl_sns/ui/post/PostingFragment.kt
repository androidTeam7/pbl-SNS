package com.example.pbl_sns.ui.post

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentPostingBinding
import com.example.pbl_sns.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*

class PostingFragment :BaseFragment<FragmentPostingBinding>(R.layout.fragment_posting) {
    var storage: FirebaseStorage? = null
    var photoUri : Uri? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    lateinit var uid : String

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    if(result.data != null){
                        photoUri = result.data!!.data
                        Glide.with(requireContext()).load(photoUri).into(binding.imgAddPhoto)
                        Log.d("imageUrl",photoUri.toString())
                    }
                }
            }
        }

    override fun initStartView() {
        super.initStartView()

        (activity as MainActivity).setBottomNavSetting("")

        //Initiate
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        uid = auth?.uid.toString()
        Log.d("안들어가져","")
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.imgAddPhoto.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            galleryResult.launch(photoPickerIntent)
        }
        binding.btnAddPhoto.setOnClickListener {
            contentUpload()
        }
    }

    private fun contentUpload() {
        if(photoUri!=null){
            var storageRef = storage?.reference?.child("UID")?.child(uid)?.child("images")

            //storage에 파일 업로드
            storageRef?.putFile(photoUri!!)

            //Promise method --> 구글 권장 방식
            storageRef?.putFile(photoUri!!)?.continueWithTask(){
                return@continueWithTask  storageRef.downloadUrl
            }?.addOnSuccessListener { uri ->

                val post = hashMapOf(
                    "id" to prefs.getString("id","-1"),
                    "content" to binding.postingEditExplain.text.toString(),
                    "image" to uri.toString(),
                    "date" to today(),
                    "time" to System.currentTimeMillis()
                )
                Firebase.firestore.collection("users")?.document(prefs.getString("email","-1"))
                    .update("postArray",FieldValue.arrayUnion(post)).addOnSuccessListener {
                        Toast.makeText(context,"업로드 성공", Toast.LENGTH_LONG).show()
                    }
            }
        }else{
            Toast.makeText(context,"사진이 선택되지 않았습니다.", Toast.LENGTH_LONG).show()
        }
    }

    private fun today():String{
        val now = Date()
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
        return dateFormat.format(now).toString()
    }
}
