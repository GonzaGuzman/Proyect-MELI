package com.zalo.proyectmeli.presenter.home

interface HomePresenterActions {
    fun initRecyclerCategories()
    fun getCategoriesList()
    fun loadRecentlySeen()
    fun showItemListDb()
    fun showItemDb()
}