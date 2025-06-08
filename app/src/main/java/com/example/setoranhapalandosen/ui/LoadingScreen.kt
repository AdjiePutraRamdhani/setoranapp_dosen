package com.example.setoranhapalandosen.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image // ⭐ Import Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale // ⭐ Import ContentScale
import androidx.compose.ui.res.painterResource // ⭐ Import painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.setoranhapalandosen.R // ⭐ Pastikan ini mengarah ke R.drawable.latar

@Composable
fun LoadingScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    val alphaAnim = rememberInfiniteTransition()
    val animatedAlpha by alphaAnim.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(), // Hapus background gradient di sini dulu
        contentAlignment = Alignment.Center
    ) {
        // ⭐ START: Background Image
        Image(
            painter = painterResource(id = R.drawable.sabarimage), // Ganti dengan ID gambar latar Anda
            contentDescription = "Background Loading Screen",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.4f), // Sesuaikan opasitas sesuai keinginan Anda (0.0f - 1.0f)
            contentScale = ContentScale.Crop // Pastikan gambar mengisi seluruh area dan dipotong jika perlu
        )
        // ⭐ END: Background Image

        // ⭐ Lapisi dengan background gradient untuk memberikan efek overlay pada gambar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), // Warna atas (transparan)
                            MaterialTheme.colorScheme.background.copy(alpha = 0.7f) // Warna bawah (sedikit lebih solid)
                        )
                    )
                )
        )

        // Konten loading (Lottie dan teks) tetap di tengah
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp) // Padding untuk konten
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Mohon tunggu sebentar \uD83D\uDE4F",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Memuat data hafalan...",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.alpha(animatedAlpha)
            )
        }
    }
}