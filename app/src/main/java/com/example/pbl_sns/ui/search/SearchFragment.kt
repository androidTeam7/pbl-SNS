package com.example.pbl_sns.ui.search

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.example.pbl_sns.MyApplication.Companion.prefs
import com.example.pbl_sns.R
import com.example.pbl_sns.base.BaseFragment
import com.example.pbl_sns.databinding.FragmentSearchBinding
import com.example.pbl_sns.model.Privacy
import com.example.pbl_sns.viewmodel.UserViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class SearchFragment: BaseFragment<FragmentSearchBinding>(R.layout.fragment_search){
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var userFollowingList:ArrayList<String>
    private val userEmail = prefs.getString("email","-1")

    private val viewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun initDataBinding() {
        super.initDataBinding()

        searchAdapter = SearchAdapter(ArrayList())
        binding.searchRecyclerview.adapter = searchAdapter
        viewModel.getAllUsersData()

        viewModel.getUserFollowing(userEmail)
        viewModel.userLiveFollowingData.observe(viewLifecycleOwner){
            userFollowingList = it
        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        setFragmentResultListener("addFollowingFPD") { _, bundle ->
            val result = bundle.getString("friendEmail")
            if(result != "-1") userFollowingList.add(result!!)
            viewModel.setUserFollowing(userFollowingList)
        }
        setFragmentResultListener("deleteFollowingFPD") { _, bundle ->
            val result = bundle.getString("friendEmail")
            userFollowingList.remove(result)
            viewModel.setUserFollowing(userFollowingList)
        }

        // 검색 기능
        binding.editTvSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searchString = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchItemList:ArrayList<Privacy> = ArrayList()
                val searchString = binding.editTvSearch.text

                if(viewModel.allUsersLiveData.value != null){
                    for (item in viewModel.allUsersLiveData.value!!) {
                        if (item.id.contains(searchString)) {
                            searchItemList.add(item)
                        }
                    }
                    searchAdapter.itemList = searchItemList
                }

                if (binding.editTvSearch.text.isEmpty()) {
                    searchItemList.clear()
                }
            }
        })

        // adapter에서 눌렸을때 친구 프로필 뜸
        searchAdapter.setItemClickListener(object: SearchAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                val id = searchAdapter.itemList[position].id
                getUserEmail(id)
            }
        })
    }


    private fun getUserEmail(id:String) {
        val db = Firebase.firestore
        var email = "-1"
        db.collection("users")
            .whereEqualTo("id", id).get()
            .addOnSuccessListener { querySnapshot ->
                val documents:MutableList<DocumentSnapshot> = querySnapshot.documents
                email = documents[0].data?.get("email") as String
            }.addOnSuccessListener {
                FriendProfileDialog(email).show(parentFragmentManager,"FriendProfileDialog")
            }
    }
}