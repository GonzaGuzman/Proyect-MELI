package com.zalo.proyectmeli.datasource.search

import com.zalo.proyectmeli.repository.search.SearchRepository
import com.zalo.proyectmeli.utils.models.SearchHistory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchDataSourceImplements(private val repository: SearchRepository) : SearchDatasource {
    override fun getAllSearch(
        onSuccess: (responsive: List<SearchHistory>) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.getAllSearch()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess(it) },
                { onError(it) }
            )
    }

    override fun insertNewSearch(
        searchHistory: SearchHistory,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.insertNewSearch(searchHistory)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess() },
                { onError(it) }
            )
    }

    override fun getPosition(): Int {
        return repository.getPosition()
    }

    override fun setPosition(position: Int) {
        repository.setPosition(position)
    }

}