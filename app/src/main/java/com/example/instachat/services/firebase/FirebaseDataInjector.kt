package com.example.instachat.services.firebase

import android.util.Log
import com.example.instachat.services.models.RandomUserModel
import com.example.instachat.services.rest.RandomUserRestApiClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

object FirebaseDataInjector {

    fun injectRandomUsersToFirebase(data: RandomUserModel){
        val db = Firebase.firestore
        db.collection("users")
            .document(data.results[0].id.value)
            .set(data.results[0])
            .addOnCompleteListener {
                Log.d("wkjbfqa", "OnComplete")
            }.addOnCanceledListener {
                Log.d("wkjbfqa", "addOnCanceledListener")
            }
    }

}
