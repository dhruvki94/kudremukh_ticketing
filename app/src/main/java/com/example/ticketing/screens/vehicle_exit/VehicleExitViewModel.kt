package com.example.ticketing.screens.vehicle_exit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketing.model.*
import com.example.ticketing.model.service.AccountService
import com.example.ticketing.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleExitViewModel
@Inject
constructor(
  private val storageService: StorageService,
  private val accountService: AccountService
) : ViewModel() {
  val vehicle = mutableStateOf(Vehicle())
  val gate = mutableStateOf("")
  val pastVehicle = mutableStateOf(Vehicle())
  private val phone = mutableStateOf("")
  private val config = mutableStateOf(VehicleConfig())


  init {
    viewModelScope.launch {
      accountService.currentUserPhone.collect {
        gate.value = storageService.getUserRecord().gate
        phone.value = it
      }
    }
  }

  suspend fun getConfig() {
    config.value = storageService.getVehicleConfig()
  }

  fun fetchByQr(qrReference: String, isCardLost: Boolean) {
    viewModelScope.launch {
      vehicle.value = storageService.getActiveVehicle(qrReference)
        ?: Vehicle()

      vehicle.value = vehicle.value.copy(
        exitTimestamp = System.currentTimeMillis(),
        exitMobile = phone.value,
        exitGate = gate.value,
        cardLost = isCardLost
      )


      calculateTime()
      calculateFine()

      pastVehicle.value = storageService.getPastVehicle(vehicle.value.uuid)
        ?: Vehicle()
      pastVehicle.value = pastVehicle.value.copy(
        exitTimestamp = System.currentTimeMillis(),
        exitMobile = phone.value,
        exitGate = gate.value,
        cardLost = isCardLost,
        status = vehicle.value.status,
        fined = vehicle.value.fined,
        totalFineLevied = vehicle.value.totalFineLevied
      )
    }
  }

  fun update() {
    viewModelScope.launch {
      if (pastVehicle.value.id.isNotBlank())
        storageService.update(pastVehicle.value)
    }
  }

  fun addRemark(remark: String) {
    vehicle.value = vehicle.value.copy(exemptRemark = remark)
  }

  suspend fun onFinishButtonClick(popUpScreen: () -> Unit) {
    viewModelScope.launch {
      storageService.update(pastVehicle.value)
      storageService.delete(vehicle.value.qrReference)
    }
    popUpScreen()
  }

  private fun calculateTime() {
    if (vehicle.value.tripType == TripType.Inspection.name) {
      vehicle.value = vehicle.value.copy(status = VehicleStatus.OnTime.name)
      return
    }
    val entryTimestamp = vehicle.value.entryTimestamp
    val exitTimestamp = vehicle.value.exitTimestamp ?: System.currentTimeMillis()
    val minutesTaken = ((exitTimestamp - entryTimestamp) / 1000) / 60
    val malaAndTanikod = setOf(Gate.Mala.name, Gate.Tanikod.name)
    val malaAndBasrikal = setOf(Gate.Mala.name, Gate.Basrikal.name)
    val tanikodAndBasrikal = setOf(Gate.Tanikod.name, Gate.Basrikal.name)

    if (vehicle.value.vehicleType == VehicleType.TwoWheeler.name) {
      if (vehicle.value.entryGate in malaAndTanikod && vehicle.value.exitGate in malaAndTanikod) {
        minutesTaken.let {
          when {
            it < (config.value.twoWheeler.minTime["MalaTanikod"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.twoWheeler.minTime["MalaTanikod"]?.toLong()
              ?: -1)..(config.value.twoWheeler.maxTime["MalaTanikod"]?.toLong()
              ?: -1) -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.twoWheeler.maxTime["MalaTanikod"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      } else if (vehicle.value.entryGate in malaAndBasrikal && vehicle.value.exitGate in malaAndBasrikal) {
        minutesTaken.let {
          when {
            it < (config.value.twoWheeler.minTime["MalaBasrikal"]?.toLong()
              ?: -1) -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.twoWheeler.minTime["MalaBasrikal"]?.toLong()
              ?: -1)..(config.value.twoWheeler.maxTime["MalaBasrikal"]?.toLong()
              ?: -1) -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.twoWheeler.maxTime["MalaBasrikal"]?.toLong()
              ?: -1) -> vehicle.value = vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      } else if (vehicle.value.entryGate in tanikodAndBasrikal && vehicle.value.exitGate in tanikodAndBasrikal) {
        minutesTaken.let {
          when {
            it < (config.value.twoWheeler.minTime["TanikodBasrikal"]?.toLong()
              ?: -1) -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.twoWheeler.minTime["TanikodBasrikal"]?.toLong()
              ?: -1)..(config.value.twoWheeler.maxTime["TanikodBasrikal"]?.toLong()
              ?: -1) -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.twoWheeler.maxTime["TanikodBasrikal"]?.toLong()
              ?: -1) -> vehicle.value = vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      } else {
        minutesTaken.let {
          when {
            it < (config.value.twoWheeler.minTime["Kattinahole"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.twoWheeler.minTime["Kattinahole"]?.toLong()
              ?: -1)..(config.value.twoWheeler.maxTime["Kattinahole"]?.toLong()
              ?: -1) -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.twoWheeler.maxTime["Kattinahole"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      }
    } else if (vehicle.value.vehicleType == VehicleType.LMV.name) {
      if (vehicle.value.entryGate in malaAndTanikod && vehicle.value.exitGate in malaAndTanikod) {
        minutesTaken.let {
          when {
            it < (config.value.lmv.minTime["MalaTanikod"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.lmv.minTime["MalaTanikod"]?.toLong()
              ?: -1)..(config.value.lmv.maxTime["MalaTanikod"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.lmv.maxTime["MalaTanikod"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      } else if (vehicle.value.entryGate in malaAndBasrikal && vehicle.value.exitGate in malaAndBasrikal) {
        minutesTaken.let {
          when {
            it < (config.value.lmv.minTime["MalaBasrikal"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.lmv.minTime["MalaBasrikal"]?.toLong()
              ?: -1)..(config.value.lmv.maxTime["MalaBasrikal"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.lmv.maxTime["MalaBasrikal"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      } else if (vehicle.value.entryGate in tanikodAndBasrikal && vehicle.value.exitGate in tanikodAndBasrikal) {
        minutesTaken.let {
          when {
            it < (config.value.lmv.minTime["TanikodBasrikal"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.lmv.minTime["TanikodBasrikal"]?.toLong()
              ?: -1)..(config.value.lmv.maxTime["TanikodBasrikal"]?.toLong()
              ?: -1) -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.lmv.maxTime["TanikodBasrikal"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      } else {
        minutesTaken.let {
          when {
            it < (config.value.lmv.minTime["Kattinahole"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.lmv.minTime["Kattinahole"]?.toLong()
              ?: -1)..(config.value.lmv.maxTime["Kattinahole"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.lmv.maxTime["Kattinahole"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      }
    } else {
      if (vehicle.value.entryGate in malaAndTanikod && vehicle.value.exitGate in malaAndTanikod) {
        minutesTaken.let {
          when {
            it < (config.value.hmv.minTime["MalaTanikod"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.hmv.minTime["MalaTanikod"]?.toLong()
              ?: -1)..(config.value.hmv.maxTime["MalaTanikod"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.hmv.maxTime["MalaTanikod"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      } else if (vehicle.value.entryGate in malaAndBasrikal && vehicle.value.exitGate in malaAndBasrikal) {
        minutesTaken.let {
          when {
            it < (config.value.hmv.minTime["MalaBasrikal"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.hmv.minTime["MalaBasrikal"]?.toLong()
              ?: -1)..(config.value.hmv.maxTime["MalaBasrikal"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.hmv.maxTime["MalaBasrikal"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      } else if (vehicle.value.entryGate in tanikodAndBasrikal && vehicle.value.exitGate in tanikodAndBasrikal) {
        minutesTaken.let {
          when {
            it < (config.value.hmv.minTime["TanikodBasrikal"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.hmv.minTime["TanikodBasrikal"]?.toLong()
              ?: -1)..(config.value.hmv.maxTime["TanikodBasrikal"]?.toLong()
              ?: -1) -> vehicle.value = vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.hmv.maxTime["TanikodBasrikal"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      } else {
        minutesTaken.let {
          when {
            it < (config.value.hmv.minTime["Kattinahole"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OverSped.name)
            it in (config.value.hmv.minTime["Kattinahole"]?.toLong()
              ?: -1)..(config.value.hmv.maxTime["Kattinahole"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.OnTime.name)
            it > (config.value.hmv.maxTime["Kattinahole"]?.toLong() ?: -1) -> vehicle.value =
              vehicle.value.copy(status = VehicleStatus.Delayed.name)
          }
        }
      }
    }
  }

  private fun calculateFine() {
    if (vehicle.value.tripType == TripType.Inspection.name) {
      vehicle.value = vehicle.value.copy(totalFineLevied = 0, fined = false)
      return
    }
    if (vehicle.value.vehicleType == VehicleType.TwoWheeler.name) {
      val cardFine = config.value.twoWheeler.cardFine
      val fine = config.value.twoWheeler.fine
      if (vehicle.value.status == VehicleStatus.Delayed.name || vehicle.value.status == VehicleStatus.OverSped.name) {
        if (vehicle.value.cardLost == true) {
          vehicle.value = vehicle.value.copy(totalFineLevied = cardFine + fine, fined = true)
        } else {
          vehicle.value = vehicle.value.copy(totalFineLevied = fine, fined = true)
        }
      } else if (vehicle.value.cardLost == true) {
        vehicle.value = vehicle.value.copy(totalFineLevied = cardFine, fined = true)
      } else {
        vehicle.value = vehicle.value.copy(totalFineLevied = 0, fined = false)
      }
    } else if (vehicle.value.vehicleType == VehicleType.LMV.name) {
      val cardFine = config.value.lmv.cardFine
      val fine = config.value.lmv.fine
      if (vehicle.value.status == VehicleStatus.Delayed.name || vehicle.value.status == VehicleStatus.OverSped.name) {
        if (vehicle.value.cardLost == true) {
          vehicle.value = vehicle.value.copy(totalFineLevied = cardFine + fine, fined = true)
        } else {
          vehicle.value = vehicle.value.copy(totalFineLevied = fine, fined = true)
        }
      } else if (vehicle.value.cardLost == true) {
        vehicle.value = vehicle.value.copy(totalFineLevied = cardFine, fined = true)
      } else {
        vehicle.value = vehicle.value.copy(totalFineLevied = 0, fined = false)
      }
    } else {
      val cardFine = config.value.hmv.cardFine
      val fine = config.value.hmv.fine
      if (vehicle.value.status == VehicleStatus.Delayed.name || vehicle.value.status == VehicleStatus.OverSped.name) {
        if (vehicle.value.cardLost == true) {
          vehicle.value = vehicle.value.copy(totalFineLevied = cardFine + fine, fined = true)
        } else {
          vehicle.value = vehicle.value.copy(totalFineLevied = fine, fined = true)
        }
      } else if (vehicle.value.cardLost == true) {
        vehicle.value = vehicle.value.copy(totalFineLevied = cardFine, fined = true)
      } else {
        vehicle.value = vehicle.value.copy(totalFineLevied = 0, fined = false)
      }
    }
  }
}
