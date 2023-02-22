package com.example.instachat.utils

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.instachat.feature.newpost.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object StorageUtils {

    suspend fun queryImagesOnDevice(context: Context): List<Image> {
        val images = mutableListOf<Image>()

        withContext(Dispatchers.IO) {
            val projection = arrayOf(MediaStore.Images.Media._ID,
                MediaStore.Images.Media.RELATIVE_PATH,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.DATE_MODIFIED)

            val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

            context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder)?.use { cursor ->

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH))
                    val name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                    val size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                    val width = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
                    val height = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                    val date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))

                    val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    // Discard invalid images that might exist on the device
                    if (size == null) {
                        continue
                    }

                    images += Image(id, uri, path, name, size, width, height, date)
                }

                cursor.close()
            }
        }

        return images
    }

}