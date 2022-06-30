package com.zalo.proyectmeli.datasource.detail

import com.zalo.proyectmeli.utils.models.ProductDataResponse
import com.zalo.proyectmeli.utils.models.ProductResponse
import com.zalo.proyectmeli.repository.detail.DetailRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailDatasourceImplements(private val repository: DetailRepository) :
    DetailDatasource {

    override fun getCategoriesDetail(
        categories: String,
        onSuccess: (responsive: ProductDataResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.getCategoriesDetails(categories)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess(it) },
                { onError(it) })
    }

    override fun getItemsList(
        item: String,
        onSuccess: (responsive: ProductDataResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.getItemsList(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess(it) },
                { onError(it) })
    }

    override fun getItemsDb(
        onSuccess: (responsive: List<ProductResponse>) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.getItemsDb()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess(it) },
                { onError(it) }
            )
    }


}