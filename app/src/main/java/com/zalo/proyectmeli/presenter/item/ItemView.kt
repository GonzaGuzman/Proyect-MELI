package com.zalo.proyectmeli.presenter.item

import com.zalo.proyectmeli.utils.models.ProductResponse

interface ItemView {
    fun navigateToMELI(permalink: String)
    fun retrieverExtras(item: ProductResponse)
    fun sharedItem(permalink: String)
    fun navigateToSearch()
    fun onBack()
}