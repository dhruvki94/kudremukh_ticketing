package com.example.ticketing.screens.vehicle_entry

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ticketing.R
import com.example.ticketing.TicketingScreens
import com.example.ticketing.common.BasicField
import com.example.ticketing.common.DropdownSelector
import com.example.ticketing.model.TripType
import com.example.ticketing.model.Vehicle
import com.example.ticketing.model.VehicleType
import kotlinx.coroutines.launch
import com.example.ticketing.R.string as AppText

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VehicleEntryScreen(
  modifier: Modifier = Modifier,
  viewModel: VehicleEntryViewModel = hiltViewModel(),
  qrCode: String,
  navController: NavController
) {
  val vehicle by viewModel.vehicle
  val qrExists by viewModel.qrExists
  val context = LocalContext.current
  val showVehicleNumberDialog = remember {
    mutableStateOf(false)
  }
  val scope = rememberCoroutineScope()

  //Add qrCode to vehicle's state
  LaunchedEffect(key1 = Unit) {
    if(qrCode.isBlank()) {
      Toast.makeText(context, "QR Code not Scanned Properly", Toast.LENGTH_LONG).show()
      navController.popBackStack(route = TicketingScreens.EntryScanner.name, inclusive = false)
    }
    viewModel.onLaunchedEffect(qrCode)
  }

  if (qrExists) {
    val onDismiss = {
      navController.navigate(TicketingScreens.Exit.name.plus("/$qrCode")) {
        popUpTo(TicketingScreens.Main.name) {
          inclusive = false
        }
      }
    }
    AlertDialog(
      onDismissRequest = {
        navController.popBackStack(
          route = TicketingScreens.Main.name,
          inclusive = false
        )
      },
      dismissButton = {
        TextButton(onClick = onDismiss) {
          Text(text = "Go To Exit")
        }
      },
      title = { Text(text = "This QR has Active Vehicle.\nPlease Exit that Vehicle.") },
      confirmButton = {}
    )
  }

  //Vehicle Number Stripped to Parts
  var vehicleNumberState by remember {
    mutableStateOf("")
  }
  var vehicleNumberDistrict by remember {
    mutableStateOf("")
  }
  var vehicleNumberAlphabets by remember {
    mutableStateOf("")
  }
  var vehicleNumberDigits by remember {
    mutableStateOf("")
  }
  val vehicleNumber = remember {
    derivedStateOf {
      vehicleNumberState.plus(vehicleNumberDistrict).plus(vehicleNumberAlphabets)
        .plus(vehicleNumberDigits).trim().uppercase()
    }
  }

  SideEffect {
    viewModel.onVehicleNumberChange(vehicleNumber.value, vehicleNumberDigits)
  }

  //Alert Dialog to Inform that vehicle number is not entered
  if (showVehicleNumberDialog.value) {
    VehicleNumberDialog(
      onDismiss = { showVehicleNumberDialog.value = false },
      showDialog = showVehicleNumberDialog.value
    )
  }

  val fieldModifier = Modifier
    .fillMaxWidth()
    .padding(16.dp, 4.dp)
  val spacerModifier = Modifier.height(2.dp)
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top
  ) {
    Spacer(modifier = spacerModifier)
    Spacer(modifier = spacerModifier)

    Text(stringResource(id = AppText.vehicle_number), style = MaterialTheme.typography.body2)
    Spacer(modifier = modifier.height(2.dp))
    Row {
      Spacer(modifier = modifier.width(1.dp))
      BasicField(
        AppText.text_placeholder_two,
        vehicleNumberState,
        { vehicleNumberState = it },
        Modifier
          .width(75.dp)
          .padding(8.dp, 4.dp),
        KeyboardOptions(
          capitalization = KeyboardCapitalization.Characters,
          imeAction = ImeAction.Next
        ),
        maxLength = 2,
        focusManager = LocalFocusManager.current,
        focusDirection = FocusDirection.Right,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = modifier.width(1.dp))
      BasicField(
        AppText.no_placeholder_two,
        vehicleNumberDistrict,
        { vehicleNumberDistrict = it },
        Modifier
          .width(75.dp)
          .padding(8.dp, 4.dp),
        KeyboardOptions(
          capitalization = KeyboardCapitalization.Characters,
          imeAction = ImeAction.Next
        ),
        maxLength = 2,
        focusManager = LocalFocusManager.current,
        focusDirection = FocusDirection.Right,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = modifier.width(1.dp))
      BasicField(
        AppText.text_placeholder_two,
        vehicleNumberAlphabets,
        { vehicleNumberAlphabets = it },
        Modifier
          .width(75.dp)
          .padding(8.dp, 4.dp),
        KeyboardOptions(
          capitalization = KeyboardCapitalization.Characters,
          imeAction = ImeAction.Next
        ),
        maxLength = 2,
        focusManager = LocalFocusManager.current,
        focusDirection = FocusDirection.Right,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = modifier.width(1.dp))
      BasicField(
        AppText.no_placeholder_four,
        vehicleNumberDigits,
        { vehicleNumberDigits = it },
        Modifier
          .width(100.dp)
          .padding(8.dp, 4.dp),
        KeyboardOptions(
          keyboardType = KeyboardType.Number,
          capitalization = KeyboardCapitalization.Characters,
          imeAction = ImeAction.Next
        ),
        maxLength = 4,
        focusManager = LocalFocusManager.current,
        focusDirection = FocusDirection.Down,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = modifier.width(1.dp))
    }

    Spacer(spacerModifier)

    Text(stringResource(id = AppText.driver_name), style = MaterialTheme.typography.body2)
    Spacer(modifier = modifier.height(2.dp))
    BasicField(
      AppText.driver_name,
      vehicle.driverName,
      viewModel::onDriverNameChange,
      fieldModifier,
      KeyboardOptions(
        capitalization = KeyboardCapitalization.Characters,
        imeAction = ImeAction.Next
      )
    )

    Spacer(spacerModifier)

    Text(stringResource(id = AppText.driver_mobile), style = MaterialTheme.typography.body2)
    Spacer(modifier = modifier.height(2.dp))
    BasicField(
      AppText.driver_mobile,
      vehicle.driverMobile,
      viewModel::onDriverMobileChange,
      fieldModifier,
      KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
      ),
      maxLength = 10,
      focusManager = LocalFocusManager.current,
      focusDirection = FocusDirection.Down
    )

    Spacer(spacerModifier)

    Text(stringResource(id = AppText.no_of_passengers), style = MaterialTheme.typography.body2)
    Spacer(modifier = modifier.height(2.dp))
    BasicField(
      AppText.no_of_passengers,
      vehicle.noOfPassengers,
      viewModel::onNoOfPassengersChange,
      fieldModifier,
      KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    Spacer(spacerModifier)

    VehicleTypeCardSelector(vehicle = vehicle, onValChange = viewModel::onVehicleTypeChange)

    Spacer(spacerModifier)

    TripTypeCardSelector(vehicle = vehicle, onValChange = viewModel::onTripTypeChange)

    Spacer(modifier.height(20.dp))

    Button(
      onClick = {
        if (vehicle.vehicleNumber.isBlank() || vehicleNumberDigits.isBlank())
          showVehicleNumberDialog.value = true
        else {
          Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show()
          scope.launch {
            viewModel.onDoneClick {
              navController.navigate(TicketingScreens.Main.name) {
                popUpTo(
                  TicketingScreens.Entry.name
                ) { inclusive = true }
              }
            }
          }
        }
      },
      modifier = modifier
        .height(75.dp)
        .width(150.dp)
    ) {
      Text(text = "Save", fontSize = 28.sp)
    }
  }

}

@Composable
@ExperimentalMaterialApi
private fun VehicleTypeCardSelector(
  vehicle: Vehicle,
  onValChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val currentSelection = VehicleType.getByName(vehicle.vehicleType).name
  Card(backgroundColor = MaterialTheme.colors.onPrimary, modifier = modifier) {
    DropdownSelector(
      R.string.vehicle_type,
      VehicleType.getOptions(),
      currentSelection,
      Modifier,
      onValChange
    )
  }
}

@Composable
@ExperimentalMaterialApi
private fun TripTypeCardSelector(
  vehicle: Vehicle,
  onValChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val currentSelection = TripType.getByName(vehicle.tripType).name
  Card(backgroundColor = MaterialTheme.colors.onPrimary, modifier = modifier) {
    DropdownSelector(
      R.string.trip_type,
      TripType.getOptions(),
      currentSelection,
      Modifier,
      onValChange
    )
  }
}

@Composable
fun VehicleNumberDialog(
  onDismiss: () -> Unit,
  showDialog: Boolean
) {
  if (showDialog) {
    AlertDialog(
      title = {
        Text(text = "Vehicle Number not entered!")
      },
      onDismissRequest = onDismiss,
      dismissButton = {
        TextButton(onClick = onDismiss) {
          Text("Dismiss")
        }
      },
      confirmButton = {}
    )
  }
}
