package com.example.ticketing.screens.vehicle_exit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketing.model.Gate
import com.example.ticketing.model.Vehicle
import com.example.ticketing.model.VehicleStatus
import com.example.ticketing.model.VehicleType
import com.example.ticketing.model.service.AccountService
import com.example.ticketing.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleExitViewModel
@Inject
constructor(
  private val storageService: StorageService,
  private val accountService: AccountService
) : ViewModel() {
  val vehicle = mutableStateOf(Vehicle())
  val gate = mutableStateOf("")
  val phone = mutableStateOf("")

  init {
    viewModelScope.launch {
      accountService.currentUserPhone.collect {
        gate.value = storageService.getGate(it)
        phone.value = it
      }
    }
  }

  fun fetchByQr(qrReference: String) {
    viewModelScope.launch {
      vehicle.value = storageService.getActiveVehicle(qrReference)
        ?: Vehicle()
      if (vehicle.value.exitTimestamp == null)
        vehicle.value = vehicle.value.copy(exitTimestamp = System.currentTimeMillis())
      vehicle.value = vehicle.value.copy(exitMobile = phone.value)

      vehicle.value = vehicle.value.copy(exitGate = gate.value)
      calculateTime()
    }
  }

  fun addRemark(remark: String) {
    vehicle.value = vehicle.value.copy(exemptRemark = remark)
  }

  fun onFinishButtonClick(popUpScreen: () -> Unit) {
    viewModelScope.launch {
      storageService.update(vehicle.value)
      storageService.delete(vehicle.value.qrReference)
    }
    popUpScreen()
  }

  private fun calculateTime() {
    val entryTimestamp = vehicle.value.entryTimestamp
    val exitTimestamp = vehicle.value.exitTimestamp ?: System.currentTimeMillis()
    val minutesTaken = ((exitTimestamp - entryTimestamp) / 1000.0) / 60

    if (vehicle.value.entryGate == Gate.Kattinahole.name || vehicle.value.exitGate == Gate.Kattinahole.name) {
      minutesTaken.let {
        when {
          it < 240.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OnTime.name)
          else -> vehicle.value = vehicle.value.copy(status = VehicleStatus.Delayed.name)
        }
      }
    } else {
      if (vehicle.value.vehicleType == VehicleType.TwoWheeler.name || vehicle.value.vehicleType == VehicleType.LMV.name) {
        minutesTaken.let {
          when {
            it < 40.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in 45.0..120.0 -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OnTime.name)
            else -> vehicle.value = vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      } else {
        minutesTaken.let {
          when {
            it < 45.0 -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in 45.0..240.0 -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OnTime.name)
            else -> vehicle.value = vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      }
    }
  }
}
