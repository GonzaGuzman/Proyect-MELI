package com.zalo.proyectmeli.datasource.detail

import com.zalo.proyectmeli.utils.models.ProductDataResponse
import com.zalo.proyectmeli.utils.models.ProductResponse
import io.reactivex.rxjava3.disposables.Disposable

interface DetailDatasource {
    fun getCategoriesDetail(
        categories: String,
        onSuccess: (responsive: ProductDataResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun getItemsList(
        item: String,
        onSuccess: (responsive: ProductDataResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun getItemsDb(
        onSuccess: (responsive: List<ProductResponse>) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable
}