package com.zalo.proyectmeli.adapter.categories

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.detail.DetailActivity
import com.zalo.proyectmeli.network.models.Categories
import com.zalo.proyectmeli.utils.CATEGORY_SHOW
import com.zalo.proyectmeli.utils.CAT_ID
import com.zalo.proyectmeli.utils.TYPE_SHOW

class CategoriesAdapter(private val categoriesList: MutableList<Categories>) :
    RecyclerView.Adapter<CategoriesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CategoriesViewHolder(layoutInflater.inflate(R.layout.categories_item, parent, false))
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = categoriesList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(CAT_ID, item.id)
            intent.putExtra(TYPE_SHOW, CATEGORY_SHOW)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    fun appendList(categories: List<Categories>) {
        this.categoriesList.addAll(categories)
        notifyDataSetChanged()
    }
}

