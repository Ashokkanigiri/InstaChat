package com.example.instachat.feature.newpostDetail

import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.instachat.services.models.PostModelItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class NewPostDetailViewModel @Inject constructor() : ViewModel() {
    val adapter = NewPostDetailAdapter()

    val postButtonEnabledStatus = ObservableField<Boolean>()

    var selectedImagesList : List<String>? = null

    val newPost = PostModelItem(
        id = Random().nextInt(1),
        userId = Firebase.auth.currentUser?.uid ?: "",
        tags = emptyList(),
        title = "",
        body = "",
        postImageUrl = "",
        likesCount = 0,
        reactions = 0
    )

    fun onPostButtonClicked(postModelItem: PostModelItem){
        postModelItem.apply {
            this.postImageUrl = selectedImagesList?.get(0)?:""
        }

    }
}