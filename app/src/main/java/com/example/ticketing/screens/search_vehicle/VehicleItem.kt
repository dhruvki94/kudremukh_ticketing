package com.example.ticketing.screens.search_vehicle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ticketing.model.Vehicle

@Composable
@ExperimentalMaterialApi
fun VehicleItem(
  vehicle: Vehicle,
  onCardClick: (String) -> Unit,
  modifier: Modifier = Modifier,
  sNo: Int
) {
  Card(
    modifier = Modifier
      .padding(8.dp, 0.dp, 8.dp, 12.dp)
      .clickable { onCardClick(vehicle.qrReference) }
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Column(modifier = Modifier.weight(0.3f)) {
        Text(text = sNo.toString().plus('.'), style = MaterialTheme.typography.h6, textAlign = TextAlign.Center)
      }
      Spacer(modifier = modifier.width(3.dp))
      Divider(
        modifier = modifier
          .fillMaxHeight()
          .width(1.dp),
        color = Color.DarkGray
      )
      Spacer(modifier = modifier.width(3.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(text = vehicle.vehicleNumber, style = MaterialTheme.typography.h6, textAlign = TextAlign.Center,)
      }
      Spacer(modifier = modifier.width(5.dp))
      Divider(
        modifier = modifier
          .fillMaxHeight()
          .width(1.dp),
        color = Color.DarkGray
      )
      Spacer(modifier = modifier.width(5.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(text = vehicle.driverName, style = MaterialTheme.typography.h6, textAlign = TextAlign.Center)
      }
    }
  }
}
