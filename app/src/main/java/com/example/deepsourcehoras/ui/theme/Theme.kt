package com.example.deepsourcehoras.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DsColorScheme = darkColorScheme(
    primary = DsMorado,
    onPrimary = Color.White,
    primaryContainer = DsBordeActivo,
    onPrimaryContainer = DsTexto,
    secondary = DsMoradoClaro,
    onSecondary = DsFondo,
    secondaryContainer = DsTarjetaDestacada,
    onSecondaryContainer = DsTexto,
    background = DsFondo,
    onBackground = DsTexto,
    surface = DsFondo,
    onSurface = DsTexto,
    surfaceVariant = DsTarjeta,
    onSurfaceVariant = DsMoradoClaro,
    surfaceContainerHighest = DsTarjeta,
    outline = DsBorde
)

@Composable
fun DeepSourceHorasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DsColorScheme,
        typography = Typography,
        content = content
    )
}