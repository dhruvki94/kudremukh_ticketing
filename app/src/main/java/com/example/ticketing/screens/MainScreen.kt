package com.example.ticketing.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ticketing.R

@Composable
fun MainScreen(
  onEntryButtonCLicked: () -> Unit,
  onExitButtonClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier.fillMaxSize()
  ) {
    Button(onClick = onEntryButtonCLicked) {
      Text(text = stringResource(id = R.string.entry))
    }
    Spacer(modifier = Modifier.height(10.dp))
    Button(onClick = onExitButtonClicked) {
      Text(text = stringResource(id = R.string.exit))
    }
  }
}
