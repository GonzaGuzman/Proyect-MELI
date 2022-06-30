package com.zalo.proyectmeli.activities.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.detail.DetailActivity
import com.zalo.proyectmeli.activities.item.ItemActivity
import com.zalo.proyectmeli.activities.search.SearchActivity
import com.zalo.proyectmeli.adapter.categories.CategoriesAdapter
import com.zalo.proyectmeli.databinding.ActivityMainBinding
import com.zalo.proyectmeli.datasource.home.HomeDatasourceImplements
import com.zalo.proyectmeli.network.APIServiceImplements
import com.zalo.proyectmeli.presenter.home.HomePresenter
import com.zalo.proyectmeli.presenter.home.HomeView
import com.zalo.proyectmeli.repository.home.HomeRepository
import com.zalo.proyectmeli.utils.*
import com.zalo.proyectmeli.utils.appController.AppController
import com.zalo.proyectmeli.utils.models.Categories
import com.zalo.proyectmeli.utils.models.ProductResponse

class HomeActivity : AppCompatActivity(), HomeView,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private val dialog: AlertDialog? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoriesAdapter
    private val apiService = APIServiceImplements
    private val dataBase = AppController.database
    private val homeRepository = HomeRepository(apiService, dataBase)
    private val homeDataSourceImplements = HomeDatasourceImplements(homeRepository)
    private lateinit var homePresenter: HomePresenter
    private var databaseIsEmpty = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homePresenter = HomePresenter(this, homeDataSourceImplements, resources)
        homePresenter.initComponent()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun loadRecycler() {
        recyclerView = binding.layoutCategories.recycler
        binding.layoutCategories.recycler.layoutManager = LinearLayoutManager(this)
        adapter = CategoriesAdapter(mutableListOf())
        recyclerView.adapter = adapter
    }

    override fun onCategoryFetched(categoriesList: List<Categories>) {
        adapter.appendList(categoriesList)
    }

    override fun emptyRecently(validate: Boolean) {
        binding.itemRecentlySeen.tvEmptyHistorial.isVisible = validate
        binding.itemRecentlySeen.viewRecently.isVisible = !validate
        databaseIsEmpty = validate
    }

    override fun loadRecentlySeen(item: ProductResponse) {
        binding.itemRecentlySeen.cardItemsRecently.tvNameProduct.text = item.title
        binding.itemRecentlySeen.cardItemsRecently.tvPrice.text =
            FormatNumber.formatNumber(item.price)
        ShowImage.showImageO(item.thumbnail,
            binding.itemRecentlySeen.cardItemsRecently.ivProduct)
    }

    override fun loadGone() {
        binding.layoutCategories.progressBar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        homePresenter.loadRecentlySeen()
    }

    override fun showHistoryDb() {
        binding.itemRecentlySeen.bottomNavigationTo.setOnClickListener {
            navigateToHistoryDb()
        }
    }

    override fun navigateToHistoryDb() {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(TYPE_SHOW, DATA_BASE_SHOW)
        startActivity(intent)
    }

    override fun showItemDb(item: ProductResponse) {
        val intent = Intent(this, ItemActivity::class.java)
        intent.putExtra(ITEM_ID, item.id)
        startActivity(intent)
    }

    override fun navigateToShowItem() {
        binding.itemRecentlySeen.cardItemsRecently.cardItems.setOnClickListener {
            homePresenter.showItemDb()
        }
    }

    override fun navigateToSearch() {
        binding.toolbarSearchHome.tvSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itHistorial -> homePresenter.showHistorial()
            R.id.itClearHistorialSee -> if (!databaseIsEmpty) homePresenter.deleteDialog() else showSnackBar(
                getString(R.string.empty_database_message))
        }
        return true
    }

    override fun showAlertCloseSession() {
        MaterialAlertDialogBuilder(this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3_Body_Text_CenterStacked)
            .setTitle(getString(R.string.clear_history_questions))
            .setMessage(getString(R.string.clear_history_warning))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ ->
                homePresenter.onNegativeButtonClicked()
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                homePresenter.onPositiveButtonClicked()
                this.startActivity(Intent(this, HomeActivity::class.java))
            }.show()
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getColor(R.color.grey_medium))
            .show()
    }

    override fun dialogDismiss() {
        dialog?.dismiss()
    }

    override fun openMenu() {
        binding.ivMenuUp.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}