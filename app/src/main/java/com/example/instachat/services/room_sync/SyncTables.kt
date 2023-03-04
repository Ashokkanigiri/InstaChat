package com.example.instachat.services.room_sync

enum class SyncTables(tableName: String) {
    POSTS("POSTS"),
    USERS("USERS"),
    COMMENTS("COMMENTS"),
    NEW_POST("NEW_POST"),
    NEW_USER("NEW_USER"),
    USERS_UPDATE_FOLLOWING("USERS_UPDATE_FOLLOWING")
}