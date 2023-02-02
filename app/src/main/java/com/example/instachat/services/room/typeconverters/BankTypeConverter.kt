package com.example.instachat.services.room.typeconverters

import androidx.room.TypeConverter
import com.example.instachat.services.models.dummyjson.Address
import com.example.instachat.services.models.dummyjson.Bank
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class BankTypeConverter {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getAddress(input: String): Bank? {
        return moshi.adapter<Bank>(Bank::class.java).fromJson(input)
    }

    @TypeConverter
    fun fromAddress(bank: Bank): String {
        return moshi.adapter<Bank>(Bank::class.java).toJson(bank)
    }
}
