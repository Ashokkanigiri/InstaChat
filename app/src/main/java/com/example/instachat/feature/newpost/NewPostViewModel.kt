package com.example.instachat.feature.newpost

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.utils.SingleLiveEvent
import com.example.instachat.utils.StorageUtils
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(): ViewModel() {

    val event = SingleLiveEvent<NewPostViewModelEvent>()
    val adapter = NewPostGalleryAdapter(this)
    var selectedAndCapturedList = ArrayList<String>()

    fun getImages(context: Context){
        viewModelScope.launch (Dispatchers.IO){
            val list = StorageUtils.queryImagesOnDevice(context = context)
            withContext(Dispatchers.Main){
                event.postValue(NewPostViewModelEvent.LoadImagesFromGallery(list))
            }
        }
    }
}