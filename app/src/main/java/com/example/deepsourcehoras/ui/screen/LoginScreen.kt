package com.example.deepsourcehoras.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deepsourcehoras.model.LoginUiState
import com.example.deepsourcehoras.ui.components.CampoTexto
import com.example.deepsourcehoras.ui.theme.DeepSourceHorasTheme
import com.example.deepsourcehoras.ui.viewmodel.LoginViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.ui.res.painterResource
import com.example.deepsourcehoras.R
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults


@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LoginScreen(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = { viewModel.onLoginClick(onLoginSuccess) }
    )
}

// Solo dibuja
@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_deepsource),
            contentDescription = "Logo Deepsource",
            modifier = Modifier.width(160.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "DS Horas",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Registro de horas",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        CampoTexto(
            valor = state.email,
            label = "Correo electrónico",
            error = state.errores.email,
            onChange = onEmailChange,
            tipoTeclado = KeyboardType.Email,
            enabled = !state.isLoading
        )

        CampoTexto(
            valor = state.password,
            label = "Contraseña",
            error = state.errores.password,
            onChange = onPasswordChange,
            esPassword = true,
            enabled = !state.isLoading
        )

        state.mensajeError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        //boton de iniciar sesion
        Button(
            onClick = onLoginClick,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Iniciar sesión")
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "O",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        //boton de iniciar sesion con google
        OutlinedButton(
            onClick = { },
            enabled = !state.isLoading,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Iniciar sesión con Google")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    DeepSourceHorasTheme {
        LoginScreen(
            state = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {}
        )
    }
}