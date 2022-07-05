package com.zalo.proyectmeli.adapter.detail

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.item.ItemActivity
import com.zalo.proyectmeli.utils.ITEM_ID
import com.zalo.proyectmeli.utils.models.ProductDataResponse
import com.zalo.proyectmeli.utils.models.ProductResponse

class DetailAdapter(private val itemList: MutableList<ProductResponse>) :
    RecyclerView.Adapter<DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder =
        DetailViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.result_item, parent, false))

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            it.context.startActivity(Intent(holder.itemView.context,
                ItemActivity::class.java).apply {
                putExtra(ITEM_ID, item.id)
            })
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun appendProduct(productList: ProductDataResponse) {
        this.itemList.addAll(productList.products)
        notifyDataSetChanged()
    }
}