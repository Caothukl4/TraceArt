package com.tuananh.traceart.presentation.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream

object DrawUtils {
    suspend fun saveBitmapToGallery(context: Context, bitmap: Bitmap, fileName: String): Uri? {
        return withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/TraceArt")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val imageUri = contentResolver.insert(imageCollection, contentValues)

            imageUri?.let { uri ->
                try {
                    val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                    outputStream?.use { 
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        contentValues.clear()
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                        contentResolver.update(uri, contentValues, null, null)
                    }
                    uri
                } catch (e: Exception) {
                    contentResolver.delete(uri, null, null)
                    null
                }
            }
        }
    }
}
