package com.example.setoranhapalandosen.ui

import androidx.compose.foundation.Image
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(nav: NavHostController, vm: AuthViewModel = hiltViewModel()) {
    var currentScreen by remember { mutableStateOf("beranda") }

    val nama by vm.nama.collectAsState()
    val email by vm.email.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                                            .size(28.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Dashboard Dosen",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                            currentScreen == "profil" -> {
                                Text(
                                    text = "Profil Dosen",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            currentScreen.startsWith("detail_mahasiswa/") -> {
                                Text(
                                    text = "Detail Mahasiswa",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            else -> {
                                Text(
                                    text = "",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == "beranda",
                    onClick = { currentScreen = "beranda" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    label = { Text("Beranda", fontSize = 12.sp) }
                )
                NavigationBarItem(
                    selected = currentScreen == "profil",
                    onClick = { currentScreen = "profil" },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profil") },
                    label = { Text("Profil", fontSize = 12.sp) }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
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
    }
}