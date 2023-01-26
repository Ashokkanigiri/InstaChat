package com.example.instachat.services.models

class LorealImagesListModel : ArrayList<LorealImagesListModelItem>()

data class LorealImagesListModelItem(
    val author: String,
    val download_url: String,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)