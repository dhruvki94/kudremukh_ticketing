package com.example.ticketing.screens.vehicle_exit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ticketing.R.string as Apptext
import com.example.ticketing.TicketingScreens
import com.example.ticketing.common.BasicField
import com.example.ticketing.model.VehicleStatus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
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
        title = { Text(text = "Please go back and try again.") },
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
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box {
          when (vehicle.status) {
            VehicleStatus.OnTime.name -> {
              Text(
                text = "ON TIME",
                color = Color.Green,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
              )
            }
            VehicleStatus.Delayed.name -> {
              Text(
                text = "DELAYED",
                color = Color.Red,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
              )
            }
            VehicleStatus.OverSped.name -> {
              Text(
                text = "OVER-SPEEDING",
                color = Color.Red,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
        val spaceModifierFull = modifier.height(10.dp)
        val spaceModifierHalf = modifier.height(5.dp)

        Spacer(spaceModifierHalf)
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
          Text(text = "Token No: ", fontSize = 30.sp)
          Text(text = vehicle.qrReference, fontWeight = FontWeight.Bold, fontSize = 30.sp, color = Color.Magenta)
        }
        Spacer(spaceModifierFull)
        Divider(thickness = 1.dp, color = Color.LightGray)
        Spacer(spaceModifierFull)

        VehicleField(key = "Time Started: ", value = timeFormat.format(Date(vehicle.entryTimestamp)))
        Spacer(spaceModifierHalf)
        vehicle.exitTimestamp ?.let { VehicleField(key = "Time Ended: ", value = timeFormat.format(Date(it))) }
        Spacer(spaceModifierHalf)
        vehicle.exitTimestamp?.let { VehicleField(key = "Time Taken: ", value = ((( it - vehicle.entryTimestamp) / 1000.0) / 60.0).roundToInt().toString().plus(" minutes")) }

        Spacer(spaceModifierFull)
        Divider(thickness = 1.dp, color = Color.LightGray)
        Spacer(spaceModifierFull)

        VehicleField(key = "Vehicle Number: ", value = vehicle.vehicleNumber)
        Spacer(spaceModifierHalf)
        VehicleField(key = "Type of Vehicle: ", value = vehicle.vehicleType)

        Spacer(spaceModifierFull)
        Divider(thickness = 1.dp, color = Color.LightGray)
        Spacer(spaceModifierFull)

        VehicleField(key = "Driver Name: ", value = vehicle.driverName)
        Spacer(spaceModifierHalf)
        VehicleField(key = "Mobile Number: ", value = vehicle.driverMobile)
        Spacer(spaceModifierHalf)
        VehicleField(key = "Number of Passengers: ", value = vehicle.noOfPassengers)

        Spacer(spaceModifierFull)
        Divider(thickness = 1.dp, color = Color.LightGray)
        Spacer(spaceModifierFull)

        VehicleField(key = "Entry Gate: ", value = vehicle.entryGate)
        Spacer(spaceModifierHalf)
        VehicleField(key = "Exit Gate: ", value = vehicle.exitGate)

        Spacer(spaceModifierFull)
        Divider(thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = modifier.height(15.dp))

        BasicField(text = Apptext.exempt_remark, value = vehicle.exemptRemark, onNewValue = { viewModel.addRemark(it) }, keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters))
        Spacer(modifier = modifier.height(15.dp))

        Button(
          onClick = { showDialog.value = true },
          modifier = modifier
            .height(75.dp)
            .width(150.dp)
        ) {
          Text(text = "Done", fontSize = 28.sp)
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
  Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
    Text(text = key, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    Text(text = value, fontWeight = FontWeight.Black, fontSize = 20.sp)
  }
}
