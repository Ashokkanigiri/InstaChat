package com.example.instachat.utils

import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.room_sync.models.CommentSync
import com.example.instachat.services.room_sync.models.PostModelItemSync
import com.example.instachat.services.room_sync.models.UserSync
import com.google.gson.Gson

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

    fun convertCommentToCommentSync(comment: Comment): CommentSync{
        return Gson().fromJson(Gson().toJson(comment), CommentSync::class.java)
    }

    fun convertCommentSyncToComment(commentSync: CommentSync): Comment{
        return Gson().fromJson(Gson().toJson(commentSync), Comment::class.java)
    }

}