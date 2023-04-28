package com.example.ticketing.model.service

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.MutableState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.Flow

interface AccountService {
  val hasUser: Boolean
  val currentUserPhone: Flow<String>

  suspend fun signOut()
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
  ) : Task<AuthResult>
}
