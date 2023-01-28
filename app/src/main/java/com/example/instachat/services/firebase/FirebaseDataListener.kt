package com.example.instachat.services.firebase

import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User

interface FirebaseDataListener {
    fun getAllPosts(posts: List<PostModelItem>)
    fun getAllComments(comments: List<Comment>)
    fun getAllUsers(users: List<User>)
}