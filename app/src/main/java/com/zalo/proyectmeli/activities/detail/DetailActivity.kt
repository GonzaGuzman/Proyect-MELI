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
import com.zalo.proyectmeli.presenter.detail.DetailPresenter
import com.zalo.proyectmeli.presenter.detail.DetailView
import com.zalo.proyectmeli.repository.detail.DetailRepository
import com.zalo.proyectmeli.utils.InternetAvailable
import com.zalo.proyectmeli.utils.appController.AppController
import com.zalo.proyectmeli.utils.models.ProductDataResponse

class DetailActivity : AppCompatActivity(), DetailView {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DetailAdapter
    private lateinit var detailPresenter: DetailPresenter
    private val apiService = APIServiceImplements
    private val dataBase = AppController.database
    private val detailRepository = DetailRepository(apiService, dataBase)
    private val detailDatasourceImplements = DetailDatasourceImplements(detailRepository)

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

    override fun onProductFetched(productList: ProductDataResponse) =
        adapter.appendProduct(productList)

    override fun navigateToSearch() = binding.toolbarSearchDetail.tvSearch.setOnClickListener {
        detailPresenter.navigateToSearch()
    }

    override fun startSearch() = startActivity(Intent(this, SearchActivity::class.java))

    override fun showSnackBar(message: String) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getColor(R.color.grey_medium))
            .show()

    override fun internetConnection(): Boolean = InternetAvailable.isConnected(this)

    override fun internetFailViewDisabled() {
        binding.layoutDetail.internetFail.visibility = View.GONE
        binding.layoutDetail.constraintRecycler.visibility = View.VISIBLE
    }

    override fun internetFailViewEnabled() {
        binding.layoutDetail.internetFail.visibility = View.VISIBLE
        binding.layoutDetail.constraintRecycler.visibility = View.GONE
        binding.layoutDetail.refreshButton.setOnClickListener {
            detailPresenter.refreshButton(intent)
        }
    }

    override fun onBack() = binding.ivBack.setOnClickListener { detailPresenter.back() }
    override fun back() = onBackPressed()
}
