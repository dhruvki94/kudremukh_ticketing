package com.example.ticketing.model.service

import com.example.ticketing.model.Vehicle
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface StorageService {
  val vehicles: Flow<List<Vehicle>>

  suspend fun getActiveVehicle(qrReference: String): Vehicle?
  suspend fun getPastVehicle(uuid: UUID): Vehicle?
  suspend fun save(vehicle: Vehicle)
  suspend fun update(vehicle: Vehicle)
  suspend fun delete(qrReference: String)
}