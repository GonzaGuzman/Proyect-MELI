package com.zalo.proyectmeli.presenter.searchPresenter

import android.content.res.Resources
import android.util.Log
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
    private val TAG = "SearchPresenter"

    override fun initComponent() {
        searchView.loadRecycler()
        getAllSearch()
        searchView.navigateToDetail()
        searchView.backToPressed()
    }

    override fun getAllSearch() {
        compositeDisposable.add(
            searchDatasource.getAllSearch(
                {
                    searchView.loadAdapter(it)
                    getArraySearch(it)
                    searchView.loadGone()
                },
                { Log.e(TAG, it.message.toString()) }

            )
        )
    }

    override fun getArraySearch(searchHistory: List<SearchHistory>) {
        val listOfSearch: ArrayList<String> = ArrayList()
        searchHistory.forEach { listOfSearch.add(it.textSearch) }
        searchView.loadArrayAdapter(listOfSearch)
    }

    override fun insertNewSearch(search: String) {
        val position = searchDatasource.getPosition()
        val searchAux = SearchHistory(search, position)
        searchDatasource.setPosition(position + 1)
        compositeDisposable.add(
            searchDatasource.insertNewSearch(searchAux,
                { Log.i(TAG,resources.getString(R.string.inserted_successful)) },
                { Log.e(TAG, it.message.toString()) }
            )
        )
    }
}