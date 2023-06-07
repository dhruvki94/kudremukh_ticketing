package com.example.ticketing.model

enum class TripType {
  Other,
  Trek,
  Transit,
  Stay,
  Inspection;

  companion object {
    fun getByName(name: String?): TripType {
      TripType.values().forEach { tripType -> if (name == tripType.name) return tripType }

      return Transit
    }

    fun getOptions(): List<String> {
      val options = mutableListOf<String>()
      TripType.values().forEach { type -> options.add(type.name) }
      return options
    }
  }
}
