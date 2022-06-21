package com.zalo.proyectmeli.activities.detail


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zalo.proyectmeli.adapter.detail.DetailAdapter
import com.zalo.proyectmeli.databinding.ActivityDetailBinding
import com.zalo.proyectmeli.datasource.detail.DetailDatasourceImplements
import com.zalo.proyectmeli.network.APIServiceImplements
import com.zalo.proyectmeli.network.models.ProductDataResponse
import com.zalo.proyectmeli.presenter.detail.DetailPresenter
import com.zalo.proyectmeli.presenter.detail.DetailView
import com.zalo.proyectmeli.repository.detail.DetailRepository
import com.zalo.proyectmeli.utils.KEY_SEARCH
import com.zalo.proyectmeli.utils.SEARCH_SHOW
import com.zalo.proyectmeli.utils.TYPE_SHOW
import com.zalo.proyectmeli.utils.applicationClass.MLApplication

class DetailActivity : AppCompatActivity(), DetailView {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DetailAdapter
    private val apiService = APIServiceImplements
    private val dataBase = MLApplication.database
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
        binding.layoutDetail.recycler.layoutManager = GridLayoutManager(this, GRID_COLUMNS)
        adapter = DetailAdapter(mutableListOf())
        recyclerView.adapter = adapter
    }

    override fun loadGone() {
        binding.layoutDetail.progressBar.visibility = View.GONE
    }

    override fun onProductFetched(productList: ProductDataResponse) {
        adapter.appendProduct(productList)
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun textSearch() {
        val intent = Intent(this, DetailActivity::class.java)
        with(binding) {
            toolbarSearchDetail.edtSearch.setOnClickListener {
                if (!toolbarSearchDetail.edtSearch.text.isNullOrEmpty()) {
                    intent.putExtra(KEY_SEARCH, toolbarSearchDetail.edtSearch.text.toString())
                    intent.putExtra(TYPE_SHOW, SEARCH_SHOW)
                    startActivity(intent)
                    toolbarSearchDetail.edtSearch.text?.clear()
                }
            }
        }
    }

    companion object {
        const val GRID_COLUMNS = 2
    }
}