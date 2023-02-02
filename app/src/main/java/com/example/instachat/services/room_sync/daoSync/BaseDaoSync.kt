package com.example.instachat.services.room_sync.daoSync

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDaoSync<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<T>)

}