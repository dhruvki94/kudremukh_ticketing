package com.example.ticketing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.ticketing.screens.login.LoginScreen
import com.example.ticketing.ui.theme.TicketingTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalGetImage
@AndroidEntryPoint
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TicketingTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
//          TicketingApp()
          LoginScreen()
        }
      }
    }
  }
}
