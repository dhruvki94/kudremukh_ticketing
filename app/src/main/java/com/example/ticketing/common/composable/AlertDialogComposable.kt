package com.example.ticketing.common.composable

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDialog(
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
  title: String,
  confirmText: String,
  dismissText: String,
  showDialog: Boolean
) {
  if (showDialog) {
    AlertDialog(
      title = {
        Text(title)
      },
      onDismissRequest = onDismiss,
      confirmButton = {
        TextButton(
          onClick = onConfirm) {
          Text(confirmText)
        }
      },
      dismissButton = {
        TextButton(onClick = onDismiss) {
          Text(dismissText)
        }
      }
    )
  }
}
