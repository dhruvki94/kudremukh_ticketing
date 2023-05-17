package com.example.ticketing.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketing.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
  private val storageService: StorageService
) : ViewModel() {
  val role = mutableStateOf("")

  init {
    viewModelScope.launch {
      role.value = storageService.getUserRecord().role
    }
  }
}
