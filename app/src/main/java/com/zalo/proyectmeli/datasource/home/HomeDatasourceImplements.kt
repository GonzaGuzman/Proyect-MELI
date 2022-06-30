package com.zalo.proyectmeli.datasource.home

import com.zalo.proyectmeli.utils.models.Categories
import com.zalo.proyectmeli.utils.models.ProductResponse
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

    override fun setIdRecentlySeenItem(id: String) {
        repository.setIdRecentlySeenItem(id)
    }

    override fun setCountItem(count: Int) {
        repository.setCountItem(count)
    }

    override fun setLinkRecentlySeen(id: String) {
        repository.setLinkRecentlySeen(id)
    }

    override fun setSearchPosition(position: Int) {
        repository.setSearchPosition(position)
    }

    override fun getRecentlyItem(
        id: String,
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

    override fun deleteHistory(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.deleteHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess() },
                { onError(it) }
            )
    }

    override fun deleteSearch(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.deleteSearchHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess },
                { onError(it) }
            )
    }
}