package com.example.ticketing.screens.vehicle_exit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ticketing.TicketingScreens
import com.example.ticketing.model.VehicleStatus
import kotlinx.coroutines.launch
import java.io.Console
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.math.roundToInt

@Composable
fun VehicleExitScreen(
  modifier: Modifier = Modifier,
  viewModel: VehicleExitViewModel = hiltViewModel(),
  qrReference: String,
  navController: NavController
) {
  val vehicle by viewModel.vehicle
  val showDialog = remember {
    mutableStateOf(false)
  }
  val timeFormat = SimpleDateFormat("HH:mm")
  val scope = rememberCoroutineScope()
  LaunchedEffect(key1 = Unit) {
    viewModel.fetchByQr(qrReference)
  }
  println(vehicle)

  if (showDialog.value) {
    ConfirmDialog(
      onDismiss = { showDialog.value = false },
      onConfirm = {
        showDialog.value = false
        scope.launch {
          viewModel.onFinishButtonClick {
            navController.navigate(
              TicketingScreens.Main.name
            ) { popUpTo(TicketingScreens.Exit.name) { inclusive = true } }
          }
        }
      },
      showDialog = showDialog.value
    )
  }

  if (vehicle.id.isBlank()) {
    Column(
      modifier = modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      CircularProgressIndicator(
        modifier
          .height(64.dp)
          .width(64.dp)
      )
    }
  } else {
    if (vehicle.status.isBlank()) {
      AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        title = { Text(text = "Please go back and scan again.") },
        dismissButton = {
          TextButton(
            onClick = { navController.popBackStack() }) { Text(text = "Go Back") }
        },
        confirmButton = {})
    } else {
      Column(
        modifier = modifier
          .fillMaxSize()
          .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box() {
          when (vehicle.status) {
            VehicleStatus.OnTime.name -> {
              Text(
                text = "ON TIME",
                color = Color.Green,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
              )
            }
            VehicleStatus.Delayed.name -> {
              Text(
                text = "DELAYED",
                color = Color.Red,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
              )
            }
            VehicleStatus.OverSped.name -> {
              Text(
                text = "OVER-SPEEDING",
                color = Color.Red,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
        Spacer(modifier = modifier.height(6.dp))
        VehicleField(key = "Time Started: ", value = timeFormat.format(Date(vehicle.entryTimestamp)))
        Spacer(modifier = modifier.height(3.dp))
        vehicle.exitTimestamp ?.let { VehicleField(key = "Time Ended: ", value = timeFormat.format(Date(it))) }
        Spacer(modifier = modifier.height(3.dp))
        vehicle.exitTimestamp?.let { VehicleField(key = "Time Taken: ", value = ((( it - vehicle.entryTimestamp) / 1000.0) / 60.0).roundToInt().toString().plus(" minutes")) }
        Spacer(modifier = modifier.height(6.dp))
        Divider(thickness = 1.dp, color = Color.Black)
        Spacer(modifier = modifier.height(6.dp))
        VehicleField(key = "Vehicle Number: ", value = vehicle.vehicleNumber)
        Spacer(modifier = modifier.height(3.dp))
        VehicleField(key = "Type of Vehicle: ", value = vehicle.vehicleType)
        Spacer(modifier = modifier.height(6.dp))
        Divider(thickness = 1.dp, color = Color.Black)
        Spacer(modifier = modifier.height(6.dp))
        VehicleField(key = "Driver Name: ", value = vehicle.driverName)
        Spacer(modifier = modifier.height(3.dp))
        VehicleField(key = "Mobile Number: ", value = vehicle.driverMobile)
        Spacer(modifier = modifier.height(3.dp))
        VehicleField(key = "Number of Passengers: ", value = vehicle.noOfPassengers)
        Spacer(modifier = modifier.height(6.dp))
        Divider(thickness = 1.dp, color = Color.Black)
        Spacer(modifier = modifier.height(6.dp))
        VehicleField(key = "Entry Gate: ", value = vehicle.entryGate)
        Spacer(modifier = modifier.height(3.dp))
        VehicleField(key = "Exit Gate: ", value = vehicle.exitGate)
        Button(
          onClick = { showDialog.value = true },
          colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
          Text(text = "Done")
        }
      }
    }
  }
}

@Composable
fun ConfirmDialog(
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
  showDialog: Boolean
) {
  if (showDialog) {
    AlertDialog(
      title = {
        Text("Are you sure?")
      },
      onDismissRequest = onDismiss,
      confirmButton = {
        TextButton(
          onClick = onConfirm) {
          Text("Yes")
        }
      },
      dismissButton = {
        TextButton(onClick = onDismiss) {
          Text("No")
        }
      }
    )
  }
}

@Composable
fun VehicleField(
  key: String,
  value: String
) {
  Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
    Text(text = key, fontWeight = FontWeight.Bold, fontSize = 24.sp)
    Text(text = value, fontWeight = FontWeight.Black, fontSize = 24.sp)
  }
}
