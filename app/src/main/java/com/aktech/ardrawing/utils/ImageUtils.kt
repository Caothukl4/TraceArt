package com.aktech.ardrawing.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream

fun bitmapToBase64(bitmap: Bitmap): String {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
    val bytes = stream.toByteArray()
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}

fun imageFileToBase64(filePath: String): String? {
    return try {
        val file = File(filePath)
        val bytes = ByteArray(file.length().toInt())
        val inputStream = FileInputStream(file)
        inputStream.read(bytes)
        inputStream.close()
        Base64.encodeToString(bytes, Base64.NO_WRAP) // hoặc Base64.DEFAULT nếu muốn có \n
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun resizeImage(
    context: Context,
    uri: Uri,
    maxWidth: Int? = null,
    maxHeight: Int? = null
): Uri = withContext(Dispatchers.IO) {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream) ?: return@withContext uri

    val originalWidth = bitmap.width
    val originalHeight = bitmap.height

    var targetWidth = originalWidth
    var targetHeight = originalHeight

    if (maxWidth != null || maxHeight != null) {
        when {
            // chỉ có maxWidth
            maxWidth != null && maxHeight == null -> {
                val ratio = maxWidth.toFloat() / originalWidth
                targetWidth = maxWidth
                targetHeight = (originalHeight * ratio).toInt()
            }
            // chỉ có maxHeight
            maxHeight != null && maxWidth == null -> {
                val ratio = maxHeight.toFloat() / originalHeight
                targetHeight = maxHeight
                targetWidth = (originalWidth * ratio).toInt()
            }
            // có cả hai
            maxWidth != null && maxHeight != null -> {
                val widthRatio = maxWidth.toFloat() / originalWidth
                val heightRatio = maxHeight.toFloat() / originalHeight
                val ratio = minOf(widthRatio, heightRatio)
                targetWidth = (originalWidth * ratio).toInt()
                targetHeight = (originalHeight * ratio).toInt()
            }
        }
    }

    val resized = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)

    val file = File(context.filesDir, "resized_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use { out ->
        resized.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }

    return@withContext Uri.fromFile(file)
}