package com.example.eventoapp.network

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object UploadUtils {

    /**
     * Copia el contenido del Uri al cache del app y crea MultipartBody.Part
     * partName = nombre del campo multipart esperado por el server (ej. "imagen")
     */
    fun prepareFilePart(context: Context, partName: String, uri: Uri): MultipartBody.Part {
        val input = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("No se puede abrir InputStream para uri: $uri")
        val tmpFile = File.createTempFile("upload_${System.currentTimeMillis()}", ".jpg", context.cacheDir)
        tmpFile.outputStream().use { output ->
            input.copyTo(output)
        }
        val mediaType = "image/jpeg".toMediaTypeOrNull()
        val requestFile = tmpFile.asRequestBody(mediaType)
        return MultipartBody.Part.createFormData(partName, tmpFile.name, requestFile)
    }

    /**
     * Crea RequestBody desde String (text/plain) para partes del multipart
     */
    fun createPartFromString(value: String): RequestBody =
        value.toRequestBody("text/plain".toMediaTypeOrNull())
}
