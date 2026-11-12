package com.example.deepsourcehoras.model

enum class FiltroProyectos { EN_CURSO, FINALIZADOS }

data class ProyectoDetalle(
    val nombre: String,
    val cliente: String,
    val horas: Int,
    val progreso: Float = 0f,
    val fijado: Boolean = false,
    val fechaCierre: String? = null // null = en curso
)

data class MisProyectosUiState(
    val filtro: FiltroProyectos = FiltroProyectos.EN_CURSO,
    val proyectos: List<ProyectoDetalle> = emptyList()
) {
    val visibles: List<ProyectoDetalle>
        get() = proyectos.filter {
            (it.fechaCierre == null) == (filtro == FiltroProyectos.EN_CURSO)
        }
}