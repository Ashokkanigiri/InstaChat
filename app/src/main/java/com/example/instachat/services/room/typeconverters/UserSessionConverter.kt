package com.example.instachat.services.room.typeconverters

import androidx.room.TypeConverter
import com.example.instachat.services.models.dummyjson.Session
import com.example.instachat.services.room_sync.models.LikedPosts
import com.example.instachat.services.room_sync.models.SessionsSync
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

class UserSessionConverter {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getList(input: String?): List<Session>? {
        input?.let {
            val listType: Type = object : TypeToken<ArrayList<Session?>?>() {}.getType()
            return Gson().fromJson(input, listType)
        }
        return null
    }

    @TypeConverter
    fun fromList(list: List<Session>?): String? {
        list?.let {
            return Gson().toJson(list)
        }
        return null
    }
}