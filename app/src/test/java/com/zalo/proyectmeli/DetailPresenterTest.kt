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
        val listProducts = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(listProducts))
        verify(detailView).loadGone()
        verify(detailView).loadRecycler()
        verify(detailView).textSearch()
    }

    @Test
    fun `initComponent call when loadFetched and getItemList success`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(2)
        getItemListSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        val listProducts = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(listProducts))
        verify(detailView).loadGone()
        verify(detailView).loadRecycler()
        verify(detailView).textSearch()
    }

    @Test
    fun `initComponent call when loadFetched and getItemDb success`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        getItemDbSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        val listProducts = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(listProducts))
        verify(detailView).loadGone()
        verify(detailView).loadRecycler()
        verify(detailView).textSearch()
    }

    @Test
    fun `initComponent call when loadFetched and getCategoryDetail fail`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(1)
        getCategoryDetailUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        verify(detailView).showSnackBar(String.format(THIS_FAILED, FAIL))
        verify(detailView).loadRecycler()
        verify(detailView).textSearch()
    }

    @Test
    fun `initComponent call when loadFetched and getItemList fail`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(intent.getIntExtra(TYPE_SHOW, 0)).thenReturn(2)
        getItemListUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        verify(detailView).showSnackBar(String.format(THIS_FAILED, FAIL))
        verify(detailView).loadRecycler()
        verify(detailView).textSearch()
    }

    @Test
    fun `initComponent call when loadFetched and getItemDb fail`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        getItmDbUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        detailPresenter.initComponent(intent)
        //THEM
        verify(detailView).showSnackBar(String.format(THIS_FAILED, FAIL))
        verify(detailView).loadRecycler()
        verify(detailView).textSearch()
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 1 and getProductList success`() {
        //GIVEN
        getCategoryDetailSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        val listProducts = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.getProductList(CAT_ID)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(listProducts))
        verify(detailView).loadGone()
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 2 and getItemList success`() {
        //GIVEN
        getItemListSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        val listProducts = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.searchItems(KEY_SEARCH)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(listProducts))
        verify(detailView).loadGone()
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 3 and getItemDb success`() {
        //GIVEN
        getItemDbSuccessfully()
        val productListResponse = Mockito.mock(listOf<ProductResponse>()::class.java)
        val listProducts = Mockito.mock(listOf<ProductResponse>()::class.java)
        //WHEN
        detailPresenter.getProductListOfDb()
        //THEM
        assertEquals(detailView.onProductFetched(ProductDataResponse(productListResponse)),
            detailView.onProductFetched(ProductDataResponse(listProducts)))
        verify(detailView).loadGone()
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 1 and getProductList fail`() {
        //GIVEN
        getCategoryDetailUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        detailPresenter.getProductList(CAT_ID)
        //THEM
        verify(detailView).showSnackBar(String.format(THIS_FAILED, FAIL))
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 2 and getItemList fail`() {
        //GIVEN
        getItemListUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        detailPresenter.searchItems(KEY_SEARCH)
        //THEM
        verify(detailView).showSnackBar(String.format(THIS_FAILED, FAIL))
    }

    @Test
    fun `loadFetched when TYPE_SHOW equals 3 and getItemDb fail`() {
        //GIVEN
        getItmDbUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        detailPresenter.getProductListOfDb()
        //THEM
        verify(detailView).showSnackBar(String.format(THIS_FAILED, FAIL))
    }

    @Test
    fun `getProductList success`() {
        //GIVEN
        getCategoryDetailSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        val listProducts = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.getProductList(CAT_ID)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(listProducts))
        verify(detailView).loadGone()
    }

    @Test
    fun `getItemList success`() {
        //GIVEN
        getItemListSuccessfully()
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        val listProducts = Mockito.mock(ProductDataResponse::class.java)
        //WHEN
        detailPresenter.searchItems(KEY_SEARCH)
        //THEM
        assertEquals(detailView.onProductFetched(productDataResponse),
            detailView.onProductFetched(listProducts))
        verify(detailView).loadGone()
    }

    @Test
    fun `getItemDb success`() {
        //GIVEN
        getItemDbSuccessfully()
        val productListResponse = Mockito.mock(listOf<ProductResponse>()::class.java)
        val listProducts = Mockito.mock(listOf<ProductResponse>()::class.java)
        //WHEN
        detailPresenter.getProductListOfDb()
        //THEM
        assertEquals(detailView.onProductFetched(ProductDataResponse(productListResponse)),
            detailView.onProductFetched(ProductDataResponse(listProducts)))
        verify(detailView).loadGone()
    }

    @Test
    fun `getProductList fail`() {
        //GIVEN
        getCategoryDetailUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        detailPresenter.getProductList(CAT_ID)
        //THEM
        verify(detailView).showSnackBar(String.format(THIS_FAILED, FAIL))
    }


    @Test
    fun `getItemList fail`() {
        //GIVEN
        getItemListUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        detailPresenter.searchItems(KEY_SEARCH)
        //THEM
        verify(detailView).showSnackBar(String.format(THIS_FAILED, FAIL))
    }

    @Test
    fun `getItemDb fail`() {
        //GIVEN
        getItmDbUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        detailPresenter.getProductListOfDb()
        //THEM
        verify(detailView).showSnackBar(String.format(THIS_FAILED, FAIL))
    }

    private fun getCategoryDetailSuccessfully() {
        val success = argumentCaptor<(ProductDataResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val listProducts = Mockito.mock(ProductDataResponse::class.java)
        whenever(detailDataSource.getCategoriesDetail(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke(listProducts)
            mockDisposable
        }
    }

    private fun getCategoryDetailUnsuccessfully() {
        val success = argumentCaptor<(ProductDataResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(responseThrowable.message).thenReturn(FAIL)
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
        val productDataResponse = Mockito.mock(ProductDataResponse::class.java)
        whenever(detailDataSource.getItemsList(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke(productDataResponse)
            mockDisposable
        }
    }

    private fun getItemListUnsuccessfully() {
        val success = argumentCaptor<(ProductDataResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(responseThrowable.message).thenReturn(FAIL)
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
        val listProductResponse = Mockito.mock(listOf<ProductResponse>()::class.java)
        whenever(detailDataSource.getItemsDb(
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke(listProductResponse)
            mockDisposable
        }
    }

    private fun getItmDbUnsuccessfully() {
        val success = argumentCaptor<(List<ProductResponse>) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(responseThrowable.message).thenReturn(FAIL)
        whenever(detailDataSource.getItemsDb(
            success.capture(),
            error.capture(),
        )).thenAnswer {
            error.firstValue.invoke(responseThrowable)
            mockDisposable
        }
    }

    companion object {
        const val THIS_FAILED = "UPS ALGO SALIO MAL %s"
        const val FAIL = "FALLA: no se obtuvo la lista"
    }
}