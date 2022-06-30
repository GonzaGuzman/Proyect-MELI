package com.zalo.proyectmeli.presenter.item

import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.widget.TextView
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
    private val TAG = "ItemPresenter"
    override fun initComponent(intent: Intent) {
        val id = intent.getStringExtra(ITEM_ID) ?: ""
        getItemById(id)
        itemView.navigateToSearch()
        itemView.onBack()
    }

    override fun getItemById(id: String) {
        compositeDisposable.add(
            itemDatasource.getItemById(id,
                {
                    itemView.retrieverExtras(it)
                    itemDatasource.setIdRecentlySeenItem(it.id)
                    itemDatasource.setPermalinkRecentlySeenItem(it.permaLink)
                    validateAndSaveInDb(it)
                },
                {
                    Log.e(TAG, it.message.toString())
                }

            )
        )
    }

    override fun validateAndSaveInDb(item: ProductResponse) {
        val numberItem = dataBaseLimit(itemDatasource.getCountItems())
        compositeDisposable.add(
            itemDatasource.getById(item.id,
                {
                    if (it <= 0) {
                        val itemDAO = item.copy(numberItem = numberItem)
                        saveItem(itemDAO)
                    } else {
                        println(resources.getString(R.string.existing_id_in_database))
                    }
                },
                {
                    Log.e(TAG, it.message.toString())
                }
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

    override fun getItemDescription(id: String, view: TextView) {
        compositeDisposable.add(
            itemDatasource.getItemDescription(id,
                { view.text = it.plainText },
                { Log.e(TAG, it.message.toString()) }
            )
        )
    }

    override fun saveItem(item: ProductResponse) {
        compositeDisposable.add(
            itemDatasource.dbInsertItem(item,
                { Log.i(TAG, resources.getString(R.string.saveSuccesfullyItem)) },
                { Log.e(TAG, it.message.toString()) })
        )
    }

    override fun navigateToMeli() {
        val permaLink = itemDatasource.getPermalinkRecentlySeenItem()
        itemView.navigateToMELI(permaLink)
    }

    override fun navigateToShared() {
        val permaLink = itemDatasource.getPermalinkRecentlySeenItem()
        itemView.sharedItem(permaLink)
    }

    override fun translateCondition(condition: String): String {
        return if (condition == resources.getString(
                R.string.newState)
        ) {
            resources.getString(R.string.newStateSpanish)
        } else
            resources.getString(R.string.usedState)
    }
}
