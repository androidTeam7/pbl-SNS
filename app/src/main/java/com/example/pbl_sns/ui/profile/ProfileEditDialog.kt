package com.example.pbl_sns.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.example.pbl_sns.MyApplication
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseDialogFragment
import com.example.pbl_sns.databinding.DialogProfileEditBinding
import com.example.pbl_sns.model.Privacy
import com.example.pbl_sns.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    private val viewModel by lazy {
        ViewModelProvider(requireParentFragment())[UserViewModel::class.java]
    }

    override fun initDataBinding() {
        super.initDataBinding()

        viewModel.getUserData(userId)
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.editTvIdProfile.setText(it.id)
            editItImage = it.image
            binding.editTvInfoProfile.setText(it.info)
            binding.editTvNameProfile.setText(it.name)
        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        binding.btnCloseEdit.setOnClickListener {
            dismiss()
        }

        binding.btnCompleteEdit.setOnClickListener {
            editItId = binding.editTvIdProfile.text.toString()
            editItInfo = binding.editTvInfoProfile.text.toString()
            editItName = binding.editTvNameProfile.text.toString()

            if(checkData()){
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

                prefs.removeAll()
                prefs.setString("id", data["id"]!!)

                viewModel.setUserData(dataToPrivacy)
                setFragmentResult("editPrivacy", bundleOf("resultPrivacy" to isEdit))
                dismiss()

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