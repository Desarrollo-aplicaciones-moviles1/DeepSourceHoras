package com.example.deepsourcehoras.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.deepsourcehoras.ui.screen.HomeScreen
import com.example.deepsourcehoras.ui.screen.LoginScreen
import com.example.deepsourcehoras.ui.screen.RegistroHorasScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN
    ) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { userName ->
                    navController.navigate(AppRoutes.home(userName)) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
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
                onRegistrarHoras = { navController.navigate(AppRoutes.REGISTRO) }
            )
        }

        composable(AppRoutes.REGISTRO) {
            RegistroHorasScreen(onBack = { navController.popBackStack() })
        }
    }
}