package com.zalo.proyectmeli.activities.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zalo.proyectmeli.activities.detail.DetailActivity
import com.zalo.proyectmeli.activities.item.ItemActivity
import com.zalo.proyectmeli.adapter.categories.CategoriesAdapter
import com.zalo.proyectmeli.databinding.ActivityMainBinding
import com.zalo.proyectmeli.datasource.home.HomeDatasourceImplements
import com.zalo.proyectmeli.network.APIServiceImplements
import com.zalo.proyectmeli.network.models.Categories
import com.zalo.proyectmeli.network.models.ProductResponse
import com.zalo.proyectmeli.presenter.home.HomePresenter
import com.zalo.proyectmeli.presenter.home.HomeView
import com.zalo.proyectmeli.repository.home.HomeRepository
import com.zalo.proyectmeli.utils.*
import com.zalo.proyectmeli.utils.applicationClass.MLApplication

class HomeActivity : AppCompatActivity(), HomeView {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoriesAdapter
    private val apiService = APIServiceImplements
    private val dataBase = MLApplication.database
    private val homeRepository = HomeRepository(apiService, dataBase)
    private val homeDataSourceImplements = HomeDatasourceImplements(homeRepository)
    private lateinit var homePresenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homePresenter = HomePresenter(this, homeDataSourceImplements, resources)
        homePresenter.initRecyclerCategories()

        binding.itemRecentlySeen.bottomNavigationTo.setOnClickListener {
            homePresenter.showItemListDb()
        }

        binding.itemRecentlySeen.cardItemsRecently.setOnClickListener {
            homePresenter.showItemDb()
        }
    }

    override fun loadRecycler() {
        recyclerView = binding.layoutCategories.recycler
        binding.layoutCategories.recycler.layoutManager = LinearLayoutManager(this)
        adapter = CategoriesAdapter(mutableListOf())
        recyclerView.adapter = adapter
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCategoryFetched(categoriesList: List<Categories>) {
        adapter.appendList(categoriesList)
    }

    override fun showListDb() {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(TYPE_SHOW, DATA_BASE_SHOW)
        startActivity(intent)
    }

    override fun showItemDb(item: ProductResponse) {
        val intent = Intent(this, ItemActivity::class.java)
        intent.putExtra(ITEM_ID, item.id)
        intent.putExtra(ITEM_PRICE, item.price)
        intent.putExtra(ITEM_TITLE, item.title)
        intent.putExtra(ITEM_CONDITION, item.condition)
        intent.putExtra(ITEM_SOLD_QUANTITY, item.soldQuantity)
        intent.putExtra(ITEM_THUMBNAIL, item.thumbnail)
        intent.putExtra(ITEM_PERMALINK, item.permaLink)
        startActivity(intent)
    }

    override fun textSearch() {
        val intent = Intent(this, DetailActivity::class.java)
        with(binding) {
            toolbarSearchHome.edtSearch.setOnClickListener {
                if (!toolbarSearchHome.edtSearch.text.isNullOrEmpty()) {
                    intent.putExtra(KEY_SEARCH, toolbarSearchHome.edtSearch.text.toString())
                    intent.putExtra(TYPE_SHOW, SEARCH_SHOW)
                    startActivity(intent)
                    toolbarSearchHome.edtSearch.text?.clear()
                }
            }
        }
    }

    override fun loadRecentlySeen(item: ProductResponse) {
        binding.itemRecentlySeen.tvTitle.text = item.title
        binding.itemRecentlySeen.tvPrice.text = FormatNumber.formatNumber(item.price)
        ShowImage.showImage(item.thumbnail, binding.itemRecentlySeen.imageRecentlySeen)
    }

    override fun loadGone() {
        binding.layoutCategories.progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        homePresenter.loadRecentlySeen()
    }


}