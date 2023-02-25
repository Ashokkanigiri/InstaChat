package com.example.instachat.feature.register

sealed class RegistrationViewModelEvent{
    object PopulateConnectivityErrorDialog: RegistrationViewModelEvent()
    object PopulateUserAlreadyExistsDialog: RegistrationViewModelEvent()
    object HandleRegistrationSuccess: RegistrationViewModelEvent()
}
