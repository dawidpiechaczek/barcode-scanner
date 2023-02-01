package pl.piechaczek.dawid.barcodescanner

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeImageAnalyzer(val context: Context) : ImageAnalysis.Analyzer {
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_AZTEC,
                    Barcode.FORMAT_CODE_128,
                    Barcode.FORMAT_CODE_93,
                    Barcode.FORMAT_CODE_39,
                    Barcode.FORMAT_EAN_13,
                    Barcode.FORMAT_EAN_8,
                    Barcode.FORMAT_UPC_E,
                    Barcode.FORMAT_UPC_A
                ).build()

            val scanner = BarcodeScanning.getClient()
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.size > 0)
                        Toast.makeText(
                            context,
                            "BARCODE SIZE: ${barcodes.size}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    doOnScanSuccess(barcodes)
                }
                .addOnFailureListener {
                    Log.e("SCANNER", it.toString())
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun doOnScanSuccess(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            val bounds = barcode.boundingBox
            val corners = barcode.cornerPoints
            val rawValue = barcode.rawValue
            // See API reference for complete list of supported types
            when (barcode.valueType) {
                Barcode.TYPE_WIFI -> {
                    val ssid = barcode.wifi!!.ssid
                    val password = barcode.wifi!!.password
                    val type = barcode.wifi!!.encryptionType
                }
                Barcode.TYPE_URL -> {
                    val title = barcode.url?.title
                    val url = barcode.url?.url
                    Log.d("SCANNER", "$url AND $title")
                }
                else -> {
                    val title = barcode.rawValue
                    Log.d("SCANNER", "EXAMPLE: $title")
                }
            }
        }
    }
}