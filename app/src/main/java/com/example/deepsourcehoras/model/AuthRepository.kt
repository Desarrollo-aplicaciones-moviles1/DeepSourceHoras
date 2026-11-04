package com.example.deepsourcehoras.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AuthRepository {

    // Simula la espera de una respuesta de servidor
    suspend fun loginUser(email: String, password: String): LoginResult =
        withContext(Dispatchers.IO) {
            try {
                delay(2000)
                if (email == "usuario@deepsource.io" && password == "123456") {
                    LoginResult.Success("fake-jwt-123", "Usuario")
                } else {
                    LoginResult.Error(Exception("Credenciales incorrectas"))
                }
            } catch (e: Exception) {
                LoginResult.Error(e)
            }
        }
}