package com.example.setoranhapalandosen.ui

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.setoranhapalandosen.viewmodel.AuthViewModel
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.setoranhapalandosen.viewmodel.LoadingStatus


@Composable
fun DashboardScreen(nav: NavHostController, vm: AuthViewModel = hiltViewModel()) {
    val nama by vm.nama.collectAsState()
    val email by vm.email.collectAsState()
    val nip by vm.nip.collectAsState()
    val status by vm.status.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (status == LoadingStatus.LOADING) {
            CircularProgressIndicator()
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Nama: $nama", style = MaterialTheme.typography.bodyLarge)
                    Text("Email: $email", style = MaterialTheme.typography.bodyLarge)
                    Text("NIP: $nip", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
