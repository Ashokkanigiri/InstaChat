package com.example.instachat.feature.newpostDetail

import com.example.instachat.feature.newpost.NewPostViewModelEvent
import com.example.instachat.services.models.PostModelItem
import java.util.UUID

sealed class NewPostDetailViewModelEvent {
    data class IsPostAdded(val workId: UUID): NewPostDetailViewModelEvent()
    object ShouldShowNetworkConnectionDialog: NewPostDetailViewModelEvent()
}