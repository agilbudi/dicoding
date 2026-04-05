package com.abupras.eventapp.utils

import android.graphics.Bitmap
import android.util.Log
import androidx.palette.graphics.Palette
import java.text.SimpleDateFormat
import java.util.Locale

fun getPalette(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()


fun changeFormatDate(dateString: String?): String {
    if (dateString == null) return "-"
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm",
            Locale.getDefault())

        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: "")
    } catch (e: Exception) {
        Log.e("Utils", "changeFormatDate: $e")
        dateString
    }
}