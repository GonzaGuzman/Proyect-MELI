package com.zalo.proyectmeli.adapter.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.detail.DetailActivity
import com.zalo.proyectmeli.utils.*
import com.zalo.proyectmeli.utils.models.SearchHistory

class SearchAdapter(private val listSearch: MutableList<SearchHistory>) :
    RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SearchViewHolder(layoutInflater.inflate(R.layout.search_item, parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = listSearch[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(KEY_SEARCH, item.textSearch)
            intent.putExtra(TYPE_SHOW, SEARCH_SHOW)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listSearch.size

    fun appendList(searched: List<SearchHistory>) {
        this.listSearch.addAll(searched)
        notifyDataSetChanged()
    }
}

