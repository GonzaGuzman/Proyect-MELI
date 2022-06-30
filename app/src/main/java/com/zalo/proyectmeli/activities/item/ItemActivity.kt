package com.zalo.proyectmeli.activities.item


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.search.SearchActivity
import com.zalo.proyectmeli.databinding.ActivityItemBinding
import com.zalo.proyectmeli.datasource.item.ItemDatasourceImplements
import com.zalo.proyectmeli.network.APIServiceImplements
import com.zalo.proyectmeli.presenter.item.ItemPresenter
import com.zalo.proyectmeli.presenter.item.ItemView
import com.zalo.proyectmeli.repository.item.ItemRepository
import com.zalo.proyectmeli.utils.FormatNumber
import com.zalo.proyectmeli.utils.ShowImage
import com.zalo.proyectmeli.utils.appController.AppController
import com.zalo.proyectmeli.utils.models.ProductResponse

class ItemActivity : AppCompatActivity(), ItemView {
    private lateinit var binding: ActivityItemBinding
    private val dataBase = AppController.database
    private val apiService = APIServiceImplements
    private val itemRepository = ItemRepository(dataBase, apiService)
    private val itemDatasourceImplements = ItemDatasourceImplements(itemRepository)
    private lateinit var itemPresenter: ItemPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemPresenter = ItemPresenter(this, itemDatasourceImplements, resources)
        itemPresenter.initComponent(intent)
        binding.btnGoTo.setOnClickListener {
            itemPresenter.navigateToMeli()
        }
        binding.shareIcon.setOnClickListener {
            itemPresenter.navigateToShared()
        }
    }

    override fun retrieverExtras(item: ProductResponse) {
        binding.txtNameDetails.text = item.title
        val condition = itemPresenter.translateCondition(item.condition)
        binding.tvState.text = getString(R.string.state_sold, condition, item.soldQuantity)
        binding.txtPriceDetails.text = FormatNumber.formatNumber(item.price)
        ShowImage.showImageW(item.thumbnail, binding.ivProfileDetails)
        binding.tvStock.text = getString(R.string.stock, item.stock)
        itemPresenter.getItemDescription(item.id, binding.tvDescriptions)
    }

    override fun sharedItem(permalink: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.productURL))
            putExtra(Intent.EXTRA_TEXT, permalink)
        }
        startActivity(Intent.createChooser(intent, resources.getString(R.string.productShare)))
    }

    override fun navigateToMELI(permalink: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(permalink)))
    }

    override fun navigateToSearch() {
        binding.toolbarSearchItem.tvSearch.setOnClickListener {
            startActivity(Intent(this@ItemActivity, SearchActivity::class.java))
        }
    }

    override fun onBack() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}