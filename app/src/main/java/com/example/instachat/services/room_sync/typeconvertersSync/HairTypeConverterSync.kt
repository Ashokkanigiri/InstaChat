package com.example.instachat.services.room_sync.typeconvertersSync

import androidx.room.TypeConverter
import com.example.instachat.services.room_sync.modelsSync.HairSync
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class HairSyncTypeConverterSync {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getAddress(input: String): HairSync? {
        return moshi.adapter<HairSync>(HairSync::class.java).fromJson(input)
    }

    @TypeConverter
    fun fromAddress(hairSync: HairSync): String {
        return moshi.adapter<HairSync>(HairSync::class.java).toJson(hairSync)
    }
}
