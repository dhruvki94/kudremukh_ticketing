package com.example.ticketing.screens.search_vehicle

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ticketing.model.Vehicle
import com.example.ticketing.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchVehicleViewModel
@Inject
constructor(
  private val storageService: StorageService
) : ViewModel() {
  val vehicles = mutableStateOf(emptyList<Vehicle>())
  val vehicleNumberDigits = mutableStateOf("")

  fun onVehicleNumberDigitsChange(vehicleDigits: String) {
    vehicleNumberDigits.value = vehicleDigits
  }
  suspend fun getVehicles() {
    vehicles.value = storageService.getActiveVehicleByVehicleDigits(vehicleDigits = vehicleNumberDigits.value)
  }
}
