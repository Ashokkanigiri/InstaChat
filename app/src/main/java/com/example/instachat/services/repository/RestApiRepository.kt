package com.example.instachat.services.repository

import com.example.instachat.services.firebase.FirebaseRepository
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.models.dummyjson.UserRest
import com.example.instachat.services.models.dummyjson.UsersModel
import com.example.instachat.services.models.rest.PostModelItemRest
import com.example.instachat.services.models.rest.RestComment
import com.example.instachat.services.rest.restclient.DummyJsonRestClient
import com.example.instachat.services.rest.restclient.LorealImageListRestClient
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.squareup.moshi.Json
import kotlinx.coroutines.supervisorScope
import java.lang.reflect.Type
import javax.inject.Inject

class RestApiRepository @Inject constructor(
    val lorealImageListRestClient: LorealImageListRestClient,
    val dummyJsonRestClient: DummyJsonRestClient,
    val roomRepository: RoomRepository,
    val firebaseRepository: FirebaseRepository
) {

    suspend fun injectAllDataBases() {
        supervisorScope {
            injectAllPosts()
            injectUsers()
            injectComments()
        }
    }

    private suspend fun injectAllPosts() {
        val imageListResponse = lorealImageListRestClient.getAllImages()
        val postsList = dummyJsonRestClient.getAllPosts().posts

        val dd: List<PostModelItemRest> = postsList.apply {
            this.map { it -> it.userId.toString() }
        }
        val json = Gson().toJson(dd)
        val listType: Type = object : TypeToken<ArrayList<PostModelItem?>?>() {}.getType()
        val postModelItems: List<PostModelItem> = (Gson().fromJson(json, listType))

        postModelItems.forEach { post ->
            post.postImageUrl = imageListResponse.get(post.id - 1).download_url
            firebaseRepository.injectPostsToFirebase(post)
            roomRepository.postsDao.insert(post)
        }
    }

    private suspend fun injectUsers() {
        val restUsersList = dummyJsonRestClient.getAllUsers().users
        val dd: List<UserRest> = restUsersList.apply {
            this.map { it -> it.id.toString() }
        }
        val json = Gson().toJson(dd)
        val listType: Type = object : TypeToken<ArrayList<User?>?>() {}.getType()
        val usersList: List<User> = (Gson().fromJson(json, listType))

        usersList.forEach {
            firebaseRepository.injectUsersToFirebase(it)
            roomRepository.usersDao.insert(it)
        }
    }

    private suspend fun injectComments() {
        val commentsList = dummyJsonRestClient.getAllComments().comments
        val dd: List<RestComment> = commentsList.apply {
            this.map { it -> it.user.id.toString() }
        }
        val json = Gson().toJson(dd)
        val listType: Type = object : TypeToken<ArrayList<Comment?>?>() {}.getType()
        val commentsList1: List<Comment> = (Gson().fromJson(json, listType))

        commentsList1.forEach {
            firebaseRepository.injectCommentsToFirebase(it)
            roomRepository.commentsDao.insert(it)
        }
    }
}