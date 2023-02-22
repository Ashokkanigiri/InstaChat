package com.example.instachat.feature.newpostDetail

import com.example.instachat.feature.newpost.NewPostViewModelEvent
import com.example.instachat.services.models.PostModelItem

sealed class NewPostDetailViewModelEvent {
    data class IsPostAddedSuccessFully (val postModelItem: PostModelItem): NewPostDetailViewModelEvent()
}