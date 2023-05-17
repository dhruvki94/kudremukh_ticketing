package com.example.ticketing.screens.search_vehicle

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketing.model.Vehicle
import com.example.ticketing.model.service.AccountService
import com.example.ticketing.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVehicleViewModel
@Inject
constructor(
  private val storageService: StorageService,
  private val accountService: AccountService
) : ViewModel() {
  val vehicles = mutableStateOf(emptyList<Vehicle>())
  val vehicleNumberDigits = mutableStateOf("")
  val role = mutableStateOf("")
  val allVehicles = storageService.vehicles.map { it.sortedByDescending { vehicle -> vehicle.entryTimestamp } }

  init {
    viewModelScope.launch {
      role.value = storageService.getUserRecord().role
    }
  }
  fun onVehicleNumberDigitsChange(vehicleDigits: String) {
    vehicleNumberDigits.value = vehicleDigits
  }
  suspend fun getVehicles() {
    vehicles.value = storageService.getActiveVehicleByVehicleDigits(vehicleDigits = vehicleNumberDigits.value)
  }
}
