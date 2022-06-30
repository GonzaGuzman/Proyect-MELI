package com.zalo.proyectmeli.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zalo.proyectmeli.utils.models.ProductResponse
import com.zalo.proyectmeli.utils.models.SearchHistory

@Database(
    entities = [
        ProductResponse::class,
        SearchHistory::class
    ],
    version = 1
)

abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDAO
    abstract fun searchDao(): SearchDAO
}