package com.example.ticketing.model.service

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.MutableState
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

interface AccountService {
  val hasUser: Boolean
  suspend fun sendVerificationCode(
    number: String,
    activity: Activity,
    callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
  )

  suspend fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    activity: Activity,
    context: Context,
    message: MutableState<String>
  )
}
