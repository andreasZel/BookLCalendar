package com.andreaszel.booklcalendar.lookup

import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

object IsbnScanner {
    private val options = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_EAN_13, Barcode.FORMAT_UPC_A).enableAutoZoom().build()

    // Null when the person backs out of the scanner
    suspend fun scan(context: Context): String? = GmsBarcodeScanning.getClient(context, options).startScan().awaitOrNull()?.rawValue
}
