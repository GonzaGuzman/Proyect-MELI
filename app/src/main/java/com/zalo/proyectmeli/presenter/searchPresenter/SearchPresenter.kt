package com.zalo.proyectmeli.presenter.searchPresenter

import android.content.res.Resources
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.datasource.search.SearchDatasource
import com.zalo.proyectmeli.utils.models.SearchHistory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.ArrayList

class SearchPresenter(
    private val searchView: SearchView,
    private val searchDatasource: SearchDatasource,
    private val resources: Resources,
) : SearchPresenterActions {
    private val compositeDisposable = CompositeDisposable()

    override fun initComponent() {
        searchView.loadRecycler()
        getAllSearch()
        searchView.navigateToDetail()
        searchView.onBack()
    }

    override fun getAllSearch() {
        compositeDisposable.add(
            searchDatasource.getAllSearch(
                {
                    searchView.loadAdapter(it)
                    getArraySearch(it)
                    searchView.loadGone()
                },
                { searchView.showSnackBar(resources.getString(R.string.simple_error_message)) }

            )
        )
    }

    override fun getArraySearch(searchHistory: List<SearchHistory>) {
        val listOfSearch: ArrayList<String> = ArrayList()
        searchHistory.forEach { listOfSearch.add(it.textSearch) }
        searchView.loadArrayAdapter(listOfSearch)
    }

    override fun startSearch(search: String) {
        insertNewSearch(search)
        searchView.startDetail(search)
    }

    override fun insertNewSearch(search: String) {
        val position = searchDatasource.getPosition()
        val searchAux = SearchHistory(search, position)
        searchDatasource.setPosition(position + 1)
        compositeDisposable.add(
            searchDatasource.insertNewSearch(searchAux,
                { },
                { searchView.showSnackBar(resources.getString(R.string.simple_error_message)) }
            )
        )
    }

    override fun back() = searchView.back()
}