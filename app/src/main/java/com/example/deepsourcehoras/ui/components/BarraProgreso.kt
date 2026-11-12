package com.example.deepsourcehoras.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

// Barra que parte vacía y se llena hasta su valor al aparecer en pantalla
@Composable
fun BarraProgreso(
    progreso: Float,
    modifier: Modifier = Modifier
) {
    var iniciar by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { iniciar = true }

    val animado by animateFloatAsState(
        targetValue = if (iniciar) progreso else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "progreso"
    )

    LinearProgressIndicator(
        progress = { animado },
        modifier = modifier
    )
}