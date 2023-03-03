package com.example.instachat.feature.userdetails

import com.example.instachat.services.models.dummyjson.User

sealed class UserDetailViewModelEvent {
    data class LoadUser(val user: User): UserDetailViewModelEvent()
    data class LoadPosts(val posts: List<UserDetailPostsModel> ): UserDetailViewModelEvent()
    object OnFollowButtonClicked: UserDetailViewModelEvent()
    object OnMessageButtonClicked: UserDetailViewModelEvent()
}