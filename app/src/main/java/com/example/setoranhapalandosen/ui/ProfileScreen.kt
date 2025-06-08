package com.example.setoranhapalandosen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.setoranhapalandosen.viewmodel.AuthViewModel

@Composable
fun ProfilScreen(nav: NavHostController, vm: AuthViewModel = hiltViewModel()) {
    val nama by vm.nama.collectAsState()
    val email by vm.email.collectAsState()
    val nip by vm.nip.collectAsState()
    val ringkasan by vm.ringkasan.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nama: $nama")
                Text("Email: $email")
                Text("NIP: $nip")
            }
        }

        // --- MODIFIKASI DIMULAI DI SINI: Memasukkan Ringkasan ke dalam Card ---
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Ringkasan Mahasiswa PA per Angkatan",
                    style = MaterialTheme.typography.titleMedium
                )
                // Memberi sedikit jarak antara judul dan daftar angkatan
                Spacer(modifier = Modifier.height(8.dp))
                if (ringkasan.isEmpty()) {
                    Text("Belum ada data ringkasan mahasiswa.")
                } else {
                    ringkasan.forEach {
                        Text("- Angkatan ${it.tahun}: ${it.total} mahasiswa")
                    }
                }
            }
        }
        // --- MODIFIKASI BERAKHIR DI SINI ---

        Spacer(modifier = Modifier.weight(1f)) // mendorong tombol ke bawah

        Button(
            onClick = {
                vm.logout {
                    nav.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout", color = MaterialTheme.colorScheme.onError)
        }
    }
}