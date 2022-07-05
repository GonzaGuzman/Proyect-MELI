package com.zalo.proyectmeli.activities.item

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.search.SearchActivity
import com.zalo.proyectmeli.databinding.ActivityItemBinding
import com.zalo.proyectmeli.datasource.item.ItemDatasourceImplements
import com.zalo.proyectmeli.network.APIServiceImplements
import com.zalo.proyectmeli.presenter.item.ItemPresenter
import com.zalo.proyectmeli.presenter.item.ItemView
import com.zalo.proyectmeli.repository.item.ItemRepository
import com.zalo.proyectmeli.utils.InternetAvailable
import com.zalo.proyectmeli.utils.ShowImage
import com.zalo.proyectmeli.utils.appController.AppController

class ItemActivity : AppCompatActivity(), ItemView {
    private lateinit var binding: ActivityItemBinding
    private lateinit var itemPresenter: ItemPresenter
    private val dataBase = AppController.database
    private val apiService = APIServiceImplements
    private val itemRepository = ItemRepository(dataBase, apiService)
    private val itemDatasourceImplements = ItemDatasourceImplements(itemRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemPresenter = ItemPresenter(this, itemDatasourceImplements, resources)
        itemPresenter.initComponent(intent)
    }

    override fun retrieveExtras(
        title: String,
        state: String,
        price: String,
        thumbnail: String,
        stock: String,
    ) {
        binding.txtNameDetails.text = title
        binding.tvState.text = state
        binding.txtPriceDetails.text = price
        ShowImage.showImageW(thumbnail, binding.ivProfileDetails)
        binding.tvStock.text = stock
    }

    override fun setDescription(description: String) {
        binding.tvDescriptions.text = description
    }

    override fun navigateToSearch() = binding.toolbarSearchItem.tvSearch.setOnClickListener {
        itemPresenter.navigateToSearch()
    }

    override fun navigateToMeli() = binding.btnGoTo.setOnClickListener {
        itemPresenter.navigateToMeli()
    }

    override fun navigateToShared() = binding.shareIcon.setOnClickListener {
        itemPresenter.navigateToShared()
    }

    override fun startSearch() =
        startActivity(Intent(this@ItemActivity, SearchActivity::class.java))

    override fun startMELI(permalink: String) =
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(permalink)))

    override fun sharedItem(permalink: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.productURL))
            putExtra(Intent.EXTRA_TEXT, permalink)
        }
        startActivity(Intent.createChooser(intent, resources.getString(R.string.productShare)))
    }

    override fun internetConnection(): Boolean = InternetAvailable.isConnected(this)

    override fun showSnackBar(message: String) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getColor(R.color.grey_medium))
            .show()

    override fun showSnackBarRed(message: String) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) { itemPresenter.refreshButton(intent) }
            .setBackgroundTint(getColor(R.color.red))
            .setActionTextColor(getColor(R.color.white))
            .show()

    override fun onBack() = binding.ivBack.setOnClickListener { itemPresenter.back() }
    override fun back() = onBackPressed()

}