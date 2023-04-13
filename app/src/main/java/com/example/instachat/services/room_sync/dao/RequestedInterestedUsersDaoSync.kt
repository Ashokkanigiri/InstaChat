package com.example.instachat.services.room_sync.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.instachat.services.models.dummyjson.RequestedForInterestModel
import com.example.instachat.services.room_sync.models.InterestedUsersModelSync
import com.example.instachat.services.room_sync.models.RequestedForInterestModelSync

@Dao
interface RequestedInterestedUsersDaoSync : BaseDaoSync<RequestedForInterestModelSync>{

    @Query("SELECT * FROM requested_for_interest_sync WHERE id =:id ")
    suspend fun getRequestedInterestedUserSync(id: String): RequestedForInterestModelSync

    @Query("DELETE FROM requested_for_interest_sync WHERE id =:id")
    fun deleteRow(id: String)
}