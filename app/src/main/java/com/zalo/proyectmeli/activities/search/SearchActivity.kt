package com.zalo.proyectmeli.activities.search

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zalo.proyectmeli.activities.detail.DetailActivity
import com.zalo.proyectmeli.adapter.search.SearchAdapter
import com.zalo.proyectmeli.databinding.ActivitySearchBinding
import com.zalo.proyectmeli.datasource.search.SearchDataSourceImplements
import com.zalo.proyectmeli.presenter.searchPresenter.SearchPresenter
import com.zalo.proyectmeli.presenter.searchPresenter.SearchView
import com.zalo.proyectmeli.repository.search.SearchRepository
import com.zalo.proyectmeli.utils.KEY_SEARCH
import com.zalo.proyectmeli.utils.SEARCH_SHOW
import com.zalo.proyectmeli.utils.TYPE_SHOW
import com.zalo.proyectmeli.utils.appController.AppController
import com.zalo.proyectmeli.utils.models.SearchHistory

class SearchActivity : AppCompatActivity(), SearchView {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var searchPresenter: SearchPresenter
    private val dataBase = AppController.database
    private val searchRepository = SearchRepository(dataBase)
    private val searchDataSourceImplements = SearchDataSourceImplements(searchRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchPresenter = SearchPresenter(this, searchDataSourceImplements, resources)
        searchPresenter.initComponent()
    }

    override fun loadRecycler() {
        recyclerView = binding.recyclerSearchView.recycler
        binding.recyclerSearchView.recycler.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter(mutableListOf())
        recyclerView.adapter = adapter
    }

    override fun loadAdapter(searchList: List<SearchHistory>) = adapter.appendList(searchList)

    override fun loadGone() {
        binding.recyclerSearchView.progressBar.visibility = View.GONE
    }

    override fun navigateToDetail() =
        binding.toolbarSearchView.tvSearchView.setOnClickListener {
            searchPresenter.startSearch(binding.toolbarSearchView.tvSearchView.text.toString())
        }

    override fun startDetail(searchText: String) =
        startActivity(Intent(this, DetailActivity::class.java).apply {
            putExtra(KEY_SEARCH, searchText)
            putExtra(TYPE_SHOW, SEARCH_SHOW)
        })

    override fun loadArrayAdapter(searched: ArrayList<String>) {
        val arrayAdapter = ArrayAdapter(this,
            R.layout.simple_list_item_1, searched)
        binding.toolbarSearchView.tvSearchView.setAdapter(arrayAdapter)
    }

    override fun showSnackBar(message: String) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getColor(com.zalo.proyectmeli.R.color.grey_medium))
            .show()

    override fun onBack() =
        binding.toolbarSearchView.buttonBack.setOnClickListener { searchPresenter.back() }

    override fun back() = onBackPressed()
}