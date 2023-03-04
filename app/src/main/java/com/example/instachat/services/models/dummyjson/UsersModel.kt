package com.example.instachat.services.models.dummyjson

import androidx.room.*
import com.example.instachat.services.room.typeconverters.*
import com.example.instachat.services.room_sync.typeconverters.TagsTypeConverterSync
import com.google.gson.annotations.SerializedName

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
    val address: Address = Address(),
    val age: Int = 0,
    @TypeConverters(BankTypeConverter::class)
    val bank: Bank = Bank(),
    val birthDate: String = "",
    val bloodGroup: String = "",
    @TypeConverters(CompanyTypeConverter::class)
    val company: Company = Company(),
    val domain: String = "",
    val ein: String = "",
    var email: String = "",
    val eyeColor: String = "",
    var firstName: String = "",
    var gender: String = "",
    @TypeConverters(HairTypeConverter::class)
    val hair: Hair = Hair(),
    val height: Int = 0,
    @PrimaryKey
    var id: String = "",
    val image: String = "",
    val ip: String = "",
    var lastName: String = "",
    val macAddress: String = "",
    val maidenName: String = "",
    var password: String = "",
    val phone: String = "",
    val ssn: String = "",
    val university: String = "",
    val userAgent: String = "",
    var username: String = "",
    val weight: Double = 0.0,

    @TypeConverters(LikesTypeConverter::class)
    var likedPosts: List<LikedPosts>? = emptyList(),

    @SerializedName("followedUserIds")
    @TypeConverters(TagsTypeConverterSync::class)
    var followedUserIds: List<String>? = emptyList(),
)

data class LikedPosts(val postId: Int)

data class Address(
    val address: String = "",
    val city: String = "",
    @Embedded
    val coordinates: Coordinates = Coordinates(),
    val postalCode: String = "",
    val state: String = ""
)

data class Bank(
    val cardExpire: String = "",
    val cardNumber: String = "",
    val cardType: String = "",
    val currency: String = "",
    val iban: String = ""
)

data class Company(
    @ColumnInfo("company_address")
    val department: String = "",
    val name: String = "",
    val title: String = ""
)

data class Hair(
    val color: String = "",
    val type: String = ""
)

data class Coordinates(
    val lat: Double = 0.0,
    val lng: Double = 0.0
)