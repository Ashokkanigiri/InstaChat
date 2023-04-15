package com.example.instachat.feature.settingstab

import com.example.instachat.services.models.dummyjson.User
import java.util.UUID

sealed interface SettingsViewModelEvent{
    object HandleLogoutButtonClicked: SettingsViewModelEvent
    data class NavigateToUserDetailFragment(val userId: String): SettingsViewModelEvent
    data class LoadUserDetails(val user: User): SettingsViewModelEvent
    data class ListenToRegistrationTokenDeleteWorkId(val uuid: UUID): SettingsViewModelEvent
    data class HandleError(val des: String): SettingsViewModelEvent
    object ShowConnectivityErrorDialog: SettingsViewModelEvent
}