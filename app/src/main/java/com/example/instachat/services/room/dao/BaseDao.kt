package com.example.instachat.services.room.dao

import androidx.room.Insert
import retrofit2.http.Query

interface BaseDao<T> {

    @Insert
    suspend fun insert(item: T)

    @Insert
    suspend fun insert(items: List<T>)

}