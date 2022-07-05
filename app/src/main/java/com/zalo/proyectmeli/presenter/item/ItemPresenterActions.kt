package com.zalo.proyectmeli.presenter.item

import android.content.Intent
import com.zalo.proyectmeli.utils.models.ProductResponse

interface ItemPresenterActions {
    fun initComponent(intent: Intent)
    fun translateCondition(condition: String): String
    fun getItemById(id: String)
    fun dataBaseLimit(num: Int): Int
    fun validateAndSaveInDb(item: ProductResponse)
    fun saveItem(item: ProductResponse)
    fun navigateToMeli()
    fun navigateToShared()
    fun navigateToSearch()
    fun getItemDescription(id: String)
    fun back()
    fun internetFail()
    fun refreshButton(intent: Intent)
}