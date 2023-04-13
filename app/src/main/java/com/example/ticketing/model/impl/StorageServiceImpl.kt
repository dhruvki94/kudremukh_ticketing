package com.example.ticketing.model.impl

import com.example.ticketing.model.Vehicle
import com.example.ticketing.model.service.StorageService
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class StorageServiceImpl
@Inject
constructor(
  private val firestore: FirebaseFirestore
) : StorageService {
  override val vehicles: Flow<List<Vehicle>>
    get() = activeCollection().snapshots().map { snapshot -> snapshot.toObjects() }

  override suspend fun getActiveVehicle(qrReference: String): Vehicle? {
    return activeCollection()
      .whereEqualTo("qrReference", qrReference)
      .orderBy("entryTimestamp", Query.Direction.DESCENDING)
      .get()
      .await()
      .toObjects<Vehicle>()
      .firstOrNull()
  }
  override suspend fun getPastVehicle(uuid: UUID): Vehicle? =
    pastCollection()
      .whereEqualTo("uuid", uuid)
      .orderBy("entryTimestamp", Query.Direction.DESCENDING)
      .get()
      .await()
      .toObjects<Vehicle>()
      .firstOrNull()

  override suspend fun update(vehicle: Vehicle) {
    pastCollection().document(vehicle.id).set(vehicle).await()
  }

  override suspend fun save(vehicle: Vehicle) {
    activeCollection().add(vehicle).await()
    pastCollection().add(vehicle).await()
  }

  override suspend fun delete(qrReference: String) {
    getActiveVehicle(qrReference)
      ?.let { activeCollection().document(it.id).delete().await() }
  }

  private fun activeCollection() = firestore.collection(ACTIVE_VEHICLES_COLLECTION)

  private fun pastCollection() = firestore.collection(PAST_VEHICLE_COLLECTION)

  private fun vehicleMapper(doc: DocumentSnapshot) =
    Vehicle(
      id = doc.id,
      uuid = doc.get("uuid").toString(),
      qrReference = doc.get("qrReference").toString(),
      vehicleNumber = doc.get("vehicleNumber").toString(),
      driverName = doc.get("driverName").toString(),
      driverMobile = doc.get("driverMobile").toString(),
      noOfPassengers = doc.get("noOfPassengers").toString(),
      vehicleType = doc.get("vehicleType").toString(),
      entryGate = doc.get("entryGate").toString(),
      exitGate = doc.get("exitGate").toString(),
      tripType = doc.get("tripType").toString(),
      entryTimestamp = doc.get("entryTimestamp").toString().toLong(),
      exitTimestamp = doc.get("exitTimestamp").toString().toLong(),
      status = doc.get("status").toString()
    )

  companion object {
    private const val TAG = "StorageService"
    private const val ACTIVE_VEHICLES_COLLECTION = "active_vehicles"
    private const val PAST_VEHICLE_COLLECTION = "all_vehicles"
  }
}