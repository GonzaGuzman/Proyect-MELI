package com.zalo.proyectmeli.datasource.home

import com.zalo.proyectmeli.network.models.Categories
import com.zalo.proyectmeli.network.models.ProductResponse
import com.zalo.proyectmeli.repository.home.HomeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeDatasourceImplements(private val repository: HomeRepository) :
    HomeDatasource {
    override fun getCategories(
        onSuccess: (responsive: List<Categories>) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess(it) },
                { onError(it) })
    }

    override fun getIdRecentlySeenItem(): String {
     return repository.getIdRecentlySeenItem()
    }

    override fun getRecentlyItem(
        id:String,
        onSuccess: (responsive: ProductResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.getRecentlyItem(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess(it) },
                { onError(it) })
    }
}