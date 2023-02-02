package com.example.instachat.services.room_sync

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.instachat.services.room_sync.daoSync.CommentsDaoSync
import com.example.instachat.services.room_sync.daoSync.PostsDaoSync
import com.example.instachat.services.room_sync.daoSync.UsersDaoSync
import com.example.instachat.services.room_sync.modelsSync.CommentSync
import com.example.instachat.services.room_sync.modelsSync.PostModelItemSync
import com.example.instachat.services.room_sync.modelsSync.UserSync
import com.example.instachat.services.room_sync.typeconvertersSync.*


@TypeConverters(
    value = [UserAddressTypeConverterSync::class,
        TagsTypeConverterSync::class,
        BankSyncTypeConverterSync::class,
        CompanyTypeConverterSync::class,
        HairSyncTypeConverterSync::class]
)
@Database(
    entities = [UserSync::class, PostModelItemSync::class, CommentSync::class],
    version = 1,
    exportSchema = false
)
abstract class InstaChatDb_sync : RoomDatabase() {
    abstract fun usersDaoSync(): UsersDaoSync
    abstract fun postsDaoSync(): PostsDaoSync
    abstract fun commentsDaoSync(): CommentsDaoSync
}