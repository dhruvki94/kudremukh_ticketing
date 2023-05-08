package com.example.ticketing.screens.stats

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketing.model.Vehicle
import com.example.ticketing.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StatsViewModel
@Inject
constructor(
  private val storageService: StorageService
) : ViewModel() {
  val startTs = mutableStateOf(0L)
  val endTs = mutableStateOf(0L)
  val finedVehicles = mutableStateOf(emptyList<Vehicle>())
  val enteredCount = mutableStateOf("")
  val startDate = mutableStateOf(Array(6) {0})
  val endDate = mutableStateOf(Array(6) {0})

  fun getFinedVehicles() {
    viewModelScope.launch {
      if (startTs.value != 0L && endTs.value != 0L) {
        enteredCount.value = storageService.getCountOfVehicles(startTs.value, endTs.value).toString()
        finedVehicles.value = storageService.getFinedVehiclesForDuration(startTs.value, endTs.value)
      }
    }
  }

  fun onStartDateChange(selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int) {
    startDate.value[0] = selectedYear
    startDate.value[1] = selectedMonth
    startDate.value[2] = selectedDayOfMonth
    val startCalendar = Calendar.getInstance()
    startCalendar.set(startDate.value[0], startDate.value[1], startDate.value[2], startDate.value[3], startDate.value[4], startDate.value[5])
    startTs.value = startCalendar.timeInMillis
  }

  fun onStartTimeChange(selectedHour: Int, selectedMinute: Int) {
    startDate.value[3] = selectedHour
    startDate.value[4] = selectedMinute
    startDate.value[5] = 0
    val startCalendar = Calendar.getInstance()
    startCalendar.set(startDate.value[0], startDate.value[1], startDate.value[2], startDate.value[3], startDate.value[4], startDate.value[5])
    startTs.value = startCalendar.timeInMillis
  }

  fun onEndTimeChange(selectedHour: Int, selectedMinute: Int) {
    endDate.value[3] = selectedHour
    endDate.value[4] = selectedMinute
    endDate.value[5] = 0
    val endCalendar = Calendar.getInstance()
    endCalendar.set(endDate.value[0], endDate.value[1], endDate.value[2], endDate.value[3], endDate.value[4], endDate.value[5])
    endTs.value = endCalendar.timeInMillis
  }

  fun onEndDateChange(selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int) {
    endDate.value[0] = selectedYear
    endDate.value[1] = selectedMonth
    endDate.value[2] = selectedDayOfMonth
    val endCalendar = Calendar.getInstance()
    endCalendar.set(endDate.value[0], endDate.value[1], endDate.value[2], endDate.value[3], endDate.value[4], endDate.value[5])
    endTs.value = endCalendar.timeInMillis
  }
}
