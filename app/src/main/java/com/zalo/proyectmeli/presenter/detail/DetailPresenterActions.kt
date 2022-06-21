package com.zalo.proyectmeli.presenter.detail

import android.content.Intent

interface DetailPresenterActions {
    fun getProductList(search: String)
    fun searchItems(itemSearch: String)
    fun getProductListOfDb()
    fun initComponent(intent: Intent)
    fun loadFetched(intent: Intent)
}