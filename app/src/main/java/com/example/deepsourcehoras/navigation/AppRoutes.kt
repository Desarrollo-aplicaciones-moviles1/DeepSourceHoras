package com.example.deepsourcehoras.navigation

import android.net.Uri

object AppRoutes {
    const val LOGIN = "login"
    const val HOME = "home/{userName}"
    const val REGISTRO = "registro"

    fun home(userName: String) = "home/${Uri.encode(userName)}"
}