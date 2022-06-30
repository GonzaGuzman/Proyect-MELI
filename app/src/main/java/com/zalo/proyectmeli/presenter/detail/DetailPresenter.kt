package com.zalo.proyectmeli.presenter.detail

import android.content.Intent
import android.content.res.Resources
import android.util.Log
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.datasource.detail.DetailDatasource
import com.zalo.proyectmeli.utils.models.ProductDataResponse
import com.zalo.proyectmeli.utils.CAT_ID
import com.zalo.proyectmeli.utils.KEY_SEARCH
import com.zalo.proyectmeli.utils.TYPE_SHOW
import io.reactivex.rxjava3.disposables.CompositeDisposable
class DetailPresenter(
    private val detailView: DetailView,
    private val detailDatasource: DetailDatasource,
    private val resources: Resources,
) : DetailPresenterActions {
    private val compositeDisposable = CompositeDisposable()
    private val TAG = "DetailPresenter"

    override fun initComponent(intent: Intent) {
        loadFetched(intent)
        detailView.loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    override fun loadFetched(intent: Intent) {
        when (intent.getIntExtra(TYPE_SHOW, 0)) {
            1 -> {
                val categoryID = intent.getStringExtra(CAT_ID).toString()
                getProductList(categoryID)
            }
            2 -> {
                val keySearch = intent.getStringExtra(KEY_SEARCH).toString()
                searchItems(keySearch)
            }
            else -> {
                getProductListOfDb()
            }
        }
    }

    override fun getProductList(search: String) {
        compositeDisposable.add(
            detailDatasource.getCategoriesDetail(search,
                {
                    detailView.onProductFetched(it)
                    detailView.loadGone()
                },
                {
                    detailView.showSnackBar(resources.getString(R.string.error_message))
                    Log.e(TAG,it.message.toString())
                }
            )
        )
    }

    override fun searchItems(itemSearch: String) {
        compositeDisposable.add(
            detailDatasource.getItemsList(itemSearch,
                {
                    detailView.onProductFetched(it)
                    detailView.loadGone()
                },
                {
                    detailView.showSnackBar(resources.getString(R.string.error_message))
                    Log.e(TAG,it.message.toString())
                }
            )
        )
    }

    override fun getProductListOfDb() {
        compositeDisposable.add(
            detailDatasource.getItemsDb(
                {
                    detailView.onProductFetched(ProductDataResponse(it))
                    detailView.loadGone()
                },
                {
                    detailView.showSnackBar(resources.getString(R.string.error_message))
                    Log.e(TAG,it.message.toString())
                }
            )
        )
    }

    override fun navigateToSearch() {
        detailView.navigateToSearch()
    }
}

