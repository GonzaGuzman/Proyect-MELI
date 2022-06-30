package com.zalo.proyectmeli.presenter.home

interface HomePresenterActions {
    fun initComponent()
    fun getCategoriesList()
    fun loadRecentlySeen()
    fun showItemListDb()
    fun showItemDb()
    fun navigateToSearch()
    fun onNegativeButtonClicked()
    fun onPositiveButtonClicked()
    fun deleteHistory()
    fun clearSharedValues()
    fun deleteSearch()
    fun deleteDialog()
    fun showHistorial()
}