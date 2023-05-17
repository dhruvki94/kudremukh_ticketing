package com.example.ticketing.screens.stats

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ticketing.R
import com.example.ticketing.model.VehicleStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StatsScreen(
  modifier: Modifier = Modifier,
  viewModel: StatsViewModel = hiltViewModel()
) {
  val context = LocalContext.current
  val calendar = Calendar.getInstance()
  val startDate by viewModel.startTs
  val endDate by viewModel.endTs
  val finedVehicles by viewModel.finedVehicles
  val enteredCount by viewModel.enteredCount
  val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
  val timeFormat = SimpleDateFormat("hh:mm", Locale.ENGLISH)

// Fetching current year, month and day
  val year = calendar.get(Calendar.YEAR)
  val month = calendar.get(Calendar.MONTH)
  val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
  val startDatePicker = DatePickerDialog(
    context,
    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
      viewModel.onStartDateChange(selectedYear, selectedMonth, selectedDayOfMonth)
    }, year, month, dayOfMonth
  )
  val endDatePicker = DatePickerDialog(
    context,
    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
      viewModel.onEndDateChange(selectedYear, selectedMonth, selectedDayOfMonth)
    }, year, month, dayOfMonth
  )
  val startTimePicker = TimePickerDialog(
    context,
    { _, selectedHour: Int, selectedMinute: Int ->
      viewModel.onStartTimeChange(selectedHour, selectedMinute)
    }, 0, 0, false
  )
  val endTimePicker = TimePickerDialog(
    context,
    { _, selectedHour: Int, selectedMinute: Int ->
      viewModel.onEndTimeChange(selectedHour, selectedMinute)
    }, 0, 0, false
  )
  startDatePicker.datePicker.maxDate = calendar.timeInMillis
  endDatePicker.datePicker.maxDate = calendar.timeInMillis
  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    CardEditor(
      title = R.string.start_date,
      icon = R.drawable.ic_calendar,
      content = dateFormat.format(Date(startDate)),
      onEditClick = { startDatePicker.show() },
      highlightColor = MaterialTheme.colors.primary,
      modifier = Modifier
    )
    Spacer(modifier = modifier.height(10.dp))
    CardEditor(
      title = R.string.start_time,
      icon = R.drawable.ic_clock,
      content = timeFormat.format(Date(startDate)),
      onEditClick = { startTimePicker.show() },
      highlightColor = MaterialTheme.colors.primary,
      modifier = Modifier
    )
    Spacer(modifier = modifier.height(10.dp))
    CardEditor(
      title = R.string.end_date,
      icon = R.drawable.ic_calendar,
      content = dateFormat.format(Date(endDate)),
      onEditClick = { endDatePicker.show() },
      highlightColor = MaterialTheme.colors.primary,
      modifier = Modifier
    )
    Spacer(modifier = modifier.height(10.dp))
    CardEditor(
      title = R.string.end_time,
      icon = R.drawable.ic_clock,
      content = timeFormat.format(Date(endDate)),
      onEditClick = { endTimePicker.show() },
      highlightColor = MaterialTheme.colors.primary,
      modifier = Modifier
    )
    Spacer(modifier = modifier.height(25.dp))
    Button(onClick = { viewModel.getFinedVehicles() }) {
      Text(text = "Get Stats")
    }
    Spacer(modifier = modifier.height(25.dp))
    if (finedVehicles.isNotEmpty() || enteredCount.isNotBlank()) {
      TextRow(key = "Total Vehicles Entered", value = enteredCount)
      Spacer(modifier = modifier.height(10.dp))
      TextRow(key = "Total Delayed", value = finedVehicles.count { it.status == VehicleStatus.Delayed.name }
        .toString())
      Spacer(modifier = modifier.height(10.dp))
      TextRow(key = "Total Over-speeding", value = finedVehicles.count { it.status == VehicleStatus.OverSped.name }
        .toString())
      Spacer(modifier = modifier.height(10.dp))
      TextRow(key = "Total Cards Lost", value = finedVehicles.count { it.cardLost == true }
        .toString())
      Spacer(modifier = modifier.height(10.dp))
      TextRow(key = "Total Fine Collected", value = "Rs." + finedVehicles.mapNotNull { it.totalFineLevied }.sum()
        .toString())
    } else {
      Text(text = "No Vehicles Found for this duration.", fontWeight = FontWeight.Black, fontSize = 20.sp)
    }
  }
}


@ExperimentalMaterialApi
@Composable
private fun CardEditor(
  @StringRes title: Int,
  @DrawableRes icon: Int,
  content: String,
  onEditClick: () -> Unit,
  highlightColor: Color,
  modifier: Modifier
) {
  Card(
    backgroundColor = MaterialTheme.colors.onPrimary,
    modifier = modifier,
    onClick = onEditClick
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth(0.9f)
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.weight(1f)) { Text(stringResource(title), color = highlightColor) }

      if (content.isNotBlank()) {
        Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
      }

      Icon(painter = painterResource(icon), contentDescription = "Icon", tint = highlightColor)
    }
  }
}

@Composable
private fun TextRow(
  key: String,
  value: String
) {
  Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(0.8f)) {
    Text(text = key, fontWeight = FontWeight.Black, fontSize = 20.sp)
    Text(text = value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
  }
}
