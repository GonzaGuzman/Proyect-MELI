package com.zalo.proyectmeli.presenter.item

import android.content.Intent
import com.zalo.proyectmeli.network.models.ProductResponse

interface ItemPresenterActions {
    fun initComponent(intent: Intent)
    fun setState(intent: Intent)
    fun getState(): ProductResponse
    fun saveItem(item: ProductResponse)
    fun navigateToMELI()
    fun translateCondition(condition: String): String
    fun dataBaseLimit(num: Int): Int
    fun validateAndSaveItem(item: ProductResponse)
}