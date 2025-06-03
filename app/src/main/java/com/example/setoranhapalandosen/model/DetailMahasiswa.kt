package com.example.setoranhapalandosen.model

import kotlinx.serialization.Serializable

@Serializable
data class DetailMahasiswaResponse(
    val response: Boolean,
    val message: String,
    val data: DetailMahasiswaData
)

@Serializable
data class DetailMahasiswaData(
    val info: MahasiswaInfo,
    val setoran: SetoranDetail
)

@Serializable
data class MahasiswaInfo(
    val nama: String,
    val nim: String,
    val email: String,
    val angkatan: String,
    val semester: Int,
    val dosen_pa: DosenPA
)

@Serializable
data class DosenPA(
    val nip: String,
    val nama: String,
    val email: String
)

@Serializable
data class SetoranDetail(
    val info_dasar: InfoSetoran,
    val ringkasan: List<RingkasanKategori>,
    val detail: List<DetailKomponenSetoran>,
    val log: List<LogSetoran>
)

@Serializable
data class RingkasanKategori(
    val label: String,
    val total_wajib_setor: Int,
    val total_sudah_setor: Int,
    val total_belum_setor: Int,
    val persentase_progres_setor: Double
)

@Serializable
data class DetailKomponenSetoran(
    val id: String,
    val nama: String,
    val label: String,
    val sudah_setor: Boolean,
    val info_setoran: InfoSetoranDetail? = null
)

@Serializable
data class InfoSetoranDetail(
    val id: String,
    val tgl_setoran: String,
    val tgl_validasi: String,
    val dosen_yang_mengesahkan: DosenPA
)

@Serializable
data class LogSetoran(
    val id: Int,
    val keterangan: String,
    val aksi: String,
    val ip: String,
    val user_agent: String,
    val timestamp: String,
    val nim: String,
    val dosen_yang_mengesahkan: DosenPA
)
