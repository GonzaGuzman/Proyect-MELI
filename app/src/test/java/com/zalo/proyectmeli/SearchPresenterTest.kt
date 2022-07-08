package com.zalo.proyectmeli

import android.content.res.Resources
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.zalo.proyectmeli.datasource.search.SearchDatasource
import com.zalo.proyectmeli.presenter.searchPresenter.SearchPresenter
import com.zalo.proyectmeli.presenter.searchPresenter.SearchView
import com.zalo.proyectmeli.utils.models.SearchHistory
import io.reactivex.rxjava3.disposables.Disposable
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchPresenterTest {
    @Mock
    private lateinit var searchDataSource: SearchDatasource

    @Mock
    private lateinit var searchView: SearchView

    @Mock
    private lateinit var mockDisposable: Disposable

    @Mock
    private lateinit var resources: Resources

    private lateinit var searchPresenter: SearchPresenter

    @Before
    fun setup() {
        searchPresenter = SearchPresenter(searchView, searchDataSource, resources)
    }

    @Test
    fun `initComponent and getAllSearch success`() {
        //GIVEN
        getAllSearchSuccessfully()
        //WHEN
        searchPresenter.initComponent()
        //THEN
        verify(searchView).loadRecycler()
        verify(searchView).loadAdapter(LIST_SEARCH_HISTORY)
        verify(searchView).loadArrayAdapter(ARRAY_LIST_SEARCH)
        verify(searchView).loadGone()
        verify(searchView).navigateToDetail()
        verify(searchView).onBack()
    }

    @Test
    fun `initComponent and getAllSearch fail`() {
        //GIVEN
        getAllSearchUnsuccessfully()
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        searchPresenter.initComponent()
        //THEN
        verify(searchView).loadRecycler()
        verify(searchView).showSnackBar(SIMPLE_FAILED)
        verify(searchView).navigateToDetail()
        verify(searchView).onBack()
    }

    @Test
    fun `getAllSearch success`() {
        //GIVEN
        getAllSearchSuccessfully()
        //WHEN
        searchPresenter.getAllSearch()
        //THEN
        verify(searchView).loadAdapter(LIST_SEARCH_HISTORY)
        verify(searchView).loadArrayAdapter(ARRAY_LIST_SEARCH)
        verify(searchView).loadGone()
    }

    @Test
    fun `getAllSearch fail`() {
        //GIVEN
        getAllSearchUnsuccessfully()
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        searchPresenter.getAllSearch()
        //THEN
        verify(searchView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `getArraySearch call`() {
        //GIVEN
        val listSearch = Mockito.mock(ArrayList<String>()::class.java)
        //WHEN
        searchPresenter.getArraySearch(LIST_SEARCH_HISTORY)
        //THEN
        assertEquals(searchView.loadArrayAdapter(listSearch), searchView.loadArrayAdapter(
            ARRAY_LIST_SEARCH))
    }

    @Test
    fun `startSearch with search is not empty and insertNewSearch success`() {
        //GIVEN
        insertNewSearchSuccessfully().apply {
            whenever(searchDataSource.getPosition()).thenReturn(2)
        }
        val search = SEARCH
        //WHEN
        searchPresenter.startSearch(search)
        //THEN
        verify(searchDataSource).setPosition(2 + 1)
        verify(searchView).startDetail(search)
    }

    @Test
    fun `startSearch with search is empty`() {
        //GIVEN
        val search = EMPTY_SEARCH
        //WHEN
        searchPresenter.startSearch(search)
        //THEN
        verify(searchDataSource, never()).setPosition(searchDataSource.getPosition())
        verify(searchView, never()).startDetail(search)
    }

    @Test
    fun `startSearch and insertNewSearch failed`() {
        //GIVEN
        insertNewSearchUnsuccessfully()
        val search = SEARCH
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        searchPresenter.startSearch(search)
        //THEN
        verify(searchView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `insertNewSearch success`() {
        //GIVEN
        insertNewSearchSuccessfully().apply {
            whenever(searchDataSource.getPosition()).thenReturn(2)
        }
        val search = SEARCH
        //WHEN
        searchPresenter.insertNewSearch(search)
        //THEN
        verify(searchDataSource).setPosition(2 + 1)
    }

    @Test
    fun `insertNewSearch failed`() {
        //GIVEN
        insertNewSearchUnsuccessfully()
        val search = SEARCH
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(SIMPLE_FAILED)
        //WHEN
        searchPresenter.insertNewSearch(search)
        //THEN
        verify(searchView).showSnackBar(SIMPLE_FAILED)
    }

    @Test
    fun `back call`() {
        //GIVEN
        //WHEN
        searchPresenter.back()
        //THEN
        verify(searchView).back()
    }

    private fun getAllSearchSuccessfully() {
        val success = argumentCaptor<(List<SearchHistory>) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        whenever(searchDataSource.getAllSearch(
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke(LIST_SEARCH_HISTORY)
            mockDisposable
        }
    }

    private fun getAllSearchUnsuccessfully() {
        val success = argumentCaptor<(List<SearchHistory>) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(searchDataSource.getAllSearch(
            success.capture(),
            error.capture(),
        )).thenAnswer {
            error.firstValue.invoke(responseThrowable)
            mockDisposable
        }
    }

    private fun insertNewSearchSuccessfully() {
        val success = argumentCaptor<() -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        whenever(searchDataSource.insertNewSearch(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            success.firstValue.invoke()
            mockDisposable
        }
    }

    private fun insertNewSearchUnsuccessfully() {
        val success = argumentCaptor<() -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseThrowable = Mockito.mock(Throwable::class.java)
        whenever(searchDataSource.insertNewSearch(
            any(),
            success.capture(),
            error.capture(),
        )).thenAnswer {
            error.firstValue.invoke(responseThrowable)
            mockDisposable
        }
    }

    companion object {
        const val SIMPLE_FAILED = "Ups algo salio mal, vuelve a intentarlo"
        const val SEARCH = "searchTest"
        const val EMPTY_SEARCH = ""
        private val SEARCH_1 = SearchHistory("searchTest1", 1)
        private val SEARCH_2 = SearchHistory("searchTest2", 2)
        private val SEARCH_3 = SearchHistory("searchTest3", 3)
        private val LIST_SEARCH_HISTORY = listOf(SEARCH_1, SEARCH_2, SEARCH_3)
        private val ARRAY_LIST_SEARCH = arrayListOf("searchTest1", "searchTest2", "searchTest3")
    }
}
