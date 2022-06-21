package com.zalo.proyectmeli.datasource.home

import com.zalo.proyectmeli.network.models.Categories
import com.zalo.proyectmeli.network.models.ProductResponse
import io.reactivex.rxjava3.disposables.Disposable

interface HomeDatasource {
    fun getCategories(
        onSuccess: (responsive: List<Categories>) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun getRecentlyItem(
        id:String,
        onSuccess: (responsive: ProductResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun getIdRecentlySeenItem():String
}