package com.example.setoranhapalandosen.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val data: UserData
)

@Serializable
data class UserData(
    val nip: String,
    val nama: String,
    val email: String
)