package com.example.instachat.utils

import android.app.Activity
import android.provider.MediaStore

object StorageUtils {

    fun getStoredImagesList(activity: Activity): List<String> {

        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)

        // Query the MediaStore for all images
        val cursor = activity.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        // Loop through the results and add each image to a list
        val imagePaths = mutableListOf<String>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get the ID and path of the image
                val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID) ?: 0)
                val path =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA) ?: 0)

                // Add the path to the list
                imagePaths.add(path)
            } while (cursor.moveToNext())
        }

        // Close the cursor
        cursor?.close()
        return imagePaths
    }

    fun getRecentPhotos(activity: Activity): List<String>{
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val cursor = activity.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        val imagePaths = mutableListOf<String>()
        var count = 0
        while (cursor?.moveToNext() == true && count < 100) {
            val imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            imagePaths.add(imagePath)
            count++
        }
        cursor?.close()

        return imagePaths
    }
}