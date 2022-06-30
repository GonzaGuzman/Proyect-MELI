package com.zalo.proyectmeli

import android.content.Intent
import android.content.res.Resources
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.zalo.proyectmeli.datasource.item.ItemDatasource
import com.zalo.proyectmeli.utils.models.ProductResponse
import com.zalo.proyectmeli.presenter.item.ItemPresenter
import com.zalo.proyectmeli.presenter.item.ItemState
import com.zalo.proyectmeli.presenter.item.ItemView
import com.zalo.proyectmeli.utils.*
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
class ItemPresenterTest {
    @Mock
    private lateinit var itemDataSource: ItemDatasource

    @Mock
    private lateinit var itemView: ItemView

    @Mock
    private lateinit var mockDisposable: Disposable

    @Mock
    private lateinit var resources: Resources

    private val itemState = ItemState()
    private lateinit var itemPresenter: ItemPresenter

    private val out = ByteArrayOutputStream()
    private val originalOut = System.out

    @Before
    fun setup() {
        itemPresenter = ItemPresenter(itemView, itemDataSource, itemState, resources)

        System.setOut(PrintStream(out))

    }

    @After
    fun restoreInitialStreams() {
        System.setOut(originalOut)
    }

    @Test
    fun `initComponent call`() {
        //GIVEN
        getByIdSuccessfullyWithDuplicateItem()
        val intent = Mockito.mock(Intent::class.java)
        whenever(itemDataSource.getCountItems()).thenReturn(ZERO)
        whenever(intent.getStringExtra(ITEM_ID)).thenReturn(ID_ITEM)
        whenever(intent.getStringExtra(ITEM_TITLE).toString()).thenReturn(TITLE_ITEM)
        whenever(intent.getDoubleExtra(ITEM_PRICE, 0.0)).thenReturn(PRICE_ITEM)
        whenever(intent.getIntExtra(ITEM_SOLD_QUANTITY, 0)).thenReturn(SOLD_ITEM)
        whenever(intent.getStringExtra(ITEM_THUMBNAIL).toString()).thenReturn(IMAGE_ITEM)
        whenever(intent.getStringExtra(ITEM_PERMALINK).toString()).thenReturn(LINK_ITEM)
        whenever(intent.getStringExtra(ITEM_CONDITION).toString()).thenReturn(NEW_CONDITION_ITEM)
        whenever(resources.getString(R.string.usedState)).thenReturn(NEW_CONDITION_ITEM)

        //WHEN
        itemPresenter.initComponent(intent)

        //THEN
        assertEquals(itemState.numberItem.get(), ZERO)
        assertEquals(itemState.id.get(), ID_ITEM)
        assertEquals(itemState.title.get(), TITLE_ITEM)
        assertEquals(itemState.price.get(), PRICE_ITEM)
        assertEquals(itemState.soldQuantity.get(), SOLD_ITEM)
        assertEquals(itemState.thumbnail.get(), IMAGE_ITEM)
        assertEquals(itemState.permaLink.get(), LINK_ITEM)
        assertEquals(itemState.condition.get(), NEW_CONDITION_ITEM)
        verify(itemDataSource).setIdRecentlySeenItem(ID_ITEM)
        verify(itemView).retrieverExtras(any())
        verify(itemView).textSearch()
    }

    @Test
    fun `setState by intent data`() {
        //GIVEN
        val intent = Mockito.mock(Intent::class.java)
        whenever(itemDataSource.getCountItems()).thenReturn(ZERO)
        whenever(intent.getStringExtra(ITEM_ID)).thenReturn(ID_ITEM)
        whenever(intent.getStringExtra(ITEM_TITLE).toString()).thenReturn(TITLE_ITEM)
        whenever(intent.getDoubleExtra(ITEM_PRICE, 0.0)).thenReturn(PRICE_ITEM)
        whenever(intent.getIntExtra(ITEM_SOLD_QUANTITY, 0)).thenReturn(SOLD_ITEM)
        whenever(intent.getStringExtra(ITEM_THUMBNAIL).toString()).thenReturn(IMAGE_ITEM)
        whenever(intent.getStringExtra(ITEM_PERMALINK).toString()).thenReturn(LINK_ITEM)
        whenever(intent.getStringExtra(ITEM_CONDITION).toString()).thenReturn(NEW_CONDITION_ITEM)
        whenever(resources.getString(R.string.usedState)).thenReturn(NEW_CONDITION_ITEM)

        //WHEN
        itemPresenter.setState(intent)
        //THEN
        assertEquals(itemState.numberItem.get(), ZERO)
        assertEquals(itemState.id.get(), ID_ITEM)
        assertEquals(itemState.title.get(), TITLE_ITEM)
        assertEquals(itemState.price.get(), PRICE_ITEM)
        assertEquals(itemState.soldQuantity.get(), SOLD_ITEM)
        assertEquals(itemState.thumbnail.get(), IMAGE_ITEM)
        assertEquals(itemState.permaLink.get(), LINK_ITEM)
        assertEquals(itemState.condition.get(), NEW_CONDITION_ITEM)
        verify(itemDataSource).setIdRecentlySeenItem(ID_ITEM)
    }

    @Test
    fun `translateCondition with "new" condition`() {
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
    fun `translateCondition with "used" condition`() {
        //GIVEN
        val condition = USED_CONDITION_ITEM
        whenever(resources.getString(R.string.newState)).thenReturn(NEW_CONDITION_ITEM)
        whenever(resources.getString(R.string.usedState)).thenReturn(USED_STATE)
        //WHEN
        itemPresenter.translateCondition(condition)
        //THEN
        assertEquals(itemPresenter.translateCondition(condition), USED_STATE)
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
    fun `getState in var productResponse`() {
        //GIVEN
        itemState.numberItem.set(ZERO)
        itemState.id.set(ID_ITEM)
        itemState.title.set(TITLE_ITEM)
        itemState.price.set(PRICE_ITEM)
        itemState.soldQuantity.set(SOLD_ITEM)
        itemState.thumbnail.set(IMAGE_ITEM)
        itemState.permaLink.set(LINK_ITEM)
        itemState.condition.set(NEW_CONDITION_ITEM)
        //WHEN
        val productResponse: ProductResponse = itemPresenter.getState()
        //THEN
        assertEquals(productResponse.numberItem, ZERO)
        assertEquals(productResponse.id, ID_ITEM)
        assertEquals(productResponse.title, TITLE_ITEM)
        assertEquals(productResponse.price, PRICE_ITEM)
        assertEquals(productResponse.soldQuantity, SOLD_ITEM)
        assertEquals(productResponse.thumbnail, IMAGE_ITEM)
        assertEquals(productResponse.permaLink, LINK_ITEM)
        assertEquals(productResponse.condition, NEW_CONDITION_ITEM)

    }

    @Test
    fun `saveItem successfully`() {
        //GIVEN
        dbInsertItemSuccessfully()
        val item = Mockito.mock(ProductResponse::class.java)
        whenever(resources.getString(R.string.saveSuccesfullyItem)).thenReturn(PRINT_RESPONSE)
        //WHEN
        itemPresenter.saveItem(item)
        //THEN
        assertEquals(PRINT_RESPONSE, out.toString().trim())
    }

    @Test
    fun `saveItem unsuccessfully`() {
        //GIVEN
        dbInsertItemUnsuccessfully()
        val item = Mockito.mock(ProductResponse::class.java)
        //WHEN
        itemPresenter.saveItem(item)
        //THEN
        assertEquals(INSERT_FAIL, out.toString().trim())
    }


    @Test
    fun `success validateAndSaveItem with nonexistent itemId in database `() {
        //GIVEN
        getByIdSuccessfullyWithNewItem()
        dbInsertItemSuccessfully()
        val item = Mockito.mock(ProductResponse::class.java)
        whenever(item.id).thenReturn(ITEM_ID)
        whenever(resources.getString(R.string.saveSuccesfullyItem)).thenReturn(PRINT_RESPONSE)
        //WHEN
        itemPresenter.validateAndSaveInDb(item)
        //THEN
        assertEquals(PRINT_RESPONSE, out.toString().trim())
    }

    @Test
    fun `success validateAndSaveItem with duplicate itemId in database `() {
        //GIVEN
        getByIdSuccessfullyWithDuplicateItem()
        val item = Mockito.mock(ProductResponse::class.java)
        whenever(item.id).thenReturn(ITEM_ID)
        whenever(resources.getString(R.string.existing_id_in_database)).thenReturn(PRINT_DUPLICATE)
        //WHEN
        itemPresenter.validateAndSaveInDb(item)
        //THEN
        assertEquals(PRINT_DUPLICATE, out.toString().trim())
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
        assertEquals(ITEM_FAIL, out.toString().trim())
    }

    @Test
    fun `navigateToMELI`() {
        //GIVEN
        //WHEN
        itemPresenter.navigateToMELI()
        //THEN
        verify(itemView).navigateToMELI(any())
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
        whenever(responseError.message).thenReturn(INSERT_FAIL)
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
        whenever(responseError.message).thenReturn(ITEM_FAIL)
        whenever(itemDataSource.getById(
            any(),
            success.capture(),
            error.capture()
        )).thenAnswer {
            error.firstValue.invoke(responseError)
            mockDisposable
        }
    }


    companion object {

        const val INSERT_FAIL = "FALLA AL INSERTAR ITEM"
        const val ITEM_FAIL = "FALLA: no se obtuvo item"
        const val ONE = 1
        const val TWO = 2
        const val ZERO = 0
        const val ID_ITEM = "idItem"
        const val TITLE_ITEM = "titleItem"
        const val PRICE_ITEM = 10.10
        const val SOLD_ITEM = 5
        const val IMAGE_ITEM = "phone"
        const val LINK_ITEM = "phoneLink"
        const val NEW_CONDITION_ITEM = "new"
        const val USED_CONDITION_ITEM = "used"
        const val NEW_STATE = "NUEVO"
        const val USED_STATE = "USADO"
        const val PRINT_RESPONSE = "save successfully item in database"
        const val PRINT_DUPLICATE = "Existing ID in database"
    }

}