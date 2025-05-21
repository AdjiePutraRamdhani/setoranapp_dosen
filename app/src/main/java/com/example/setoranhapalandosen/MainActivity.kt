package com.example.setoranhapalandosen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.setoranhapalandosen.ui.DashboardScreen
import com.example.setoranhapalandosen.ui.LoginScreen
import com.example.setoranhapalandosen.viewmodel.AuthViewModel
import com.example.setoranhapalanmahasiswa.theme.SetoranHapalanmahasiswaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetoranDosenApp()
        }
    }

    @Composable
    fun SetoranDosenApp() {
        val navController = rememberNavController()
        val vm: AuthViewModel = hiltViewModel()

        SetoranHapalanmahasiswaTheme {
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController) }
                composable("dashboard") { DashboardScreen(navController) }
            }
        }
    }
}