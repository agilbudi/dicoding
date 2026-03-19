package com.abupras.eventapp.utils

import android.graphics.Bitmap
import androidx.palette.graphics.Palette

// Generate palette synchronously and return it.
fun getPalette(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

