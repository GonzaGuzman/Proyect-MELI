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
    fun clearSharedValues()
    fun deleteDialog()
    fun showHistorial()
    fun openMenu()
    fun activateViews(validate: Boolean)
    fun internetFail()
    fun refreshButton()
}