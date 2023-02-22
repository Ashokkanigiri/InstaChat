package com.example.instachat.feature.newpostDetail

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPostDetailViewModel @Inject constructor(): ViewModel() {

    var selectedUriList : List<Uri>? = null
    val adapter = NewPostDetailAdapter()
}