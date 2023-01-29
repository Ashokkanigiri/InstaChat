package com.example.instachat.services.firebase

import com.example.instachat.services.models.dummyjson.Comment

interface FirebaseCommentsListener {
    fun getAllCommentsFromFirebase(comments: List<Comment>)
}