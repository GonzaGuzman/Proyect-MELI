package com.zalo.proyectmeli.datasource.item

import com.zalo.proyectmeli.utils.models.DescriptionResponse
import com.zalo.proyectmeli.utils.models.ProductResponse
import com.zalo.proyectmeli.repository.item.ItemRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ItemDatasourceImplements(private val repository: ItemRepository) : ItemDatasource {
    override fun dbInsertItem(
        productResponse: ProductResponse,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.insertItemDb(productResponse)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess() },
                { onError(it) })
    }

    override fun getById(
        id: String,
        onSuccess: (response: Int) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.getById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess(it) },
                { onError(it) })
    }

    override fun setCountItems(itemNumber: Int) {
        repository.setCountItems(itemNumber)
    }

    override fun getCountItems(): Int {
        return repository.getCountItems()
    }

    override fun setIdRecentlySeenItem(id: String) {
        repository.setIdRecentlySeenItems(id)
    }

    override fun setPermalinkRecentlySeenItem(permalink: String) {
        repository.setPermalinkRecentlySeenItems(permalink)
    }

    override fun getPermalinkRecentlySeenItem(): String {
        return repository.getPermalinkRecentlySeenItems()
    }

    override fun getItemDescription(
        id: String,
        onSuccess: (response: DescriptionResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.getItemDescription(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess(it) },
                { onError(it) }
            )
    }

    override fun getItemById(
        id: String,
        onSuccess: (response: ProductResponse) -> Unit,
        onError: (Throwable) -> Unit,
    ): Disposable {
        return repository.getItemById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess(it) },
                { onError(it) }
            )
    }
}
