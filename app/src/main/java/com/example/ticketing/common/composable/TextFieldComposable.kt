package com.example.ticketing.common

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BasicField(
  @StringRes text: Int,
  value: String,
  onNewValue: (String) -> Unit = {},
  modifier: Modifier = Modifier,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  maxLength: Int? = null,
  focusManager: FocusManager? = null,
  focusDirection: FocusDirection? = null,
  textAlign: TextAlign = TextAlign.Left
) {
  if (maxLength != null && focusManager != null && focusDirection != null)
    OutlinedTextField(
      singleLine = true,
      modifier = modifier,
      value = value,
      onValueChange = {
        if (it.length >= maxLength) {
          focusManager.moveFocus(focusDirection) // Or receive a lambda function
        }
        onNewValue(it) },
      placeholder = { Text(stringResource(text), textAlign = textAlign) },
      keyboardOptions = keyboardOptions
    )
  else
    OutlinedTextField(
      singleLine = true,
      modifier = modifier,
      value = value,
      onValueChange = { onNewValue(it) },
      placeholder = { Text(stringResource(text), textAlign = textAlign) },
      keyboardOptions = keyboardOptions
    )
}
