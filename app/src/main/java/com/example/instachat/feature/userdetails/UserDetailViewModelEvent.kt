package com.example.instachat.feature.userdetails

import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.RequestedForInterestModel
import com.example.instachat.services.models.dummyjson.User
import java.util.*

sealed class UserDetailViewModelEvent {
    data class LoadUser(val user: User) : UserDetailViewModelEvent()
    data class LoadPosts(val posts: List<UserDetailPostsModel>) : UserDetailViewModelEvent()
    object OnFollowButtonClicked : UserDetailViewModelEvent()
    object OnMessageButtonClicked : UserDetailViewModelEvent()
    data class OnFollowStatusRequested(
        val workId: UUID,
        val interestedUsersModel: InterestedUsersModel,
        val requestedForInterestModel: RequestedForInterestModel
    ) : UserDetailViewModelEvent()
}