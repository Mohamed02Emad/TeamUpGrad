package com.team.cat_hackathon.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
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

    val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("file", file.name, requestFile)
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





