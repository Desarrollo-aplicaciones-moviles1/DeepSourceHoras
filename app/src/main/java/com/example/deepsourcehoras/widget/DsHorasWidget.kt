package com.example.deepsourcehoras.widget

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.deepsourcehoras.MainActivity
import com.example.deepsourcehoras.R
import com.example.deepsourcehoras.model.CronometroStorage

// Señal para que la app abra el formulario después del login
const val EXTRA_ABRIR_REGISTRO = "abrir_registro"

// Colores del prototipo
private val FondoWidget = Color(0xFF252338)
private val FondoInterno = Color(0xFF1A1828)
private val TextoPrincipal = Color(0xFFC9C6DA)
private val TextoSecundario = Color(0xFF8A87A0)
private val MoradoClaro = Color(0xFF9B8AD4)
private val Morado = Color(0xFF6B5AAD)
private val MoradoOscuro = Color(0xFF3A3560)

// Mismo color de día y de noche (la versión simple de ColorProvider no es pública)
private fun colorFijo(color: Color) = ColorProvider(day = color, night = color)

class DsHorasWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val inicio = CronometroStorage.inicioMillis(context)
        val proyecto = CronometroStorage.proyecto(context)

        // Si hay un cronómetro corriendo, se arma el reloj nativo que avanza solo
        val cronometro: RemoteViews? = if (inicio > 0L) {
            RemoteViews(context.packageName, R.layout.widget_cronometro).apply {
                val transcurrido = System.currentTimeMillis() - inicio
                val base = SystemClock.elapsedRealtime() - transcurrido
                setChronometer(R.id.cronometro, base, null, true)
            }
        } else {
            null
        }

        provideContent {
            ContenidoWidget(proyecto = proyecto, cronometro = cronometro)
        }
    }
}

@Composable
private fun ContenidoWidget(proyecto: String, cronometro: RemoteViews?) {
    val context = LocalContext.current
    val abrirFormulario = actionStartActivity(
        Intent(context, MainActivity::class.java).putExtra(EXTRA_ABRIR_REGISTRO, true)
    )

    // Datos de ejemplo, los mismos del Home
    val proyectos = listOf(
        "Rediseño Web" to "24h",
        "App Móvil" to "38h",
        "Dashboard" to "12h",
        "API Integración" to "7h"
    )

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(colorFijo(FondoWidget))
            .cornerRadius(20.dp)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                provider = ImageProvider(R.drawable.logo_deepsource),
                contentDescription = "Deepsource",
                contentScale = ContentScale.Fit,
                modifier = GlanceModifier.width(48.dp).height(16.dp)
            )
            Spacer(modifier = GlanceModifier.width(8.dp))
            Text(
                text = "DS HORAS",
                style = TextStyle(color = colorFijo(TextoSecundario), fontSize = 10.sp)
            )
        }

        Spacer(modifier = GlanceModifier.height(8.dp))

        proyectos.forEach { (nombre, horas) ->
            Row(
                modifier = GlanceModifier.fillMaxWidth().padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nombre,
                    modifier = GlanceModifier.defaultWeight(),
                    style = TextStyle(color = colorFijo(TextoPrincipal), fontSize = 13.sp)
                )
                Text(
                    text = horas,
                    style = TextStyle(
                        color = colorFijo(MoradoClaro),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        Spacer(modifier = GlanceModifier.height(10.dp))

        if (cronometro == null) {
            // En reposo
            Button(
                text = "Registrar horas",
                onClick = abrirFormulario,
                modifier = GlanceModifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorFijo(Morado),
                    contentColor = colorFijo(Color.White)
                )
            )
        } else {
            // En curso: proyecto, tiempo y botón Finalizar
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .background(colorFijo(FondoInterno))
                    .cornerRadius(12.dp)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "INICIO",
                    modifier = GlanceModifier.defaultWeight(),
                    style = TextStyle(color = colorFijo(TextoSecundario), fontSize = 11.sp)
                )
                Text(
                    text = proyecto.ifBlank { "Sin proyecto" },
                    style = TextStyle(color = colorFijo(MoradoClaro), fontSize = 12.sp)
                )
                Spacer(modifier = GlanceModifier.width(8.dp))
                AndroidRemoteViews(
                    remoteViews = cronometro,
                    modifier = GlanceModifier.width(72.dp).height(24.dp)
                )
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            Button(
                text = "Finalizar",
                onClick = abrirFormulario,
                modifier = GlanceModifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorFijo(MoradoOscuro),
                    contentColor = colorFijo(Color.White)
                )
            )
        }
    }
}