package com.zalo.proyectmeli.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zalo.proyectmeli.network.models.ProductResponse

@Database(
    entities = [ProductResponse::class],
    version = 1
)

abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDAO
}