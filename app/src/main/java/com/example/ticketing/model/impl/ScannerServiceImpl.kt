package com.example.ticketing.model.impl

import com.example.ticketing.model.service.ScannerService
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScannerServiceImpl
@Inject
constructor(
  private val scanner: GmsBarcodeScanner
) : ScannerService {
  override fun startScanning(): Flow<String?> {
    return callbackFlow {
      scanner.startScan()
        .addOnSuccessListener {
          launch {
            send(it.rawValue)
          }
        }.addOnFailureListener {
          it.printStackTrace()
        }
      awaitClose {  }
    }
  }
}
