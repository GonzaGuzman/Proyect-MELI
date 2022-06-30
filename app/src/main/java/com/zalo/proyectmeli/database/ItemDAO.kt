package com.zalo.proyectmeli.database

import androidx.room.*
import com.zalo.proyectmeli.utils.models.ProductResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ItemDAO {

    @Query("SELECT * FROM recentlySearch ORDER BY numberItem DESC")
    fun getItems(): Single<List<ProductResponse>>

    @Query("SELECT * FROM recentlySearch WHERE id =:id")
    fun getLastItem(id: String): Single<ProductResponse>

    @Query("SELECT COUNT(id) FROM recentlySearch WHERE id =:id")
    fun getById(id: String): Single<Int>

    @Update
    fun update(productResponse: ProductResponse): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productResponse: ProductResponse): Completable

    @Query("DELETE FROM recentlySearch")
    fun deleteHistory(): Completable
}
