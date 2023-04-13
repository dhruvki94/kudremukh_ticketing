package com.example.ticketing

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ticketing.screens.MainScreen
import com.example.ticketing.screens.scanner.ScannerScreen
import com.example.ticketing.screens.vehicle_entry.VehicleEntryScreen
import com.example.ticketing.screens.vehicle_exit.VehicleExitScreen

enum class TicketingScreens(@StringRes val title: Int) {
  Splash(R.string.splash),
  Main(R.string.main),
  EntryScanner(R.string.scanner),
  ExitScanner(R.string.scanner),
  Entry(R.string.entry),
  Exit(R.string.exit)
}

@Composable
fun TicketingAppBar(
  currentScreen: TicketingScreens,
  canNavigateBack: Boolean,
  navigateUp: () -> Unit,
  modifier: Modifier = Modifier
) {
  TopAppBar(
    title = { Text(text = stringResource(id = currentScreen.title)) },
    modifier = modifier,
    navigationIcon = {
      if (canNavigateBack) {
        IconButton(onClick = navigateUp) {
          Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_button)
          )
        }
      }
    }
  )
}

@Composable
fun TicketingApp() {
  val navController = rememberNavController()

  val backStackEntry by navController.currentBackStackEntryAsState()

  // TODO: Get the name of the current screen
  val currentScreenName = TicketingScreens.valueOf(
    backStackEntry?.destination?.route?.substringBefore('/') ?: TicketingScreens.Main.name
  )

  Scaffold(
    topBar = {
      TicketingAppBar(
        currentScreen = currentScreenName,
        canNavigateBack = navController.previousBackStackEntry != null,
        navigateUp = { navController.navigateUp() })
    }
  ) { innerPadding ->

    NavHost(
      navController = navController, startDestination = TicketingScreens.Main.name,
      modifier = Modifier.padding(innerPadding)
    ) {
      composable(route = TicketingScreens.Main.name) {
        MainScreen(
          onEntryButtonCLicked = { navController.navigate(TicketingScreens.EntryScanner.name) },
          onExitButtonClicked = { navController.navigate(TicketingScreens.ExitScanner.name) })
      }

      composable(route = TicketingScreens.EntryScanner.name) {
        ScannerScreen(onQRScanned = {
          navController.navigate(TicketingScreens.Entry.name.plus("/${it}"))
        })
      }

      composable(route = TicketingScreens.ExitScanner.name) {
        ScannerScreen(onQRScanned = {
          navController.navigate(TicketingScreens.Exit.name.plus("/${it}"))
        })
      }

      composable(
        route = TicketingScreens.Entry.name.plus("/{QR_KEY}")
      ) {
        it.arguments?.getString("QR_KEY")
          ?.let { qrReference ->
            VehicleEntryScreen(qrCode = qrReference, navController = navController)
          }
      }

      composable(
        route = TicketingScreens.Exit.name.plus("/{QR_KEY}")
      ) {
        it.arguments?.getString("QR_KEY")
          ?.let { qrReference ->
            VehicleExitScreen(qrReference = qrReference, navController = navController)
          }
      }
    }
  }
}

private fun navigateWithArguments(
  argument: String? = null,
  screen: TicketingScreens,
  navController: NavHostController
) {
  var route = screen.name
  // If argument is supplied, navigate using that argument
  argument?.let {
    route = screen.name.plus(it)
  }
  navController.navigate(route)
}
