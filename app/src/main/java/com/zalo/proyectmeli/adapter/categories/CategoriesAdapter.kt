package com.zalo.proyectmeli.adapter.categories

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.detail.DetailActivity
import com.zalo.proyectmeli.utils.CATEGORY_SHOW
import com.zalo.proyectmeli.utils.CAT_ID
import com.zalo.proyectmeli.utils.TYPE_SHOW
import com.zalo.proyectmeli.utils.models.Categories

class CategoriesAdapter(private val categoriesList: MutableList<Categories>) :
    RecyclerView.Adapter<CategoriesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder =
        CategoriesViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.categories_item, parent, false))

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = categoriesList[position]
        holder.bind(item).apply {
            holder.itemView.setOnClickListener {
                it.context.startActivity(Intent(holder.itemView.context,
                    DetailActivity::class.java).apply {
                    putExtra(CAT_ID, item.id)
                    putExtra(TYPE_SHOW, CATEGORY_SHOW)
                })
            }
        }
    }
    override fun getItemCount(): Int = categoriesList.size

    fun appendList(categories: List<Categories>) {
        this.categoriesList.addAll(categories)
        notifyDataSetChanged()
    }
}

