package com.zalo.proyectmeli.datasource.item

import com.zalo.proyectmeli.utils.models.DescriptionResponse
import com.zalo.proyectmeli.utils.models.ProductResponse
import io.reactivex.rxjava3.disposables.Disposable

interface ItemDatasource {
    fun dbInsertItem(
        productResponse: ProductResponse,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun getById(
        id: String,
        onSuccess: (response: Int) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable


    fun setCountItems(itemNumber: Int)
    fun getCountItems(): Int
    fun setIdRecentlySeenItem(id: String)
    fun setPermalinkRecentlySeenItem(permalink: String)
    fun getPermalinkRecentlySeenItem(): String

    fun getItemDescription(
        id: String,
        onSuccess: (response: DescriptionResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun getItemById(
        id: String,
        onSuccess: (response: ProductResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable
}