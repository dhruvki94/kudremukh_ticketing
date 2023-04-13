package com.example.ticketing.screens.vehicle_exit

import android.content.res.Resources.NotFoundException
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketing.model.Gate
import com.example.ticketing.model.Vehicle
import com.example.ticketing.model.VehicleStatus
import com.example.ticketing.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class VehicleExitViewModel
@Inject
constructor(
  private val storageService: StorageService
) : ViewModel() {
  val vehicle = mutableStateOf(Vehicle())

  fun fetchByQr(qrReference: String) {
    viewModelScope.launch {
      vehicle.value = storageService.getActiveVehicle(qrReference)
        ?: Vehicle()
      if(vehicle.value.exitTimestamp == null)
        vehicle.value = vehicle.value.copy(exitTimestamp = System.currentTimeMillis())
      calculateTime()
    }
  }

  fun onFinishButtonClick(popUpScreen: () -> Unit) {
    viewModelScope.launch {
      storageService.update(vehicle.value.copy(exitTimestamp = System.currentTimeMillis()))
      storageService.delete(vehicle.value.qrReference)
    }
    popUpScreen()
  }

  private fun calculateTime() {
    val malaAndTanikod = setOf(Gate.Mala.name, Gate.Tanikod.name)
    val malaAndBasrikal = setOf(Gate.Mala.name, Gate.Basrikal.name)
    val tanikodAndBasrikal = setOf(Gate.Tanikod.name, Gate.Basrikal.name)
    val entryTimestamp = vehicle.value.entryTimestamp
    val exitTimestamp = vehicle.value.exitTimestamp ?: System.currentTimeMillis()
    val minutesTaken = ((exitTimestamp - entryTimestamp) / 1000.0) / 60
    if (vehicle.value.entryGate in malaAndTanikod && vehicle.value.exitGate in malaAndTanikod) {
      minutesTaken.let {
        when {
          it < 45.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OverSped.name)
          it in 45.0..90.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OnTime.name)
          it > 90.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.Delayed.name)
        }
      }
    } else if (vehicle.value.entryGate in malaAndBasrikal && vehicle.value.exitGate in malaAndBasrikal) {
      minutesTaken.let {
        when {
          it < 60.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OverSped.name)
          it in 60.0..105.0 -> vehicle.value =
            vehicle.value.copy(status = VehicleStatus.OnTime.name)
          it > 105.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.Delayed.name)
        }
      }
    } else if (vehicle.value.entryGate in tanikodAndBasrikal && vehicle.value.exitGate in tanikodAndBasrikal) {
      minutesTaken.let {
        when {
          it < 75.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OverSped.name)
          it in 75.0..120.0 -> vehicle.value =
            vehicle.value.copy(status = VehicleStatus.OnTime.name)
          it > 120.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.Delayed.name)
        }
      }
    } else {
      minutesTaken.let {
        when {
          it < 240.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OnTime.name)
          else -> vehicle.value = vehicle.value.copy(status = VehicleStatus.Delayed.name)
        }
      }
    }
  }
}
