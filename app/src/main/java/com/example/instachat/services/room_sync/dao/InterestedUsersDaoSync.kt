package com.example.instachat.services.room_sync.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.room_sync.models.InterestedUsersModelSync

@Dao
interface InterestedUsersDaoSync: BaseDaoSync<InterestedUsersModelSync> {

    @Query("SELECT * FROM interested_users_sync WHERE id =:id ")
    suspend fun getInterestedUserSync(id: String): InterestedUsersModelSync

    @Query("DELETE FROM interested_users_sync WHERE id =:id")
    fun deleteRow(id: String)
}