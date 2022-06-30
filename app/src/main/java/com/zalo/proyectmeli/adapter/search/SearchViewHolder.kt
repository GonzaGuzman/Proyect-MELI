package com.zalo.proyectmeli.adapter.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zalo.proyectmeli.databinding.SearchItemBinding
import com.zalo.proyectmeli.utils.models.SearchHistory

class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = SearchItemBinding.bind(view)

    fun bind(searchHistory: SearchHistory) {
        binding.tvSearched.text = searchHistory.textSearch
    }
}

