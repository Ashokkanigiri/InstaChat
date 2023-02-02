package com.example.instachat.services.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.room.dao.CommentsDao
import com.example.instachat.services.room.dao.PostsDao
import com.example.instachat.services.room.dao.UsersDao
import com.example.instachat.services.room.typeconverters.*

@TypeConverters(
    value = [UserAddressTypeConverter::class,
        TagsTypeConverter::class,
        BankTypeConverter::class,
        CompanyTypeConverter::class,
        HairTypeConverter::class,
        LikesTypeConverter::class]
)
@Database(
    entities = [User::class, PostModelItem::class, Comment::class],
    version = 1,
    exportSchema = false
)
abstract class InstaChatDb : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun postsDao(): PostsDao
    abstract fun commentsDao(): CommentsDao
}