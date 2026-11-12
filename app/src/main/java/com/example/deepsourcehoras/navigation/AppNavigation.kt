package com.example.deepsourcehoras.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.deepsourcehoras.ui.screen.ConfirmacionScreen
import com.example.deepsourcehoras.ui.screen.HomeScreen
import com.example.deepsourcehoras.ui.screen.LoginScreen
import com.example.deepsourcehoras.ui.screen.MisProyectosScreen
import com.example.deepsourcehoras.ui.screen.RegistroHorasScreen
import com.example.deepsourcehoras.ui.screen.SplashScreen

@Composable
fun AppNavigation(abrirRegistro: Boolean = false) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH,
        // Al avanzar la pantalla entra deslizando; al volver, sale hacia el otro lado
        enterTransition = { slideInHorizontally(tween(350)) { it / 4 } + fadeIn(tween(350)) },
        exitTransition = { slideOutHorizontally(tween(350)) { -it / 4 } + fadeOut(tween(350)) },
        popEnterTransition = { slideInHorizontally(tween(350)) { -it / 4 } + fadeIn(tween(350)) },
        popExitTransition = { slideOutHorizontally(tween(350)) { it / 4 } + fadeOut(tween(350)) }
    ) {
        composable(AppRoutes.SPLASH) {
            SplashScreen(
                onTerminado = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { userName ->
                    navController.navigate(AppRoutes.home(userName)) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                    // Si venía del widget, abre el formulario encima del Home
                    if (abrirRegistro) {
                        navController.navigate(AppRoutes.REGISTRO)
                    }
                }
            )
        }

        composable(
            route = AppRoutes.HOME,
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "Usuario"
            HomeScreen(
                userName = userName,
                onRegistrarHoras = { navController.navigate(AppRoutes.REGISTRO) },
                onVerProyectos = { navController.navigate(AppRoutes.MIS_PROYECTOS) }
            )
        }

        composable(AppRoutes.REGISTRO) {
            RegistroHorasScreen(
                onBack = { navController.popBackStack() },
                onRegistrado = { proyecto, horas, minutos, dia ->
                    // Va a la confirmación y saca el formulario de la pila
                    navController.navigate(
                        AppRoutes.confirmacion(proyecto, horas, minutos, dia)
                    ) {
                        popUpTo(AppRoutes.REGISTRO) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.MIS_PROYECTOS) {
            MisProyectosScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = AppRoutes.CONFIRMACION,
            arguments = listOf(
                navArgument("proyecto") { type = NavType.StringType },
                navArgument("horas") { type = NavType.IntType },
                navArgument("minutos") { type = NavType.IntType },
                navArgument("dia") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            ConfirmacionScreen(
                proyecto = args?.getString("proyecto") ?: "",
                horas = args?.getInt("horas") ?: 0,
                minutos = args?.getInt("minutos") ?: 0,
                dia = args?.getString("dia") ?: "",
                onVolverInicio = { navController.popBackStack() }
            )
        }
    }
}