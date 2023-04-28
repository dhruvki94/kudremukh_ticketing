package com.example.ticketing

import androidx.annotation.StringRes
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ticketing.screens.CameraPreviewScreen
import com.example.ticketing.screens.MainScreen
import com.example.ticketing.screens.login.LoginScreen
import com.example.ticketing.screens.scanner.ScannerScreen
import com.example.ticketing.screens.search_vehicle.SearchVehicleScreen
import com.example.ticketing.screens.splash.SplashScreen
import com.example.ticketing.screens.user.UserScreen
import com.example.ticketing.screens.vehicle_entry.VehicleEntryScreen
import com.example.ticketing.screens.vehicle_exit.VehicleExitScreen

enum class TicketingScreens(@StringRes val title: Int) {
  Splash(R.string.splash),
  Login(R.string.login),
  Main(R.string.main),
  User(R.string.user),
  EntryScanner(R.string.scanner),
  ExitScanner(R.string.scanner),
  Entry(R.string.entry),
  Exit(R.string.exit),
  Search(R.string.search_screen),
  EntryCamera(R.string.entry_camera),
  ExitCamera(R.string.entry_camera)
}

@Composable
fun TicketingAppBar(
  currentScreen: TicketingScreens,
  isHomeScreen: Boolean,
  navigateToHome: () -> Unit,
  navigateToUser: () -> Unit,
  modifier: Modifier = Modifier
) {
  TopAppBar(
    title = { Text(text = stringResource(id = currentScreen.title)) },
    modifier = modifier,
    navigationIcon = {
      if (!isHomeScreen) {
        IconButton(onClick = navigateToHome) {
          Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = stringResource(R.string.home_button)
          )
        }
      } else {
        IconButton(onClick = { }) {
          Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = stringResource(R.string.home_button)
          )
        }
      }
    },
    actions = {
      if(isHomeScreen) {
        IconButton(onClick = navigateToUser) {
          Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = stringResource(R.string.account_button)
          )
        }
      }
    }
  )
}

@Composable
@ExperimentalMaterialApi
@ExperimentalGetImage
fun TicketingApp() {
  val navController = rememberNavController()

  val backStackEntry by navController.currentBackStackEntryAsState()

  // TODO: Get the name of the current screen
  val currentScreenName = TicketingScreens.valueOf(
    backStackEntry?.destination?.route?.substringBefore('/') ?: TicketingScreens.Main.name
  )

  Box {
    Image(
      modifier = Modifier.fillMaxSize(),
      painter = painterResource(R.drawable.kudremukh_bg),
      contentDescription = "Kudremukh_background",
      contentScale = ContentScale.FillBounds,
      alpha = 0.2f,
      colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
        setToScale(
          1.5f,
          1.5f,
          1.5f,
          1f
        )
      })
    )
  }
  Scaffold(
    backgroundColor = Color.Transparent,
    topBar = {
      TicketingAppBar(
        currentScreen = TicketingScreens.Main,
        isHomeScreen = navController.currentBackStackEntryAsState().value?.destination?.route?.substringBefore(
          '/'
        ) == TicketingScreens.Main.name,
        navigateToHome = { navController.navigate(TicketingScreens.Main.name) },
        navigateToUser = { navController.navigate(TicketingScreens.User.name) }
      )
    }
  ) { innerPadding ->

    NavHost(
      navController = navController, startDestination = TicketingScreens.Splash.name,
      modifier = Modifier.padding(innerPadding)
    ) {
      composable(route = TicketingScreens.Splash.name) {
        SplashScreen(navController)
      }

      composable(route = TicketingScreens.Main.name) {
        MainScreen(
          onEntryButtonCLicked = { navController.navigate(TicketingScreens.EntryScanner.name) },
          onExitButtonClicked = { navController.navigate(TicketingScreens.ExitScanner.name) },
          onSearchButtonClicked = { navController.navigate(TicketingScreens.Search.name) }
        )
      }

      composable(route = TicketingScreens.Login.name) {
        LoginScreen(onSuccessfulLogin = { navController.navigate(TicketingScreens.Splash.name) { popUpTo(TicketingScreens.Splash.name) { inclusive = false } } })
      }

      composable(route = TicketingScreens.User.name) {
        UserScreen {
          navController.navigate(route = TicketingScreens.Splash.name) {
            launchSingleTop = true
            popUpTo(TicketingScreens.Splash.name) { inclusive = true }
          }
        }
      }

      composable(route = TicketingScreens.EntryScanner.name) {
        ScannerScreen(
          onScannerButton = {
            navController.navigate(TicketingScreens.EntryCamera.name)
          },
          onQrCodeEnter = {
            navController.navigate(TicketingScreens.Entry.name.plus("/${it}"))
          }
        )
      }

      composable(route = TicketingScreens.ExitScanner.name) {
        ScannerScreen(
          onScannerButton = {
            navController.navigate(TicketingScreens.ExitCamera.name)
          },
          onQrCodeEnter = {
            navController.navigate(TicketingScreens.Exit.name.plus("/${it}"))
          }
        )
      }

      composable(route = TicketingScreens.EntryCamera.name) {
        CameraPreviewScreen(callback = {
          navController.navigate(TicketingScreens.Entry.name.plus("/${it}"))
        })
      }

      composable(route = TicketingScreens.ExitCamera.name) {
        CameraPreviewScreen(callback = {
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

      composable(
        route = TicketingScreens.Search.name
      ) {
        SearchVehicleScreen(navController = navController)
      }
    }
  }
}
