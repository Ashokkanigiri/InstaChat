package com.example.instachat.feature.hometab.repository

import com.example.instachat.feature.hometab.models.HomeDataModel
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RoomDataSource
import com.example.instachat.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(val roomDataSource: RoomDataSource) : HomeRepository {

    override suspend fun loadHomeData(userId: String): Flow<Response<List<HomeDataModel>>> {
        return flow {
            emit(Response.Loading)
            try {
                roomDataSource.getHomeDataForUser(userId).collect{
                    emit(Response.Success(it))
                }
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }
    }

    override suspend fun loadHomeUsersData(): Flow<Response<List<User>>> {
        return flow {
            emit(Response.Loading)
            try {
                roomDataSource.getAllUsersInDB().collect{
                    emit(Response.Success(it))
                }
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }
    }

    override suspend fun loadSearchData(): Flow<Response<List<HomeDataModel>>> {
        return flow {
            emit(Response.Loading)
            try {
                roomDataSource.getSearchHomeData().collect{
                    emit(Response.Success(it))
                }
            }catch (e: Exception){
                emit(Response.Failure(e))
            }
        }
    }


}