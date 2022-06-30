package com.zalo.proyectmeli.presenter.home

import com.zalo.proyectmeli.utils.models.Categories
import com.zalo.proyectmeli.utils.models.ProductResponse

interface HomeView {
    fun showSnackBar(message: String)
    fun onCategoryFetched(categoriesList: List<Categories>)
    fun loadRecycler()
    fun loadRecentlySeen(item: ProductResponse)
    fun showHistoryDb()
    fun loadGone()
    fun showItemDb(item: ProductResponse)
    fun navigateToSearch()
    fun navigateToShowItem()
    fun dialogDismiss()
    fun emptyRecently(validate: Boolean)
    fun showAlertCloseSession()
    fun navigateToHistoryDb()
    fun openMenu()
}