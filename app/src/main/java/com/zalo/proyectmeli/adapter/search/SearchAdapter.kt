package com.zalo.proyectmeli.adapter.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.detail.DetailActivity
import com.zalo.proyectmeli.utils.KEY_SEARCH
import com.zalo.proyectmeli.utils.SEARCH_SHOW
import com.zalo.proyectmeli.utils.TYPE_SHOW
import com.zalo.proyectmeli.utils.models.SearchHistory

class SearchAdapter(private val listSearch: MutableList<SearchHistory>) :
    RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false))

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = listSearch[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            it.context.startActivity(Intent(holder.itemView.context,
                DetailActivity::class.java).apply {
                putExtra(KEY_SEARCH, item.textSearch)
                putExtra(TYPE_SHOW, SEARCH_SHOW)
            })
        }
    }

    override fun getItemCount(): Int = listSearch.size

    fun appendList(searched: List<SearchHistory>) {
        this.listSearch.addAll(searched)
        notifyDataSetChanged()
    }
}

