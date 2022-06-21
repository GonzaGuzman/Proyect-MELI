package com.zalo.proyectmeli.datasource.item

import com.zalo.proyectmeli.network.models.ProductResponse
import io.reactivex.rxjava3.disposables.Disposable

interface ItemDatasource {
    fun dbInsertItem(
        productResponse: ProductResponse,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun getById(
        id:String,
        onSuccess: (response: Int) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable


    fun setCountItems(itemNumber: Int)
    fun getCountItems(): Int
    fun setIdRecentlySeenItem(id:String)


}