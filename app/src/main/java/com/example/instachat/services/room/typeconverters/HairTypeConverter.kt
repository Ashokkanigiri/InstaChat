package com.example.instachat.services.room.typeconverters

import androidx.room.TypeConverter
import com.example.instachat.services.models.dummyjson.Address
import com.example.instachat.services.models.dummyjson.Hair
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class HairTypeConverter {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getAddress(input: String): Hair? {
        return moshi.adapter<Hair>(Hair::class.java).fromJson(input)
    }

    @TypeConverter
    fun fromAddress(hair: Hair): String {
        return moshi.adapter<Hair>(Hair::class.java).toJson(hair)
    }
}
