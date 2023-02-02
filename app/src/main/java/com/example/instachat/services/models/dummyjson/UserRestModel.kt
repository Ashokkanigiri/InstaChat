package com.example.instachat.services.models.dummyjson

import androidx.room.*
import com.example.instachat.services.room.typeconverters.BankTypeConverter
import com.example.instachat.services.room.typeconverters.CompanyTypeConverter
import com.example.instachat.services.room.typeconverters.HairTypeConverter
import com.example.instachat.services.room.typeconverters.UserAddressTypeConverter

data class UsersRestModel(
    val limit: Int,
    val skip: Int,
    val total: Int,
    val users: List<UserRest>
)

data class UserRest(

    val address: Address,
    val age: Int,
    val bank: Bank,
    val birthDate: String,
    val bloodGroup: String,
    val company: Company,
    val domain: String,
    val ein: String,
    val email: String,
    val eyeColor: String,
    val firstName: String,
    val gender: String,
    val hair: Hair,
    val height: Int,
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

data class AddressRest(
    val address: String,
    val city: String,
    @Embedded
    val coordinates: Coordinates,
    val postalCode: String,
    val state: String
)

data class BankRest(
    val cardExpire: String,
    val cardNumber: String,
    val cardType: String,
    val currency: String,
    val iban: String
)

data class CompanyRest(
    @ColumnInfo("company_address")
    val department: String,
    val name: String,
    val title: String
)

data class HairRest(
    val color: String,
    val type: String
)

data class CoordinatesRest(
    val lat: Double,
    val lng: Double
)