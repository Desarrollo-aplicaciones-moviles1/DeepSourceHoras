package com.example.deepsourcehoras.ui.screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deepsourcehoras.model.RegistroHorasUiState
import com.example.deepsourcehoras.ui.components.CampoTexto
import com.example.deepsourcehoras.ui.theme.DeepSourceHorasTheme
import com.example.deepsourcehoras.ui.viewmodel.RegistroHorasViewModel


@Composable
fun RegistroHorasScreen(
    onBack: () -> Unit,
    viewModel: RegistroHorasViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    RegistroHorasScreen(
        state = state,
        onBack = onBack,
        onProyectoChange = viewModel::onProyectoChange,
        onDiaChange = viewModel::onDiaChange,
        onHorasChange = viewModel::onHorasChange,
        onMinutosChange = viewModel::onMinutosChange,
        onTareaChange = viewModel::onTareaChange,
        onRegistrarClick = { viewModel.onRegistrarClick(onBack) }
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
                // El botón "Iniciar" se conectará después con el widget
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Iniciar")
                }
                Button(
                    onClick = onRegistrarClick,
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
            // TEMPORAL: estos 3 campos se cambian por el menú desplegable
            // y el selector de fecha en el siguiente paso
            CampoTexto(
                valor = state.proyecto,
                label = "Proyecto",
                error = state.errores.proyecto,
                onChange = onProyectoChange
            )
            CampoTexto(
                valor = state.dia,
                label = "Día",
                error = state.errores.dia,
                onChange = onDiaChange
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoTexto(
                    valor = state.horas,
                    label = "Horas",
                    error = state.errores.horas,
                    onChange = onHorasChange,
                    tipoTeclado = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                CampoTexto(
                    valor = state.minutos,
                    label = "Minutos",
                    error = state.errores.minutos,
                    onChange = onMinutosChange,
                    tipoTeclado = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
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

@Preview(showBackground = true)
@Composable
fun RegistroHorasScreenPreview() {
    DeepSourceHorasTheme {
        RegistroHorasScreen(
            state = RegistroHorasUiState(dia = "29/10/2026"),
            onBack = {},
            onProyectoChange = {},
            onDiaChange = {},
            onHorasChange = {},
            onMinutosChange = {},
            onTareaChange = {},
            onRegistrarClick = {}
        )
    }
}