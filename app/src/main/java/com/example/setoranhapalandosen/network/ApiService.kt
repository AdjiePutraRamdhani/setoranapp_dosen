package com.example.setoranhapalandosen.network

import android.util.Log
import com.example.setoranhapalandosen.model.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

suspend fun getUserProfileFromApi(token: String): UserData? = withContext(Dispatchers.IO) {
    try {
        val response: HttpResponse =
            ApiClient.client.get("https://api.tif.uin-suska.ac.id/setoran-dev/v1/dosen/pa-saya") {
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

suspend fun getDetailMahasiswaFromApi(nim: String, token: String): DetailMahasiswaResponse? = withContext(Dispatchers.IO) {
    try {
        val response: HttpResponse =
            ApiClient.client.get("https://api.tif.uin-suska.ac.id/setoran-dev/v1/mahasiswa/setoran/$nim") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }

        val body = response.bodyAsText()
        val detailResponse = Json { ignoreUnknownKeys = true }.decodeFromString<DetailMahasiswaResponse>(body)
        return@withContext detailResponse

    } catch (e: Exception) {
        Log.e("API Detail", "Gagal mengambil detail mahasiswa $nim: ${e.message}")
        return@withContext null
    }
}

suspend fun simpanSetoran(token: String, nim: String, body: SimpanSetoranRequest): SimpanSetoranResponse {
    return try {
        val response = ApiClient.client.post("https://api.tif.uin-suska.ac.id/setoran-dev/v1/mahasiswa/setoran/$nim") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                append(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }
            setBody(body)
        }

        val responseText = response.bodyAsText()
        Log.d("API", "Status: ${response.status.value}, Body: $responseText")

        SimpanSetoranResponse(
            response = response.status.value in 200..299,
            message = responseText
        )
    } catch (e: Exception) {
        Log.e("API", "Gagal validasi: ${e.message}")
        SimpanSetoranResponse(response = false, message = "Gagal validasi: ${e.message}")
    }
}

suspend fun hapusSetoran(token: String, nim: String, data: List<HapusKomponen>): SimpanSetoranResponse {
    return try {
        val response = ApiClient.client.request("https://api.tif.uin-suska.ac.id/setoran-dev/v1/mahasiswa/setoran/$nim") {
            method = io.ktor.http.HttpMethod.Delete
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                append(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }
            setBody(HapusSetoranRequest(data_setoran = data))
        }

        val responseText = response.bodyAsText()
        Log.d("API", "Hapus Status: ${response.status.value}, Body: $responseText")

        SimpanSetoranResponse(
            response = response.status.value in 200..299,
            message = responseText
        )
    } catch (e: Exception) {
        Log.e("API", "Gagal hapus setoran: ${e.message}")
        SimpanSetoranResponse(response = false, message = "Gagal hapus setoran: ${e.message}")
    }
}




