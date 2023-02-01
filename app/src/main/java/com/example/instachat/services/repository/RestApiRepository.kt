package com.example.instachat.services.repository

import com.example.instachat.services.firebase.FirebaseRepository
import com.example.instachat.services.rest.restclient.DummyJsonRestClient
import com.example.instachat.services.rest.restclient.LorealImageListRestClient
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class RestApiRepository @Inject constructor(
    val lorealImageListRestClient: LorealImageListRestClient,
    val dummyJsonRestClient: DummyJsonRestClient,
    val roomRepository: RoomRepository,
    val firebaseRepository: FirebaseRepository
) {

    suspend fun injectAllDataBases(){
        supervisorScope {
            injectAllPosts()
            injectUsers()
            injectComments()
        }
    }

    private suspend fun injectAllPosts() {
        val imageListResponse = lorealImageListRestClient.getAllImages()
        val postsList = dummyJsonRestClient.getAllPosts().posts
        postsList.forEach { post ->
            post.postImageUrl = imageListResponse.get(post.id - 1).download_url
            firebaseRepository.injectPostsToFirebase(post)
            roomRepository.postsDao.insert(post)
        }
    }

    private suspend fun injectUsers() {
        val usersList = dummyJsonRestClient.getAllUsers().users
        usersList.forEach {
            firebaseRepository.injectUsersToFirebase(it)
            roomRepository.usersDao.insert(usersList)
        }
    }

    private suspend fun injectComments() {
        val commentsList = dummyJsonRestClient.getAllComments().comments
        commentsList.forEach {
            firebaseRepository.injectCommentsToFirebase(it)
            roomRepository.commentsDao.insert(commentsList)
        }
    }
}