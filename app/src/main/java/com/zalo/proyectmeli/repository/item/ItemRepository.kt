package com.zalo.proyectmeli.repository.item

import com.zalo.proyectmeli.database.ItemDatabase
import com.zalo.proyectmeli.network.models.ProductResponse
import com.zalo.proyectmeli.sharedPreferences.SharedPreferencesML
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class ItemRepository(private val dataBase: ItemDatabase) {
    fun insertItemDb(item: ProductResponse): Completable {
        return dataBase.itemDao().insert(item)
    }

    fun setCountItems(itemNumber: Int) {
        SharedPreferencesML().countItem = itemNumber
        }

    fun getCountItems(): Int {
        return SharedPreferencesML().countItem
    }

    fun setIdRecentlySeenItems(id: String) {
        SharedPreferencesML().idRecentlySeen = id
    }

    fun getById(id:String): Single<Int>{
        return dataBase.itemDao().getById(id)
    }

}