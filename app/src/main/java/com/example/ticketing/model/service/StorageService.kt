package com.example.ticketing.model.service

import com.example.ticketing.model.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface StorageService {
  val admins: Flow<List<String>>
  val vehicles: Flow<List<Vehicle>>
  suspend fun getActiveVehicle(qrReference: String): Vehicle?
  suspend fun getActiveVehicleByVehicleDigits(vehicleDigits: String): List<Vehicle>
  suspend fun getPastVehicle(uuid: UUID): Vehicle?
  suspend fun save(vehicle: Vehicle)
  suspend fun update(vehicle: Vehicle)
  suspend fun delete(qrReference: String)
  suspend fun addUser(user: User)
  suspend fun deleteUser(phoneNumber: String)
  suspend fun getUserRecord(): UserRecord
  suspend fun getFinedVehiclesForDuration(startTs: Long, endTs: Long): List<Vehicle>
  suspend fun getCountOfVehicles(startTs: Long, endTs: Long): Long
  suspend fun getVehicleConfig(): VehicleConfig
}