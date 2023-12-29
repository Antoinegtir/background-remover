package com.example.background_remover

import android.graphics.Bitmap
import java.lang.Exception

interface OnBackgroundChangeListener {

    fun onSuccess(bitmap: Bitmap)

    fun onFailed(exception: Exception)

}