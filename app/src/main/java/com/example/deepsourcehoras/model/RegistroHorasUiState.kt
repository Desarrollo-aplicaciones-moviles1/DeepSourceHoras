package com.example.deepsourcehoras.model

data class RegistroHorasUiState(
    val proyecto: String = "",
    val dia: String = "",
    val horas: String = "",
    val minutos: String = "",
    val tarea: String = "",
    val proyectos: List<String> = emptyList(),
    // Cronómetro
    val corriendo: Boolean = false,
    val segundos: Int = 0,
    val horaInicio: String? = null,
    val errores: RegistroHorasErrores = RegistroHorasErrores()
)