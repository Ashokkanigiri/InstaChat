package com.example.instachat.feature.login

import android.app.Dialog
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.instachat.utils.SingleLiveEvent
import com.example.instachat.utils.ValidationUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.processNextEventInCurrentThread
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    val errorText = ObservableField<String>()
    val email = SingleLiveEvent<String>()
    val password = SingleLiveEvent<String>()
    val event = SingleLiveEvent<LoginViewModelEvent>()
    val progressBarEvent = SingleLiveEvent<Boolean>()

    val auth = Firebase.auth

    init {
        errorText.set(null)
    }

    fun navigateToRegistrationScreen(){
        event.postValue(LoginViewModelEvent.NavigateToRegistrationScreen)
    }

    fun onLoginClicked(){
        if(ValidationUtils.isValidLoginEmail(email.value) && ValidationUtils.isValidLoginPassword(password.value)){
            login()
        }else{
            errorText.set("** Username or Password should not be empty")
        }
    }

    private fun login() {
        progressBarEvent.postValue(true)
        auth.signInWithEmailAndPassword(email.value?:"", password.value?:"")
            .addOnSuccessListener {
                event.postValue(LoginViewModelEvent.NavigateToHomeScreen)
                progressBarEvent.postValue(false)
                errorText.set(null)
            }.addOnFailureListener {
                errorText.set("** ${it.localizedMessage}")
                progressBarEvent.postValue(false)
            }
    }
}