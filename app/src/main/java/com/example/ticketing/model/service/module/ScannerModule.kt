package com.example.ticketing.model.service.module

import android.app.Application
import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ScannerModule {
  @Provides
  @Singleton
  fun provideContext(app: Application): Context {
    return app.applicationContext
  }

  @Provides
  @Singleton
  fun provideBarCodeOptions() : GmsBarcodeScannerOptions {
    return GmsBarcodeScannerOptions.Builder()
      .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
      .build()
  }

  @Provides
  @Singleton
  fun provideBarCodeScanner(context: Context,options: GmsBarcodeScannerOptions): GmsBarcodeScanner =
    GmsBarcodeScanning.getClient(context, options)

}
