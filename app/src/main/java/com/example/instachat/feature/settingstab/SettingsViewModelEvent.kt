package com.example.instachat.feature.settingstab

import com.example.instachat.services.models.dummyjson.User

sealed interface SettingsViewModelEvent{
    object HandleLogoutButtonClicked: SettingsViewModelEvent
    data class NavigateToUserDetailFragment(val userId: String): SettingsViewModelEvent
    data class LoadUserDetails(val user: User): SettingsViewModelEvent
}