package com.example.instachat.feature.newpost

sealed class NewPostViewModelEvent {
    data class LoadImagesFromGallery(val imagesList: List<Image>): NewPostViewModelEvent()
}