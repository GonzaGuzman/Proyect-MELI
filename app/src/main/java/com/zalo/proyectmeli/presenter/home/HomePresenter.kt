package com.zalo.proyectmeli.presenter.home

import android.content.res.Resources
import android.util.Log
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.datasource.home.HomeDatasource
import io.reactivex.rxjava3.disposables.CompositeDisposable

class HomePresenter(
    private val homeView: HomeView,
    private val homeDatasource: HomeDatasource,
    private val resources: Resources,
) : HomePresenterActions {
    private val compositeDisposable = CompositeDisposable()
    private val TAG = "HomePresenter"

    override fun initComponent() {
        getCategoriesList()
        homeView.loadRecycler()
        homeView.navigateToSearch()
        homeView.showHistoryDb()
        homeView.navigateToShowItem()
        homeView.openMenu()
    }

    override fun getCategoriesList() {
        compositeDisposable.add(
            homeDatasource.getCategories(
                {
                    homeView.onCategoryFetched(it)
                    homeView.loadGone()
                },
                {
                    homeView.showSnackBar(resources.getString(R.string.error_message))
                    Log.e(TAG, it.message.toString())
                }
            )
        )
    }

    override fun showItemListDb() {
        homeView.showHistoryDb()
    }

    override fun showHistorial() {
        homeView.navigateToHistoryDb()
    }

    override fun showItemDb() {
        val id = homeDatasource.getIdRecentlySeenItem()
        compositeDisposable.add(
            homeDatasource.getRecentlyItem(id,
                { homeView.showItemDb(it) },
                {
                    Log.e(TAG, it.message.toString())
                }
            )
        )
    }

    override fun loadRecentlySeen() {
        val id = homeDatasource.getIdRecentlySeenItem()
        homeView.emptyRecently(id.isNullOrEmpty())
        compositeDisposable.add(
            homeDatasource.getRecentlyItem(id,
                { homeView.loadRecentlySeen(it) },
                {
                    Log.e(TAG, it.message.toString())
                }
            )
        )
    }

    override fun deleteHistory() {
        compositeDisposable.add(
            homeDatasource.deleteHistory(
                { Log.i(TAG, resources.getString(R.string.delete_history_complete))},
                { Log.e(TAG, it.message.toString()) }
            )
        )
    }

    override fun deleteSearch() {
        compositeDisposable.add(
            homeDatasource.deleteSearch(
                { Log.i(TAG, resources.getString(R.string.delete_search_complete)) },
                { Log.e(TAG, it.message.toString()) }
            )
        )
    }

    override fun navigateToSearch() {
        homeView.navigateToSearch()
    }

    override fun onNegativeButtonClicked() {
        homeView.dialogDismiss()
    }

    override fun onPositiveButtonClicked() {
        deleteHistory()
        deleteSearch()
        clearSharedValues()
    }

    override fun clearSharedValues() {
        homeDatasource.setCountItem(0)
        homeDatasource.setSearchPosition(0)
        homeDatasource.setIdRecentlySeenItem("")
        homeDatasource.setLinkRecentlySeen("")
    }

    override fun deleteDialog() {
        homeView.showAlertCloseSession()
    }
}