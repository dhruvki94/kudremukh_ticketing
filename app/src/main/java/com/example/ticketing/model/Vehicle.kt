package com.example.ticketing.model

import com.google.firebase.firestore.DocumentId
import java.util.UUID

data class Vehicle(
  @DocumentId val id: String = "",
  val uuid: String = UUID.randomUUID().toString(),
  val qrReference: String = "",
  val vehicleNumber: String = "",
  val vehicleDigits: String = "",
  val driverName: String = "",
  val driverMobile: String = "",
  val noOfPassengers: String = "",
  val vehicleType: String = VehicleType.LMV.name,
  val entryGate: String = "",
  val exitGate:  String = "",
  val tripType: String = TripType.Transit.name,
  val entryTimestamp: Long = System.currentTimeMillis(),
  val exitTimestamp: Long? = null,
  val status:  String = "",
  val exemptRemark: String = ""
)
