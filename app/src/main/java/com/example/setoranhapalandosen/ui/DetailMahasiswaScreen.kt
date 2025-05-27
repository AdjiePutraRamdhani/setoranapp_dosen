package com.example.setoranhapalandosen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.setoranhapalandosen.viewmodel.AuthViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun DetailMahasiswaScreen(nim: String, vm: AuthViewModel = hiltViewModel()) {
    val mahasiswa = vm.daftarMahasiswa.collectAsState().value.find { it.nim == nim }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Detail Mahasiswa") })
        }
    ) { padding ->
        mahasiswa?.let { mhs ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Nama: ${mhs.nama}", style = MaterialTheme.typography.bodyLarge)
                Text("NIM: ${mhs.nim}")
                Text("Angkatan: ${mhs.angkatan}")
                Text("Semester: ${mhs.semester}")
                Text("Progres Hafalan: ${mhs.info_setoran.persentase_progres_setor}%")
                Text("Terakhir Setor: ${mhs.info_setoran.terakhir_setor}")
            }
        } ?: run {
            Text("Data mahasiswa tidak ditemukan.", modifier = Modifier.padding(16.dp))
        }
    }
}
