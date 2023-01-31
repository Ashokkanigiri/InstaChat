package com.example.instachat.services.room.typeconverters

import androidx.room.TypeConverter
import com.example.instachat.services.models.dummyjson.Address
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class UserAddressTypeConverter {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getAddress(input: String): Address? {
        return moshi.adapter<Address>(Address::class.java).fromJson(input)
    }

    @TypeConverter
    fun fromAddress(address: Address): String {
        return moshi.adapter<Address>(Address::class.java).toJson(address)
    }
}
