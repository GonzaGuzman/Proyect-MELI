package com.zalo.proyectmeli.adapter.categories

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zalo.proyectmeli.databinding.CategoriesItemBinding
import com.zalo.proyectmeli.network.models.Categories

class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = CategoriesItemBinding.bind(view)

    fun bind(categories: Categories) {
        binding.btnCat.text = categories.name
    }
}
