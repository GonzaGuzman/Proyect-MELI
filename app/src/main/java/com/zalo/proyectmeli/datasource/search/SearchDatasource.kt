package com.zalo.proyectmeli.datasource.search

import com.zalo.proyectmeli.utils.models.SearchHistory
import io.reactivex.rxjava3.disposables.Disposable

interface SearchDatasource {
    fun getAllSearch(
        onSuccess: (responsive: List<SearchHistory>) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun insertNewSearch(
        searchHistory: SearchHistory,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable

    fun getPosition(): Int
    fun setPosition(position: Int)
}