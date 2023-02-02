package com.example.instachat.services.room_sync.typeconvertersSync

import androidx.room.TypeConverter
import com.example.instachat.services.models.dummyjson.Company
import com.example.instachat.services.room_sync.modelsSync.CompanySync
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class CompanyTypeConverterSync {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun getAddress(input: String): CompanySync? {
        return moshi.adapter<CompanySync>(CompanySync::class.java).fromJson(input)
    }

    @TypeConverter
    fun fromAddress(company: CompanySync): String {
        return moshi.adapter<CompanySync>(CompanySync::class.java).toJson(company)
    }
}
