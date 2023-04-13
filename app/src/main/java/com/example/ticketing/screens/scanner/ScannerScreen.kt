package com.example.ticketing.screens.scanner

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScannerScreen(
  viewModel: ScannerViewModel = hiltViewModel(),
  onQRScanned: (String) -> Unit
) {
//  val state = viewModel.state.collectAsState()
  LaunchedEffect(key1 = Unit) {
    viewModel.startScanning(onQRScanned)
  }
//
//  Column(
//    modifier = Modifier
//      .fillMaxSize()
//      .padding(vertical = 10.dp),
//    horizontalAlignment = Alignment.CenterHorizontally,
//    verticalArrangement = Arrangement.Center
//  ) {
//    Box(modifier = Modifier
//      .fillMaxSize()
//      .weight(0.5f), contentAlignment = Alignment.Center) {
//      Text(text =  state.value )
//    }
//
//    Box(modifier = Modifier
//      .fillMaxSize()
//      .weight(0.5f), contentAlignment = Alignment.BottomCenter) {
//      Button(onClick = {  }) {
//        Text(text = "Scan QR")
//      }
//    }
//  }
}
