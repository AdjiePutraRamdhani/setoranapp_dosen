package com.example.setoranhapalandosen.model

import kotlinx.serialization.Serializable

@Serializable
data class SimpanSetoranRequest(
    val data_setoran: List<KomponenSetoranRequest>,
    val tgl_setoran: String? = null
)

@Serializable
data class KomponenSetoranRequest(
    val nama_komponen_setoran: String,
    val id_komponen_setoran: String
)

@Serializable
data class SimpanSetoranResponse(
    val response: Boolean,
    val message: String
)

@Serializable
data class HapusSetoranRequest(
    val data_setoran: List<HapusKomponen>
)

@Serializable
data class HapusKomponen(
    val id: String,
    val id_komponen_setoran: String,
    val nama_komponen_setoran: String
)

