package com.example.deepsourcehoras.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.deepsourcehoras.model.HomeUiState
import com.example.deepsourcehoras.model.Proyecto
import com.example.deepsourcehoras.model.RegistroHoras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    // Datos de ejemplo
    private val _uiState = MutableStateFlow(
        HomeUiState(
            proyectos = listOf(
                Proyecto("Rediseño Web", 24, 0.65f),
                Proyecto("App Móvil", 38, 0.80f),
                Proyecto("Dashboard", 12, 0.30f),
                Proyecto("API Integración", 7, 0.15f)
            ),
            registros = listOf(
                RegistroHoras("Maquetado pantallas principales", "Rediseño Web · Hoy", "2h 15min"),
                RegistroHoras("Revisión de endpoints", "API Integración · Ayer", "1h 30min"),
                RegistroHoras("Componentes del dashboard", "Dashboard · 06/10", "3h 00min")
            )
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}