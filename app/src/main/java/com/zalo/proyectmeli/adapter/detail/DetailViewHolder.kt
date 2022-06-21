package com.zalo.proyectmeli.adapter.detail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zalo.proyectmeli.databinding.ResultItemBinding
import com.zalo.proyectmeli.network.models.ProductResponse
import com.zalo.proyectmeli.utils.FormatNumber
import com.zalo.proyectmeli.utils.ShowImage

class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ResultItemBinding.bind(view)

    fun bind(productResponse: ProductResponse) {
        ShowImage.showImage(productResponse.thumbnail, binding.ivProduct)
        binding.tvNameProduct.text = productResponse.title
        binding.tvPrice.text = FormatNumber.formatNumber(productResponse.price)
    }
}