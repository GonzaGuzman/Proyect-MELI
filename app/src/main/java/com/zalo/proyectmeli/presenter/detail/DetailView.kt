package com.zalo.proyectmeli.presenter.detail

import com.zalo.proyectmeli.utils.models.ProductDataResponse

interface DetailView {
    fun showSnackBar(message: String)
    fun loadRecycler()
    fun onProductFetched(productList: ProductDataResponse)
    fun loadGone()
    fun navigateToSearch()
    fun onBack()
}