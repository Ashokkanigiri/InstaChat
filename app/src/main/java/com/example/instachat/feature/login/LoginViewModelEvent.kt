package com.example.instachat.feature.login

sealed class LoginViewModelEvent {

    object NavigateToRegistrationScreen: LoginViewModelEvent()
    object NavigateToHomeScreen: LoginViewModelEvent()
}