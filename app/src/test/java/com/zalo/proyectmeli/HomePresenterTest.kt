package com.zalo.proyectmeli

import android.content.res.Resources
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.zalo.proyectmeli.datasource.home.HomeDatasource
import com.zalo.proyectmeli.network.models.Categories
import com.zalo.proyectmeli.network.models.ProductResponse
import com.zalo.proyectmeli.presenter.home.HomePresenter
import com.zalo.proyectmeli.presenter.home.HomeView
import io.reactivex.rxjava3.disposables.Disposable
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@RunWith(MockitoJUnitRunner::class)
class HomePresenterTest {

    @Mock
    private lateinit var homeDataSource: HomeDatasource

    @Mock
    private lateinit var homeView: HomeView

    @Mock
    private lateinit var mockDisposable: Disposable

    @Mock
    private lateinit var resources: Resources

    private lateinit var homePresenter: HomePresenter

    private val out = ByteArrayOutputStream()
    private val originalOut = System.out

    @Before
    fun setup() {
        homePresenter = HomePresenter(homeView, homeDataSource, resources)

        System.setOut(PrintStream(out))

    }

    @After
    fun restoreInitialStreams() {
        System.setOut(originalOut)
    }

    @Test
    fun `initRecyclerCategories when getCategories is successfully`() {
        //GIVEN
        getCategoryListSuccessfully()
        val listOfCategories = Mockito.mock(listOf<Categories>()::class.java)
        //WHEN
        homePresenter.initRecyclerCategories()
        //THEN
        assertEquals(homeView.onCategoryFetched(listOf()),
            homeView.onCategoryFetched(listOfCategories))
        verify(homeView).loadGone()
        verify(homeView).loadRecycler()
        verify(homeView).textSearch()
    }

    @Test
    fun `initRecyclerCategories when getCategories is unsuccessfully`() {
        //GIVEN
        getCategoryListUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        homePresenter.initRecyclerCategories()
        //THEN
        verify(homeView).showSnackBar(String.format(THIS_FAILED, FAIL))
        verify(homeView).loadRecycler()
        verify(homeView).textSearch()
    }

    @Test
    fun `getCategoriesLis successfully`() {
        //GIVEN
        getCategoryListSuccessfully()
        val listOfCategories = Mockito.mock(listOf<Categories>()::class.java)
        //WHEN
        homePresenter.initRecyclerCategories()
        //THEN
        assertEquals(homeView.onCategoryFetched(listOf()),
            homeView.onCategoryFetched(listOfCategories))
        verify(homeView).loadGone()
    }


    @Test
    fun `getCategoriesList unsuccessfully`() {
        //GIVEN
        getCategoryListUnsuccessfully()
        whenever(resources.getString(R.string.error_message)).thenReturn(THIS_FAILED)
        //WHEN
        homePresenter.getCategoriesList()
        //THEN
        verify(homeView).showSnackBar(String.format(THIS_FAILED, FAIL))
    }

    @Test
    fun `button showItemListDb is pressed`() {
        //GIVEN
        //WHEN
        homePresenter.showItemListDb()
        //THEN
        verify(homeView).showListDb()
    }

    @Test
    fun `imageItem RecentlySeen is pressed and getRecentlyItem is successfully`() {
        //GIVEN
        getRecentlyItemSuccessfully()
        val itemRecently = Mockito.mock(ProductResponse::class.java)
        val productResponse = Mockito.mock(ProductResponse::class.java)
        val id = TEST_TEXT
        whenever(homeDataSource.getIdRecentlySeenItem()).thenReturn(id)
        //WHEN
        homePresenter.showItemDb()
        //THEN
        assertEquals(homeView.showItemDb(productResponse), homeView.showItemDb(itemRecently))

    }

    @Test
    fun `imageItem RecentlySeen is pressed and getRecentlyItem fail`() {
        //GIVEN
        getRecentlyItemUnsuccessfully()
        val id = TEST_TEXT
        whenever(homeDataSource.getIdRecentlySeenItem()).thenReturn(id)
        //WHEN
        homePresenter.showItemDb()
        //THEN
        assertEquals(ERROR_MESSAGE, out.toString().trim())
    }

    @Test
    fun `loadRecentlySeen is successfully`() {
        //GIVEN
        getRecentlyItemSuccessfully()
        val itemRecently = Mockito.mock(ProductResponse::class.java)
        val productResponse = Mockito.mock(ProductResponse::class.java)
        val id = TEST_TEXT
        whenever(homeDataSource.getIdRecentlySeenItem()).thenReturn(id)
        //WHEN
        homePresenter.loadRecentlySeen()
        //THEN
        assertEquals(homeView.loadRecentlySeen(productResponse), homeView.loadRecentlySeen(itemRecently))

    }

    @Test
    fun `loadRecentlySeen fail`() {
        //GIVEN
        getRecentlyItemUnsuccessfully()
        val id = TEST_TEXT
        whenever(homeDataSource.getIdRecentlySeenItem()).thenReturn(id)
        //WHEN
        homePresenter.loadRecentlySeen()
        //THEN
        assertEquals(ERROR_MESSAGE, out.toString().trim())
    }

    private fun getCategoryListSuccessfully() {
        val success = argumentCaptor<(List<Categories>) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val listOfCategories = Mockito.mock(listOf<Categories>()::class.java)
        whenever(homeDataSource.getCategories(
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke(listOfCategories)
            mockDisposable
        }
    }

    private fun getCategoryListUnsuccessfully() {
        val success = argumentCaptor<(List<Categories>) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(responseThrowable.message).thenReturn(FAIL)
        whenever(homeDataSource.getCategories(
            success.capture(),
            error.capture(),
        )).thenAnswer {
            error.firstValue.invoke(responseThrowable)
            mockDisposable
        }
    }

    private fun getRecentlyItemSuccessfully() {
        val success = argumentCaptor<(ProductResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val itemRecently = Mockito.mock(ProductResponse::class.java)
        whenever(homeDataSource.getRecentlyItem(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke(itemRecently)
            mockDisposable
        }
    }

    private fun getRecentlyItemUnsuccessfully() {
        val success = argumentCaptor<(ProductResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(responseThrowable.message).thenReturn(ERROR_MESSAGE)
        whenever(homeDataSource.getRecentlyItem(
            any(),
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
        const val TEST_TEXT = "test"
        const val ERROR_MESSAGE = "GET DATABASE FAIL"
    }

}