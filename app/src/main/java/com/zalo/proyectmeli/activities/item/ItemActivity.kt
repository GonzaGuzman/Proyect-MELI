package com.zalo.proyectmeli.activities.item

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zalo.proyectmeli.R
import com.zalo.proyectmeli.activities.detail.DetailActivity
import com.zalo.proyectmeli.databinding.ActivityItemBinding
import com.zalo.proyectmeli.datasource.item.ItemDatasourceImplements
import com.zalo.proyectmeli.network.models.ProductResponse
import com.zalo.proyectmeli.presenter.item.ItemPresenter
import com.zalo.proyectmeli.presenter.item.ItemState
import com.zalo.proyectmeli.presenter.item.ItemView
import com.zalo.proyectmeli.repository.item.ItemRepository
import com.zalo.proyectmeli.utils.*
import com.zalo.proyectmeli.utils.applicationClass.MLApplication

class ItemActivity : AppCompatActivity(), ItemView {
    private lateinit var binding: ActivityItemBinding
    private val dataBase = MLApplication.database
    private val itemRepository = ItemRepository(dataBase)
    private val itemDatasourceImplements = ItemDatasourceImplements(itemRepository)
    private val itemState = ItemState()
    private lateinit var itemPresenter: ItemPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemPresenter = ItemPresenter(this, itemDatasourceImplements, itemState, resources)
        itemPresenter.initComponent(intent)

        binding.btnGoTo.setOnClickListener{
            itemPresenter.navigateToMELI()
        }
    }

    override fun retrieverExtras(item: ProductResponse) {
        binding.txtNameDetails.text = item.title
        binding.tvState.text = getString(R.string.state_sold,item.condition,item.soldQuantity)
        binding.txtPriceDetails.text = FormatNumber.formatNumber(item.price)
        ShowImage.showImage(item.thumbnail, binding.imgProfileDetails)
    }

    override fun textSearch() {
        val intent = Intent(this, DetailActivity::class.java)
        with(binding) {
            toolbarSearchItem.edtSearch.setOnClickListener {
                if (!toolbarSearchItem.edtSearch.text.isNullOrEmpty()) {
                    intent.putExtra(KEY_SEARCH, toolbarSearchItem.edtSearch.text.toString())
                    intent.putExtra(TYPE_SHOW, SEARCH_SHOW)
                    startActivity(intent)
                    toolbarSearchItem.edtSearch.text?.clear()
                }
            }
        }
    }

    override fun navigateToMELI(permalink: String){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(permalink)))
    }

}