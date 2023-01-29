package com.example.instachat.services.firebase

import com.example.instachat.services.models.PostModelItem

interface FirebasePostListener {
    fun getPost(post: PostModelItem)

}