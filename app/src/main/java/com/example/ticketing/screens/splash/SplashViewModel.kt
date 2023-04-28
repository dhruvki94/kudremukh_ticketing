package com.example.ticketing.screens.splash

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.ticketing.TicketingScreens
import com.example.ticketing.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
  private val accountService: AccountService
) : ViewModel() {
  fun onAppStart(navController: NavController) {
    if(accountService.hasUser) {
      navController.navigate(TicketingScreens.Main.name) {
        popUpTo(TicketingScreens.Splash.name) {
          inclusive = true
        }
      }
    }
    else {
      navController.navigate(TicketingScreens.Login.name) {
        popUpTo(TicketingScreens.Splash.name) {
          inclusive = true
        }
      }
    }
  }
}
