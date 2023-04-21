package com.example.ticketing.screens.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ticketing.R
import com.example.ticketing.common.BasicField
import com.example.ticketing.common.DropdownSelector
import com.example.ticketing.model.Gate
import com.example.ticketing.model.Vehicle
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginScreen(
  viewModel: LoginViewModel = hiltViewModel(),
  onSuccessfulLogin: () -> Unit
) {
  val context = LocalContext.current
  lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
  val scope = rememberCoroutineScope()

  val phoneNumber by viewModel.phoneNumber
  val otp by viewModel.otp
  val gate by viewModel.gate

  val verificationID = remember {
    mutableStateOf("")
  }

  val message = remember {
    mutableStateOf("")
  }

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    BasicField(
      text = R.string.phone_no,
      value = phoneNumber,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Go
      ),
      maxLength = 10,
      onNewValue = { viewModel.onPhoneNumberChange(it) })

    Spacer(modifier = Modifier.height(10.dp))

    Button(
      onClick = {
        if (phoneNumber.isBlank()) {
          Toast.makeText(context, "Please enter phone number..", Toast.LENGTH_SHORT)
            .show()
        } else {

          // on below line calling method to generate verification code.
          scope.launch {
            viewModel.sendVerificationCode(context as Activity, callbacks)
          }
        }
      },
      // on below line we are
      // adding modifier to our button.
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Text(text = "Generate OTP", modifier = Modifier.padding(8.dp))
    }

    Spacer(modifier = Modifier.height(10.dp))

    GateCardSelector(gateSelected = gate, onValChange = { viewModel.onGateChange(it) })

    BasicField(
      text = R.string.otp,
      value = otp,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
      ),
      maxLength = 10,
      onNewValue = { viewModel.onOtpChange(it) })

    Spacer(modifier = Modifier.height(10.dp))

    Button(
      onClick = {
        // on below line we are validating
        // user input parameters.
        if (otp.isBlank()) {
          // displaying toast message on below line.
          Toast.makeText(context, "Please enter otp..", Toast.LENGTH_SHORT)
            .show()
        } else {
          // on below line generating phone credentials.
          val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
            verificationID.value, otp
          )
          // on below line signing within credentials.
          scope.launch {
            viewModel.signInWithPhoneAuthCredential(
              credential,
              context as Activity,
              context,
              message
            )
          }
        }
      },
      // on below line we are
      // adding modifier to our button.
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // on below line we are adding text for our button
      Text(text = "Verify OTP", modifier = Modifier.padding(8.dp))
    }
  }

  callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
      // on below line updating message
      // and displaying toast message
      message.value = "Verification successful"
      Toast.makeText(context, "Verification successful..", Toast.LENGTH_SHORT).show()
      onSuccessfulLogin()
    }

    override fun onVerificationFailed(p0: FirebaseException) {
      // on below line displaying error as toast message.
      message.value = "Fail to verify user : \n" + p0.message
      Toast.makeText(context, "Verification failed..", Toast.LENGTH_SHORT).show()
    }

    override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
      // this method is called when code is send
      super.onCodeSent(verificationId, p1)
      verificationID.value = verificationId
    }
  }
}

@Composable
@ExperimentalMaterialApi
private fun GateCardSelector(
  gateSelected: String,
  onValChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val currentSelection = Gate.getByName(gateSelected).name
  Card(backgroundColor = MaterialTheme.colors.onPrimary, modifier = modifier) {
    DropdownSelector(
      R.string.select_gate,
      Gate.getOptions(),
      currentSelection,
      Modifier,
      onValChange
    )
  }
}
