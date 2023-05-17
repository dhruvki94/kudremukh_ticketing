package com.example.ticketing.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ticketing.R
import com.example.ticketing.model.UserRole

@Composable
fun MainScreen(
  onEntryButtonCLicked: () -> Unit,
  onExitButtonClicked: () -> Unit,
  onSearchButtonClicked: () -> Unit,
  onStatsButtonClicked: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: MainViewModel = hiltViewModel()
) {
  val role by viewModel.role

  if(role == UserRole.Admin.name) {
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier.fillMaxHeight(0.9f)
    ) {
      Spacer(modifier = Modifier.height(25.dp))
      Image(
        painter = painterResource(id = R.drawable.gok_logo), contentDescription = stringResource(
          id = R.string.logo_description
        ),
        modifier = modifier
          .height(175.dp)
          .width(175.dp)
      )
      Spacer(modifier = Modifier.height(25.dp))
      Text(
        text = stringResource(id = R.string.kudremukh),
        fontSize = 30.sp,
        style = TextStyle.Default.copy(
          color = Color(115, 20, 6),
          fontFamily = FontFamily.SansSerif
        ),
        textAlign = TextAlign.Center,
      )
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = stringResource(id = R.string.app_name),
        fontSize = 40.sp,
        style = TextStyle.Default.copy(
          color = Color(115, 20, 6),
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Center,
      )

      Spacer(modifier = Modifier.height(25.dp))
      Row {
        Button(
          onClick = onEntryButtonCLicked,
          modifier = modifier
            .height(75.dp)
            .width(150.dp)
        ) {
          Text(
            text = stringResource(id = R.string.vehicle_entry),
            fontSize = 28.sp
          )
        }
        Spacer(modifier = Modifier.width(25.dp))
        Button(
          onClick = onExitButtonClicked,
          modifier = modifier
            .height(75.dp)
            .width(150.dp)
        ) {
          Text(
            text = stringResource(id = R.string.vehicle_exit),
            fontSize = 28.sp
          )
        }
      }
      Spacer(modifier = Modifier.height(25.dp))
      Row {
        Button(
          onClick = onSearchButtonClicked,
          modifier = modifier
            .height(75.dp)
            .width(150.dp)
        ) {
          Text(
            text = stringResource(id = R.string.list),
            fontSize = 28.sp,
            textAlign = TextAlign.Center
          )
        }
        Spacer(modifier = Modifier.width(25.dp))
        Button(
          onClick = onStatsButtonClicked,
          modifier = modifier
            .height(75.dp)
            .width(150.dp)
        ) {
          Text(
            text = stringResource(id = R.string.stats),
            fontSize = 28.sp,
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }


  else {
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier.fillMaxHeight(0.9f)
    ) {
      Spacer(modifier = Modifier.height(25.dp))
      Image(
        painter = painterResource(id = R.drawable.gok_logo), contentDescription = stringResource(
          id = R.string.logo_description
        ),
        modifier = modifier
          .height(175.dp)
          .width(175.dp)
      )
      Spacer(modifier = Modifier.height(25.dp))
      Text(
        text = stringResource(id = R.string.kudremukh),
        fontSize = 30.sp,
        style = TextStyle.Default.copy(
          color = Color(115, 20, 6),
          fontFamily = FontFamily.SansSerif
        ),
        textAlign = TextAlign.Center,
      )
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = stringResource(id = R.string.app_name),
        fontSize = 40.sp,
        style = TextStyle.Default.copy(
          color = Color(115, 20, 6),
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Center,
      )

      Spacer(modifier = Modifier.height(25.dp))
      Row {
        Button(
          onClick = onEntryButtonCLicked,
          modifier = modifier
            .height(75.dp)
            .width(150.dp)
        ) {
          Text(
            text = stringResource(id = R.string.vehicle_entry),
            fontSize = 28.sp
          )
        }
        Spacer(modifier = Modifier.width(25.dp))
        Button(
          onClick = onExitButtonClicked,
          modifier = modifier
            .height(75.dp)
            .width(150.dp)
        ) {
          Text(
            text = stringResource(id = R.string.vehicle_exit),
            fontSize = 28.sp
          )
        }
      }
      Spacer(modifier = Modifier.height(25.dp))
      Button(
        onClick = onSearchButtonClicked,
        modifier = modifier
          .height(75.dp)
          .width(325.dp)
      ) {
        Text(
          text = stringResource(id = R.string.card_loss),
          fontSize = 28.sp,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}
