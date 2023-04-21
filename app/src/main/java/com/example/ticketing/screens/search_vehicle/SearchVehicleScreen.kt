package com.example.ticketing.screens.search_vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ticketing.TicketingScreens
import com.example.ticketing.common.BasicField
import kotlinx.coroutines.launch
import com.example.ticketing.R.string as AppText

@Composable
@ExperimentalMaterialApi
fun SearchVehicleScreen(
  viewModel: SearchVehicleViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
  navController: NavController
) {
  val vehicles by viewModel.vehicles
  val scope = rememberCoroutineScope()
  val vehicleNumberDigits by viewModel.vehicleNumberDigits


  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = modifier.height(10.dp))

    Row(
      horizontalArrangement = Arrangement.Center
    ) {
      BasicField(
        AppText.no_placeholder_four,
        vehicleNumberDigits,
        { viewModel.onVehicleNumberDigitsChange(it) },
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
    }

    Spacer(modifier = modifier.height(10.dp))

    Button(onClick = { scope.launch { viewModel.getVehicles() } }) {
      Text(text = "Search")
    }
    Spacer(modifier = modifier.height(10.dp))

    LazyColumn() {
      items(
        vehicles,
        key = null
      ) { vehicleItem ->
        VehicleItem(
          vehicle = vehicleItem,
          onCardClick = {
            navController.navigate(TicketingScreens.Exit.name.plus("/$it"))
          }
        )
      }
    }
  }
}