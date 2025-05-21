package com.example.setoranhapalandosen.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.setoranhapalandosen.datastore.DataStoreManager
import com.example.setoranhapalandosen.model.LoginResponse
import com.example.setoranhapalandosen.network.ApiClient
import com.example.setoranhapalandosen.network.getUserProfileFromApi
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

// State untuk menandakan status pengambilan data
enum class LoadingStatus {
    LOADING, SUCCESS, ERROR
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    var token by mutableStateOf("")
    private val _nama = MutableStateFlow("Nama Tidak Diketahui")
    val nama: StateFlow<String> = _nama

    private val _email = MutableStateFlow("Email Tidak Diketahui")
    val email: StateFlow<String> = _email

    private val _nip = MutableStateFlow("NIP Tidak Diketahui")
    val nip: StateFlow<String> = _nip

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _status = MutableStateFlow(LoadingStatus.SUCCESS)
    val status: StateFlow<LoadingStatus> = _status

    init {
        viewModelScope.launch {
            dataStoreManager.token.collect { savedToken ->
                token = savedToken ?: ""
                if (token.isNotEmpty()) {
                    try {
                        fetchUserInfo(token)
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Error during initialization: ${e.message}")
                        _error.value = "Gagal memuat data: ${e.message}"
                    }
                }
            }
        }
    }

    // Fungsi login dosen
    fun login(email: String, pass: String) {
        viewModelScope.launch {
            if (pass.isBlank()) {
                _error.value = "Password tidak boleh kosong"
                return@launch
            }
            updateStatus(LoadingStatus.LOADING)
            try {
                val newToken = loginDosen(email, pass)
                token = newToken
                _error.value = ""
                updateStatus(LoadingStatus.SUCCESS)

            } catch (e: Exception) {
                _error.value = "Login gagal: ${e.message}"
                updateStatus(LoadingStatus.ERROR)
            }
        }
    }

    // Fungsi login ke server
    private suspend fun loginDosen(email: String, pass: String): String {
        val response = ApiClient.client.submitForm(
            url = "https://id.tif.uin-suska.ac.id/realms/dev/protocol/openid-connect/token",
            formParameters = Parameters.build {
                append("grant_type", "password")
                append("client_id", "setoran-mobile-dev")
                append("client_secret", "aqJp3xnXKudgC7RMOshEQP7ZoVKWzoSl")
                append("username", email)
                append("password", pass)
                append("scope", "openid profile email")
            }
        ) {
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            }
        }

        val responseBody = response.bodyAsText()
        if (response.status.value != 200) throw Exception("Login gagal: $responseBody")

        val body = Json { ignoreUnknownKeys = true }.decodeFromString<LoginResponse>(responseBody)
        dataStoreManager.saveToken(body.accessToken)
        dataStoreManager.saveRefreshToken(body.refreshToken)
        return body.accessToken
    }

    // Fungsi mengambil informasi pengguna
    fun fetchUserInfo(token: String) {
        viewModelScope.launch {
            updateStatus(LoadingStatus.LOADING)
            try {
                val profile = getUserProfileFromApi(token)
                _nama.value = profile?.nama ?: "Nama Tidak Diketahui"
                _email.value = profile?.email ?: "Email Tidak Diketahui"
                _nip.value = profile?.nip ?: "NIP Tidak Diketahui"
                updateStatus(LoadingStatus.SUCCESS)
            } catch (e: Exception) {
                _error.value = "Gagal mengambil profil: ${e.message}"
                updateStatus(LoadingStatus.ERROR)
            }
        }
    }

    private fun updateStatus(newStatus: LoadingStatus) {
        _status.value = newStatus
    }
}