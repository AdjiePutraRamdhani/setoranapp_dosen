package com.example.setoranhapalandosen.ui

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.setoranhapalandosen.viewmodel.AuthViewModel
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.setoranhapalandosen.viewmodel.LoadingStatus
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight


@Composable
fun BerandaScreen(nav: NavHostController, vm: AuthViewModel = hiltViewModel()) {
    val daftarMahasiswa by vm.daftarMahasiswa.collectAsState()
    val status by vm.status.collectAsState()

    val ringkasan by vm.ringkasan.collectAsState()
    val listAngkatan = listOf("Semua") + ringkasan.map { it.tahun }

    var searchQuery by remember { mutableStateOf("") }
    var selectedAngkatan by remember { mutableStateOf("Semua") }

    val filteredMahasiswa = daftarMahasiswa.filter {
        (selectedAngkatan == "Semua" || it.angkatan == selectedAngkatan) &&
                it.nama.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cari nama mahasiswa") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            DropdownMenuFilter(
                items = listAngkatan,
                selected = selectedAngkatan,
                onSelected = { selectedAngkatan = it }
            )
        }

        item {
            Text("Daftar Mahasiswa Bimbingan:", style = MaterialTheme.typography.titleMedium)
        }

        items(filteredMahasiswa) { mhs ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        println("Klik: ${mhs.nama}")
                        nav.navigate("detail_mahasiswa/${mhs.nim}")
                    },
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = mhs.nama,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "NIM: ${mhs.nim}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DropdownMenuFilter(
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Angkatan: $selected")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { label ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onSelected(label)
                        expanded = false
                    }
                )
            }
        }
    }
}

