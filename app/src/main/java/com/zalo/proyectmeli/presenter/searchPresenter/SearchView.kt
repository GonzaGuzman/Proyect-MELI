package com.zalo.proyectmeli.presenter.searchPresenter

import com.zalo.proyectmeli.utils.models.SearchHistory

interface SearchView {
    fun loadGone()
    fun loadRecycler()
    fun showSnackBar(message: String)
    fun loadAdapter(searchList: List<SearchHistory>)
    fun navigateToDetail()
    fun loadArrayAdapter(searched: ArrayList<String>)
    fun backToPressed()
}