package com.example.instachat.services.room.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import retrofit2.http.Query

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: T) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBlocking(items: T)

}