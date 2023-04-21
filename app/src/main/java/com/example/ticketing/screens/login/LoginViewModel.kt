package com.example.ticketing.screens.login

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.ticketing.model.service.AccountService
import com.example.ticketing.model.service.StorageService
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
  private val accountService: AccountService,
  private val storageService: StorageService
) : ViewModel() {
  val phoneNumber = mutableStateOf("")
  val otp = mutableStateOf("")
  val gate = mutableStateOf("")

  fun onPhoneNumberChange(newValue: String) {
    phoneNumber.value = newValue
  }

  fun onOtpChange(newValue: String) {
    otp.value = newValue
  }

  fun onGateChange(newValue: String) {
    gate.value = newValue
  }

  suspend fun sendVerificationCode(
    activity: Activity,
    callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
  ) {
    val number = "+91${phoneNumber}"
    accountService.sendVerificationCode(number, activity, callbacks)
  }

  suspend fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    activity: Activity,
    context: Context,
    message: MutableState<String>
  ) {
    accountService.signInWithPhoneAuthCredential(credential, activity, context, message)
  }
}
