package com.example.ticketing.screens

import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ticketing.model.impl.BarcodeAnalyser
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@androidx.camera.core.ExperimentalGetImage
@Composable
fun CameraPreviewScreen(
  callback: (String) -> Unit
) {
  val cameraPermissionState = rememberPermissionState(
    android.Manifest.permission.CAMERA
  )

  if (cameraPermissionState.status.isGranted) {
    var cameraProvider: ProcessCameraProvider? = null
    DisposableEffect(key1 = cameraProvider) {
      onDispose {
        cameraProvider?.let { it.unbindAll() } // closes the camera
      }
    }
    AndroidView(
      { context ->
        val cameraExecutor = Executors.newSingleThreadExecutor()
        val previewView = PreviewView(context).also {
          it.scaleType = PreviewView.ScaleType.FILL_CENTER
        }
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
          cameraProvider = cameraProviderFuture.get()

          val preview = Preview.Builder()
            .build()
            .also {
              it.setSurfaceProvider(previewView.surfaceProvider)
            }

          val imageCapture = ImageCapture.Builder().build()

          val imageAnalyzer = ImageAnalysis.Builder()
            .build()
            .also {
              it.setAnalyzer(cameraExecutor, BarcodeAnalyser(callback))
            }

          val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

          try {
            // Unbind use cases before rebinding
            cameraProvider?.let {
              it.unbindAll()

              // Bind use cases to camera
              it.bindToLifecycle(
                context as ComponentActivity, cameraSelector, preview, imageCapture, imageAnalyzer
              )
            }

          } catch (exc: Exception) {
          }
        }, ContextCompat.getMainExecutor(context))
        previewView
      },
      modifier = Modifier.fillMaxSize()
    )
  } else {
    Column {
      LaunchedEffect(key1 = Unit) { cameraPermissionState.launchPermissionRequest() }
    }
  }
}
