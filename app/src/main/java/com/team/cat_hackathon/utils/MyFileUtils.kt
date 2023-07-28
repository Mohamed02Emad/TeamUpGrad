package com.team.cat_hackathon.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun fileToMultipartBodyPart(file: File?): MultipartBody.Part? {
    if (file == null) return null
    try {
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
fun getFileFromUri(contentResolver: ContentResolver, uri: Uri?): File? {
    if (uri == null) return null
    try {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File.createTempFile("temp", null)
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}
fun getRealPathFromURI(context: Context, uri: Uri?): String? {
    if (uri == null) return null

    val contentResolver = context.contentResolver
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(uri, projection, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return it.getString(columnIndex)
        }
    }
    return null
}

fun getImageFileFromRealPath(realPath: String?): File? {
    if (realPath == null) return null
    val file = File(realPath)
    return if (file.exists()) file else null
}

fun createMultipartBodyPartFromFile(file: File?): MultipartBody.Part? {
    if (file == null) return null
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("imageUrl", file.name, requestFile)
}


private fun getDataColumn(
    context: Context,
    uri: Uri,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return it.getString(columnIndex)
        }
    }
    return null
}


fun cacheImageToFile(context: Context, uri: Uri): String? {
    try {
        // Open the input stream for the selected image
        val imageStream = context.contentResolver.openInputStream(uri)
        val selectedImage = BitmapFactory.decodeStream(imageStream)

        // Compress the bitmap and convert it to a byte array
        val baos = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val data = baos.toByteArray()

        // Determine the directory where you want to save the cached image
        val cacheDir = context.externalCacheDir // Change this if needed

        // Create a unique filename for the image
        val fileName = "cached_image_${System.currentTimeMillis()}.jpg"

        // Create a new file in the cache directory and write the data to it
        val cachedFile = File(cacheDir, fileName)
        val fileOutputStream = FileOutputStream(cachedFile)
        fileOutputStream.write(data)
        fileOutputStream.flush()
        fileOutputStream.close()

        // Return the path of the saved file
        return cachedFile.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // Return null if there was an error saving the image
    return null
}




