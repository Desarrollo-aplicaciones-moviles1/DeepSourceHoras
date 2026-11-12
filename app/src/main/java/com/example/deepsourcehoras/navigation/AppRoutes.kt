package com.example.deepsourcehoras.navigation

import android.net.Uri

object AppRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val HOME = "home/{userName}"
    const val REGISTRO = "registro"
    const val MIS_PROYECTOS = "mis_proyectos"
    const val CONFIRMACION = "confirmacion/{proyecto}/{horas}/{minutos}/{dia}"

    fun home(userName: String) = "home/${Uri.encode(userName)}"

    fun confirmacion(proyecto: String, horas: Int, minutos: Int, dia: String) =
        "confirmacion/${Uri.encode(proyecto)}/$horas/$minutos/${Uri.encode(dia)}"
}