package com.example.ticketing.model.impl

import com.example.ticketing.model.*
import com.example.ticketing.model.service.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class StorageServiceImpl
@Inject
constructor(
  private val firestore: FirebaseFirestore,
  private val auth: FirebaseAuth
) : StorageService {
  override val admins: Flow<List<String>>
    get() = adminCollection().snapshots().map {
      it.documents.map {
          document -> document.id
      }
    }

  override val vehicles: Flow<List<Vehicle>>
    get() = activeCollection().snapshots().map { snapshot -> snapshot.toObjects() }

  override suspend fun getActiveVehicle(qrReference: String): Vehicle? {
    return activeCollection()
      .whereEqualTo("qrReference", qrReference)
      .orderBy("entryTimestamp", Query.Direction.DESCENDING)
      .limit(1)
      .get()
      .await()
      .toObjects<Vehicle>()
      .firstOrNull()
  }


  override suspend fun getActiveVehicleByVehicleDigits(vehicleDigits: String): List<Vehicle> {
    return activeCollection()
      .whereEqualTo("vehicleDigits", vehicleDigits)
      .orderBy("entryTimestamp", Query.Direction.DESCENDING)
      .get()
      .await()
      .toObjects()
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

  override suspend fun addUser(user: User) {
    userCollection().document(user.phoneNumber).set(mapOf("gate" to user.gate, "role" to user.role)).await()
  }

  override suspend fun deleteUser(phoneNumber: String) {
    userCollection().document(phoneNumber).delete().await()
  }

  override suspend fun getUserRecord(): UserRecord {
    val phoneNumber = auth.currentUser?.phoneNumber
    return if(phoneNumber != null)
      userCollection().document(phoneNumber).getCacheFirstSuspend().toObject<UserRecord>() ?: UserRecord()
    else
      UserRecord()
  }

  override suspend fun getFinedVehiclesForDuration(startTs: Long, endTs: Long): List<Vehicle> {
    return pastCollection()
      .whereGreaterThanOrEqualTo("entryTimestamp", startTs)
      .whereLessThanOrEqualTo("entryTimestamp", endTs)
      .whereEqualTo("fined", true)
      .get()
      .await()
      .toObjects()
  }

  override suspend fun getCountOfVehicles(startTs: Long, endTs: Long): Long {
    return pastCollection()
      .whereGreaterThanOrEqualTo("entryTimestamp", startTs)
      .whereLessThanOrEqualTo("entryTimestamp", endTs)
      .count()
      .get(AggregateSource.SERVER)
      .await()
      .count
  }

  override suspend fun getVehicleConfig(): VehicleConfig {
    val twoWheeler = configCollection().document(VehicleType.TwoWheeler.name).getCacheFirstSuspend().toObject<VehicleConfigRecord>()
    val lmv = configCollection().document(VehicleType.LMV.name).getCacheFirstSuspend().toObject<VehicleConfigRecord>()
    val hmv = configCollection().document(VehicleType.HMV.name).getCacheFirstSuspend().toObject<VehicleConfigRecord>()
    return if (twoWheeler != null && lmv != null && hmv != null) {
      VehicleConfig(
        twoWheeler = twoWheeler,
        lmv = lmv,
        hmv = hmv
      )
    } else
      VehicleConfig()
  }

  private suspend fun DocumentReference.getCacheFirstSuspend(): DocumentSnapshot {
    return try {
      // com.google.firebase.firestore.FirebaseFirestoreException: Failed to get document from cache. (However, this document may exist on the server. Run again without setting source to CACHE to attempt to retrieve the document from the server.)
      get(Source.CACHE).await()
    }
    catch (e: FirebaseFirestoreException) {
      // com.google.firebase.firestore.FirebaseFirestoreException: Failed to get document because the client is offline.
      get(Source.SERVER).await()
    }
  }

  private fun activeCollection() = firestore.collection(ACTIVE_VEHICLES_COLLECTION)

  private fun pastCollection() = firestore.collection(PAST_VEHICLE_COLLECTION)

  private fun userCollection() = firestore.collection(USER_COLLECTION)

  private fun adminCollection() = firestore.collection(ADMIN_COLLECTION)

  private fun configCollection() = firestore.collection(CONFIG_COLLECTION)

  companion object {
    private const val ACTIVE_VEHICLES_COLLECTION = "active_vehicles"
    private const val PAST_VEHICLE_COLLECTION = "all_vehicles"
    private const val USER_COLLECTION = "users"
    private const val ADMIN_COLLECTION = "admins"
    private const val CONFIG_COLLECTION = "configs"
   }
}