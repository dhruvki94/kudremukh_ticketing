package com.example.ticketing.model.service

import com.example.ticketing.model.User
import com.example.ticketing.model.UserRecord
import com.example.ticketing.model.Vehicle
import com.example.ticketing.model.VehicleConfig
import kotlinx.coroutines.flow.Flow

interface StorageService {
  val admins: Flow<List<String>>
  val vehicles: Flow<List<Vehicle>>
  suspend fun getActiveVehicle(qrReference: String): Vehicle?
  suspend fun getActiveVehicleByVehicleDigits(vehicleDigits: String): List<Vehicle>
  suspend fun getPastVehicle(uuid: String): Vehicle?
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