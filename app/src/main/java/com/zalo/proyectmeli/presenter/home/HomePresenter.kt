package com.zalo.proyectmeli.presenter.home

import android.content.res.Resources
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.datasource.home.HomeDatasource
import com.zalo.proyectmeli.utils.FormatNumber
import io.reactivex.rxjava3.disposables.CompositeDisposable

class HomePresenter(
    private val homeView: HomeView,
    private val homeDatasource: HomeDatasource,
    private val resources: Resources,
) : HomePresenterActions {
    private val compositeDisposable = CompositeDisposable()

    override fun initComponent() {
        getCategoriesList()
        homeView.loadRecycler()
        homeView.navigateToSearch()
        homeView.navigateToHistoryDb()
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
                    if (!homeView.internetConnection()) internetFail() else homeView.showSnackBar(
                        resources.getString(R.string.simple_error_message))
                }
            )
        )
    }

    override fun showItemDb() {
        val id = homeDatasource.getIdRecentlySeenItem()
        compositeDisposable.add(
            homeDatasource.getRecentlyItem(id,
                { homeView.showItemDb(it) },
                {
                    homeView.showSnackBar(resources.getString(R.string.simple_error_message))
                }
            )
        )
    }

    override fun loadRecentlySeen() {
        val id = homeDatasource.getIdRecentlySeenItem()
        activateViews(id.isEmpty())
        if (id.isNotEmpty())
            compositeDisposable.add(
                homeDatasource.getRecentlyItem(id,
                    {
                        homeView.loadRecentlySeen(it.title,
                            FormatNumber.formatNumber(it.price),
                            it.thumbnail)
                    },
                    {
                        homeView.showSnackBar(resources.getString(R.string.simple_error_message))
                    }
                )
            )
    }

    override fun activateViews(validate: Boolean) {
        homeView.onOffEmptyRecently(validate)
        homeView.onOffRecyclerView(!validate)
    }


    override fun onPositiveButtonClicked() {
        compositeDisposable.add(
            homeDatasource.clearHistory(
                {
                    clearSharedValues()
                    homeView.refresh()
                },
                { homeView.showSnackBar(resources.getString(R.string.simple_error_message)) }
            )
        )
    }

    override fun showItemListDb() = homeView.navigateToHistoryDb()
    override fun showHistorial() = homeView.showHistory()
    override fun openMenu() = homeView.open()
    override fun navigateToSearch() = homeView.startSearch()
    override fun onNegativeButtonClicked() = homeView.dialogDismiss()
    override fun clearSharedValues() {
        homeDatasource.setCountItem(0)
        homeDatasource.setSearchPosition(0)
        homeDatasource.setIdRecentlySeenItem("")
        homeDatasource.setLinkRecentlySeen("")
    }

    override fun deleteDialog() = homeView.showAlertCloseSession()
    override fun internetFail() = homeView.internetFailViewEnabled()
    override fun refreshButton() {
        homeView.internetFailViewDisabled()
        initComponent()
    }
}