package com.zalo.proyectmeli.presenter.item

interface ItemView {
    fun startMELI(permalink: String)
    fun sharedItem(permalink: String)
    fun navigateToSearch()
    fun onBack()
    fun startSearch()
    fun navigateToShared()
    fun navigateToMeli()
    fun retrieveExtras(
        title: String,
        state: String,
        price: String,
        thumbnail: String,
        stock: String,
    )

    fun setDescription(description: String)
    fun back()
    fun showSnackBarRed(message: String)
    fun internetConnection(): Boolean
    fun showSnackBar(message: String)
}