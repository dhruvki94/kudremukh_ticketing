package com.example.ticketing.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SplashScreen(
  navController: NavController,
  viewModel: SplashViewModel = hiltViewModel()
) {
  LaunchedEffect(key1 = Unit) {
    viewModel.onAppStart(navController)
  }
}
