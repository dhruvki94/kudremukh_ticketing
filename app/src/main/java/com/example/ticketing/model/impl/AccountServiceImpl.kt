package com.example.ticketing.model.impl

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.example.ticketing.model.service.AccountService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AccountServiceImpl
@Inject
constructor(
  private val auth: FirebaseAuth
) : AccountService {
  override val hasUser: Boolean
    get() = auth.currentUser != null

  override val currentUserPhone: Flow<String>
    get() = callbackFlow {
    val listener =
      FirebaseAuth.AuthStateListener { auth ->
        this.trySend(auth.currentUser?.phoneNumber ?: "")
      }
    auth.addAuthStateListener(listener)
    awaitClose { auth.removeAuthStateListener(listener) }
  }

  override suspend fun signOut() {
    auth.signOut()
  }

  override suspend fun sendVerificationCode(
    number: String,
    activity: Activity,
    callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
  ) {
    val options = PhoneAuthOptions.newBuilder(auth)
      .setPhoneNumber(number) // Phone number to verify
      .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
      .setActivity(activity) // Activity (for callback binding)
      .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
      .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
  }

  override suspend fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    activity: Activity,
    context: Context,
    message: MutableState<String>
  ) : Task<AuthResult> {
    return auth.signInWithCredential(credential)
  }
}
