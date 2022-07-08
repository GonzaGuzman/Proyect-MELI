package com.zalo.proyectmeli.presenter.item

import android.content.Intent
import android.content.res.Resources
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.datasource.item.ItemDatasource
import com.zalo.proyectmeli.utils.models.ProductResponse
import com.zalo.proyectmeli.utils.*
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ItemPresenter(
    private val itemView: ItemView,
    private val itemDatasource: ItemDatasource,
    private val resources: Resources,
) :
    ItemPresenterActions {
    private val compositeDisposable = CompositeDisposable()
    override fun initComponent(intent: Intent) {
        val id = intent.getStringExtra(ITEM_ID) ?: ""
        getItemById(id)
        getItemDescription(id)
        itemView.navigateToSearch()
        itemView.navigateToMeli()
        itemView.navigateToShared()
        itemView.onBack()
    }

    override fun getItemById(id: String) {
        compositeDisposable.add(
            itemDatasource.getItemById(id,
                {
                    setRetrieveExtras(it)
                    itemDatasource.setIdRecentlySeenItem(it.id)
                    itemDatasource.setPermalinkRecentlySeenItem(it.permaLink)
                    validateAndSaveInDb(it)
                },
                {
                    if (!itemView.internetConnection()) internetFail() else itemView.showSnackBar(
                        resources.getString(R.string.simple_error_message))
                }
            )
        )
    }

    private fun setRetrieveExtras(item: ProductResponse){
        val condition = translateCondition(item.condition)
        val title = item.title
        val thumbnail = item.thumbnail
        val state = resources.getString(R.string.state_sold, condition, item.soldQuantity)
        val price = FormatNumber.formatNumber(item.price)
        val stock = resources.getString(R.string.stock, item.stock)
        itemView.retrieveExtras(title, state, price, thumbnail, stock)
    }

    override fun validateAndSaveInDb(item: ProductResponse) {
        val numberItem = dataBaseLimit(itemDatasource.getCountItems())
        compositeDisposable.add(
            itemDatasource.getById(item.id,
                {
                    if (it <= 0) {
                        val itemDAO = item.copy(numberItem = numberItem)
                        saveItem(itemDAO)
                    }
                },
                { }
            )
        )
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

    override fun getItemDescription(id: String) {
        compositeDisposable.add(
            itemDatasource.getItemDescription(id,
                { itemView.setDescription(it.plainText) },
                { itemView.showSnackBar(resources.getString(R.string.simple_error_message)) }
            )
        )
    }

    override fun saveItem(item: ProductResponse) {
        compositeDisposable.add(
            itemDatasource.dbInsertItem(item,
                { },
                { })
        )
    }

    private fun permaLink() = itemDatasource.getPermalinkRecentlySeenItem()
    override fun navigateToMeli() = itemView.startMELI(permaLink())
    override fun navigateToShared() = itemView.sharedItem(permaLink())
    override fun navigateToSearch() = itemView.startSearch()
    override fun back() = itemView.back()
    override fun internetFail() =
        itemView.showSnackBarRed(resources.getString(R.string.it_seems_there_is_no_internet))

    override fun refreshButton(intent: Intent) = initComponent(intent)
    override fun translateCondition(condition: String): String =
        if (condition == resources.getString(R.string.newState)) resources.getString(R.string.newStateSpanish) else resources.getString(R.string.usedState)
}


