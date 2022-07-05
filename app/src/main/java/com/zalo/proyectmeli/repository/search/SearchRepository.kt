package com.zalo.proyectmeli.repository.search

import com.zalo.proyectmeli.database.ItemDatabase
import com.zalo.proyectmeli.utils.models.SearchHistory
import com.zalo.proyectmeli.utils.sharedPreferences.SharedPreferencesML
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class SearchRepository(private val dataBase: ItemDatabase) {
    fun getAllSearch(): Single<List<SearchHistory>> = dataBase.searchDao().getAllSearch()

    fun insertNewSearch(searchHistory: SearchHistory): Completable = dataBase.searchDao().insertNewSearch(searchHistory)

    fun getPosition(): Int = SharedPreferencesML().searchPosition

    fun setPosition(position: Int) {
        SharedPreferencesML().searchPosition = position
    }
}