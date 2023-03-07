package com.example.instachat.services.room_sync.models

import androidx.room.*
import com.example.instachat.services.room.typeconverters.BankTypeConverter
import com.example.instachat.services.room.typeconverters.CompanyTypeConverter
import com.example.instachat.services.room.typeconverters.TagsTypeConverter
import com.example.instachat.services.room.typeconverters.UserAddressTypeConverter
import com.example.instachat.services.room_sync.typeconverters.HairSyncTypeConverterSync
import com.example.instachat.services.room_sync.typeconverters.LikesTypeConverterSync
import com.example.instachat.services.room_sync.typeconverters.TagsTypeConverterSync
import com.google.gson.annotations.SerializedName


@Entity("users_sync")
data class UserSync(

    @ColumnInfo("userAddress")
    @TypeConverters(UserAddressTypeConverter::class)
    val address: AddressSync,
    val age: Int,
    @TypeConverters(BankTypeConverter::class)
    val bank: BankSync,
    val birthDate: String,
    val bloodGroup: String,
    @TypeConverters(CompanyTypeConverter::class)
    val company: CompanySync,
    val domain: String,
    val ein: String,
    val email: String,
    val eyeColor: String,
    val firstName: String,
    val gender: String,
    @TypeConverters(HairSyncTypeConverterSync::class)
    val hair: HairSync,
    val height: Int,
    @PrimaryKey
    val id: String,
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
    val weight: Double,

    @TypeConverters(LikesTypeConverterSync::class)
    var likedPosts: List<LikedPosts>? = emptyList(),

    @SerializedName("followedUserIds")
    @TypeConverters(TagsTypeConverterSync::class)
    var followedUserIds: List<String>? = emptyList(),

    @TypeConverters(TagsTypeConverterSync::class)
    var interestedUsersList: List<String>? = emptyList(),

    @TypeConverters(TagsTypeConverterSync::class)
    var requestedForInterestsList: List<String>? = emptyList()
)

data class LikedPosts(val postId: Int)


data class AddressSync(
    val address: String,
    val city: String,
    @Embedded
    val coordinates: CoordinatesSync,
    val postalCode: String,
    val state: String
)

data class BankSync(
    val cardExpire: String,
    val cardNumber: String,
    val cardType: String,
    val currency: String,
    val iban: String
)

data class CompanySync(
    @ColumnInfo("company_address")
    val department: String,
    val name: String,
    val title: String
)

data class HairSync(
    val color: String,
    val type: String
)

data class CoordinatesSync(
    val lat: Double,
    val lng: Double
)