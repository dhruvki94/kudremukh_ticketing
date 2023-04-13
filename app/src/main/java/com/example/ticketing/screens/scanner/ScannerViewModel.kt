package com.example.ticketing.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketing.model.service.ScannerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel
@Inject
constructor(
  private val service: @JvmSuppressWildcards ScannerService
) : ViewModel() {
  private val _state = MutableStateFlow("")
  val state = _state.asStateFlow()


  fun startScanning(onValueFound: (String) -> Unit){
    viewModelScope.launch {
      service.startScanning().collect{
        if (!it.isNullOrBlank()){
          _state.value = state.value
          onValueFound(it)
        }
      }
    }
  }
}
