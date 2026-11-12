package com.example.deepsourcehoras.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deepsourcehoras.model.FiltroProyectos
import com.example.deepsourcehoras.model.MisProyectosUiState
import com.example.deepsourcehoras.model.ProyectoDetalle
import com.example.deepsourcehoras.ui.components.BarraProgreso
import com.example.deepsourcehoras.ui.theme.DeepSourceHorasTheme
import com.example.deepsourcehoras.ui.viewmodel.MisProyectosViewModel


@Composable
fun MisProyectosScreen(
    onBack: () -> Unit,
    viewModel: MisProyectosViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    MisProyectosScreen(
        state = state,
        onBack = onBack,
        onFiltroChange = viewModel::onFiltroChange
    )
}

// 2) Solo dibuja
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisProyectosScreen(
    state: MisProyectosUiState,
    onBack: () -> Unit,
    onFiltroChange: (FiltroProyectos) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis proyectos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.filtro == FiltroProyectos.EN_CURSO,
                    onClick = { onFiltroChange(FiltroProyectos.EN_CURSO) },
                    label = { Text("En curso") }
                )
                FilterChip(
                    selected = state.filtro == FiltroProyectos.FINALIZADOS,
                    onClick = { onFiltroChange(FiltroProyectos.FINALIZADOS) },
                    label = { Text("Finalizados") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.visibles) { proyecto ->
                    ProyectoCard(proyecto)
                }
            }
        }
    }
}

@Composable
private fun ProyectoCard(proyecto: ProyectoDetalle) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(proyecto.nombre, style = MaterialTheme.typography.titleMedium)
                Row {
                    Text(
                        text = "${proyecto.horas}h",
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (proyecto.fijado) {
                        Text("📌", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
            Text("Cliente: ${proyecto.cliente}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))

            if (proyecto.fechaCierre == null) {
                // En curso: barra de progreso
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BarraProgreso(
                        progreso = proyecto.progreso,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${(proyecto.progreso * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            } else {
                // Finalizado: estado y fecha de cierre
                HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFF1E3A2B)
                    ) {
                        Text(
                            text = "Finalizado",
                            color = Color(0xFF6FCF97),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = "Cerrado ${proyecto.fechaCierre}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MisProyectosScreenPreview() {
    DeepSourceHorasTheme {
        MisProyectosScreen(
            state = MisProyectosUiState(
                proyectos = listOf(
                    ProyectoDetalle("Rediseño Web", "AFC", 24, 0.65f, fijado = true),
                    ProyectoDetalle("Portal Cliente", "AFC", 3, 0.10f)
                )
            ),
            onBack = {},
            onFiltroChange = {}
        )
    }
}