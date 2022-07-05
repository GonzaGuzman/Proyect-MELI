package com.zalo.proyectmeli.datasource.home

import com.zalo.proyectmeli.utils.models.Categories
import com.zalo.proyectmeli.utils.models.ProductResponse
import io.reactivex.rxjava3.disposables.Disposable

interface HomeDatasource {
    fun getCategories(
        onSuccess: (responsive: List<Categories>) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun getRecentlyItem(
        id: String,
        onSuccess: (responsive: ProductResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun clearHistory(
        onSuccess: ()-> Unit,
        onError: (Throwable) -> Unit,
    ):Disposable

    fun getIdRecentlySeenItem(): String
    fun setIdRecentlySeenItem(id: String)
    fun setCountItem(count: Int)
    fun setLinkRecentlySeen(id: String)
    fun setSearchPosition(position: Int)
   }