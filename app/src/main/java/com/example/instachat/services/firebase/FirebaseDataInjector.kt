package com.example.instachat.services.firebase

import android.util.Log
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.RandomUserModel
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseDataInjector {

    fun injectCommentsToFirebase(data: Comment){
        val db = Firebase.firestore
        db.collection("comments")
            .document(data.id.toString())
            .set(data)
            .addOnCompleteListener {
                Log.d("wkjbfqa", "OnComplete")
            }.addOnCanceledListener {
                Log.d("wkjbfqa", "addOnCanceledListener")
            }
    }


    fun injectUsersToFirebase(data: User){
        val db = Firebase.firestore
        db.collection("users")
            .document(data.id.toString())
            .set(data)
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
