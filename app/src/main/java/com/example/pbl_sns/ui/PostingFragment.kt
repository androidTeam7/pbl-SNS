package com.example.pbl_sns.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.liveData
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentPostingBinding
import com.example.pbl_sns.repository.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*

class PostingFragment :BaseFragment<FragmentPostingBinding>(R.layout.fragment_posting) {
    var PICK_IMAGE_FROM_ALBUM = 0 // request code
    var storage: FirebaseStorage? = null
    var photoUri : Uri? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    lateinit var uid : String

    override fun initStartView() {
        super.initStartView()

    }

    override fun initDataBinding() {
        super.initDataBinding()

        FragmentPostingBinding.inflate(layoutInflater)
        //setContentView(R.layout.fragment_posting)
        //return R.layout.fragment_posting(getActivity())
        //setContentView(binding.root)



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPostingBinding.inflate(layoutInflater)
        //setContentView(R.layout.fragment_posting)
        //setContentView(binding.root)




        //Initiate
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        uid = auth?.uid.toString()

        //Open the album
        //var photoPickerIntent = Intent(Intent.ACTION_PICK)
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        Log.d("DD","PPPPPPP")
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)


        //binding.btnAddPhoto.setOnClickListener {
        //    Log.d("DD","BTN")
        //    contentUpload()
       // }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM)
            if (resultCode == Activity.RESULT_OK) {
                // This is path to the selected image
                photoUri = data?.data
                binding.imgAddPhoto.setImageURI(photoUri)
            } else {
                //exit the addPhotoActivity if you leave the album without selecting it
                //finish()
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this)
                    ?.commit()
            }

        binding.btnAddPhoto.setOnClickListener {
            Log.d("DD","BTN")
            contentUpload()
        }
    }

    fun contentUpload() {
        //Make file name

        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"

        //var storageRef = storage?.reference?.child("images")?.child(imageFileName)
        var storageRef = storage?.reference?.child("UID")?.child(uid)?.child("images")
        Log.d("사진업로드위치",storageRef.toString())



        //storage에 파일 업로드
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener{
            Toast.makeText(context,"업로드 성공", Toast.LENGTH_LONG).show()
        }

        //Promise method --> 구글 권장 방식
        storageRef?.putFile(photoUri!!)?.continueWithTask(){ task: com.google.android.gms.tasks.Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask  storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            var contentDTO = ContentDTO()

            //Insert downloadUrl of image
            contentDTO.imageUrl = uri.toString()

            //Insert uid of user
            contentDTO.uid = auth?.currentUser?.uid

            //Insert userId
            contentDTO.userId = auth?.currentUser?.email

            //Insert explain of content
            contentDTO.explain = binding.postingEditExplain.text.toString()

            //Insert timestamp
            contentDTO.timestamp = System.currentTimeMillis()

            //파이어스토어로 올림
            Log.d("dd","firestore전")
            firestore?.collection("UID")?.document(uid)?.collection("images")?.document(imageFileName)?.set(contentDTO)


            //프로필 사진 올리는 경로
            //firestore?.collection("uid")?.document(uid)?.collection("images")?.document("profile")?.set(contentDTO)

            //setResult(Activity.RESULT_OK)
            getActivity()?.setResult(Activity.RESULT_OK)

            //finish()
            //activity?.supportFragmentManager
            //    ?.beginTransaction()
            //    ?.remove(this)
            //    ?.commit()
        }




    }
}
