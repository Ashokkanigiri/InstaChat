package com.example.instachat.feature.settingstab

sealed interface SettingsViewModelEvent{
    object HandleLogoutButtonClicked: SettingsViewModelEvent
    data class NavigateToUserDetailFragment(val userId: String): SettingsViewModelEvent
}