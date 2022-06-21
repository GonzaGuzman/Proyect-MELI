package com.zalo.proyectmeli.presenter.detail

import com.zalo.proyectmeli.network.models.ProductDataResponse

interface DetailView {
    fun showSnackBar(message: String)
    fun loadRecycler()
    fun onProductFetched(productList: ProductDataResponse)
    fun textSearch()
    fun loadGone()
}