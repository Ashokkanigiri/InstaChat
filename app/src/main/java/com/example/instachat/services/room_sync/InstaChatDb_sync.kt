package com.example.instachat.services.room_sync

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.instachat.services.room_sync.dao.CommentsDaoSync
import com.example.instachat.services.room_sync.dao.PostsDaoSync
import com.example.instachat.services.room_sync.dao.UsersDaoSync
import com.example.instachat.services.room_sync.models.CommentSync
import com.example.instachat.services.room_sync.models.PostModelItemSync
import com.example.instachat.services.room_sync.models.UserSync
import com.example.instachat.services.room_sync.typeconverters.*


@TypeConverters(
    value = [UserAddressTypeConverterSync::class,
        TagsTypeConverterSync::class,
        BankSyncTypeConverterSync::class,
        CompanyTypeConverterSync::class,
        HairSyncTypeConverterSync::class,
        LikesTypeConverterSync::class]
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