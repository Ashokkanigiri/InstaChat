package com.example.instachat.feature.settingstab

sealed interface SettingsViewModelEvent{
    object HandleLogoutButtonClicked: SettingsViewModelEvent
}