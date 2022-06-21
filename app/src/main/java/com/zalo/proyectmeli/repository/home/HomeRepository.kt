package com.zalo.proyectmeli.repository.home

import com.zalo.proyectmeli.database.ItemDatabase
import com.zalo.proyectmeli.network.APIServiceImplements
import com.zalo.proyectmeli.network.models.Categories
import com.zalo.proyectmeli.network.models.ProductResponse
import com.zalo.proyectmeli.sharedPreferences.SharedPreferencesML
import io.reactivex.rxjava3.core.Single

class HomeRepository(
    private val apiService: APIServiceImplements,
    private val dataBase: ItemDatabase,
) {
    fun getCategories(): Single<List<Categories>> {
        return apiService.getCategories()
    }

    fun getRecentlyItem(id:String): Single<ProductResponse> {
        return dataBase.itemDao().getLastItem(id)
    }

    fun getIdRecentlySeenItem(): String {
        return SharedPreferencesML().idRecentlySeen
    }

}