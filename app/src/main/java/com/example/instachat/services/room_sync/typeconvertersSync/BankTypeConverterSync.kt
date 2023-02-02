package com.example.instachat.services.room_sync.typeconvertersSync

import androidx.room.TypeConverter
import com.example.instachat.services.room_sync.modelsSync.BankSync
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class BankSyncTypeConverterSync {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getAddress(input: String): BankSync? {
        return moshi.adapter<BankSync>(BankSync::class.java).fromJson(input)
    }

    @TypeConverter
    fun fromAddress(bank: BankSync): String {
        return moshi.adapter<BankSync>(BankSync::class.java).toJson(bank)
    }
}
