package com.example.instachat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val firebaseRepository: FirebaseRepository): ViewModel() {

    fun injectDataFromFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.getAllPostsFromFirebase()
            firebaseRepository.getAllUsersFromFirebase()
            firebaseRepository.getAllCommentsFromFirebase()
        }
    }
}