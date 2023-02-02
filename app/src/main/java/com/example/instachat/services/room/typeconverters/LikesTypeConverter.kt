package com.example.instachat.services.room.typeconverters

import androidx.room.TypeConverter
import com.example.instachat.services.models.dummyjson.LikedPosts
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

class LikesTypeConverter {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getList(input: String?): List<LikedPosts>? {
        input?.let {
            val listType: Type = object : TypeToken<ArrayList<LikedPosts?>?>() {}.type
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