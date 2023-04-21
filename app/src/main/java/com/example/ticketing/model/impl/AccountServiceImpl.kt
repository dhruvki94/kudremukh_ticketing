package com.example.ticketing.model.impl

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.example.ticketing.model.service.AccountService
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AccountServiceImpl
@Inject
constructor(
  private val auth: FirebaseAuth
) : AccountService {
  override val hasUser: Boolean
    get() = auth.currentUser != null
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
  ) {
    auth.signInWithCredential(credential)
      .addOnCompleteListener(activity) { task ->
        // displaying toast message when
        // verification is successful
        if (task.isSuccessful) {
          message.value = "Verification successful"
          Toast.makeText(context, "Verification successful..", Toast.LENGTH_SHORT).show()
        } else {
          // Sign in failed, display a message
          if (task.exception is FirebaseAuthInvalidCredentialsException) {
            // The verification code
            // entered was invalid
            Toast.makeText(
              context,
              "Verification failed.." + (task.exception as FirebaseAuthInvalidCredentialsException).message,
              Toast.LENGTH_SHORT
            ).show()
          }
        }
      }
  }
}
