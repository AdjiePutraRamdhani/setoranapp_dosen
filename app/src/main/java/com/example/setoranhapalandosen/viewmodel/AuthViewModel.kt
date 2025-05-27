package com.example.setoranhapalandosen.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.setoranhapalandosen.datastore.DataStoreManager
import com.example.setoranhapalandosen.model.*
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

enum class LoadingStatus {
    LOADING, SUCCESS, ERROR
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    var token by mutableStateOf("")

    private val _nama = MutableStateFlow("Nama Tidak Diketahui")
    val nama: StateFlow<String> = _nama.asStateFlow()

    private val _email = MutableStateFlow("Email Tidak Diketahui")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _nip = MutableStateFlow("NIP Tidak Diketahui")
    val nip: StateFlow<String> = _nip.asStateFlow()

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error.asStateFlow()

    private val _status = MutableStateFlow(LoadingStatus.SUCCESS)
    val status: StateFlow<LoadingStatus> = _status.asStateFlow()

    private val _ringkasan = MutableStateFlow<List<RingkasanAngkatan>>(emptyList())
    val ringkasan: StateFlow<List<RingkasanAngkatan>> = _ringkasan.asStateFlow()

    private val _daftarMahasiswa = MutableStateFlow<List<MahasiswaPA>>(emptyList())
    val daftarMahasiswa: StateFlow<List<MahasiswaPA>> = _daftarMahasiswa.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.token.collect { savedToken ->
                token = savedToken ?: ""
                if (token.isNotEmpty()) {
                    try {
                        fetchUserInfo(token)
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Error during init: ${e.message}")
                        _error.value = "Gagal memuat data: ${e.message}"
                    }
                }
            }
        }
    }

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
                fetchUserInfo(newToken)
                updateStatus(LoadingStatus.SUCCESS)
            } catch (e: Exception) {
                _error.value = "Login gagal: ${e.message}"
                updateStatus(LoadingStatus.ERROR)
            }
        }
    }

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

    fun fetchUserInfo(token: String) {
        viewModelScope.launch {
            updateStatus(LoadingStatus.LOADING)
            try {
                val profile = getUserProfileFromApi(token)

                _nama.value = profile?.nama ?: "Nama Tidak Diketahui"
                _email.value = profile?.email ?: "Email Tidak Diketahui"
                _nip.value = profile?.nip ?: "NIP Tidak Diketahui"

                profile?.info_mahasiswa_pa?.let {
                    _ringkasan.value = it.ringkasan
                    _daftarMahasiswa.value = it.daftar_mahasiswa
                }

                updateStatus(LoadingStatus.SUCCESS)
            } catch (e: Exception) {
                _error.value = "Gagal mengambil profil: ${e.message}"
                updateStatus(LoadingStatus.ERROR)
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            try {
                dataStoreManager.clear()
                token = ""
                _nama.value = "Nama Tidak Diketahui"
                _email.value = "Email Tidak Diketahui"
                _nip.value = "NIP Tidak Diketahui"
                _ringkasan.value = emptyList()
                _daftarMahasiswa.value = emptyList()
                _error.value = ""
                _status.value = LoadingStatus.SUCCESS
                onLoggedOut()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Gagal logout: ${e.message}")
                _error.value = "Logout gagal: ${e.message}"
            }
        }
    }

    private fun updateStatus(newStatus: LoadingStatus) {
        _status.value = newStatus
    }
}

