package com.zalo.proyectmeli.presenter.home

import android.content.res.Resources
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.datasource.home.HomeDatasource
import io.reactivex.rxjava3.disposables.CompositeDisposable

class HomePresenter(
    private val homeView: HomeView,
    private val homeDatasource: HomeDatasource,
    private val resources: Resources,
) : HomePresenterActions {
    private val compositeDisposable = CompositeDisposable()

    override fun initRecyclerCategories() {
        getCategoriesList()
        homeView.loadRecycler()
        homeView.textSearch()
    }

    override fun getCategoriesList() {
        compositeDisposable.add(
            homeDatasource.getCategories(
                {
                    homeView.onCategoryFetched(it)
                    homeView.loadGone()
                },
                {
                    homeView.showSnackBar(String.format(resources.getString(R.string.error_message),
                        it.message))
                }
            )
        )
    }

    override fun showItemListDb() {
        homeView.showListDb()
    }

    override fun showItemDb() {
        val id = homeDatasource.getIdRecentlySeenItem()
        compositeDisposable.add(
            homeDatasource.getRecentlyItem(id,
                { homeView.showItemDb(it) },
                {
                    println(it.message.toString())
                }
            )
        )
    }

    override fun loadRecentlySeen() {
        val id = homeDatasource.getIdRecentlySeenItem()
        compositeDisposable.add(
            homeDatasource.getRecentlyItem(id,
                { homeView.loadRecentlySeen(it) },
                {
                    println(it.message.toString())
                }
            )
        )

    }
}