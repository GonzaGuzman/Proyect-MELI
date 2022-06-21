package com.zalo.proyectmeli.presenter.item

import android.content.Intent
import android.content.res.Resources
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.datasource.item.ItemDatasource
import com.zalo.proyectmeli.network.models.ProductResponse
import com.zalo.proyectmeli.utils.*
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ItemPresenter(
    private val itemView: ItemView,
    private val itemDatasource: ItemDatasource,
    private val itemState: ItemState,
    private val resources: Resources,
) :
    ItemPresenterActions {

    private val compositeDisposable = CompositeDisposable()

    override fun initComponent(intent: Intent) {
        setState(intent)
        val item = getState()
        itemView.retrieverExtras(item)
        itemView.textSearch()
        validateAndSaveItem(item)
    }

    override fun setState(intent: Intent) {
        val numberItem = dataBaseLimit(itemDatasource.getCountItems())
        val id = intent.getStringExtra(ITEM_ID)
        val title = intent.getStringExtra(ITEM_TITLE).toString()
        val price = intent.getDoubleExtra(ITEM_PRICE, 0.0)
        val soldQuantity = intent.getIntExtra(ITEM_SOLD_QUANTITY, 0)
        val thumbnail = intent.getStringExtra(ITEM_THUMBNAIL).toString()
        val permaLink = intent.getStringExtra(ITEM_PERMALINK).toString()
        val condition = translateCondition(intent.getStringExtra(ITEM_CONDITION).toString())

        itemState.numberItem.set(numberItem)
        itemState.id.set(id)
        itemState.title.set(title)
        itemState.price.set(price)
        itemState.condition.set(condition)
        itemState.soldQuantity.set(soldQuantity)
        itemState.thumbnail.set(thumbnail)
        itemState.permaLink.set(permaLink)

        itemDatasource.setIdRecentlySeenItem(id.toString())
    }

    override fun translateCondition(condition: String): String {
        return if (condition == resources.getString(
                R.string.newState)
        ) {
            resources.getString(R.string.newStateSpanish)
        } else
            resources.getString(R.string.usedState)
    }

    override fun dataBaseLimit(num: Int): Int {
        var numberReturn = num
        if (num >= LIMIT_DATABASE) {
            numberReturn = 0
            itemDatasource.setCountItems(1)
        } else {
            itemDatasource.setCountItems(num + 1)
        }
        return numberReturn
    }

    override fun getState(): ProductResponse {
        return ProductResponse(
            itemState.numberItem.get() ?: 1,
            itemState.id.get().orEmpty(),
            itemState.title.get().orEmpty(),
            itemState.thumbnail.get().orEmpty(),
            itemState.condition.get().orEmpty(),
            itemState.price.get() ?: 0.0,
            itemState.soldQuantity.get() ?: 0,
            itemState.permaLink.get().orEmpty()
        )
    }

    override fun saveItem(item: ProductResponse) {
        compositeDisposable.add(
            itemDatasource.dbInsertItem(item,
                { println(resources.getString(R.string.saveSuccesfullyItem)) },
                { println(it.message.toString()) })
        )
    }

    override fun validateAndSaveItem(item: ProductResponse) {
        compositeDisposable.add(
            itemDatasource.getById(item.id,
                { if (it <= 0) saveItem(item) else println(resources.getString(R.string.existing_id_in_database)) },
                { println(it.message.toString()) }
            )
        )
    }

    override fun navigateToMELI() {
        val permaLink = itemState.permaLink.get().orEmpty()
        itemView.navigateToMELI(permaLink)
    }
}