package com.example.ticketing.model.service

import com.example.ticketing.model.User
import com.example.ticketing.model.Vehicle
import java.util.UUID

interface StorageService {

  suspend fun getActiveVehicle(qrReference: String): Vehicle?
  suspend fun getActiveVehicleByVehicleDigits(vehicleDigits: String): List<Vehicle>
  suspend fun getPastVehicle(uuid: UUID): Vehicle?
  suspend fun save(vehicle: Vehicle)
  suspend fun update(vehicle: Vehicle)
  suspend fun delete(qrReference: String)
  suspend fun addUser(user: User)
  suspend fun deleteUser(phoneNumber: String)
  suspend fun getGate(phoneNumber: String): String
}