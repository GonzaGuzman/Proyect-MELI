package com.zalo.proyectmeli

import android.content.Intent
import android.content.res.Resources
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.zalo.proyectmeli.datasource.detail.DetailDatasource
import com.zalo.proyectmeli.utils.models.ProductDataResponse
import com.zalo.proyectmeli.utils.models.ProductResponse
import com.zalo.proyectmeli.presenter.detail.DetailPresenter
import com.zalo.proyectmeli.presenter.detail.DetailView
import com.zalo.proyectmeli.utils.CAT_ID
import com.zalo.proyectmeli.utils.KEY_SEARCH
import com.zalo.proyectmeli.utils.TYPE_SHOW
import io.reactivex.rxjava3.disposables.Disposable
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailPresenterTest {
    @Mock
    private lateinit var detailDataSource: DetailDatasource

    @Mock
    private lateinit var detailView: DetailView

    @Mock
    private lateinit var mockDisposable: Disposable

    @Mock
    private lateinit var resources: Resources

    private lateinit var detailPresenter: DetailPresenter

    @Before
    fun setup() {
        detailPresenter = DetailPresenter(detailView, detailDataSource, resources)
    }

    @Test
    fun `initComponent call when loadFetched and getCategoryDetail success`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(1)
        getCategoryDetailSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(PRODUCT_DATA_RESPONSE))
        verify(detailView).loadGone()
        verify(detailView).loadRecycler()
        verify(detailView).navigateToSearch()
        verify(detailView).onBack()
    }

    @Test
    fun `initComponent call when loadFetched and getItemList success`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(2)
        getItemListSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(PRODUCT_DATA_RESPONSE))
        verify(detailView).loadGone()
        verify(detailView).loadRecycler()
        verify(detailView).navigateToSearch()
        verify(detailView).onBack()
    }

    @Test
    fun `initComponent call when loadFetched and getItemDb success`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        getItemDbSuccessfully()
        val listProducts = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        assertEquals(detailView.onProductFetched(listProducts),
            detailView.onProductFetched(PRODUCT_DATA_RESPONSE))
        verify(detailView).loadGone()
        verify(detailView).loadRecycler()
        verify(detailView).navigateToSearch()
        verify(detailView).onBack()
    }

    @Test
    fun `initComponent call when loadFetched and getCategoryDetail fail with out internet failure`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(1)
        getCategoryDetailUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        verify(detailView).showSnackBar(SIMPLE_FAILED)
        verify(detailView).loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    @Test
    fun `initComponent call when loadFetched and getCategoryDetail fail with internet failure`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(1)
        getCategoryDetailUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(false)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        verify(detailView).internetFailViewEnabled()
        verify(detailView).loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    @Test
    fun `initComponent call when loadFetched and getItemList fail with out internet failure`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(2)
        getItemListUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        verify(detailView).showSnackBar(SIMPLE_FAILED)
        verify(detailView).loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    @Test
    fun `initComponent call when loadFetched and getItemList fail with internet failure`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(2)
        getItemListUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(false)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        verify(detailView).internetFailViewEnabled()
        verify(detailView).loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    @Test
    fun `initComponent call when loadFetched and getItemDb fail`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        getItmDbUnsuccessfully()
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        verify(detailView).showSnackBar(SIMPLE_FAILED)
        verify(detailView).loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 1 and getProductList success`() {
        //GIVEN
        getCategoryDetailSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.getProductList(CAT_ID)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(PRODUCT_DATA_RESPONSE))
        verify(detailView).loadGone()

    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 2 and getItemList success`() {
        //GIVEN
        getItemListSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.searchItems(KEY_SEARCH)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(PRODUCT_DATA_RESPONSE))
        verify(detailView).loadGone()
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 3 and getItemDb success`() {
        //GIVEN
        getItemDbSuccessfully()
        val listProducts = Mockito.mock(listOf<ProductResponse>()::class.java)
        //WHEN
        detailPresenter.getProductListOfDb()
        //THEM
        assertEquals(detailView.onProductFetched(ProductDataResponse(listProducts)),
            detailView.onProductFetched(ProductDataResponse(LIST_PRODUCT_RESPONSE)))
        verify(detailView).loadGone()
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 1 and getProductList fail with out internet failure`() {
        //GIVEN
        getCategoryDetailUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.getProductList(CAT_ID)
        //THEM
        verify(detailView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 1 and getProductList fail with internet failure`() {
        //GIVEN
        getCategoryDetailUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(false)
        //WHEN
        detailPresenter.getProductList(CAT_ID)
        //THEM
        verify(detailView).internetFailViewEnabled()
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 2 and getItemList fail with out internet failure`() {
        //GIVEN
        getItemListUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.searchItems(KEY_SEARCH)
        //THEM
        verify(detailView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 2 and getItemList fail with internet failure`() {
        //GIVEN
        getItemListUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(false)
        //WHEN
        detailPresenter.searchItems(KEY_SEARCH)
        //THEM
        verify(detailView).internetFailViewEnabled()
    }


    @Test
    fun `loadFetched when TYPE_SHOW equals 3 and getItemDb fail`() {
        //GIVEN
        getItmDbUnsuccessfully()
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.getProductListOfDb()
        //THEM
        verify(detailView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `getProductList success`() {
        //GIVEN
        getCategoryDetailSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.getProductList(CAT_ID)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(PRODUCT_DATA_RESPONSE))
        verify(detailView).loadGone()
    }

    @Test
    fun `getItemList success`() {
        //GIVEN
        getItemListSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.searchItems(KEY_SEARCH)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(PRODUCT_DATA_RESPONSE))
        verify(detailView).loadGone()
    }

    @Test
    fun `getItemDb success`() {
        //GIVEN
        getItemDbSuccessfully()
        val listProducts = Mockito.mock(listOf<ProductResponse>()::class.java)
        //WHEN
        detailPresenter.getProductListOfDb()
        //THEM
        assertEquals(detailView.onProductFetched(ProductDataResponse(listProducts)),
            detailView.onProductFetched(ProductDataResponse(LIST_PRODUCT_RESPONSE)))
        verify(detailView).loadGone()
    }

    @Test
    fun `getProductList fail with out internet failure`() {
        //GIVEN
        getCategoryDetailUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.getProductList(CAT_ID)
        //THEM
        verify(detailView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `getProductList fail with internet failure`() {
        //GIVEN
        getCategoryDetailUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(false)
        //WHEN
        detailPresenter.getProductList(CAT_ID)
        //THEM
        verify(detailView).internetFailViewEnabled()
    }


    @Test
    fun `getItemList fail with out internet failure`() {
        //GIVEN
        getItemListUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.searchItems(KEY_SEARCH)
        //THEM
        verify(detailView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `getItemList fail with internet failure`() {
        //GIVEN
        getItemListUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(false)
        //WHEN
        detailPresenter.searchItems(KEY_SEARCH)
        //THEM
        verify(detailView).internetFailViewEnabled()
    }


    @Test
    fun `getItemDb fail`() {
        //GIVEN
        getItmDbUnsuccessfully()
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.getProductListOfDb()
        //THEM
        verify(detailView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `navigateToSearch pressed`() {
        //GIVEN
        //WHEN
        detailPresenter.navigateToSearch()
        //THEN
        verify(detailView).startSearch()
    }

    @Test
    fun `back pressed`() {
        //GIVEN
        //WHEN
        detailPresenter.back()
        //THEN
        verify(detailView).back()
    }

    @Test
    fun `internetFail is call`() {
        //GIVEN
        //WHEN
        detailPresenter.internetFail()
        //THEN
        verify(detailView).internetFailViewEnabled()
    }

    @Test
    fun `refresh button is pressed and initComponent call when loadFetched and getCategoryDetail success`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(1)
        getCategoryDetailSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.refreshButton(intent)
        //THEN
        verify(detailView).internetFailViewDisabled()
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(PRODUCT_DATA_RESPONSE))
        verify(detailView).loadGone()
        verify(detailView).loadRecycler()
        verify(detailView).navigateToSearch()
        verify(detailView).onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent call when loadFetched and getItemList success`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(2)
        getItemListSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.refreshButton(intent)
        //THEM
        verify(detailView).internetFailViewDisabled()
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(PRODUCT_DATA_RESPONSE))
        verify(detailView).loadGone()
        verify(detailView).loadRecycler()
        verify(detailView).navigateToSearch()
        verify(detailView).onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent call when loadFetched and getItemDb success`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        getItemDbSuccessfully()
        val listProducts = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.refreshButton(intent)
        //THEM
        verify(detailView).internetFailViewDisabled()
        assertEquals(detailView.onProductFetched(listProducts),
            detailView.onProductFetched(PRODUCT_DATA_RESPONSE))
        verify(detailView).loadGone()
        verify(detailView).loadRecycler()
        verify(detailView).navigateToSearch()
        verify(detailView).onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent call when loadFetched and getCategoryDetail fail with out internet failure`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(1)
        getCategoryDetailUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.refreshButton(intent)
        //THEM
        verify(detailView).internetFailViewDisabled()
        verify(detailView).showSnackBar(SIMPLE_FAILED)
        verify(detailView).loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent call when loadFetched and getCategoryDetail fail with internet failure`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(1)
        getCategoryDetailUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(false)
        //WHEN
        detailPresenter.refreshButton(intent)
        //THEM
        verify(detailView).internetFailViewDisabled()
        verify(detailView).internetFailViewEnabled()
        verify(detailView).loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent call when loadFetched and getItemList fail with out internet failure`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(2)
        getItemListUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.refreshButton(intent)
        //THEM
        verify(detailView).internetFailViewDisabled()
        verify(detailView).showSnackBar(SIMPLE_FAILED)
        verify(detailView).loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent call when loadFetched and getItemList fail with internet failure`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(2)
        getItemListUnsuccessfully()
        whenever(detailView.internetConnection()).thenReturn(false)
        //WHEN
        detailPresenter.refreshButton(intent)
        //THEM
        verify(detailView).internetFailViewDisabled()
        verify(detailView).internetFailViewEnabled()
        verify(detailView).loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent call when loadFetched and getItemDb fail`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        getItmDbUnsuccessfully()
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        detailPresenter.refreshButton(intent)
        //THEM
        verify(detailView).internetFailViewDisabled()
        verify(detailView).showSnackBar(SIMPLE_FAILED)
        verify(detailView).loadRecycler()
        detailView.navigateToSearch()
        detailView.onBack()
    }

    private fun getCategoryDetailSuccessfully() {
        val success = argumentCaptor<(ProductDataResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        whenever(detailDataSource.getCategoriesDetail(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke(PRODUCT_DATA_RESPONSE)
            mockDisposable
        }
    }

    private fun getCategoryDetailUnsuccessfully() {
        val success = argumentCaptor<(ProductDataResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(detailDataSource.getCategoriesDetail(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            error.firstValue.invoke(responseThrowable)
            mockDisposable
        }
    }

    private fun getItemListSuccessfully() {
        val success = argumentCaptor<(ProductDataResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        whenever(detailDataSource.getItemsList(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke(PRODUCT_DATA_RESPONSE)
            mockDisposable
        }
    }

    private fun getItemListUnsuccessfully() {
        val success = argumentCaptor<(ProductDataResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(detailDataSource.getItemsList(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            error.firstValue.invoke(responseThrowable)
            mockDisposable
        }
    }

    private fun getItemDbSuccessfully() {
        val success = argumentCaptor<(List<ProductResponse>) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        whenever(detailDataSource.getItemsDb(
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke(LIST_PRODUCT_RESPONSE)
            mockDisposable
        }
    }

    private fun getItmDbUnsuccessfully() {
        val success = argumentCaptor<(List<ProductResponse>) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(detailDataSource.getItemsDb(
            success.capture(),
            error.capture(),
        )).thenAnswer {
            error.firstValue.invoke(responseThrowable)
            mockDisposable
        }
    }

    companion object {
        const val SIMPLE_FAILED = "Ups algo salio mal, vuelve a intentarlo"
        private val ITEM_1 =
            ProductResponse(1, "item1", "product1", "", "", 0.0, 0, "product1.com", 0)
        private val ITEM_2 =
            ProductResponse(2, "item2", "product2", "", "", 0.0, 0, "product2.com", 0)
        private val ITEM_3 =
            ProductResponse(3, "item3", "product3", "", "", 0.0, 0, "product3.com", 0)
        private val PRODUCT_DATA_RESPONSE = ProductDataResponse(listOf(ITEM_1, ITEM_2, ITEM_3))
        private val LIST_PRODUCT_RESPONSE = listOf(ITEM_1, ITEM_2, ITEM_3)
    }
}