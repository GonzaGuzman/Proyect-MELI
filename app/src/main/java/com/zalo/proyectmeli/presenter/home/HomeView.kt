package com.zalo.proyectmeli.presenter.home

import com.zalo.proyectmeli.network.models.Categories
import com.zalo.proyectmeli.network.models.ProductResponse

interface HomeView {
    fun showSnackBar(message: String)
    fun textSearch()
    fun onCategoryFetched(categoriesList: List<Categories>)
    fun loadRecycler()
    fun loadRecentlySeen(item: ProductResponse)
    fun showListDb()
    fun loadGone()
    fun showItemDb(item: ProductResponse)
}