package com.example.deepsourcehoras.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.deepsourcehoras.model.RegistroHorasErrores
import com.example.deepsourcehoras.model.RegistroHorasUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistroHorasViewModel : ViewModel() {

    // Datos de ejemplo
    private val _uiState = MutableStateFlow(
        RegistroHorasUiState(
            dia = hoy(),
            proyectos = listOf("Rediseño Web", "App Móvil", "Dashboard", "API Integración")
        )
    )
    val uiState: StateFlow<RegistroHorasUiState> = _uiState.asStateFlow()

    private fun hoy(): String =
        SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("es-CL")).format(Date())

    fun onProyectoChange(valor: String) {
        _uiState.update {
            it.copy(proyecto = valor, errores = it.errores.copy(proyecto = null))
        }
    }

    fun onDiaChange(valor: String) {
        _uiState.update {
            it.copy(dia = valor, errores = it.errores.copy(dia = null))
        }
    }

    fun onHorasChange(valor: String) {
        // Solo dígitos, máximo 2
        val limpio = valor.filter { it.isDigit() }.take(2)
        _uiState.update {
            it.copy(horas = limpio, errores = it.errores.copy(horas = null, tiempo = null))
        }
    }

    fun onMinutosChange(valor: String) {
        val limpio = valor.filter { it.isDigit() }.take(2)
        _uiState.update {
            it.copy(minutos = limpio, errores = it.errores.copy(minutos = null, tiempo = null))
        }
    }

    fun onTareaChange(valor: String) {
        _uiState.update {
            it.copy(tarea = valor, errores = it.errores.copy(tarea = null))
        }
    }

    private fun validar(): Boolean {
        val estado = _uiState.value
        val horas = estado.horas.toIntOrNull() ?: 0
        val minutos = estado.minutos.toIntOrNull() ?: 0

        val errores = RegistroHorasErrores(
            proyecto = if (estado.proyecto.isBlank()) "Selecciona un proyecto" else null,
            dia = if (estado.dia.isBlank()) "Selecciona un día" else null,
            horas = if (horas > 24) "Máximo 24 horas" else null,
            minutos = if (minutos > 59) "Máximo 59 minutos" else null,
            tarea = if (estado.tarea.isBlank()) "Escribe el nombre de la tarea" else null,
            tiempo = if (horas == 0 && minutos == 0) "Ingresa un tiempo mayor a cero" else null
        )

        _uiState.update { it.copy(errores = errores) }
        return errores == RegistroHorasErrores()
    }

    fun onRegistrarClick(onSuccess: () -> Unit) {
        if (validar()) onSuccess()
    }
}