package com.example.deepsourcehoras.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deepsourcehoras.model.RegistroHorasUiState
import com.example.deepsourcehoras.ui.components.CampoDesplegable
import com.example.deepsourcehoras.ui.components.CampoFecha
import com.example.deepsourcehoras.ui.components.CampoTexto
import com.example.deepsourcehoras.ui.theme.DeepSourceHorasTheme
import com.example.deepsourcehoras.ui.viewmodel.RegistroHorasViewModel


@Composable
fun RegistroHorasScreen(
    onBack: () -> Unit,
    onRegistrado: (proyecto: String, horas: Int, minutos: Int, dia: String) -> Unit,
    viewModel: RegistroHorasViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Android 13 o superior: pide el permiso de notificaciones la primera vez
    val pedirPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    RegistroHorasScreen(
        state = state,
        onBack = onBack,
        onProyectoChange = viewModel::onProyectoChange,
        onDiaChange = viewModel::onDiaChange,
        onHorasChange = viewModel::onHorasChange,
        onMinutosChange = viewModel::onMinutosChange,
        onTareaChange = viewModel::onTareaChange,
        onIniciarClick = viewModel::onIniciarClick,
        onFinalizarClick = viewModel::onFinalizarClick,
        onRegistrarClick = {
            viewModel.onRegistrarClick {
                onRegistrado(
                    state.proyecto,
                    state.horas.toIntOrNull() ?: 0,
                    state.minutos.toIntOrNull() ?: 0,
                    state.dia
                )
            }
        }
    )
}

// 2) Solo dibuja
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroHorasScreen(
    state: RegistroHorasUiState,
    onBack: () -> Unit,
    onProyectoChange: (String) -> Unit,
    onDiaChange: (String) -> Unit,
    onHorasChange: (String) -> Unit,
    onMinutosChange: (String) -> Unit,
    onTareaChange: (String) -> Unit,
    onIniciarClick: () -> Unit,
    onFinalizarClick: () -> Unit,
    onRegistrarClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar horas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = if (state.corriendo) onFinalizarClick else onIniciarClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (state.corriendo) "Finalizar" else "Iniciar")
                }
                Button(
                    onClick = onRegistrarClick,
                    enabled = !state.corriendo,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Registrar")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedVisibility(visible = state.corriendo) {
                Text(
                    text = formatearCronometro(state.segundos),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }

            CampoDesplegable(
                valor = state.proyecto,
                label = "Proyecto",
                error = state.errores.proyecto,
                opciones = state.proyectos,
                onSeleccion = onProyectoChange
            )
            CampoFecha(
                valor = state.dia,
                label = "Día",
                error = state.errores.dia,
                onFechaSeleccionada = onDiaChange
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoTexto(
                    valor = state.horas,
                    label = "Horas",
                    error = state.errores.horas,
                    onChange = onHorasChange,
                    tipoTeclado = KeyboardType.Number,
                    enabled = !state.corriendo,
                    modifier = Modifier.weight(1f)
                )
                CampoTexto(
                    valor = state.minutos,
                    label = "Minutos",
                    error = state.errores.minutos,
                    onChange = onMinutosChange,
                    tipoTeclado = KeyboardType.Number,
                    enabled = !state.corriendo,
                    modifier = Modifier.weight(1f)
                )
            }

            if (!state.corriendo) {
                state.horaInicio?.let {
                    Text(
                        text = "Calculado desde las $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            state.errores.tiempo?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            CampoTexto(
                valor = state.tarea,
                label = "Tarea",
                error = state.errores.tarea,
                onChange = onTareaChange
            )
        }
    }
}

// Convierte los segundos a formato 00:00:00
private fun formatearCronometro(segundos: Int): String =
    String.format("%02d:%02d:%02d", segundos / 3600, (segundos % 3600) / 60, segundos % 60)

@Preview(showBackground = true)
@Composable
fun RegistroHorasScreenPreview() {
    DeepSourceHorasTheme {
        RegistroHorasScreen(
            state = RegistroHorasUiState(
                dia = "29/10/2026",
                proyectos = listOf("Rediseño Web", "App Móvil"),
                corriendo = true,
                segundos = 3725
            ),
            onBack = {},
            onProyectoChange = {},
            onDiaChange = {},
            onHorasChange = {},
            onMinutosChange = {},
            onTareaChange = {},
            onIniciarClick = {},
            onFinalizarClick = {},
            onRegistrarClick = {}
        )
    }
}