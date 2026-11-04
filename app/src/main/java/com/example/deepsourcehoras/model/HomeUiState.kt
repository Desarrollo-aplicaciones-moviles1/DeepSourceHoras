package com.example.deepsourcehoras.model

data class Proyecto(
    val nombre: String,
    val horas: Int,
    val progreso: Float
)

data class RegistroHoras(
    val tarea: String,
    val detalle: String,
    val duracion: String
)

data class HomeUiState(
    val proyectos: List<Proyecto> = emptyList(),
    val registros: List<RegistroHoras> = emptyList()
) {
    val totalHoras: Int get() = proyectos.sumOf { it.horas }
}