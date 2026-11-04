package com.example.deepsourcehoras.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deepsourcehoras.model.AuthRepository
import com.example.deepsourcehoras.model.LoginErrores
import com.example.deepsourcehoras.model.LoginResult
import com.example.deepsourcehoras.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(valor: String) {
        _uiState.update {
            it.copy(
                email = valor,
                errores = it.errores.copy(email = null),
                mensajeError = null
            )
        }
    }

    fun onPasswordChange(valor: String) {
        _uiState.update {
            it.copy(
                password = valor,
                errores = it.errores.copy(password = null),
                mensajeError = null
            )
        }
    }

    private fun validar(): Boolean {
        val estado = _uiState.value

        val errorEmail = when {
            estado.email.isBlank() -> "Ingresa tu correo"
            !Patterns.EMAIL_ADDRESS.matcher(estado.email.trim()).matches() -> "Correo no válido"
            else -> null
        }
        val errorPassword = if (estado.password.isBlank()) "Ingresa tu contraseña" else null

        _uiState.update {
            it.copy(errores = LoginErrores(email = errorEmail, password = errorPassword))
        }
        return errorEmail == null && errorPassword == null
    }

    fun onLoginClick(onSuccess: (String) -> Unit) {
        if (!validar()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, mensajeError = null) }

            val estado = _uiState.value
            when (val result = repository.loginUser(estado.email.trim(), estado.password)) {
                is LoginResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess(result.userName)
                }
                is LoginResult.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, mensajeError = "Correo o contraseña incorrectos")
                    }
                }
            }
        }
    }
}