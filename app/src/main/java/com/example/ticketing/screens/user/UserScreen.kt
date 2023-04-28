package com.example.ticketing.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ticketing.common.composable.ConfirmDialog
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
  viewModel: UserViewModel = hiltViewModel(),
  restartApp: () -> Unit
) {
  val phoneNumber by viewModel.currentUserPhone.collectAsState(initial = "")
  val gate by viewModel.gate
  val scope = rememberCoroutineScope()

  val showDialog = remember {
    mutableStateOf(false)
  }

  LaunchedEffect(key1 = Unit) {
    viewModel.getGate()
  }

  if (showDialog.value) {
    ConfirmDialog(
      onDismiss = { showDialog.value = false },
      onConfirm = {
        showDialog.value = false
        scope.launch {
          viewModel.onSignOut(phoneNumber, restartApp)
        }
      },
      showDialog = showDialog.value,
      title = "Are you sure?",
      confirmText = "Yes",
      dismissText = "No"
    )
  }

  val spacerModifier = Modifier.height(12.dp)

  if (phoneNumber.isNotBlank()) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxSize()
    ) {
      Spacer(modifier = spacerModifier)
      Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "account")
      Spacer(modifier = spacerModifier)
      Text(text = "Mobile: $phoneNumber", style = MaterialTheme.typography.h4, textAlign = TextAlign.Center)
      Spacer(modifier = spacerModifier)
      Text(text = "Gate: $gate", style = MaterialTheme.typography.h4, textAlign = TextAlign.Center)
      Spacer(modifier = spacerModifier)
      Button(
        onClick = { showDialog.value = true }, modifier = Modifier
          .height(75.dp)
          .width(150.dp)
      ) {
        Text(text = "Sign Out", fontSize = 25.sp)
      }
    }
  }
  else {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxSize()
    ) {
      Text(text = "Could not find User", style = MaterialTheme.typography.h5, textAlign = TextAlign.Center)
      Spacer(modifier = spacerModifier)
      Button(
        onClick = { restartApp() }, modifier = Modifier
          .height(75.dp)
          .width(150.dp)
      ) {
        Text(text = "Try Again")
      }
    }
  }
}
