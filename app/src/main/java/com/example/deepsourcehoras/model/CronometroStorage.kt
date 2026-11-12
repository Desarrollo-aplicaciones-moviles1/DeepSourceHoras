package com.example.deepsourcehoras.model

import android.content.Context

// Guarda el cronómetro en el teléfono para que la app y el widget vean lo mismo.
// Si inicioMillis es 0, no hay cronómetro corriendo.
object CronometroStorage {
    private const val ARCHIVO = "cronometro"
    private const val INICIO = "inicio_millis"
    private const val PROYECTO = "proyecto"

    private fun prefs(context: Context) =
        context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    fun iniciar(context: Context, inicioMillis: Long, proyecto: String) {
        prefs(context).edit()
            .putLong(INICIO, inicioMillis)
            .putString(PROYECTO, proyecto)
            .apply()
    }

    fun guardarProyecto(context: Context, proyecto: String) {
        prefs(context).edit().putString(PROYECTO, proyecto).apply()
    }

    fun detener(context: Context) {
        prefs(context).edit().remove(INICIO).remove(PROYECTO).apply()
    }

    fun inicioMillis(context: Context): Long = prefs(context).getLong(INICIO, 0L)

    fun proyecto(context: Context): String = prefs(context).getString(PROYECTO, "") ?: ""
}