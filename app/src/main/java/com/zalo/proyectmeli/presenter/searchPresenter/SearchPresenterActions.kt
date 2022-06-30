package com.zalo.proyectmeli.presenter.searchPresenter

import com.zalo.proyectmeli.utils.models.SearchHistory

interface SearchPresenterActions {
    fun initComponent()
    fun getAllSearch()
    fun getArraySearch(searchHistory: List<SearchHistory>)
    fun insertNewSearch(search: String)
}