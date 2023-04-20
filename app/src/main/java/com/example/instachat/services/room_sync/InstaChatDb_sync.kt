package com.example.instachat.services.room_sync

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.instachat.services.room_sync.dao.*
import com.example.instachat.services.room_sync.models.*
import com.example.instachat.services.room_sync.typeconverters.*


@TypeConverters(
    value = [UserAddressTypeConverterSync::class,
        TagsTypeConverterSync::class,
        BankSyncTypeConverterSync::class,
        CompanyTypeConverterSync::class,
        HairSyncTypeConverterSync::class,
        LikesTypeConverterSync::class,
        UserSessionConverterSync::class]
)
@Database(
    entities = [UserSync::class, PostModelItemSync::class, CommentSync::class, InterestedUsersModelSync::class, RequestedForInterestModelSync::class],
    version = 1,
    exportSchema = false
)
abstract class InstaChatDb_sync : RoomDatabase() {
    abstract fun usersDaoSync(): UsersDaoSync
    abstract fun postsDaoSync(): PostsDaoSync
    abstract fun commentsDaoSync(): CommentsDaoSync
    abstract fun interestedUsersDaoSync(): InterestedUsersDaoSync
    abstract fun requestedInterestedUsersDaoSync(): RequestedInterestedUsersDaoSync
}