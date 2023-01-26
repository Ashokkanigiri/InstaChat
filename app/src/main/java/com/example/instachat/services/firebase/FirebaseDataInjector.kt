package com.example.instachat.services.firebase

import android.util.Log
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.RandomUserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    fun injectPostsToFirebase(data: PostModelItem){
        val db = Firebase.firestore
        db.collection("posts")
            .document(data.id.toString())
            .set(data)
            .addOnCompleteListener {
                Log.d("wkjbfqa", "OnComplete")
            }.addOnCanceledListener {
                Log.d("wkjbfqa", "addOnCanceledListener")
            }
    }

}
