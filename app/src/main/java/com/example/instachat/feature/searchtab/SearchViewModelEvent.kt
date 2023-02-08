package com.example.instachat.feature.searchtab

import com.example.instachat.services.models.PostModelItem

sealed class SearchViewModelEvent{
    data class NavigateToHomeFragment(val postId: Int): SearchViewModelEvent()
    data class GetAllPosts(val postsList: List<PostModelItem>): SearchViewModelEvent()
}