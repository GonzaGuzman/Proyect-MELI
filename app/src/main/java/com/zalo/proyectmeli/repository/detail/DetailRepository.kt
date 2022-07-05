package com.zalo.proyectmeli.repository.detail

import com.zalo.proyectmeli.database.ItemDatabase
import com.zalo.proyectmeli.network.APIServiceImplements
import com.zalo.proyectmeli.utils.models.ProductDataResponse
import com.zalo.proyectmeli.utils.models.ProductResponse
import io.reactivex.rxjava3.core.Single

class DetailRepository(
    private val apiService: APIServiceImplements,
    private val dataBase: ItemDatabase,
) {
    fun getCategoriesDetails(categories: String): Single<ProductDataResponse> = apiService.getCategoriesDetail(categories)

    fun getItemsList(item: String): Single<ProductDataResponse> = apiService.getItemsList(item)

    fun getItemsDb(): Single<List<ProductResponse>> = dataBase.itemDao().getItems()
}