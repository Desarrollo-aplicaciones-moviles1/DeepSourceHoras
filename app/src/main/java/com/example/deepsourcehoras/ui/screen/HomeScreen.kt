package com.example.deepsourcehoras.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deepsourcehoras.model.HomeUiState
import com.example.deepsourcehoras.model.Proyecto
import com.example.deepsourcehoras.model.RegistroHoras
import com.example.deepsourcehoras.ui.theme.DeepSourceHorasTheme
import com.example.deepsourcehoras.ui.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    userName: String,
    onRegistrarHoras: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    HomeScreen(
        state = state,
        userName = userName,
        onRegistrarHoras = onRegistrarHoras
    )
}

// 2) Solo dibuja
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    userName: String,
    onRegistrarHoras: () -> Unit
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text("Hola, $userName") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Button(
                onClick = onRegistrarHoras,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar horas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Proyectos activos", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    state.proyectos.forEach { proyecto ->
                        ProyectoItem(proyecto)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", style = MaterialTheme.typography.titleSmall)
                        Text("${state.totalHoras}h", style = MaterialTheme.typography.titleSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Últimos registros", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    state.registros.forEachIndexed { index, registro ->
                        if (index > 0) HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        RegistroItem(registro)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ProyectoItem(proyecto: Proyecto) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(proyecto.nombre)
            Text("${proyecto.horas}h")
        }
        LinearProgressIndicator(
            progress = { proyecto.progreso },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
    }
}

@Composable
private fun RegistroItem(registro: RegistroHoras) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(registro.tarea)
            Text(registro.detalle, style = MaterialTheme.typography.bodySmall)
        }
        Text(registro.duracion, modifier = Modifier.padding(start = 8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DeepSourceHorasTheme {
        HomeScreen(
            state = HomeUiState(
                proyectos = listOf(Proyecto("Rediseño Web", 24, 0.65f)),
                registros = listOf(RegistroHoras("Maquetado", "Rediseño Web · Hoy", "2h 15min"))
            ),
            userName = "Usuario",
            onRegistrarHoras = {}
        )
    }
}