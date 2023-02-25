package com.example.instachat.feature.register

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.SingleLiveEvent
import com.example.instachat.utils.ValidationUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val syncRepository: SyncRepository,
    val connectivityService: ConnectivityService
) : ViewModel() {

    val errorText = ObservableField<String>()
    val reEnterPassword = ObservableField<String>()
    val navigateBackToLoginScreenEvent = SingleLiveEvent<Boolean>()
    val passwordDoestMatchError = ObservableField<Boolean>()
    var user = User()
    val auth = Firebase.auth

    fun navigateBackToLoginScreen() {
        navigateBackToLoginScreenEvent.postValue(true)
    }

    init {
        errorText.set("")
    }

    fun onRegisterClicked() {
        if (connectivityService.hasActiveNetwork()) {
            ValidationUtils.shouldRegisterUser(user, reEnterPassword.get() ?: "").let {
                if (it) {
                    passwordDoestMatchError.set(false)
                    errorText.set("")
                    createAndSyncNewUser(user)
                } else {
                    handleErrors()
                }
            }
        } else {

        }
    }

    fun createAndSyncNewUser(user: User) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                viewModelScope.launch(Dispatchers.IO) {
                    if (connectivityService.hasActiveNetwork()) syncRepository.addNewUser(user.apply {
                        id = it?.user?.uid ?: ""
                        password = ""
                    })
                }
                navigateBackToLoginScreenEvent.postValue(true)
            }
    }

    fun handleErrors() {
        errorText.set("")
        passwordDoestMatchError.set(false)
        ValidationUtils.isValidEmail(user.email).let {
            if (!it) {
                errorText.set(errorText.get() + "\n** Please enter valid email")
            }
        }
        ValidationUtils.isValidPassword(user.password).let {
            if (!it) {
                errorText.set(
                    errorText.get() + "\n** Password should contain a small case, a capital case, a digit & a special character"
                )
            } else {
                if ((reEnterPassword.get() ?: "").equals(user.password)) {
                    passwordDoestMatchError.set(false)
                } else {
                    passwordDoestMatchError.set(true)
                }
            }
        }
        ValidationUtils.isValidFirstName(user.firstName).let {
            if (!it) {
                errorText.set(errorText.get() + "\n** First Name should not be Empty")
            }
        }
    }

}