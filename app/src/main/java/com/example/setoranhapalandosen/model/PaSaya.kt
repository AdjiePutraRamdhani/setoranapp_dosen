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
    val email: String,
    val info_mahasiswa_pa: MahasiswaPAInfo
)

@Serializable
data class MahasiswaPAInfo(
    val ringkasan: List<RingkasanAngkatan>,
    val daftar_mahasiswa: List<MahasiswaPA>
)

@Serializable
data class RingkasanAngkatan(
    val tahun: String,
    val total: Int
)

@Serializable
data class MahasiswaPA(
    val email: String,
    val nim: String,
    val nama: String,
    val angkatan: String,
    val semester: Int,
    val info_setoran: InfoSetoran
)

@Serializable
data class InfoSetoran(
    val total_wajib_setor: Int,
    val total_sudah_setor: Int,
    val total_belum_setor: Int,
    val persentase_progres_setor: Double,
    val tgl_terakhir_setor: String? = null,
    val terakhir_setor: String
)
