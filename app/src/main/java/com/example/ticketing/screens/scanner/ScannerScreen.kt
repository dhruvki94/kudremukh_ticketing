package com.example.ticketing.screens.scanner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketing.R
import com.example.ticketing.common.BasicField

@androidx.camera.core.ExperimentalGetImage
@Composable
fun ScannerScreen(
  onScannerButton: () -> Unit,
  onQrCodeEnter: (String) -> Unit
) {
  var qrCode by remember {
    mutableStateOf("")
  }
  val showQrCodeDialog = remember {
    mutableStateOf(false)
  }
  if (showQrCodeDialog.value) {
    AlertDialog(
      title = {
        Text(text = "QR Code not entered!")
      },
      onDismissRequest = { showQrCodeDialog.value = false },
      dismissButton = {
        TextButton(onClick = { showQrCodeDialog.value = false }) {
          Text("Dismiss")
        }
      },
      confirmButton = {}
    )
  }
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Button(
      onClick = onScannerButton, modifier = Modifier
        .height(75.dp)
        .width(150.dp)
    ) {
      Text(text = "SCAN", fontSize = 28.sp)
    }
    Spacer(modifier = Modifier.height(75.dp))
    Divider(modifier = Modifier.height(1.dp), color = Color.LightGray)
    Spacer(modifier = Modifier.height(75.dp))
    BasicField(
      text = R.string.qr_code,
      value = qrCode,
      onNewValue = { qrCode = it },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Spacer(modifier = Modifier.height(25.dp))
    Button(
      onClick = {
        if (qrCode.isBlank())
          showQrCodeDialog.value = true
        else
          onQrCodeEnter(qrCode)
      }, modifier = Modifier
        .height(75.dp)
        .width(325.dp)
    ) {
      Text(text = "Enter QR Code", fontSize = 28.sp)
    }
  }
}
