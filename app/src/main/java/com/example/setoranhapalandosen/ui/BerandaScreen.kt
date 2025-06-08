package com.example.setoranhapalandosen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.setoranhapalandosen.R
import com.example.setoranhapalandosen.viewmodel.AuthViewModel
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxHeight

// Import tambahan yang diperlukan untuk alpha
import androidx.compose.ui.draw.alpha

// Import tambahan yang diperlukan
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.foundation.shape.RoundedCornerShape

// Import tambahan untuk border
import androidx.compose.foundation.border // ⭐ Pastikan ini diimport

// Import tambahan yang mungkin dibutuhkan untuk ikon dropdown jika Anda ingin menambahkannya di DropdownMenuFilter
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown


@Composable
fun BerandaScreen(
    nav: NavHostController,
    vm: AuthViewModel = hiltViewModel(),
    onMahasiswaClick: (String) -> Unit
) {
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

    val capsuleHeight = 56.dp // Tinggi yang konsisten untuk kedua elemen

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.latar),
            contentDescription = "Background Layar Utama",
            modifier = Modifier
                .fillMaxSize()
                .alpha(1.0f),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Cari nama") },
                        modifier = Modifier
                            .weight(0.7f)
                            .height(capsuleHeight),
                        shape = RoundedCornerShape(50),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF0F0F0),
                            unfocusedContainerColor = Color(0xFFF0F0F0),
                            disabledContainerColor = Color(0xFFF0F0F0),
                            focusedBorderColor = Color(0xFF4B00A0),
                            unfocusedBorderColor = Color(0xFF8A00C8),
                            focusedLabelColor = Color.DarkGray,
                            unfocusedLabelColor = Color.Gray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    DropdownMenuFilter(
                        items = listAngkatan,
                        selected = selectedAngkatan,
                        onSelected = { selectedAngkatan = it },
                        modifier = Modifier
                            .weight(0.3f)
                            .height(capsuleHeight)
                    )
                }
            }

            item {
                Text(
                    "Daftar Mahasiswa Bimbingan:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            items(filteredMahasiswa) { mhs ->
                // ⭐ START: Bungkus Surface dengan Box untuk menambahkan border putih
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp, // Ketebalan border
                            color = Color.White, // Warna border putih
                            shape = MaterialTheme.shapes.medium // Bentuk border (sesuaikan dengan bentuk Surface)
                        )
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                println("Klik: ${mhs.nama}")
                                onMahasiswaClick(mhs.nim)
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
                }
                // ⭐ END: Bungkus Surface dengan Box untuk menambahkan border putih
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DropdownMenuFilter(
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFF0F0F0),
                contentColor = Color.Black
            ),
            border = BorderStroke(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF4B00A0),
                        Color(0xFF8A00C8)
                    )
                )
            ),
            shape = RoundedCornerShape(50)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = selected)
                // Anda bisa menambahkan ikon dropdown di sini jika ingin tampilannya seperti dropdown sungguhan
                // Icon(Icons.Default.ArrowDropDown, contentDescription = "Panah Dropdown")
            }
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