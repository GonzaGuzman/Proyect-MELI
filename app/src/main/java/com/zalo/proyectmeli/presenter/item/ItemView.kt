package com.zalo.proyectmeli.presenter.item

import com.zalo.proyectmeli.network.models.ProductResponse

interface ItemView {
    fun textSearch()
    fun retrieverExtras(productResponse: ProductResponse)
    fun navigateToMELI(item: String)
}