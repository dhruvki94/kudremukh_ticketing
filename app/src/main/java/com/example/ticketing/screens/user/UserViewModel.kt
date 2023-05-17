package com.example.ticketing.screens.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketing.model.UserRecord
import com.example.ticketing.model.service.AccountService
import com.example.ticketing.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel
@Inject
constructor(
  private val auth: AccountService,
  private val storage: StorageService
) : ViewModel() {
  val currentUserPhone = auth.currentUserPhone
  val userRecord = mutableStateOf(UserRecord())

  fun onSignOut(currentUserPhone: String, restartApp: () -> Unit) {
    viewModelScope.launch {
      storage.deleteUser(currentUserPhone)
      auth.signOut()
      restartApp()
    }
  }

  fun getUserRecord(){
    viewModelScope.launch {
      userRecord.value = storage.getUserRecord()
    }
  }
}
