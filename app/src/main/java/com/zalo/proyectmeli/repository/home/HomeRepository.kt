package com.zalo.proyectmeli.repository.home

import com.zalo.proyectmeli.database.ItemDatabase
import com.zalo.proyectmeli.network.APIServiceImplements
import com.zalo.proyectmeli.utils.models.Categories
import com.zalo.proyectmeli.utils.models.ProductResponse
import com.zalo.proyectmeli.utils.sharedPreferences.SharedPreferencesML
import io.reactivex.rxjava3.core.Single

class HomeRepository(
    private val apiService: APIServiceImplements,
    private val dataBase: ItemDatabase,
) {
    fun getCategories(): Single<List<Categories>> = apiService.getCategories()

    fun getRecentlyItem(id: String): Single<ProductResponse> = dataBase.itemDao().getLastItem(id)

    fun getIdRecentlySeenItem(): String = SharedPreferencesML().idRecentlySeen

    fun setIdRecentlySeenItem(id: String) {
        SharedPreferencesML().idRecentlySeen = id
    }

    fun setCountItem(count: Int) {
        SharedPreferencesML().countItem = count
    }

    fun setLinkRecentlySeen(id: String) {
        SharedPreferencesML().permaLinkRecentlySeen = id
    }

    fun setSearchPosition(position: Int) {
        SharedPreferencesML().searchPosition = position
    }

    fun deleteAllDbTables() = dataBase.clearAllTables()
}