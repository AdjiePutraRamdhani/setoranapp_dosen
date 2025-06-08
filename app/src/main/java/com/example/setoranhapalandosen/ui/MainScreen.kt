package com.example.setoranhapalandosen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.setoranhapalandosen.R
import com.example.setoranhapalandosen.viewmodel.AuthViewModel

// Import tambahan untuk bentuk melengkung dan shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.compose.ui.layout.ContentScale
import android.util.Log
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.BlendMode

// ⭐ Import yang diperlukan untuk alpha()
import androidx.compose.ui.draw.alpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(nav: NavHostController, vm: AuthViewModel = hiltViewModel()) {
    var currentScreen by remember { mutableStateOf("beranda") }

    val nama by vm.nama.collectAsState()
    val email by vm.email.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        // ⭐ PERUBAHAN DI SINI: Menambahkan .alpha() ke modifier Image latar belakang utama
        Image(
            painter = painterResource(id = R.drawable.latar),
            contentDescription = "Background Utama",
            modifier = Modifier
                .fillMaxSize()
                .alpha(1.0f), // Anda bisa mengatur nilai alpha sesuai keinginan Anda
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp, bottom = 65.dp) // Sesuaikan padding agar tidak tumpang tindih
        ) {
            when {
                currentScreen == "beranda" -> {
                    BerandaScreen(
                        nav = nav,
                        vm = vm,
                        onMahasiswaClick = { nim ->
                            currentScreen = "detail_mahasiswa/$nim"
                        }
                    )
                }
                currentScreen == "profil" -> {
                    ProfilScreen(nav = nav, vm = vm)
                }
                currentScreen.startsWith("detail_mahasiswa/") -> {
                    val nim = currentScreen.removePrefix("detail_mahasiswa/")
                    DetailMahasiswaScreen(nim = nim)
                }
            }
        }

        // --- MODIFIKASI TOPAPPBAR UNTUK LATAR BELAKANG GAMBAR DAN WARNA TEKS ---
        Box( // Gunakan Box untuk menumpuk gambar dan TopAppBar
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .shadow(8.dp, RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                .zIndex(1f)
        ) {
            // Gambar latar belakang untuk TopAppBar
            Image(
                painter = painterResource(id = R.drawable.naavigasii),
                contentDescription = "Background TopAppBar",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            // TopAppBar itu sendiri dengan warna transparan
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Menggunakan kondisi `when` untuk menentukan teks dan warnanya
                        when {
                            currentScreen == "beranda" -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.logouinsuskariau),
                                        contentDescription = "Logo UIN",
                                        modifier = Modifier
                                            .size(36.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Dashboard Dosen",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentScreen == "beranda") Color.Black else Color.White
                                    )
                                }
                            }
                            currentScreen == "profil" -> {
                                // --- PERUBAHAN DI SINI: MENAMBAHKAN ICON KE PROFIL DOSEN ---
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.iconosen),
                                        contentDescription = "Ikon Profil",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 8.dp)
                                            .offset(y = (-2).dp),
                                        colorFilter = ColorFilter.tint(Color.Black)
                                    )
                                    Text(
                                        text = "Profil Dosen",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentScreen == "profil") Color.Black else Color.White
                                    )
                                }
                            }
                            currentScreen.startsWith("detail_mahasiswa/") -> {
                                Text(
                                    text = "Detail Mahasiswa",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentScreen.startsWith("detail_mahasiswa/")) Color.Black else Color.White
                                )
                            }
                            else -> {
                                Text(
                                    text = "",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }

        // --- MODIFIKASI NAVIGATIONBAR UNTUK LATAR BELAKANG GAMBAR DAN WARNA TEKS ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .shadow(8.dp, RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .zIndex(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.naavigasii),
                contentDescription = "Background NavigationBar",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent
            ) {
                NavigationBarItem(
                    selected = currentScreen == "beranda",
                    onClick = {
                        currentScreen = "beranda"
                        Log.d("MainScreen", "Current screen set to: $currentScreen")
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.iconberanda),
                            contentDescription = "Beranda",
                            modifier = Modifier.size(24.dp),
                            colorFilter = if (currentScreen == "beranda") ColorFilter.tint(Color.Yellow, BlendMode.SrcAtop) else null
                        )
                    },
                    label = {
                        Text(
                            text = "Beranda",
                            fontSize = 12.sp,
                            color = if (currentScreen == "beranda") Color.Black else Color.White
                        )
                    }
                )
                NavigationBarItem(
                    selected = currentScreen == "profil",
                    onClick = {
                        currentScreen = "profil"
                        Log.d("MainScreen", "Current screen set to: $currentScreen")
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profil",
                            modifier = Modifier.size(24.dp),
                            colorFilter = if (currentScreen == "profil") ColorFilter.tint(Color.Yellow, BlendMode.SrcAtop) else null
                        )
                    },
                    label = {
                        Text(
                            text = "Profil",
                            fontSize = 12.sp,
                            color = if (currentScreen == "profil") Color.Black else Color.White
                        )
                    }
                )
            }
        }
    }
}