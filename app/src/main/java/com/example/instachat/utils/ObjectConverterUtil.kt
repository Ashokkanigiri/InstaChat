package com.example.instachat.utils

import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.RequestedForInterestModel
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.room_sync.models.*
import com.google.gson.Gson

object ObjectConverterUtil {

    fun convertUserToUserSync(user: User): UserSync {
        return Gson().fromJson(Gson().toJson(user), UserSync::class.java)
    }

    fun convertUserSyncToUser(userSync: UserSync): User {
        return Gson().fromJson(Gson().toJson(userSync), User::class.java)
    }

    fun convertPostToPostSync(post: PostModelItem): PostModelItemSync {
        return Gson().fromJson(Gson().toJson(post), PostModelItemSync::class.java)
    }

    fun convertPostSyncToPost(postModelItemSync: PostModelItemSync): PostModelItem {
        return Gson().fromJson(Gson().toJson(postModelItemSync), PostModelItem::class.java)
    }

    fun convertCommentToCommentSync(comment: Comment): CommentSync {
        return Gson().fromJson(Gson().toJson(comment), CommentSync::class.java)
    }

    fun convertCommentSyncToComment(commentSync: CommentSync): Comment {
        return Gson().fromJson(Gson().toJson(commentSync), Comment::class.java)
    }

    fun convertInterestedUsersToInterestedUsersSync(interestedUsersModel: InterestedUsersModel): InterestedUsersModelSync {
        return Gson().fromJson(
            Gson().toJson(interestedUsersModel),
            InterestedUsersModelSync::class.java
        )
    }

    fun convertInterestedUsersSyncToInterestedUsers(interestedUsersModelSync: InterestedUsersModelSync): InterestedUsersModel {
        return Gson().fromJson(
            Gson().toJson(interestedUsersModelSync),
            InterestedUsersModel::class.java
        )
    }

    fun convertRequestedInterestedModelToRequestedInterestedModelSync(requestedForInterestModel: RequestedForInterestModel): RequestedForInterestModelSync {
        return Gson().fromJson(
            Gson().toJson(requestedForInterestModel),
            RequestedForInterestModelSync::class.java
        )
    }

    fun convertRequestedInterestedModelSyncToRequestedInterestedModel(requestedForInterestModelSync: RequestedForInterestModelSync): RequestedForInterestModel {
        return Gson().fromJson(
            Gson().toJson(requestedForInterestModelSync),
            RequestedForInterestModel::class.java
        )
    }

}