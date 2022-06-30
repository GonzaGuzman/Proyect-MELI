package com.zalo.proyectmeli.utils.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Categories(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
)

@Entity(tableName = "recentlySearch")
data class ProductResponse(
    @PrimaryKey
    @NonNull
    val numberItem: Int,
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("condition") val condition: String,
    @SerializedName("price") val price: Double,
    @SerializedName("sold_quantity") val soldQuantity: Int,
    @SerializedName("permalink") val permaLink: String,
    @SerializedName("available_quantity") val stock: Int,
)

data class ProductDataResponse(
    @SerializedName("results") val products: List<ProductResponse>,
)

data class DescriptionResponse(
    @SerializedName("text") val text: String,
    @SerializedName("plain_text") val plainText: String,
)

@Entity(tableName = "searchHistory")
data class SearchHistory(
    @PrimaryKey
    val textSearch: String,
    val orderId: Int,
)
