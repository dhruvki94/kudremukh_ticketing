package com.example.ticketing.model

enum class VehicleType {
  TwoWheeler,
  LMV,
  HMV,
  Other;

  companion object {
    fun getByName(name: String?): VehicleType {
      VehicleType.values().forEach { type -> if (name == type.name) return type }

      return LMV
    }

    fun getOptions(): List<String> {
      val options = mutableListOf<String>()
      VehicleType.values().forEach { type -> options.add(type.name) }
      return options
    }
  }
}
