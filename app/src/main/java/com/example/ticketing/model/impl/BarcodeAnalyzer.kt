package com.example.ticketing.model.impl

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@ExperimentalGetImage
class BarcodeAnalyser(
  val callback: (String) -> Unit
) : ImageAnalysis.Analyzer {
  override fun analyze(imageProxy: ImageProxy) {
    val options = BarcodeScannerOptions.Builder()
      .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
      .build()

    val scanner = BarcodeScanning.getClient(options)
    val mediaImage = imageProxy.image
    mediaImage?.let {
      val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

      scanner.process(image)
        .addOnSuccessListener { barcodes ->
          if (barcodes.size > 0) {
            barcodes.firstOrNull()?.rawValue
              ?.let { callback(it.replace("/","")) }
          }
        }
        .addOnFailureListener {
          // Task failed with an exception
          // ...
        }
    }
    imageProxy.close()
  }
}