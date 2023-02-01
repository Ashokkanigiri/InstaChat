package com.example.instachat.services.models.dummyjson

import androidx.room.*
import com.example.instachat.services.room.typeconverters.BankTypeConverter
import com.example.instachat.services.room.typeconverters.CompanyTypeConverter
import com.example.instachat.services.room.typeconverters.HairTypeConverter
import com.example.instachat.services.room.typeconverters.UserAddressTypeConverter

data class UsersModel(
    val limit: Int,
    val skip: Int,
    val total: Int,
    val users: List<User>
)

@Entity("users")
data class User(

    @ColumnInfo("userAddress")
    @TypeConverters(UserAddressTypeConverter::class)
    val address: Address,
    val age: Int,
    @TypeConverters(BankTypeConverter::class)
    val bank: Bank,
    val birthDate: String,
    val bloodGroup: String,
    @TypeConverters(CompanyTypeConverter::class)
    val company: Company,
    val domain: String,
    val ein: String,
    val email: String,
    val eyeColor: String,
    val firstName: String,
    val gender: String,
    @TypeConverters(HairTypeConverter::class)
    val hair: Hair,
    val height: Int,
    @PrimaryKey
    val id: Int,
    val image: String,
    val ip: String,
    val lastName: String,
    val macAddress: String,
    val maidenName: String,
    val password: String,
    val phone: String,
    val ssn: String,
    val university: String,
    val userAgent: String,
    val username: String,
    val weight: Double
)

data class Address(
    val address: String,
    val city: String,
    @Embedded
    val coordinates: Coordinates,
    val postalCode: String,
    val state: String
)

data class Bank(
    val cardExpire: String,
    val cardNumber: String,
    val cardType: String,
    val currency: String,
    val iban: String
)

data class Company(
    @ColumnInfo("company_address")
    val department: String,
    val name: String,
    val title: String
)

data class Hair(
    val color: String,
    val type: String
)

data class Coordinates(
    val lat: Double,
    val lng: Double
)