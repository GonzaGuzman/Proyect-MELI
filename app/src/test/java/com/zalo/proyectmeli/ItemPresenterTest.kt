package com.zalo.proyectmeli

import android.content.Intent
import android.content.res.Resources
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.zalo.proyectmeli.datasource.item.ItemDatasource
import com.zalo.proyectmeli.presenter.item.ItemPresenter
import com.zalo.proyectmeli.presenter.item.ItemView
import com.zalo.proyectmeli.utils.FormatNumber
import com.zalo.proyectmeli.utils.ITEM_ID
import com.zalo.proyectmeli.utils.models.DescriptionResponse
import com.zalo.proyectmeli.utils.models.ProductResponse
import io.reactivex.rxjava3.disposables.Disposable
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ItemPresenterTest {
    @Mock
    private lateinit var itemDataSource: ItemDatasource

    @Mock
    private lateinit var itemView: ItemView

    @Mock
    private lateinit var mockDisposable: Disposable

    @Mock
    private lateinit var resources: Resources

    private lateinit var itemPresenter: ItemPresenter

    @Before
    fun setup() {
        itemPresenter = ItemPresenter(itemView, itemDataSource, resources)
    }

    @Test
    fun `initComponent with all success`() {
        //GIVEN
        getItemByIdSuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemSuccessfully()
        getByIdSuccessfullyWithNewItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn("NUEVO")
        whenever(resources.getString(R.string.newState)).thenReturn("new")
        val condition = itemPresenter.translateCondition(ITEM_1.condition)
        whenever(resources.getString(R.string.state_sold,
            condition,
            ITEM_1.soldQuantity)).thenReturn("Estado $condition / Vendidos ${ITEM_1.soldQuantity}")
        whenever(resources.getString(R.string.stock,
            ITEM_1.stock)).thenReturn("Stock Disponible ${ITEM_1.stock} unidades")
        val state = resources.getString(R.string.state_sold, condition, ITEM_1.soldQuantity)
        val price = FormatNumber.formatNumber(ITEM_1.price)
        val stock = resources.getString(R.string.stock, ITEM_1.stock)
        //WHEN
        itemPresenter.initComponent(intent)
        //THEN
        verify(itemView).retrieveExtras(ITEM_1.title, state, price, ITEM_1.thumbnail, stock)
        verify(itemDataSource).setIdRecentlySeenItem(ITEM_1.id)
        verify(itemDataSource).setPermalinkRecentlySeenItem(ITEM_1.permaLink)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `initComponent with getItemById fail with internet failure`() {
        //GIVEN
        getItemByIdUnsuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemSuccessfully()
        getByIdSuccessfullyWithNewItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(itemView.internetConnection()).thenReturn(false)
        whenever(resources.getString(R.string.it_seems_there_is_no_internet)).thenReturn(
            INTERNET_FAIL)
        //WHEN
        itemPresenter.initComponent(intent)
        //THEN
        verify(itemView).showSnackBarRed(INTERNET_FAIL)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `initComponent with getItemById fail with out internet failure`() {
        //GIVEN
        getItemByIdUnsuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemSuccessfully()
        getByIdSuccessfullyWithNewItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(itemView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(
            SIMPLE_FAILED)
        //WHEN
        itemPresenter.initComponent(intent)
        //THEN
        verify(itemView).showSnackBar(SIMPLE_FAILED)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `initComponent with getItemDescription fail`() {
        //GIVEN
        getItemByIdSuccessfully()
        getItemDescriptionUnsuccessfully()
        dbInsertItemSuccessfully()
        getByIdSuccessfullyWithNewItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn("NUEVO")
        whenever(resources.getString(R.string.newState)).thenReturn("new")
        val condition = itemPresenter.translateCondition(ITEM_1.condition)
        whenever(resources.getString(R.string.state_sold,
            condition,
            ITEM_1.soldQuantity)).thenReturn("Estado $condition / Vendidos ${ITEM_1.soldQuantity}")
        whenever(resources.getString(R.string.stock,
            ITEM_1.stock)).thenReturn("Stock Disponible ${ITEM_1.stock} unidades")
        val state = resources.getString(R.string.state_sold, condition, ITEM_1.soldQuantity)
        val price = FormatNumber.formatNumber(ITEM_1.price)
        val stock = resources.getString(R.string.stock, ITEM_1.stock)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(
            SIMPLE_FAILED)
        //WHEN
        itemPresenter.initComponent(intent)
        //THEN
        verify(itemView).retrieveExtras(ITEM_1.title, state, price, ITEM_1.thumbnail, stock)
        verify(itemDataSource).setIdRecentlySeenItem(ITEM_1.id)
        verify(itemDataSource).setPermalinkRecentlySeenItem(ITEM_1.permaLink)
        verify(itemView).showSnackBar(SIMPLE_FAILED)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `initComponent with deInsertItem fail`() {
        //GIVEN
        getItemByIdSuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemUnsuccessfully()
        getByIdSuccessfullyWithNewItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn("NUEVO")
        whenever(resources.getString(R.string.newState)).thenReturn("new")
        val condition = itemPresenter.translateCondition(ITEM_1.condition)
        whenever(resources.getString(R.string.state_sold,
            condition,
            ITEM_1.soldQuantity)).thenReturn("Estado $condition / Vendidos ${ITEM_1.soldQuantity}")
        whenever(resources.getString(R.string.stock,
            ITEM_1.stock)).thenReturn("Stock Disponible ${ITEM_1.stock} unidades")
        val state = resources.getString(R.string.state_sold, condition, ITEM_1.soldQuantity)
        val price = FormatNumber.formatNumber(ITEM_1.price)
        val stock = resources.getString(R.string.stock, ITEM_1.stock)
        //WHEN
        itemPresenter.initComponent(intent)
        //THEN
        verify(itemView).retrieveExtras(ITEM_1.title, state, price, ITEM_1.thumbnail, stock)
        verify(itemDataSource).setIdRecentlySeenItem(ITEM_1.id)
        verify(itemDataSource).setPermalinkRecentlySeenItem(ITEM_1.permaLink)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `initComponent with getById fail`() {
        //GIVEN
        getItemByIdSuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemSuccessfully()
        getByIdUnsuccessfully()
        val intent = Mockito.mock(Intent::class.java)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn("NUEVO")
        whenever(resources.getString(R.string.newState)).thenReturn("new")
        val condition = itemPresenter.translateCondition(ITEM_1.condition)
        whenever(resources.getString(R.string.state_sold,
            condition,
            ITEM_1.soldQuantity)).thenReturn("Estado $condition / Vendidos ${ITEM_1.soldQuantity}")
        whenever(resources.getString(R.string.stock,
            ITEM_1.stock)).thenReturn("Stock Disponible ${ITEM_1.stock} unidades")
        val state = resources.getString(R.string.state_sold, condition, ITEM_1.soldQuantity)
        val price = FormatNumber.formatNumber(ITEM_1.price)
        val stock = resources.getString(R.string.stock, ITEM_1.stock)
        //WHEN
        itemPresenter.initComponent(intent)
        //THEN
        verify(itemView).retrieveExtras(ITEM_1.title, state, price, ITEM_1.thumbnail, stock)
        verify(itemDataSource).setIdRecentlySeenItem(ITEM_1.id)
        verify(itemDataSource).setPermalinkRecentlySeenItem(ITEM_1.permaLink)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `initComponent with getById duplicate item`() {
        //GIVEN
        getItemByIdSuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemSuccessfully()
        getByIdSuccessfullyWithDuplicateItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn("NUEVO")
        whenever(resources.getString(R.string.newState)).thenReturn("new")
        val condition = itemPresenter.translateCondition(ITEM_1.condition)
        whenever(resources.getString(R.string.state_sold,
            condition,
            ITEM_1.soldQuantity)).thenReturn("Estado $condition / Vendidos ${ITEM_1.soldQuantity}")
        whenever(resources.getString(R.string.stock,
            ITEM_1.stock)).thenReturn("Stock Disponible ${ITEM_1.stock} unidades")
        val state = resources.getString(R.string.state_sold, condition, ITEM_1.soldQuantity)
        val price = FormatNumber.formatNumber(ITEM_1.price)
        val stock = resources.getString(R.string.stock, ITEM_1.stock)
        //WHEN
        itemPresenter.initComponent(intent)
        //THEN
        verify(itemView).retrieveExtras(ITEM_1.title, state, price, ITEM_1.thumbnail, stock)
        verify(itemDataSource).setIdRecentlySeenItem(ITEM_1.id)
        verify(itemDataSource).setPermalinkRecentlySeenItem(ITEM_1.permaLink)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `dataBaseLimit with argument less than limit`() {
        //GIVEN
        val num = ONE
        //WHEN
        itemPresenter.dataBaseLimit(num)
        //THEN
        verify(itemDataSource).setCountItems(TWO)
        assertEquals(itemPresenter.dataBaseLimit(num), ONE)
    }

    @Test
    fun `dataBaseLimit with argument greater than limit`() {
        //GIVEN
        val num = 50
        //WHEN
        itemPresenter.dataBaseLimit(num)
        //THEN
        verify(itemDataSource).setCountItems(ONE)
        assertEquals(itemPresenter.dataBaseLimit(num), ZERO)
    }

    @Test
    fun `saveItem successfully`() {
        //GIVEN
        dbInsertItemSuccessfully()
        //WHEN
        itemPresenter.saveItem(ITEM_1)
        //THEN
    }

    @Test
    fun `saveItem unsuccessfully`() {
        //GIVEN
        dbInsertItemUnsuccessfully()
        //WHEN
        itemPresenter.saveItem(ITEM_1)
        //THEN
    }


    @Test
    fun `success validateAndSaveItem with nonexistent itemId in database `() {
        //GIVEN
        getByIdSuccessfullyWithNewItem()
        dbInsertItemSuccessfully()
        //WHEN
        itemPresenter.validateAndSaveInDb(ITEM_1)
        //THEN

    }

    @Test
    fun `success validateAndSaveItem with duplicate itemId in database `() {
        //GIVEN
        getByIdSuccessfullyWithDuplicateItem()
        //WHEN
        itemPresenter.validateAndSaveInDb(ITEM_1)
        //THEN
    }

    @Test
    fun `fail validateAndSaveItem`() {
        //GIVEN
        getByIdUnsuccessfully()
        val item = Mockito.mock(ProductResponse::class.java)
        whenever(item.id).thenReturn(ITEM_ID)
        //WHEN
        itemPresenter.validateAndSaveInDb(item)
        //THEN
    }

    @Test
    fun `navigateToMeli call`() {
        //GIVEN
        whenever(itemDataSource.getPermalinkRecentlySeenItem()).thenReturn(ITEM_1.permaLink)
        //WHEN
        itemPresenter.navigateToMeli()
        //THEN
        verify(itemView).startMELI(ITEM_1.permaLink)
    }


    @Test
    fun `navigateToShared call`() {
        //GIVEN
        whenever(itemDataSource.getPermalinkRecentlySeenItem()).thenReturn(ITEM_1.permaLink)
        //WHEN
        itemPresenter.navigateToShared()
        //THEN
        verify(itemView).sharedItem(ITEM_1.permaLink)
    }

    @Test
    fun `navigateToSearch call`() {
        //GIVEN
        //WHEN
        itemPresenter.navigateToSearch()
        //THEN
        verify(itemView).startSearch()
    }

    @Test
    fun `back is clicked`() {
        //GIVEN
        //WHEN
        itemPresenter.back()
        //THEN
        verify(itemView).back()
    }

    @Test
    fun `internetFail call`() {
        //GIVEN
        whenever(resources.getString(R.string.it_seems_there_is_no_internet)).thenReturn(
            INTERNET_FAIL)
        //WHEN
        itemPresenter.internetFail()
        //THEN
        verify(itemView).showSnackBarRed(INTERNET_FAIL)
    }

    @Test
    fun `refresh button is pressed and initComponent with all success`() {
        //GIVEN
        getItemByIdSuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemSuccessfully()
        getByIdSuccessfullyWithNewItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn("NUEVO")
        whenever(resources.getString(R.string.newState)).thenReturn("new")
        val condition = itemPresenter.translateCondition(ITEM_1.condition)
        whenever(resources.getString(R.string.state_sold,
            condition,
            ITEM_1.soldQuantity)).thenReturn("Estado $condition / Vendidos ${ITEM_1.soldQuantity}")
        whenever(resources.getString(R.string.stock,
            ITEM_1.stock)).thenReturn("Stock Disponible ${ITEM_1.stock} unidades")
        val state = resources.getString(R.string.state_sold, condition, ITEM_1.soldQuantity)
        val price = FormatNumber.formatNumber(ITEM_1.price)
        val stock = resources.getString(R.string.stock, ITEM_1.stock)
        //WHEN
        itemPresenter.refreshButton(intent)
        //THEN
        verify(itemView).retrieveExtras(ITEM_1.title, state, price, ITEM_1.thumbnail, stock)
        verify(itemDataSource).setIdRecentlySeenItem(ITEM_1.id)
        verify(itemDataSource).setPermalinkRecentlySeenItem(ITEM_1.permaLink)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent with getItemById fail with internet failure`() {
        //GIVEN
        getItemByIdUnsuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemSuccessfully()
        getByIdSuccessfullyWithNewItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(itemView.internetConnection()).thenReturn(false)
        whenever(resources.getString(R.string.it_seems_there_is_no_internet)).thenReturn(
            INTERNET_FAIL)
        //WHEN
        itemPresenter.refreshButton(intent)
        //THEN
        verify(itemView).showSnackBarRed(INTERNET_FAIL)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent with getItemById fail with out internet failure`() {
        //GIVEN
        getItemByIdUnsuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemSuccessfully()
        getByIdSuccessfullyWithNewItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(itemView.internetConnection()).thenReturn(true)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(
            SIMPLE_FAILED)
        //WHEN
        itemPresenter.refreshButton(intent)
        //THEN
        verify(itemView).showSnackBar(SIMPLE_FAILED)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent with getItemDescription fail`() {
        //GIVEN
        getItemByIdSuccessfully()
        getItemDescriptionUnsuccessfully()
        dbInsertItemSuccessfully()
        getByIdSuccessfullyWithNewItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn("NUEVO")
        whenever(resources.getString(R.string.newState)).thenReturn("new")
        val condition = itemPresenter.translateCondition(ITEM_1.condition)
        whenever(resources.getString(R.string.state_sold,
            condition,
            ITEM_1.soldQuantity)).thenReturn("Estado $condition / Vendidos ${ITEM_1.soldQuantity}")
        whenever(resources.getString(R.string.stock,
            ITEM_1.stock)).thenReturn("Stock Disponible ${ITEM_1.stock} unidades")
        val state = resources.getString(R.string.state_sold, condition, ITEM_1.soldQuantity)
        val price = FormatNumber.formatNumber(ITEM_1.price)
        val stock = resources.getString(R.string.stock, ITEM_1.stock)
        whenever(resources.getString(R.string.simple_error_message)).thenReturn(
            SIMPLE_FAILED)
        //WHEN
        itemPresenter.refreshButton(intent)
        //THEN
        verify(itemView).retrieveExtras(ITEM_1.title, state, price, ITEM_1.thumbnail, stock)
        verify(itemDataSource).setIdRecentlySeenItem(ITEM_1.id)
        verify(itemDataSource).setPermalinkRecentlySeenItem(ITEM_1.permaLink)
        verify(itemView).showSnackBar(SIMPLE_FAILED)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent with deInsertItem fail`() {
        //GIVEN
        getItemByIdSuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemUnsuccessfully()
        getByIdSuccessfullyWithNewItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn("NUEVO")
        whenever(resources.getString(R.string.newState)).thenReturn("new")
        val condition = itemPresenter.translateCondition(ITEM_1.condition)
        whenever(resources.getString(R.string.state_sold,
            condition,
            ITEM_1.soldQuantity)).thenReturn("Estado $condition / Vendidos ${ITEM_1.soldQuantity}")
        whenever(resources.getString(R.string.stock,
            ITEM_1.stock)).thenReturn("Stock Disponible ${ITEM_1.stock} unidades")
        val state = resources.getString(R.string.state_sold, condition, ITEM_1.soldQuantity)
        val price = FormatNumber.formatNumber(ITEM_1.price)
        val stock = resources.getString(R.string.stock, ITEM_1.stock)
        //WHEN
        itemPresenter.refreshButton(intent)
        //THEN
        verify(itemView).retrieveExtras(ITEM_1.title, state, price, ITEM_1.thumbnail, stock)
        verify(itemDataSource).setIdRecentlySeenItem(ITEM_1.id)
        verify(itemDataSource).setPermalinkRecentlySeenItem(ITEM_1.permaLink)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent with getById fail`() {
        //GIVEN
        getItemByIdSuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemSuccessfully()
        getByIdUnsuccessfully()
        val intent = Mockito.mock(Intent::class.java)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn("NUEVO")
        whenever(resources.getString(R.string.newState)).thenReturn("new")
        val condition = itemPresenter.translateCondition(ITEM_1.condition)
        whenever(resources.getString(R.string.state_sold,
            condition,
            ITEM_1.soldQuantity)).thenReturn("Estado $condition / Vendidos ${ITEM_1.soldQuantity}")
        whenever(resources.getString(R.string.stock,
            ITEM_1.stock)).thenReturn("Stock Disponible ${ITEM_1.stock} unidades")
        val state = resources.getString(R.string.state_sold, condition, ITEM_1.soldQuantity)
        val price = FormatNumber.formatNumber(ITEM_1.price)
        val stock = resources.getString(R.string.stock, ITEM_1.stock)
        //WHEN
        itemPresenter.refreshButton(intent)
        //THEN
        verify(itemView).retrieveExtras(ITEM_1.title, state, price, ITEM_1.thumbnail, stock)
        verify(itemDataSource).setIdRecentlySeenItem(ITEM_1.id)
        verify(itemDataSource).setPermalinkRecentlySeenItem(ITEM_1.permaLink)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `refresh button is pressed and initComponent with getById duplicate item`() {
        //GIVEN
        getItemByIdSuccessfully()
        getItemDescriptionSuccessfully()
        dbInsertItemSuccessfully()
        getByIdSuccessfullyWithDuplicateItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn("NUEVO")
        whenever(resources.getString(R.string.newState)).thenReturn("new")
        val condition = itemPresenter.translateCondition(ITEM_1.condition)
        whenever(resources.getString(R.string.state_sold,
            condition,
            ITEM_1.soldQuantity)).thenReturn("Estado $condition / Vendidos ${ITEM_1.soldQuantity}")
        whenever(resources.getString(R.string.stock,
            ITEM_1.stock)).thenReturn("Stock Disponible ${ITEM_1.stock} unidades")
        val state = resources.getString(R.string.state_sold, condition, ITEM_1.soldQuantity)
        val price = FormatNumber.formatNumber(ITEM_1.price)
        val stock = resources.getString(R.string.stock, ITEM_1.stock)
        //WHEN
        itemPresenter.refreshButton(intent)
        //THEN
        verify(itemView).retrieveExtras(ITEM_1.title, state, price, ITEM_1.thumbnail, stock)
        verify(itemDataSource).setIdRecentlySeenItem(ITEM_1.id)
        verify(itemDataSource).setPermalinkRecentlySeenItem(ITEM_1.permaLink)
        verify(itemView).setDescription(DESCRIPTION)
        verify(itemView).navigateToSearch()
        verify(itemView).navigateToMeli()
        verify(itemView).navigateToSearch()
        verify(itemView).onBack()
    }

    @Test
    fun `translateCondition with new condition`() {
        //GIVEN
        val condition = NEW_CONDITION_ITEM
        whenever(resources.getString(R.string.newState)).thenReturn(NEW_CONDITION_ITEM)
        whenever(resources.getString(R.string.newStateSpanish)).thenReturn(NEW_STATE)
        //WHEN
        itemPresenter.translateCondition(condition)
        //THEN
        assertEquals(itemPresenter.translateCondition(condition), NEW_STATE)
    }

    @Test
    fun `translateCondition with used condition`() {
        //GIVEN
        val condition = USED_CONDITION_ITEM
        whenever(resources.getString(R.string.newState)).thenReturn(NEW_CONDITION_ITEM)
        whenever(resources.getString(R.string.usedState)).thenReturn(USED_STATE)
        //WHEN
        itemPresenter.translateCondition(condition)
        //THEN
        assertEquals(itemPresenter.translateCondition(condition), USED_STATE)
    }


    private fun dbInsertItemSuccessfully() {
        val success = argumentCaptor<() -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        whenever(itemDataSource.dbInsertItem(
            any(),
            success.capture(),
            error.capture()
        )
        ).thenAnswer {
            success.firstValue.invoke()
            mockDisposable
        }
    }

    private fun dbInsertItemUnsuccessfully() {
        val success = argumentCaptor<() -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseError = Mockito.mock(Throwable::class.java)
        whenever(itemDataSource.dbInsertItem(
            any(),
            success.capture(),
            error.capture()
        )
        ).thenAnswer {
            error.firstValue.invoke(responseError)
            mockDisposable
        }
    }

    private fun getItemByIdSuccessfully() {
        val success = argumentCaptor<(ProductResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        whenever(itemDataSource.getItemById(
            any(),
            success.capture(),
            error.capture()
        )).thenAnswer {
            success.firstValue.invoke(ITEM_1)
            mockDisposable
        }
    }

    private fun getItemByIdUnsuccessfully() {
        val success = argumentCaptor<(ProductResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseError = Mockito.mock(Throwable::class.java)
        whenever(itemDataSource.getItemById(
            any(),
            success.capture(),
            error.capture()
        )).thenAnswer {
            error.firstValue.invoke(responseError)
            mockDisposable
        }
    }

    private fun getByIdSuccessfullyWithNewItem() {
        val success = argumentCaptor<(Int) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val intResponse = ZERO
        whenever(itemDataSource.getById(
            any(),
            success.capture(),
            error.capture()
        )).thenAnswer {
            success.firstValue.invoke(intResponse)
            mockDisposable
        }
    }

    private fun getByIdSuccessfullyWithDuplicateItem() {
        val success = argumentCaptor<(Int) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val intResponse = ONE
        whenever(itemDataSource.getById(
            any(),
            success.capture(),
            error.capture()
        )).thenAnswer {
            success.firstValue.invoke(intResponse)
            mockDisposable
        }
    }

    private fun getByIdUnsuccessfully() {
        val success = argumentCaptor<(Int) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseError = Mockito.mock(Throwable::class.java)
        whenever(itemDataSource.getById(
            any(),
            success.capture(),
            error.capture()
        )).thenAnswer {
            error.firstValue.invoke(responseError)
            mockDisposable
        }
    }

    private fun getItemDescriptionSuccessfully() {
        val success = argumentCaptor<(DescriptionResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val descriptionResponse = Mockito.mock(DescriptionResponse::class.java)
        whenever(descriptionResponse.plainText).thenReturn(DESCRIPTION)
        whenever(itemDataSource.getItemDescription(
            any(),
            success.capture(),
            error.capture()
        )).thenAnswer {
            success.firstValue.invoke(descriptionResponse)
            mockDisposable
        }
    }

    private fun getItemDescriptionUnsuccessfully() {
        val success = argumentCaptor<(DescriptionResponse) -> Unit>()
        val error = argumentCaptor<(Throwable) -> Unit>()
        val responseError = Mockito.mock(Throwable::class.java)
        whenever(itemDataSource.getItemDescription(
            any(),
            success.capture(),
            error.capture()
        )).thenAnswer {
            error.firstValue.invoke(responseError)
            mockDisposable
        }
    }

    companion object {
        private val ITEM_1 =
            ProductResponse(1, "item1", "product1", "", "new", 0.0, 0, "product1.com", 0)
        const val DESCRIPTION = "this is a product"
        const val INTERNET_FAIL = "¡Parece que no hay internet!"
        const val SIMPLE_FAILED = "Ups algo salio mal, vuelve a intentarlo"
        const val ONE = 1
        const val TWO = 2
        const val ZERO = 0
        const val NEW_CONDITION_ITEM = "new"
        const val USED_CONDITION_ITEM = "used"
        const val NEW_STATE = "NUEVO"
        const val USED_STATE = "USADO"
        }
}
