package com.example.pbl_sns.ui.profile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogProfileEditBinding
import com.example.pbl_sns.model.Privacy
import com.example.pbl_sns.repository.ContentDTO
import com.example.pbl_sns.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*

class ProfileEditDialog:BaseDialogFragment<DialogProfileEditBinding>(R.layout.dialog_profile_edit) {
    private val db = Firebase.firestore
    private var result : Privacy = Privacy()
    private lateinit var editItId:String
    private lateinit var editItImage:String
    private lateinit var editItInfo:String
    private lateinit var editItName:String
    private var isEdit:Boolean = false
    private var emptyIs:String = ""
    private val userId = prefs.getString("email","-1")

    var storage: FirebaseStorage? = null
    var photoUri : Uri? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    lateinit var uid : String
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    if(result.data != null){
                        photoUri = result.data!!.data
                        Glide.with(requireContext()).load(photoUri).into(binding.imgProfileEdit)
                        Log.d("imageUrl",photoUri.toString())
                    }
                }
            }
        }

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment())[UserViewModel::class.java]
    }

    override fun initStartView() {
        super.initStartView()

        //Initiate
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        uid = auth?.uid.toString()
    }

    override fun initDataBinding() {
        super.initDataBinding()

        viewModel.getUserData(userId)
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.editTvIdProfile.setText(it.id)
            editItImage = it.image.toString()
            binding.editTvInfoProfile.setText(it.info)
            binding.editTvNameProfile.setText(it.name)
            if(it.image != ""){
                Glide.with(requireContext()).load(it.image).into(binding.imgProfileEdit)
                Log.d("imageUrl",it.image)
            }

        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.btnCloseEdit.setOnClickListener {
            dismiss()
        }

        binding.btnEditProfilePhoto.setOnClickListener {
            //Open the album
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            galleryResult.launch(photoPickerIntent)
        }

        binding.btnCompleteEdit.setOnClickListener {

            editItId = binding.editTvIdProfile.text.toString()
            editItInfo = binding.editTvInfoProfile.text.toString()
            editItName = binding.editTvNameProfile.text.toString()

            if(checkData()){
                val storageRef = storage?.reference?.child("UID")?.child(uid)?.child("profile")
                //storage에 파일 업로드
                storageRef?.putFile(photoUri!!)

                //Promise method --> 구글 권장 방식
                storageRef?.putFile(photoUri!!)?.continueWithTask(){
                    return@continueWithTask  storageRef.downloadUrl
                }?.addOnSuccessListener { uri ->
                    editItImage = uri.toString()

                    val data =  hashMapOf(
                        "id" to editItId,
                        "image" to editItImage,
                        "info" to editItInfo,
                        "name" to editItName,
                    )
                    var dataToPrivacy:Privacy = Privacy()
                    dataToPrivacy.id = data["id"].toString()
                    dataToPrivacy.image = data["image"].toString()
                    dataToPrivacy.info = data["info"].toString()
                    dataToPrivacy.name = data["name"].toString()
                    Log.d("dataa", data.toString())

                    // id 수정
                    db.collection("users").document(userId)
                        .update("id", data["id"])

                    // private 필드 수정
                    db.collection("users").document(userId)
                        .update("privacy", data)

                    val email = prefs.getString("email","-1")
                    prefs.removeAll()
                    prefs.setString("id", data["id"]!!)
                    prefs.setString("email",email)
                    prefs.setString("profile", editItImage)

                    viewModel.setUserData(dataToPrivacy)
                    setFragmentResult("editPrivacy", bundleOf("resultPrivacy" to isEdit))
                    dismiss()
                }
            } else{
                Toast.makeText(context, "${emptyIs}가 입력되지 않았습니다.", Toast.LENGTH_LONG)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // dialog full Screen code
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }


    private fun checkData():Boolean{
        if(editItId.isEmpty()){
            emptyIs = "아이디"
            isEdit = false
        } else if(editItName.isEmpty()){
            emptyIs = "이름"
            isEdit = false
        } else
            isEdit = true

        return isEdit
    }

}