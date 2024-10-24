package com.joohnq.crosbooks.model.network

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object NetworkHelper {
    fun extractErrorFromJson(jsonString: String?): String? {
        return try {
            if (jsonString == null) return null

            val jsonObject = JSONObject(jsonString)

            if (jsonObject.has("error") && jsonObject.get("error") is String) {
                jsonObject.getString("error")
            } else {
                null
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }

    fun convertUriToMultipartBodyPart(context: Context, uri: Uri): MultipartBody.Part {
        val file = getFileFromUri(context, uri)
        if (file == null) throw Exception("File is null")
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestBody)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)

        return inputStream?.use { input ->
            val tempFile = File(context.cacheDir, "tempFile")
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
            tempFile
        }
    }

}
