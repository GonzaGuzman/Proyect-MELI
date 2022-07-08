package com.zalo.proyectmeli

import android.content.res.Resources
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.zalo.proyectmeli.datasource.home.HomeDatasource
import com.zalo.proyectmeli.utils.models.Categories
import com.zalo.proyectmeli.utils.models.ProductResponse
import com.zalo.proyectmeli.presenter.home.HomePresenter
import com.zalo.proyectmeli.presenter.home.HomeView
import com.zalo.proyectmeli.utils.FormatNumber
import io.reactivex.rxjava3.disposables.Disposable
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

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

    @Before
    fun setup() {
        homePresenter = HomePresenter(homeView, homeDataSource, resources)
    }

    @Test
    fun `initComponent when getCategories is successfully`() {
        //GIVEN
        getCategoryListSuccessfully()
        val listOfCategories = Mockito.mock(listOf<Categories>()::class.java)
        //WHEN
        homePresenter.initComponent()
        //THEN
        assertEquals(homeView.onCategoryFetched(listOf()),
            homeView.onCategoryFetched(listOfCategories))
        verify(homeView).loadGone()
        verify(homeView).loadRecycler()
        verify(homeView).navigateToSearch()
        verify(homeView).navigateToHistoryDb()
        verify(homeView).navigateToShowItem()
        verify(homeView).openMenu()
    }

    @Test
    fun `initComponent when getCategories is unsuccessfully with out internet failure`() {
        //GIVEN
        getCategoryListUnsuccessfully()
        whenever(homeView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        homePresenter.initComponent()
        //THEN
        verify(homeView).showSnackBar(SIMPLE_FAILED)
        verify(homeView).loadRecycler()
        verify(homeView).navigateToSearch()
        verify(homeView).navigateToHistoryDb()
        verify(homeView).navigateToShowItem()
        verify(homeView).openMenu()
    }

    @Test
    fun `initComponent when getCategories is unsuccessfully with internet failure`() {
        //GIVEN
        getCategoryListUnsuccessfully()
        whenever(homeView.internetConnection()).thenReturn(false)
        //WHEN
        homePresenter.initComponent()
        //THEN
        verify(homeView).internetFailViewEnabled()
        verify(homeView).loadRecycler()
        verify(homeView).navigateToSearch()
        verify(homeView).navigateToHistoryDb()
        verify(homeView).navigateToShowItem()
        verify(homeView).openMenu()
    }

    @Test
    fun `getCategoriesLis successfully`() {
        //GIVEN
        getCategoryListSuccessfully()
        val listOfCategories = Mockito.mock(listOf<Categories>()::class.java)
        //WHEN
        homePresenter.initComponent()
        //THEN
        assertEquals(homeView.onCategoryFetched(listOf()),
            homeView.onCategoryFetched(listOfCategories))
        verify(homeView).loadGone()
    }

    @Test
    fun `getCategoriesList unsuccessfully with out internet failure`() {
        //GIVEN
        getCategoryListUnsuccessfully()
        whenever(homeView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        homePresenter.getCategoriesList()
        //THEN
        verify(homeView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `getCategoriesList unsuccessfully with internet failure`() {
        //GIVEN
        getCategoryListUnsuccessfully()
        whenever(homeView.internetConnection()).thenReturn(false)
        //WHEN
        homePresenter.getCategoriesList()
        //THEN
        verify(homeView).internetFailViewEnabled()
    }

    @Test
    fun `showItemDb successfully`() {
        //GIVEN
        whenever(homeDataSource.getIdRecentlySeenItem()).thenReturn(ID)
        getRecentlyItemSuccessfully()
        //WHEN
        homePresenter.showItemDb()
        //THEN
        verify(homeView).showItemDb(ITEM_1)
    }

    @Test
    fun `showItemDb unsuccessfully`() {
        //GIVEN
        whenever(homeDataSource.getIdRecentlySeenItem()).thenReturn(ID)
        getRecentlyItemUnsuccessfully()
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        homePresenter.showItemDb()
        //THEN
        verify(homeView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `loadRecentlySeen is successfully and ID is not empty`() {
        //GIVEN
        whenever(homeDataSource.getIdRecentlySeenItem()).thenReturn(ID)
        getRecentlyItemSuccessfully()
        val itemRecently = Mockito.mock(ProductResponse::class.java)
        //WHEN
        homePresenter.loadRecentlySeen()
        //THEN
        assertEquals(homeView.loadRecentlySeen(itemRecently.title,
            FormatNumber.formatNumber(itemRecently.price),
            itemRecently.thumbnail),
            homeView.loadRecentlySeen(ITEM_1.title,
                FormatNumber.formatNumber(ITEM_1.price),
                ITEM_1.thumbnail))
        verify(homeView).onOffEmptyRecently(false)
        verify(homeView).onOffRecyclerView(true)
    }

    @Test
    fun `loadRecentlySeen is successfully and ID is empty`() {
        //GIVEN
        whenever(homeDataSource.getIdRecentlySeenItem()).thenReturn(EMPTY_ID)
        //WHEN
        homePresenter.loadRecentlySeen()
        //THEN
        verify(homeView).onOffEmptyRecently(true)
        verify(homeView).onOffRecyclerView(false)
    }

    @Test
    fun `loadRecentlySeen fail and id is not Empty`() {
        //GIVEN
        whenever(homeDataSource.getIdRecentlySeenItem()).thenReturn(ID)
        getRecentlyItemUnsuccessfully()
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        homePresenter.loadRecentlySeen()
        //THEN
        verify(homeView).showSnackBar(SIMPLE_FAILED)
        verify(homeView).onOffEmptyRecently(false)
        verify(homeView).onOffRecyclerView(true)
    }

    @Test
    fun `loadRecentlySeen fail and id is Empty`() {
        //GIVEN
        whenever(homeDataSource.getIdRecentlySeenItem()).thenReturn(EMPTY_ID)
        getRecentlyItemUnsuccessfully()
        //WHEN
        homePresenter.loadRecentlySeen()
        //THEN
        verify(homeView).onOffEmptyRecently(true)
        verify(homeView).onOffRecyclerView(false)
    }

    @Test
    fun `activateViews with validate true`() {
        //GIVEN
        val validate = true
        //WHEN
        homePresenter.activateViews(validate)
        //THEN
        verify(homeView).onOffEmptyRecently(true)
        verify(homeView).onOffRecyclerView(false)
    }

    @Test
    fun `activateViews with validate false`() {
        //GIVEN
        val validate = false
        //WHEN
        homePresenter.activateViews(validate)
        //THEN
        verify(homeView).onOffEmptyRecently(false)
        verify(homeView).onOffRecyclerView(true)
    }

    @Test
    fun `positiveButton is clicked and clearHistory success`() {
        //GIVEN
        clearHistorySuccessfully()
        //WHEN
        homePresenter.onPositiveButtonClicked()
        //THEN
        verify(homeDataSource).setCountItem(0)
        verify(homeDataSource).setSearchPosition(0)
        verify(homeDataSource).setIdRecentlySeenItem(EMPTY_ID)
        verify(homeDataSource).setLinkRecentlySeen(EMPTY_LINK)
        verify(homeView).refresh()
    }

    @Test
    fun `positiveButton is clicked and clearHistory fail`() {
        //GIVEN
        clearHistoryUnsuccessfully()
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        homePresenter.onPositiveButtonClicked()
        //THEN
        verify(homeView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `showItemListDb is call`() {
        //GIVEN
        //WHEN
        homePresenter.showItemListDb()
        //THEN
        verify(homeView).navigateToHistoryDb()
    }

    @Test
    fun `showHistorial is call`() {
        //GIVEN
        //WHEN
        homePresenter.showHistorial()
        //THEN
        verify(homeView).showHistory()
    }

    @Test
    fun `openMenu is call`() {
        //GIVEN
        //WHEN
        homePresenter.openMenu()
        //THEN
        verify(homeView).open()
    }

    @Test
    fun `navigateToSearch is call`() {
        //GIVEN
        //WHEN
        homePresenter.navigateToSearch()
        //THEN
        verify(homeView).startSearch()
    }

    @Test
    fun `onNegativeButtonClicked is pressed`() {
        //GIVEN
        //WHEN
        homePresenter.onNegativeButtonClicked()
        //THEN
        verify(homeView).dialogDismiss()
    }

    @Test
    fun `clearSharedValues is call`() {
        //GIVEN
        //WHEN
        homePresenter.clearSharedValues()
        //THEN
        verify(homeDataSource).setCountItem(0)
        verify(homeDataSource).setSearchPosition(0)
        verify(homeDataSource).setIdRecentlySeenItem(EMPTY_ID)
        verify(homeDataSource).setLinkRecentlySeen(EMPTY_LINK)
    }

    @Test
    fun `deleteDialog is call`() {
        //GIVEN
        //WHEN
        homePresenter.deleteDialog()
        //THEN
        verify(homeView).showAlertCloseSession()
    }

    @Test
    fun `internetFail is call`() {
        //GIVEN
        //WHEN
        homePresenter.internetFail()
        //THEN
        verify(homeView).internetFailViewEnabled()
    }


    @Test
    fun `refresh button is pressed and initComponent when getCategories is successfully`() {
        //GIVEN
        getCategoryListSuccessfully()
        val listOfCategories = Mockito.mock(listOf<Categories>()::class.java)
        //WHEN
        //WHEN
        homePresenter.refreshButton()
        //THEN
        verify(homeView).internetFailViewDisabled()
        assertEquals(homeView.onCategoryFetched(listOf()),
            homeView.onCategoryFetched(listOfCategories))
        verify(homeView).loadGone()
        verify(homeView).loadRecycler()
        verify(homeView).navigateToSearch()
        verify(homeView).navigateToHistoryDb()
        verify(homeView).navigateToShowItem()
        verify(homeView).openMenu()
    }

    @Test
    fun `refresh button is pressed and initComponent when getCategories is unsuccessfully with out internet failure`() {
        //GIVEN
        getCategoryListUnsuccessfully()
        whenever(homeView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        //WHEN
        homePresenter.refreshButton()
        //THEN
        verify(homeView).internetFailViewDisabled()
        verify(homeView).showSnackBar(SIMPLE_FAILED)
        verify(homeView).loadRecycler()
        verify(homeView).navigateToSearch()
        verify(homeView).navigateToHistoryDb()
        verify(homeView).navigateToShowItem()
        verify(homeView).openMenu()
    }

    @Test
    fun `refresh button is pressed and initComponent when getCategories is unsuccessfully with internet failure`() {
        //GIVEN
        getCategoryListUnsuccessfully()
        whenever(homeView.internetConnection()).thenReturn(false)
        //WHEN
        homePresenter.refreshButton()
        //THEN
        verify(homeView).internetFailViewDisabled()
        verify(homeView).internetFailViewEnabled()
        verify(homeView).loadRecycler()
        verify(homeView).navigateToSearch()
        verify(homeView).navigateToHistoryDb()
        verify(homeView).navigateToShowItem()
        verify(homeView).openMenu()
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
        whenever(homeDataSource.getRecentlyItem(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke(ITEM_1)
            mockDisposable
        }
    }

    private fun getRecentlyItemUnsuccessfully() {
        val success = argumentCaptor<(ProductResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(homeDataSource.getRecentlyItem(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            error.firstValue.invoke(responseThrowable)
            mockDisposable
        }
    }

    private fun clearHistorySuccessfully() {
        val success = argumentCaptor<() -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        whenever(homeDataSource.clearHistory(
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke()
            mockDisposable
        }
    }

    private fun clearHistoryUnsuccessfully() {
        val success = argumentCaptor<() -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(homeDataSource.clearHistory(
            success.capture(),
            error.capture(),
        )).thenAnswer {
            error.firstValue.invoke(responseThrowable)
            mockDisposable
        }
    }

    companion object {
        const val SIMPLE_FAILED = "Ups algo salio mal, vuelve a intentarlo"
        const val ID = "idTest"
        const val EMPTY_ID = ""
        const val EMPTY_LINK = ""
        private val ITEM_1 =
            ProductResponse(1, "item1", "product1", "", "", 0.0, 0, "product1.com", 0)
    }
}
