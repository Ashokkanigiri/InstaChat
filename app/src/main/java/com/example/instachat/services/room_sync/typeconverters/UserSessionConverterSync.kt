package com.example.instachat.services.room_sync.typeconverters

import androidx.room.TypeConverter
import com.example.instachat.services.room_sync.models.LikedPosts
import com.example.instachat.services.room_sync.models.SessionsSync
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

class UserSessionConverterSync {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getList(input: String?): List<SessionsSync>? {
        input?.let {
            val listType: Type = object : TypeToken<ArrayList<SessionsSync?>?>() {}.getType()
            return Gson().fromJson(input, listType)
        }
        return null
    }

    @TypeConverter
    fun fromList(list: List<SessionsSync>?): String? {
        list?.let {
            return Gson().toJson(list)
        }
        return null
    }
}