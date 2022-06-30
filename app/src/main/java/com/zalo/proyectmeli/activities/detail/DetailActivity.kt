package com.zalo.proyectmeli.activities.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.search.SearchActivity
import com.zalo.proyectmeli.adapter.detail.DetailAdapter
import com.zalo.proyectmeli.databinding.ActivityDetailBinding
import com.zalo.proyectmeli.datasource.detail.DetailDatasourceImplements
import com.zalo.proyectmeli.network.APIServiceImplements
import com.zalo.proyectmeli.utils.models.ProductDataResponse
import com.zalo.proyectmeli.presenter.detail.DetailPresenter
import com.zalo.proyectmeli.presenter.detail.DetailView
import com.zalo.proyectmeli.repository.detail.DetailRepository
import com.zalo.proyectmeli.utils.appController.AppController

class DetailActivity : AppCompatActivity(), DetailView {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DetailAdapter
    private val apiService = APIServiceImplements
    private val dataBase = AppController.database
    private val detailRepository = DetailRepository(apiService, dataBase)
    private val detailDatasourceImplements = DetailDatasourceImplements(detailRepository)
    private lateinit var detailPresenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        detailPresenter = DetailPresenter(this, detailDatasourceImplements, resources)
        detailPresenter.initComponent(intent)
    }

    override fun loadRecycler() {
        recyclerView = binding.layoutDetail.recycler
        binding.layoutDetail.recycler.layoutManager = LinearLayoutManager(this)
        adapter = DetailAdapter(mutableListOf())
        recyclerView.adapter = adapter
    }

    override fun loadGone() {
        binding.layoutDetail.progressBar.visibility = View.GONE
    }

    override fun onProductFetched(productList: ProductDataResponse) {
        adapter.appendProduct(productList)
    }

    override fun navigateToSearch() {
        binding.toolbarSearchDetail.tvSearch.setOnClickListener {
            startActivity(Intent(this@DetailActivity, SearchActivity::class.java))
        }
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getColor(R.color.grey_medium))
            .show()
    }
    override fun onBack() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}
