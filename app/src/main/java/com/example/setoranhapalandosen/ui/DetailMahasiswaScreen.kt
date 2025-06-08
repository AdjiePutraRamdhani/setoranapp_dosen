package com.example.setoranhapalandosen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.setoranhapalandosen.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.border // Pastikan ini ada
import androidx.compose.ui.graphics.Brush // Pastikan ini ada
import androidx.compose.foundation.shape.RoundedCornerShape // Pastikan ini ada (jika diperlukan untuk border shape TabRow)

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

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.latar),
            contentDescription = "Background Layar Detail Mahasiswa",
            modifier = Modifier
                .fillMaxSize()
                .alpha(1.0f), // Alpha tetap 1.0f sesuai permintaan terakhir
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { /* Anda bisa menambahkan judul di sini jika diinginkan, misal: Text("Detail Mahasiswa") */ },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent) // Tetap transparan agar background terlihat
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            containerColor = Color.Transparent, // Scaffold harus transparan
            contentColor = MaterialTheme.colorScheme.onSurface // Warna default untuk konten Scaffold
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                // ⭐ START: Implementasi Border Gradient untuk TabRow
                val tabRowGradientBrush = remember {
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF4B00A0), Color(0xFF8A00C8)) // Warna gradient yang Anda inginkan
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp, // Ketebalan border
                            brush = tabRowGradientBrush, // Brush gradient untuk border
                            shape = RoundedCornerShape(8.dp) // Bentuk border, misalnya dengan sudut membulat
                        )
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = MaterialTheme.colorScheme.surface, // Background internal TabRow
                        contentColor = MaterialTheme.colorScheme.primary // Warna teks tab aktif
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Profil") },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Detail Setoran") },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("Riwayat") },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                // ⭐ END: Implementasi Border Gradient untuk TabRow

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
                        else -> Text("Error: Tab tidak dikenal", color = MaterialTheme.colorScheme.error)
                    }
                } ?: Text(
                    "Memuat data...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

// --- Fungsi ProfilTab, DetailSetoranTab, RiwayatTab (sama seperti yang sudah ada) ---
// (Tidak perlu diubah lagi karena sudah memiliki border gradient pada masing-masing Card di dalamnya)

@Composable
fun ProfilTab(data: DetailMahasiswaData) {
    val scrollState = rememberScrollState()
    val info = data.info
    val setoran = data.setoran.info_dasar

    val gradientBrush = remember {
        Brush.linearGradient(
            colors = listOf(Color(0xFF4B00A0), Color(0xFF8A00C8))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    brush = gradientBrush,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Informasi Mahasiswa", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Nama: ${info.nama}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("NIM: ${info.nim}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Email: ${info.email}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Angkatan: ${info.angkatan}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Semester: ${info.semester}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Dosen PA: ${info.dosen_pa.nama}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    brush = gradientBrush,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📚 Informasi Setoran", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Total Wajib Setor: ${setoran.total_wajib_setor}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Total Sudah Setor: ${setoran.total_sudah_setor}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Total Belum Setor: ${setoran.total_belum_setor}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Progres Hafalan: ${setoran.persentase_progres_setor}%", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Terakhir Setor: ${setoran.terakhir_setor}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
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
    val gradientBrush = remember {
        Brush.linearGradient(
            colors = listOf(Color(0xFF4B00A0), Color(0xFF8A00C8))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "📘 Komponen Setoran",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        detailList.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        brush = gradientBrush,
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("${item.nama} [${item.label}]", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(if (item.sudah_setor) "✅ Sudah disetor" else "❌ Belum disetor", color = MaterialTheme.colorScheme.onPrimaryContainer)

                        item.info_setoran?.let {
                            Text("📅 Disetor: ${it.tgl_setoran}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("✅ Divalidasi: ${it.tgl_validasi}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("👤 Dosen: ${it.dosen_yang_mengesahkan.nama}", color = MaterialTheme.colorScheme.onPrimaryContainer)
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
}

@Composable
fun RiwayatTab(logList: List<LogSetoran>) {
    val scrollState = rememberScrollState()
    val gradientBrush = remember {
        Brush.linearGradient(
            colors = listOf(Color(0xFF4B00A0), Color(0xFF8A00C8))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "🕓 Riwayat Setoran",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        logList.forEach { log ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        brush = gradientBrush,
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("• ${log.aksi.uppercase()} - ${log.keterangan}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("👤 Oleh: ${log.dosen_yang_mengesahkan.nama}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("🕒 Waktu: ${log.timestamp}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
        }
    }
}