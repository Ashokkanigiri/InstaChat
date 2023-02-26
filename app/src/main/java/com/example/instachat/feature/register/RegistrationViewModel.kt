package com.example.instachat.feature.register

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.R
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.FirebaseRepository
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.SingleLiveEvent
import com.example.instachat.utils.ValidationUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val syncRepository: SyncRepository,
    val connectivityService: ConnectivityService,
    val firebaseRepository: FirebaseRepository
) : ViewModel() {

    val errorText = ObservableField<String>()
    val reEnterPassword = ObservableField<String>()
    val navigateBackToLoginScreenEvent = SingleLiveEvent<Boolean>()
    val passwordDoestMatchError = ObservableField<Boolean>()
    val event = SingleLiveEvent<RegistrationViewModelEvent>()
    var user = User()
    val auth = Firebase.auth

    fun navigateBackToLoginScreen() {
        navigateBackToLoginScreenEvent.postValue(true)
    }

    init {
        errorText.set("")
        firebaseRepository.isUserAlreadyExists("ashokkanigiri98@gmail.com"){

        }
    }

    fun onRegisterClicked() {
        if (connectivityService.hasActiveNetwork()) {
            passwordDoestMatchError.set(false)
            errorText.set("")
            validateUserDetails()
        } else {
            event.postValue(RegistrationViewModelEvent.PopulateConnectivityErrorDialog)
        }
    }

    private fun validateUserDetails() {
        ValidationUtils.shouldRegisterUser(user, reEnterPassword.get() ?: "").let {detailsValidated->
            if (detailsValidated) {
                checkIsUserAlreadyPresentInFirebase()
            } else {
                handleValidationErrors()
            }
        }
    }

    private fun checkIsUserAlreadyPresentInFirebase() {
        firebaseRepository.isUserAlreadyExists(user.email){ isUserAlreadyPresent ->
            if(isUserAlreadyPresent){
                event.postValue(RegistrationViewModelEvent.PopulateUserAlreadyExistsDialog)
            }else{
                createAndSyncNewUser(user)
            }
        }
    }

    fun createAndSyncNewUser(user: User) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                viewModelScope.launch(Dispatchers.IO) {
                    if (connectivityService.hasActiveNetwork()) syncRepository.addNewUser(user.apply {
                        id = it?.user?.uid ?: ""
                        password = ""
                        username = user.email
                    })
                    withContext(Dispatchers.Main){
                        event.postValue(RegistrationViewModelEvent.HandleRegistrationSuccess)
                    }
                }

            }
    }

    fun handleValidationErrors() {
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