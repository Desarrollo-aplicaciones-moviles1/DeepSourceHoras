package com.example.deepsourcehoras.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.deepsourcehoras.model.FiltroProyectos
import com.example.deepsourcehoras.model.MisProyectosUiState
import com.example.deepsourcehoras.model.ProyectoDetalle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MisProyectosViewModel : ViewModel() {

    // Datos de ejemplo
    private val _uiState = MutableStateFlow(
        MisProyectosUiState(
            proyectos = listOf(
                ProyectoDetalle("Rediseño Web", "AFC", 24, 0.65f, fijado = true),
                ProyectoDetalle("App Móvil", "Deepsource", 38, 0.80f, fijado = true),
                ProyectoDetalle("Dashboard", "AFC", 12, 0.30f, fijado = true),
                ProyectoDetalle("API Integración", "Deepsource", 7, 0.15f, fijado = true),
                ProyectoDetalle("Portal Cliente", "AFC", 3, 0.10f),
                ProyectoDetalle("Intranet Soporte", "AFC", 54, fechaCierre = "15/09/2026"),
                ProyectoDetalle("Sitio Corporativo", "Deepsource", 41, fechaCierre = "28/08/2026")
            )
        )
    )
    val uiState: StateFlow<MisProyectosUiState> = _uiState.asStateFlow()

    fun onFiltroChange(filtro: FiltroProyectos) {
        _uiState.update { it.copy(filtro = filtro) }
    }
}