package com.example.deepsourcehoras.model

sealed class LoginResult {
    data class Success(val token: String, val userName: String) : LoginResult()
    data class Error(val exception: Exception) : LoginResult()
}