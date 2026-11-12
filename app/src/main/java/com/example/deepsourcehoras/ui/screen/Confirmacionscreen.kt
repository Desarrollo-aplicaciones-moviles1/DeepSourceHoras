package com.example.deepsourcehoras.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deepsourcehoras.ui.theme.DeepSourceHorasTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ConfirmacionScreen(
    proyecto: String,
    horas: Int,
    minutos: Int,
    dia: String,
    onVolverInicio: () -> Unit
) {
    // Animación 1: el ícono aparece creciendo con un pequeño rebote
    val escala = remember { Animatable(0f) }
    // Animación 2: cuando termina el rebote, el resto aparece con un fundido
    var mostrarTexto by remember { mutableStateOf(false) }
    val transparencia by animateFloatAsState(
        targetValue = if (mostrarTexto) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "transparencia"
    )

    LaunchedEffect(Unit) {
        escala.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
        mostrarTexto = true
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .size(80.dp)
                    .scale(escala.value)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.alpha(transparencia),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Horas registradas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(proyecto, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text("${horas}h ${minutos}min", style = MaterialTheme.typography.displayMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(formatearFechaLarga(dia), style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onVolverInicio,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al inicio")
                }
            }
        }
    }
}

// "11/11/2026" -> "MIÉRCOLES, 11 DE NOVIEMBRE 2026"
private fun formatearFechaLarga(dia: String): String {
    val locale = Locale.forLanguageTag("es-CL")
    return runCatching {
        val fecha = SimpleDateFormat("dd/MM/yyyy", locale).parse(dia)!!
        SimpleDateFormat("EEEE, d 'de' MMMM yyyy", locale).format(fecha).uppercase(locale)
    }.getOrDefault(dia)
}

@Preview(showBackground = true)
@Composable
fun ConfirmacionScreenPreview() {
    DeepSourceHorasTheme {
        ConfirmacionScreen(
            proyecto = "Rediseño Web",
            horas = 2,
            minutos = 15,
            dia = "11/11/2026",
            onVolverInicio = {}
        )
    }
}