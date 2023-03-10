package com.example.instachat.feature.hometab

import com.example.instachat.feature.hometab.models.HomeDataModel
import com.example.instachat.feature.hometab.viewmodel.HomeViewModel
import com.example.instachat.services.models.dummyjson.User

sealed class HomeViewModelEvent{
    object ShowConnectivityErrorDialog: HomeViewModelEvent()
    object ShowActionBarForHome: HomeViewModelEvent()
    object ShowActionBarFromSearch: HomeViewModelEvent()
    data class LoadHomeData(val data: List<HomeDataModel>): HomeViewModelEvent()
    data class LoadHomeDataFromSearch(val data: List<HomeDataModel>): HomeViewModelEvent()
    data class LoadHomeUsersData(val data: List<User>): HomeViewModelEvent()
    data class NavigateFromHomeToCommentsFragment(val postId: Int): HomeViewModelEvent()
    data class NavigateToUserDetailScreen(val userId: String): HomeViewModelEvent()
}
