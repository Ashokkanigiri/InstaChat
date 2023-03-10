package com.example.instachat.services.room_sync.typeconverters

import androidx.room.TypeConverter
import com.example.instachat.services.room_sync.models.AddressSync
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class UserAddressTypeConverterSync {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getAddress(input: String): AddressSync? {
        return moshi.adapter<AddressSync>(AddressSync::class.java).fromJson(input)
    }

    @TypeConverter
    fun fromAddress(address: AddressSync): String {
        return moshi.adapter<AddressSync>(AddressSync::class.java).toJson(address)
    }
}
