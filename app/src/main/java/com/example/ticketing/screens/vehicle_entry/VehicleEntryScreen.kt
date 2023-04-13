package com.example.ticketing.screens.vehicle_entry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ticketing.R
import com.example.ticketing.TicketingScreens
import com.example.ticketing.R.string as AppText
import com.example.ticketing.common.BasicField
import com.example.ticketing.common.DropdownSelector
import com.example.ticketing.model.Gate
import com.example.ticketing.model.TripType
import com.example.ticketing.model.Vehicle
import com.example.ticketing.model.VehicleType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VehicleEntryScreen(
  modifier: Modifier = Modifier,
  viewModel: VehicleEntryViewModel = hiltViewModel(),
  qrCode: String,
  navController: NavController
) {
  val vehicle by viewModel.vehicle

  LaunchedEffect(key1 = Unit) {
    viewModel.addQrCode(qrCode)
  }

  val fieldModifier = Modifier
    .fillMaxWidth()
    .padding(16.dp, 4.dp)
  val spacerModifier = Modifier.height(8.dp)
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top
  ) {
    BasicField(
      AppText.vehicle_number,
      vehicle.vehicleNumber,
      viewModel::onVehicleNumberChange,
      fieldModifier
    )
    Spacer(spacerModifier)
    BasicField(
      AppText.driver_name,
      vehicle.driverName,
      viewModel::onDriverNameChange,
      fieldModifier
    )
    Spacer(spacerModifier)
    BasicField(
      AppText.driver_mobile,
      vehicle.driverMobile,
      viewModel::onDriverMobileChange,
      fieldModifier,
      KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Spacer(spacerModifier)
    BasicField(
      AppText.no_of_passengers,
      vehicle.noOfPassengers,
      viewModel::onNoOfPassengersChange,
      fieldModifier,
      KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    Spacer(spacerModifier)
    EntryGateCardSelector(vehicle = vehicle, onValChange = viewModel::onEntryGateChange)
    Spacer(spacerModifier)
    ExitGateCardSelector(vehicle = vehicle, onValChange = viewModel::onExitGateChange)
    Spacer(spacerModifier)
    VehicleTypeCardSelector(vehicle = vehicle, onValChange = viewModel::onVehicleTypeChange)
    Spacer(spacerModifier)
    TripTypeCardSelector(vehicle = vehicle, onValChange = viewModel::onTripTypeChange)

    Spacer(spacerModifier)
    Button(onClick = {
      viewModel.onDoneClick {
        navController.navigate(TicketingScreens.Main.name) {
          popUpTo(
            TicketingScreens.Entry.name
          ) { inclusive = true }
        }
      }
    }) {
      Text(text = "Save")
    }
  }

}

@Composable
@ExperimentalMaterialApi
private fun EntryGateCardSelector(
  vehicle: Vehicle,
  onValChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val currentSelection = Gate.getByName(vehicle.entryGate).name
  Card(backgroundColor = MaterialTheme.colors.onPrimary, modifier = modifier) {
    DropdownSelector(
      R.string.entry_gate,
      Gate.getOptions(),
      currentSelection,
      Modifier,
      onValChange
    )
  }
}

@Composable
@ExperimentalMaterialApi
private fun ExitGateCardSelector(
  vehicle: Vehicle,
  onValChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  if(vehicle.entryGate != Gate.Kattinahole.name) {
    val currentSelection = Gate.getByName(vehicle.exitGate).name
    Card(backgroundColor = MaterialTheme.colors.onPrimary, modifier = modifier) {
      DropdownSelector(
        R.string.exit_gate,
        Gate.getOptions().filterNot { it == Gate.Kattinahole.name },
        currentSelection,
        Modifier,
        onValChange
      )
    }
  }
  else {
    val currentSelection = Gate.getByName(vehicle.exitGate).name
    Card(backgroundColor = MaterialTheme.colors.onPrimary, modifier = modifier) {
      DropdownSelector(
        R.string.exit_gate,
        Gate.getOptions().filter { it == Gate.Kattinahole.name },
        currentSelection,
        Modifier,
        onValChange
      )
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

