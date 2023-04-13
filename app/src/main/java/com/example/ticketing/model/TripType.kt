package com.example.ticketing.model

enum class TripType {
  None,
  Trek,
  Transit,
  Stay;

  companion object {
    fun getByName(name: String?): TripType {
      TripType.values().forEach { tripType -> if (name == tripType.name) return tripType }

      return None
    }

    fun getOptions(): List<String> {
      val options = mutableListOf<String>()
      TripType.values().forEach { type -> options.add(type.name) }
      return options
    }
  }
}
