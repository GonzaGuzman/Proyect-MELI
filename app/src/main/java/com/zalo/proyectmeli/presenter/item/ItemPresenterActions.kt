package com.zalo.proyectmeli.presenter.item

import android.content.Intent
import android.widget.TextView
import com.zalo.proyectmeli.utils.models.ProductResponse

interface ItemPresenterActions {
    fun initComponent(intent: Intent)
    fun translateCondition(condition: String): String
    fun getItemById(id: String)
    fun getItemDescription(id: String, view: TextView)
    fun dataBaseLimit(num: Int): Int
    fun validateAndSaveInDb(item: ProductResponse)
    fun saveItem(item: ProductResponse)
    fun navigateToMeli()
    fun navigateToShared()
}