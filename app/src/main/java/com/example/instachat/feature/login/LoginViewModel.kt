package com.example.instachat.feature.login

import androidx.lifecycle.ViewModel
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    val email = SingleLiveEvent<String>()
    val password = SingleLiveEvent<String>()
    val navigateToRegistrationScreenEvent = SingleLiveEvent<Boolean>()
    val navigateToHomeScreenEvent = SingleLiveEvent<Boolean>()

    val auth = Firebase.auth

    fun navigateToRegistrationScreen(){
        navigateToRegistrationScreenEvent.postValue(true)
    }

    fun onLoginClicked(){
        auth.signInWithEmailAndPassword(email.value?:"", password.value?:"")
            .addOnSuccessListener {
                navigateToHomeScreenEvent.postValue(true)
            }
    }
}