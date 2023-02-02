package com.example.instachat.services.room.typeconverters

import androidx.room.TypeConverter
import com.example.instachat.services.models.dummyjson.Address
import com.example.instachat.services.models.dummyjson.Bank
import com.example.instachat.services.models.dummyjson.Company
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class CompanyTypeConverter {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getAddress(input: String): Company? {
        return moshi.adapter<Company>(Company::class.java).fromJson(input)
    }

    @TypeConverter
    fun fromAddress(company: Company): String {
        return moshi.adapter<Company>(Company::class.java).toJson(company)
    }
}
