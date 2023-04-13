package com.example.ticketing.screens.vehicle_entry

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketing.model.Vehicle
import com.example.ticketing.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleEntryViewModel @Inject constructor(
  private val storageService: StorageService
) : ViewModel() {
  val vehicle = mutableStateOf(Vehicle())

  fun addQrCode(newValue: String) {
    vehicle.value = vehicle.value.copy(qrReference = newValue)
  }

  fun onVehicleNumberChange(newValue: String) {
    vehicle.value = vehicle.value.copy(vehicleNumber = newValue)
  }

  fun onDriverNameChange(newValue: String) {
    vehicle.value = vehicle.value.copy(driverName = newValue)
  }

  fun onDriverMobileChange(newValue: String) {
    vehicle.value = vehicle.value.copy(driverMobile = newValue)
  }

  fun onNoOfPassengersChange(newValue: String) {
    vehicle.value = vehicle.value.copy(noOfPassengers = newValue)
  }

  fun onVehicleTypeChange(newValue: String) {
    vehicle.value = vehicle.value.copy(vehicleType = newValue)
  }
  fun onEntryGateChange(newValue: String) {
    vehicle.value = vehicle.value.copy(entryGate = newValue)
  }
  fun onExitGateChange(newValue: String) {
    vehicle.value = vehicle.value.copy(exitGate = newValue)
  }
  fun onTripTypeChange(newValue: String) {
    vehicle.value = vehicle.value.copy(tripType = newValue)
  }
  fun onDoneClick(popUpScreen: () -> Unit) {
    val updatedVehicle = vehicle.value.copy(entryTimestamp = System.currentTimeMillis())
    viewModelScope.launch {
      storageService.save(updatedVehicle)
    }
    popUpScreen()
  }
}