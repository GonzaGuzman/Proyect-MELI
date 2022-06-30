package com.zalo.proyectmeli.database

import androidx.room.*
import com.zalo.proyectmeli.utils.models.SearchHistory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface SearchDAO {
    @Query("SELECT * FROM searchHistory ORDER BY orderId DESC")
    fun getAllSearch(): Single<List<SearchHistory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNewSearch(newSearch: SearchHistory): Completable

    @Query("DELETE FROM searchHistory")
    fun deleteSearchHistory(): Completable
}