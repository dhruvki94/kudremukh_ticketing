package com.example.ticketing.model

data class VehicleConfig(
  val twoWheeler: VehicleConfigRecord = VehicleConfigRecord(),
  val lmv: VehicleConfigRecord = VehicleConfigRecord(),
  val hmv: VehicleConfigRecord = VehicleConfigRecord()
)

data class VehicleConfigRecord(
  val minTime: Map<String, String> = emptyMap(),
  val maxTime: Map<String, String> = emptyMap(),
  val cardFine: Int = 0,
  val fine: Int = 0
)
