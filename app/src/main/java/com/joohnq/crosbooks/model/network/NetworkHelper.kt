package com.joohnq.crosbooks.model.network

import org.json.JSONException
import org.json.JSONObject

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
}