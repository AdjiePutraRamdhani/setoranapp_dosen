package com.example.setoranhapalandosen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.setoranhapalandosen.model.DetailKomponenSetoran
import com.example.setoranhapalandosen.model.DetailMahasiswaData
import com.example.setoranhapalandosen.model.HapusKomponen
import com.example.setoranhapalandosen.model.LogSetoran
import com.example.setoranhapalandosen.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMahasiswaScreen(nim: String, vm: AuthViewModel = hiltViewModel()) {
    val detail = vm.detailMahasiswa.collectAsState().value
    val errorMessage = vm.error.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(nim) {
        vm.fetchDetailMahasiswa(nim)
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotBlank()) {
            snackbarHostState.showSnackbar(errorMessage)
            vm.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Detail Mahasiswa") })
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Profil") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Detail Setoran") })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Riwayat") })
            }

            Spacer(modifier = Modifier.height(16.dp))

            detail?.data?.let { data ->
                when (selectedTab) {
                    0 -> ProfilTab(data)
                    1 -> DetailSetoranTab(
                        detailList = data.setoran.detail,
                        nim = data.info.nim,
                        onValidasiClicked = { id, nama, nim ->
                            vm.validasiKomponenSetoran(id, nama, nim)
                        },
                        onHapusClicked = { id, nama, nim, infoId ->
                            if (infoId != null) {
                                vm.hapusKomponenSetoran(
                                    nim = nim,
                                    komponen = HapusKomponen(
                                        id = infoId,
                                        id_komponen_setoran = id,
                                        nama_komponen_setoran = nama
                                    )
                                )
                            } else {
                                vm.setError("Gagal hapus: ID info setoran tidak ditemukan.")
                            }
                        }
                    )
                    2 -> RiwayatTab(data.setoran.log)
                }
            } ?: Text("Memuat data...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ProfilTab(data: DetailMahasiswaData) {
    val scrollState = rememberScrollState()
    val info = data.info
    val setoran = data.setoran.info_dasar

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Nama: ${info.nama}", style = MaterialTheme.typography.bodyLarge)
        Text("NIM: ${info.nim}")
        Text("Email: ${info.email}")
        Text("Angkatan: ${info.angkatan}")
        Text("Semester: ${info.semester}")
        Text("Dosen PA: ${info.dosen_pa.nama}")

        Spacer(modifier = Modifier.height(12.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))

        Text("📚 Informasi Setoran", style = MaterialTheme.typography.titleMedium)
        Text("Total Wajib Setor: ${setoran.total_wajib_setor}")
        Text("Total Sudah Setor: ${setoran.total_sudah_setor}")
        Text("Total Belum Setor: ${setoran.total_belum_setor}")
        Text("Progres Hafalan: ${setoran.persentase_progres_setor}%")
        Text("Terakhir Setor: ${setoran.terakhir_setor}")
    }
}

@Composable
fun DetailSetoranTab(
    detailList: List<DetailKomponenSetoran>,
    nim: String,
    onValidasiClicked: (id: String, nama: String, nim: String) -> Unit,
    onHapusClicked: (id: String, nama: String, nim: String, infoId: String?) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("📘 Komponen Setoran", style = MaterialTheme.typography.titleMedium)
        detailList.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("${item.nama} [${item.label}]", style = MaterialTheme.typography.titleSmall)
                    Text(if (item.sudah_setor) "✅ Sudah disetor" else "❌ Belum disetor")

                    item.info_setoran?.let {
                        Text("📅 Disetor: ${it.tgl_setoran}")
                        Text("✅ Divalidasi: ${it.tgl_validasi}")
                        Text("👤 Dosen: ${it.dosen_yang_mengesahkan.nama}")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    if (!item.sudah_setor) {
                        Button(onClick = {
                            onValidasiClicked(item.id, item.nama, nim)
                        }) {
                            Text("Validasi")
                        }
                    } else {
                        Button(
                            onClick = {
                                onHapusClicked(item.id, item.nama, nim, item.info_setoran?.id)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Batalkan Setoran")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RiwayatTab(logList: List<LogSetoran>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("🕓 Riwayat Setoran", style = MaterialTheme.typography.titleMedium)
        logList.forEach { log ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("• ${log.aksi.uppercase()} - ${log.keterangan}", style = MaterialTheme.typography.bodyLarge)
                    Text("👤 Oleh: ${log.dosen_yang_mengesahkan.nama}")
                    Text("🕒 Waktu: ${log.timestamp}")
                }
            }
        }
    }
}
