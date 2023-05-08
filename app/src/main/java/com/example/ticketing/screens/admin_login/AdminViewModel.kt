package com.example.ticketing.screens.admin_login

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketing.model.User
import com.example.ticketing.model.UserRole
import com.example.ticketing.model.service.AccountService
import com.example.ticketing.model.service.StorageService
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel
@Inject
constructor(
  private val accountService: AccountService,
  private val storageService: StorageService
) : ViewModel() {
  val admins = storageService.admins
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

  fun addUser() {
    viewModelScope.launch {
      if (phoneNumber.value.isNotBlank() && gate.value.isNotBlank())
        storageService.addUser(
          User(
            phoneNumber = "+91${phoneNumber.value}",
            gate = gate.value,
            role = UserRole.Admin.name
          )
        )
    }
  }

  suspend fun sendVerificationCode(
    activity: Activity,
    callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
  ) {
    val number = "+91${phoneNumber.value}"
    accountService.sendVerificationCode(number, activity, callbacks)
  }

  suspend fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    activity: Activity,
    context: Context,
    message: MutableState<String>,
    onSuccessfulLogin: () -> Unit
  ) {
    accountService.signInWithPhoneAuthCredential(credential, activity, context, message)
      .addOnCompleteListener(activity) { task ->
        // displaying toast message when
        // verification is successful
        if (task.isSuccessful) {
          message.value = "Verification successful"
          Toast.makeText(context, "Verification successful..", Toast.LENGTH_LONG).show()
          addUser()
          onSuccessfulLogin()
        } else {
          // Sign in failed, display a message
          if (task.exception is FirebaseAuthInvalidCredentialsException) {
            // The verification code
            // entered was invalid
            Toast.makeText(
              context,
              "Verification failed.." + (task.exception as FirebaseAuthInvalidCredentialsException).message,
              Toast.LENGTH_LONG
            ).show()
          }
        }
      }
  }
}

