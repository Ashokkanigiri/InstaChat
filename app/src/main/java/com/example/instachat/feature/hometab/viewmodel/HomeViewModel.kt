package com.example.instachat.feature.hometab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.feature.hometab.HomeDataAdapter
import com.example.instachat.feature.hometab.models.HomeDataCommentsModel
import com.example.instachat.feature.hometab.models.HomeDataModel
import com.example.instachat.feature.hometab.HomeViewModelEvent
import com.example.instachat.feature.hometab.adapter.HomeUsersAdapter
import com.example.instachat.services.repository.FirebaseDataSource
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.LikedPosts
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RoomDataSource
import com.example.instachat.services.repository.RoomSyncRepository
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val firebaseDataSource: FirebaseDataSource,
    val roomDataSource: RoomDataSource,
    val roomSyncRepository: RoomSyncRepository,
    val syncRepository: SyncRepository,
    val connectivityService: ConnectivityService
) : ViewModel() {

    val adapter = HomeDataAdapter(this)
    val usersAdapter = HomeUsersAdapter(this)
    val auth = Firebase.auth
    val event = SingleLiveEvent<HomeViewModelEvent>()
    var selectedPostId = 0
    var isFromSearchFragment = false

    fun loadHomeData(){
        viewModelScope.launch {
            if(isFromSearchFragment){
                getHomeDataFromSearch().collect(){
                    event.postValue(HomeViewModelEvent.LoadHomeDataFromSearch(it))
                }
            }else{
                getHomeData().collect(){
                    event.postValue(HomeViewModelEvent.LoadHomeData(it))
                }
            }
        }
    }

    fun asyncTest(){
        viewModelScope.launch {

        }
    }

    fun navigateToUserDetails(userId: String){
        event.postValue(HomeViewModelEvent.NavigateToUserDetailScreen(userId))
    }

    fun loadUsersData(){
        viewModelScope.launch {
            getHomeUsersData().collect(){
                event.postValue(HomeViewModelEvent.LoadHomeUsersData(it))
            }
        }
    }

    fun setUpActionBar(){
        if(isFromSearchFragment){
            event.postValue(HomeViewModelEvent.ShowActionBarFromSearch)
        }else{
            event.postValue(HomeViewModelEvent.ShowActionBarForHome)
        }
    }

    suspend fun getHomeData() = flow<List<HomeDataModel>>{
        roomDataSource.postsDao.getPostsHomeDataFlow(auth.uid?:"").collect(){
            emit(it)
        }
    }

    suspend fun getHomeDataFromSearch() = flow<List<HomeDataModel>>{
        roomDataSource.postsDao.getAllPostsHomeDataFlow().collect(){
            val list = it
            val clickedPost = list.filter { it.postId == selectedPostId }.first()
            list.toMutableList().remove(clickedPost)
            val finallist: List<HomeDataModel> = listOf(clickedPost)+list
            emit(finallist)
        }
    }

    suspend fun getHomeUsersData() = flow<List<User>>{
        roomDataSource.usersDao.getallUsersFlow().collect(){
            emit(it)
        }
    }

    /**
     * This method injects all the data from API into Firebase
     *
     * Need to be used with care
     */
    fun injectDataFromFirebase() {
        if(connectivityService.hasActiveNetwork()){
            viewModelScope.launch(Dispatchers.IO) {
                firebaseDataSource.injectAllPostsFromFirebase()
                firebaseDataSource.injectAllUsersFromFirebase()
                firebaseDataSource.injectAllCommentsFromFirebase()
                firebaseDataSource.injectAllNotificationsFromFirebase(auth.currentUser?.uid?:"")
            }
        }else{
            event.postValue(HomeViewModelEvent.ShowConnectivityErrorDialog)
        }
    }

    /**
     * This Method will be trigerred when comments edittext
     * in post gets clicked
     */
    fun onCommentsTextClicked(postId: Int) {
        event.postValue(HomeViewModelEvent.NavigateFromHomeToCommentsFragment(postId))
    }

    fun onLikeButtonClicked(homeDataModel: HomeDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = roomDataSource.usersDao.getUser(auth.currentUser?.uid ?: "0")
            val postModelItem = roomDataSource.postsDao.getPost(homeDataModel.postId)

            val isUserAlreadyLikedPost =
                currentUser.likedPosts?.map { it.postId }?.contains(postModelItem.id)

            val incrementLikeCondition =
                currentUser.likedPosts.isNullOrEmpty()

            when {
                (incrementLikeCondition) -> {
                    incrementAndSyncPost(postModelItem, currentUser)
                }
                isUserAlreadyLikedPost == true -> {
                    decrementAndSyncPost(postModelItem, currentUser)
                }
                isUserAlreadyLikedPost == false -> {
                    incrementAndSyncPost(postModelItem, currentUser)
                }
            }
        }
    }

    private suspend fun decrementAndSyncPost(
        postModelItem: PostModelItem,
        currentUser: User
    ) {
        val post = postModelItem.apply {
            this.likesCount = (this.likesCount ?: 0) - 1
        }
        val currentList = (currentUser.likedPosts ?: emptyList()) - (listOf(
            LikedPosts(post.id)
        )).toSet()

        currentUser.likedPosts = currentList
        syncLikedPost(post, currentUser)
    }

    private suspend fun incrementAndSyncPost(
        postModelItem: PostModelItem,
        currentUser: User
    ) {
        val post = postModelItem.apply {
            this.likesCount = (this.likesCount ?: 0) + 1
        }
        val currentList = (currentUser.likedPosts ?: emptyList()) + (listOf(
            LikedPosts(post.id)
        ))

        currentUser.likedPosts = currentList
        syncLikedPost(post, currentUser)
    }

    private suspend fun syncLikedPost(
        post: PostModelItem,
        currentUser: User
    ) {
        syncRepository.updateLikeForPost(post)
        syncRepository.updateUser(currentUser)
    }

    fun getFirstCommentForPost(postId: Int, commentData: ((HomeDataCommentsModel) -> Unit)) {
        viewModelScope.launch {
            val data = roomDataSource.commentsDao.getAllCommentsForPostLiveData(postId)
            withContext(Dispatchers.Main) {
                commentData(data)
            }
        }
    }

}