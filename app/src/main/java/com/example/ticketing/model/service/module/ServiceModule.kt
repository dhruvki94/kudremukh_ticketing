package com.example.ticketing.model.service.module

//import com.example.ticketing.model.impl.ScannerServiceImpl
import com.example.ticketing.model.impl.StorageServiceImpl
//import com.example.ticketing.model.service.ScannerService
import com.example.ticketing.model.service.StorageService
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
  @Provides
  @Singleton
  fun provideStorageService(impl: StorageServiceImpl): StorageService = impl

  @Provides
  @Singleton
  fun provideFirestore() = FirebaseFirestore.getInstance()

//  @Provides
//  fun provideScannerService(impl: ScannerServiceImpl): ScannerService = impl
}
