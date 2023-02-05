package com.example.instachat.feature.home

sealed class HomeViewModelEvent{
    object ShowConnectivityErrorDialog: HomeViewModelEvent()
}
