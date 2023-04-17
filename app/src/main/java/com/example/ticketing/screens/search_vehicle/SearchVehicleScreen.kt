package com.example.ticketing.screens.search_vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ticketing.TicketingScreens
import com.example.ticketing.common.BasicField
import com.example.ticketing.R.string as AppText

@Composable
@ExperimentalMaterialApi
fun SearchVehicleScreen(
  viewModel: SearchVehicleViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
  navController: NavController
) {
  val vehicles = viewModel.vehicles.collectAsStateWithLifecycle(initialValue = emptyList())
  var searchKey by remember {
    mutableStateOf("")
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = modifier.height(10.dp))
    BasicField(text = AppText.vehicle_number, value = searchKey, onNewValue = { searchKey = it }, modifier = modifier.fillMaxWidth(0.75f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Search))
    Spacer(modifier = modifier.height(10.dp))
    LazyColumn() {
      itemsIndexed(
        vehicles.value.filter { it.vehicleNumber.contains(searchKey, ignoreCase = true) },
        key = null
      ) {vehicleIndex, vehicleItem ->
        VehicleItem(
          vehicle = vehicleItem,
          onCardClick = {
            navController.navigate(TicketingScreens.Exit.name.plus("/$it"))
          },
          sNo = vehicleIndex + 1
        )
      }
    }
  }
}