package com.example.instachat.services.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.instachat.services.models.dummyjson.RequestedForInterestModel
import com.example.instachat.services.room_sync.models.RequestedForInterestModelSync

@Dao
interface RequestedInterestedUsersDao : BaseDao<RequestedForInterestModel>{

    @Query("SELECT * FROM requested_for_interest WHERE id =:id ")
    suspend fun getRequestedInterestedUser(id: String): RequestedForInterestModel

    @Query("SELECT * FROM requested_for_interest WHERE requestedUserId =:userId ")
    suspend fun getIntestedUsersInRequestedList(userId: String): List<RequestedForInterestModel>?

    @Query("DELETE FROM requested_for_interest WHERE id =:id")
    fun deleteRow(id: String)
}