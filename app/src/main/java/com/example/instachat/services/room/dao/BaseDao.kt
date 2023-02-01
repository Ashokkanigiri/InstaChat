package com.example.instachat.services.room.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import retrofit2.http.Query

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: T)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(items: List<T>)

}