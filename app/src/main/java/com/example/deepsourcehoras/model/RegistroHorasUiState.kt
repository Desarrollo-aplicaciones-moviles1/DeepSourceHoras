package com.example.deepsourcehoras.model

data class RegistroHorasUiState(
    val proyecto: String = "",
    val dia: String = "",
    val horas: String = "",
    val minutos: String = "",
    val tarea: String = "",
    val proyectos: List<String> = emptyList(),
    val errores: RegistroHorasErrores = RegistroHorasErrores()
)