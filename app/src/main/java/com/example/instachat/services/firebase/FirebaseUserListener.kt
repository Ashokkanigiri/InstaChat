package com.example.instachat.services.firebase

import com.example.instachat.services.models.dummyjson.User


interface FirebaseUserListener {
    fun getUser(user: User)
}