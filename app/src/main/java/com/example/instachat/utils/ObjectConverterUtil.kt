package com.example.instachat.utils

import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.room_sync.modelsSync.PostModelItemSync
import com.example.instachat.services.room_sync.modelsSync.UserSync
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

object ObjectConverterUtil {

    fun convertUserToUserSync(user: User): UserSync{
        return Gson().fromJson(Gson().toJson(user), UserSync::class.java)
    }

    fun convertUserSyncToUser(userSync: UserSync): User{
        return Gson().fromJson(Gson().toJson(userSync), User::class.java)
    }

    fun convertPostToPostSync(post: PostModelItem): PostModelItemSync{
        return Gson().fromJson(Gson().toJson(post), PostModelItemSync::class.java)
    }

    fun convertPostSyncToPost(postModelItemSync: PostModelItemSync): PostModelItem{
        return Gson().fromJson(Gson().toJson(postModelItemSync), PostModelItem::class.java)
    }

}