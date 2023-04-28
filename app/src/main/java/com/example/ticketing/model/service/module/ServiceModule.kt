package com.example.ticketing.model.service.module

import com.example.ticketing.model.impl.AccountServiceImpl
import com.example.ticketing.model.impl.StorageServiceImpl
import com.example.ticketing.model.service.AccountService
import com.example.ticketing.model.service.StorageService
import com.google.firebase.auth.FirebaseAuth
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
  fun provideStorageService(impl: StorageServiceImpl): StorageService = impl

  @Provides
  fun provideAccountService(impl: AccountServiceImpl): AccountService = impl

  @Provides
  fun provideFirestore() = FirebaseFirestore.getInstance()

  @Provides
  fun provideFirebaseAuth() = FirebaseAuth.getInstance()

}
