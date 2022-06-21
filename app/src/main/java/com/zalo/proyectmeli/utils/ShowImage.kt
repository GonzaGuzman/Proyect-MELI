package com.zalo.proyectmeli.utils

import android.widget.ImageView
import androidx.core.net.toUri
import coil.load
import com.zalo.proyectmeli.R

object ShowImage {
   fun showImage(image: String, view: ImageView) {
        val imgUri = image.toUri().buildUpon()?.scheme("https")?.build()
        view.load(imgUri) {
            error(R.drawable.ic_baseline_cloud_off_24)
        }
    }
}