package com.example.ticketing.model

enum class Gate(val no: Int) {
  Mala(no = 1),
  Tanikod(no = 2),
  Basrikal(no = 3),
  Kattinahole(no = 4),
  None(no = 0);

  companion object {
    fun getByName(name: String?): Gate {
      values().forEach { gate -> if (name == gate.name) return gate }

      return None
    }

    fun getOptions(): List<String> {
      val options = mutableListOf<String>()
      values().forEach { gate -> options.add(gate.name) }
      return options
    }
  }
}
