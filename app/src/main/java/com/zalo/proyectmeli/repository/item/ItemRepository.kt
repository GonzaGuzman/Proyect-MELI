package com.zalo.proyectmeli.repository.item

import com.zalo.proyectmeli.database.ItemDatabase
import com.zalo.proyectmeli.network.APIServiceImplements
import com.zalo.proyectmeli.utils.models.DescriptionResponse
import com.zalo.proyectmeli.utils.models.ProductResponse
import com.zalo.proyectmeli.utils.sharedPreferences.SharedPreferencesML
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class ItemRepository(
    private val dataBase: ItemDatabase,
    private val apiService: APIServiceImplements,
) {
    fun insertItemDb(productResponse: ProductResponse): Completable = dataBase.itemDao().insert(productResponse)

    fun setCountItems(itemNumber: Int) {
        SharedPreferencesML().countItem = itemNumber
    }

    fun getCountItems(): Int = SharedPreferencesML().countItem

    fun setIdRecentlySeenItems(id: String) {
        SharedPreferencesML().idRecentlySeen = id
    }

    fun setPermalinkRecentlySeenItems(permalink: String) {
        SharedPreferencesML().permaLinkRecentlySeen = permalink
    }

    fun getPermalinkRecentlySeenItems(): String = SharedPreferencesML().permaLinkRecentlySeen

    fun getById(id: String): Single<Int> = dataBase.itemDao().getById(id)

    fun getItemDescription(id: String): Single<DescriptionResponse> = apiService.getItemDescription(id)

    fun getItemById(id: String): Single<ProductResponse> = apiService.getItemById(id)
}