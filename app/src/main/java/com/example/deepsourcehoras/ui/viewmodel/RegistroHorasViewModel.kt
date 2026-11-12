package com.example.deepsourcehoras.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.deepsourcehoras.model.CronometroStorage
import com.example.deepsourcehoras.model.RegistroHorasErrores
import com.example.deepsourcehoras.model.RegistroHorasUiState
import com.example.deepsourcehoras.widget.DsHorasWidget
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistroHorasViewModel(application: Application) : AndroidViewModel(application) {

    private val contexto: Context = application.applicationContext

    // Datos de ejemplo
    private val _uiState = MutableStateFlow(
        RegistroHorasUiState(
            dia = hoy(),
            proyectos = listOf("Rediseño Web", "App Móvil", "Dashboard", "API Integración")
        )
    )
    val uiState: StateFlow<RegistroHorasUiState> = _uiState.asStateFlow()

    private var cronometroJob: Job? = null
    private var inicioMillis = 0L

    init {
        restaurarCronometro()
    }

    private fun hoy(): String =
        SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("es-CL")).format(Date())

    private fun hora(millis: Long): String =
        SimpleDateFormat("HH:mm", Locale.forLanguageTag("es-CL")).format(Date(millis))

    private fun actualizarWidget() {
        viewModelScope.launch { DsHorasWidget().updateAll(contexto) }
    }

    fun onProyectoChange(valor: String) {
        // Si el cronómetro está corriendo, el widget también muestra el cambio
        if (_uiState.value.corriendo) {
            CronometroStorage.guardarProyecto(contexto, valor)
            actualizarWidget()
        }
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
            it.copy(
                horas = limpio,
                horaInicio = null, // si edita a mano, ya no es el tiempo calculado
                errores = it.errores.copy(horas = null, tiempo = null)
            )
        }
    }

    fun onMinutosChange(valor: String) {
        val limpio = valor.filter { it.isDigit() }.take(2)
        _uiState.update {
            it.copy(
                minutos = limpio,
                horaInicio = null,
                errores = it.errores.copy(minutos = null, tiempo = null)
            )
        }
    }

    fun onTareaChange(valor: String) {
        _uiState.update {
            it.copy(tarea = valor, errores = it.errores.copy(tarea = null))
        }
    }

    // ---------- Cronómetro ----------

    // Si la app se cerró con el cronómetro corriendo, lo retoma donde iba
    private fun restaurarCronometro() {
        val inicio = CronometroStorage.inicioMillis(contexto)
        if (inicio <= 0L) return

        inicioMillis = inicio
        _uiState.update {
            it.copy(
                corriendo = true,
                proyecto = CronometroStorage.proyecto(contexto),
                horaInicio = hora(inicio),
                segundos = segundosTranscurridos()
            )
        }
        empezarConteo()
    }

    private fun segundosTranscurridos(): Int =
        ((System.currentTimeMillis() - inicioMillis) / 1000).toInt()

    private fun empezarConteo() {
        cronometroJob?.cancel()
        cronometroJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                // Se calcula desde la hora de inicio para que no se desfase
                _uiState.update { it.copy(segundos = segundosTranscurridos()) }
            }
        }
    }

    fun onIniciarClick() {
        if (_uiState.value.corriendo) return

        // El cronómetro necesita un proyecto, porque el widget lo muestra
        if (_uiState.value.proyecto.isBlank()) {
            _uiState.update {
                it.copy(errores = it.errores.copy(proyecto = "Selecciona un proyecto para iniciar"))
            }
            return
        }

        inicioMillis = System.currentTimeMillis()
        CronometroStorage.iniciar(contexto, inicioMillis, _uiState.value.proyecto)
        actualizarWidget()

        _uiState.update {
            it.copy(
                corriendo = true,
                segundos = 0,
                horaInicio = hora(inicioMillis),
                horas = "",
                minutos = "",
                errores = it.errores.copy(horas = null, minutos = null, tiempo = null)
            )
        }
        empezarConteo()
    }

    fun onFinalizarClick() {
        cronometroJob?.cancel()
        val segundos = segundosTranscurridos()
        // Se redondea hacia arriba para que cualquier tiempo cuente al menos 1 minuto
        val totalMinutos = (segundos + 59) / 60

        CronometroStorage.detener(contexto)
        actualizarWidget()

        _uiState.update {
            it.copy(
                corriendo = false,
                segundos = segundos,
                horas = (totalMinutos / 60).toString(),
                minutos = (totalMinutos % 60).toString()
            )
        }
    }

    // ---------- Registro ----------

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