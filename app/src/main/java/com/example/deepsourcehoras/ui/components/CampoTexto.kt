package com.example.deepsourcehoras.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CampoTexto(
    valor: String,
    label: String,
    error: String?,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    esPassword: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = {
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        },
        singleLine = true,
        enabled = enabled,
        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esPassword) KeyboardType.Password else tipoTeclado
        ),
        modifier = modifier.fillMaxWidth()
    )
}