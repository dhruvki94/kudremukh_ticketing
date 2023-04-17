package com.example.ticketing.screens.search_vehicle

import androidx.lifecycle.ViewModel
import com.example.ticketing.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchVehicleViewModel
@Inject
constructor(
  private val storageService: StorageService
) : ViewModel() {
  val vehicles = storageService.vehicles.map { it.sortedByDescending { vehicle -> vehicle.entryTimestamp } }
}
