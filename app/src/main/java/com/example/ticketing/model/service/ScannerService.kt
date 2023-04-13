package com.example.ticketing.model.service

import kotlinx.coroutines.flow.Flow

interface ScannerService {
  fun startScanning(): Flow<String?>
}