package com.zalo.proyectmeli.adapter.detail

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.item.ItemActivity
import com.zalo.proyectmeli.utils.models.ProductDataResponse
import com.zalo.proyectmeli.utils.models.ProductResponse
import com.zalo.proyectmeli.utils.*

class DetailAdapter(private val itemList: MutableList<ProductResponse>) :
    RecyclerView.Adapter<DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DetailViewHolder(layoutInflater.inflate(R.layout.result_item, parent, false))
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ItemActivity::class.java)
            intent.putExtra(ITEM_ID, item.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun appendProduct(productList: ProductDataResponse) {
        this.itemList.addAll(productList.products)
        notifyDataSetChanged()
    }
}