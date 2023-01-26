package com.example.instachat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.firebase.FirebaseDataInjector
import com.example.instachat.services.repository.RestApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val restApiRepository: RestApiRepository): ViewModel() {

    fun injectRandomUsersToFirebase(){
        viewModelScope.launch {
            supervisorScope {
                (1 .. 20).forEach {
                    val randomUser = restApiRepository.randomUserRestApiClient.getRandomUser()
                    FirebaseDataInjector.injectRandomUsersToFirebase(randomUser)
                }
            }
        }
    }
}