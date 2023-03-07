package com.example.instachat.services.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.room_sync.models.InterestedUsersModelSync

@Dao
interface InterestedUsersDao: BaseDao<InterestedUsersModel> {

    @Query("SELECT * FROM interested_users WHERE id =:id ")
    suspend fun getInterestedUser(id: String): InterestedUsersModel

    @Query("DELETE FROM interested_users WHERE id =:id")
    fun deleteRow(id: String)

}