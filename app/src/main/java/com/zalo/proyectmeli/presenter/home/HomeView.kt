package com.zalo.proyectmeli.presenter.home

import com.zalo.proyectmeli.utils.models.Categories
import com.zalo.proyectmeli.utils.models.ProductResponse

interface HomeView {
    fun showSnackBar(message: String)
    fun onCategoryFetched(categoriesList: List<Categories>)
    fun loadRecycler()
    fun navigateToHistoryDb()
    fun loadGone()
    fun showItemDb(item: ProductResponse)
    fun navigateToSearch()
    fun navigateToShowItem()
    fun dialogDismiss()
    fun onOffEmptyRecently(validate: Boolean)
    fun showAlertCloseSession()
    fun showHistory()
    fun openMenu()
    fun loadRecentlySeen(title: String, price: String, thumbnail: String)
    fun startSearch()
    fun refresh()
    fun open()
    fun onOffRecyclerView(validate: Boolean)
    fun validateDatabaseEmptyData(validate: Boolean)
    fun internetConnection(): Boolean
    fun internetFailViewEnabled()
    fun internetFailViewDisabled()
  }