package pl.piechaczek.dawid.barcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pl.piechaczek.dawid.barcodescanner.ui.theme.BarcodescannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarcodescannerTheme {
                BarcodeCameraPreview(BarcodeImageAnalyzer(applicationContext))
            }
        }
    }
}