package com.example.ticketing.common

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun BasicField(
  @StringRes text: Int,
  value: String,
  onNewValue: (String) -> Unit = {},
  modifier: Modifier = Modifier,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
  OutlinedTextField(
    singleLine = true,
    modifier = modifier,
    value = value,
    onValueChange = { onNewValue(it) },
    placeholder = { Text(stringResource(text)) },
    keyboardOptions = keyboardOptions
  )
}