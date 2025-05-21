package com.example.setoranhapalandosen.network

import android.util.Log
import com.example.setoranhapalandosen.model.UserData
import com.example.setoranhapalandosen.model.UserInfo
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

// Fungsi untuk mengambil data profil dosen
suspend fun getUserProfileFromApi(token: String): UserData? = withContext(Dispatchers.IO) {
    try {
        val response: HttpResponse = ApiClient.client.get("https://api.tif.uin-suska.ac.id/setoran-dev/v1/dosen/pa-saya") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        val body = response.bodyAsText()
        val userResponse = Json { ignoreUnknownKeys = true }.decodeFromString<UserInfo>(body)
        return@withContext userResponse.data

    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("API Response", "Gagal mengambil data profil: ${e.message}")
        return@withContext null
    }
}