package com.example.instachat.feature.hometab.repository

import com.example.instachat.feature.hometab.models.HomeDataModel
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.utils.Response
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun loadHomeData(userId: String): Flow<Response<List<HomeDataModel>>>
    suspend fun loadHomeUsersData(): Flow<Response<List<User>>>
    suspend fun loadSearchData(): Flow<Response<List<HomeDataModel>>>
}