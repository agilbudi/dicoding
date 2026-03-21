package com.abupras.eventapp.utils

import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import java.text.SimpleDateFormat
import java.util.Locale

// Generate palette synchronously and return it.
fun getPalette(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

fun changeFormatDate(dateString: String?): String {
    if (dateString == null) return "-"
    return try {
        // Format asal dari API
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.getDefault())
        // Format tujuan untuk tampilan
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm",
            Locale.getDefault())

        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: "")
    } catch (e: Exception) {
        dateString // Jika error, kembalikan string asli
    }
}