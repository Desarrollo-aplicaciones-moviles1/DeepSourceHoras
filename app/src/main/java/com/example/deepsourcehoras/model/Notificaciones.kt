package com.example.deepsourcehoras.model

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

// Recurso nativo: notificaciones de Android
object Notificaciones {
    private const val CANAL_ID = "horas_registradas"
    private const val NOTIFICACION_ID = 1

    @SuppressLint("MissingPermission") // se revisa con areNotificationsEnabled()
    fun mostrarHorasRegistradas(context: Context, proyecto: String, horas: Int, minutos: Int) {
        val manager = NotificationManagerCompat.from(context)

        // Si el usuario no aceptó el permiso (Android 13+), no se muestra nada
        if (!manager.areNotificationsEnabled()) return

        // Desde Android 8 toda notificación necesita un canal
        manager.createNotificationChannel(
            NotificationChannelCompat.Builder(CANAL_ID, NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setName("Horas registradas")
                .build()
        )

        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Horas registradas")
            .setContentText("${horas}h ${minutos}min en $proyecto")
            .setAutoCancel(true)
            .build()

        manager.notify(NOTIFICACION_ID, notificacion)
    }
}