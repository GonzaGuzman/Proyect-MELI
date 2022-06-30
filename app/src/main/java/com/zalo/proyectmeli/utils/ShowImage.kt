package com.zalo.proyectmeli.utils

import android.widget.ImageView
import androidx.core.net.toUri
import coil.load
import com.zalo.proyectmeli.R

object ShowImage {
    fun showImageO(image: String, view: ImageView) {
        val photo = image.replace("-I", "-O")
        val imgUri = photo.toUri().buildUpon()?.scheme("https")?.build()
        view.load(imgUri) {
            error(R.drawable.ic_baseline_cloud_off_24)
        }

    }

    fun showImageW(image: String, view: ImageView) {
        val photo = image.replace("-I", "-W")
        val imgUri = photo.toUri().buildUpon()?.scheme("https")?.build()
        view.load(imgUri) {
            error(R.drawable.ic_baseline_cloud_off_24)
        }

    }
}
