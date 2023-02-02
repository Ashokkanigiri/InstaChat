package com.example.instachat.services.room_sync.typeconvertersSync

import androidx.room.TypeConverter
import com.example.instachat.services.room_sync.modelsSync.LikedPosts
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

class LikesTypeConverterSync {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getList(input: String?): List<LikedPosts>? {
        input?.let {
            val listType: Type = object : TypeToken<ArrayList<LikedPosts?>?>() {}.getType()
            return Gson().fromJson(input, listType)
        }
        return null
    }

    @TypeConverter
    fun fromList(list: List<LikedPosts>?): String? {
        list?.let {
            return Gson().toJson(list)
        }
        return null
    }
}