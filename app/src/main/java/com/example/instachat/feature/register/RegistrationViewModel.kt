package com.example.instachat.feature.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor() : ViewModel() {

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val navigateBackToLoginScreenEvent = SingleLiveEvent<Boolean>()

    val auth = Firebase.auth

    fun navigateBackToLoginScreen() {
        navigateBackToLoginScreenEvent.postValue(true)
    }

    fun onRegisterClicked() {
        auth.createUserWithEmailAndPassword(email.value ?: "", password.value ?: "")
            .addOnSuccessListener {
                navigateBackToLoginScreenEvent.postValue(true)
            }
    }
}