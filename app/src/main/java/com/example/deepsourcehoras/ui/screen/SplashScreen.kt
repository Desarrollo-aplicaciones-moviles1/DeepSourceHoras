package com.example.deepsourcehoras.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deepsourcehoras.R
import com.example.deepsourcehoras.ui.theme.DeepSourceHorasTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTerminado: () -> Unit) {
    // Espera 2 segundos y pasa al login
    LaunchedEffect(Unit) {
        delay(2000)
        onTerminado()
    }

    SplashContenido()
}

// Solo dibuja
@Composable
private fun SplashContenido() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_deepsource),
                    contentDescription = "Deepsource",
                    modifier = Modifier.width(120.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "DS Horas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    DeepSourceHorasTheme {
        SplashContenido()
    }
}