package com.zalo.proyectmeli.utils.applicationClass

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.zalo.proyectmeli.BuildConfig.ITEM_DATABASE
import com.zalo.proyectmeli.database.ItemDatabase

class MLApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences("mlPreferences", 0)

        database = Room.databaseBuilder(this, ItemDatabase::class.java, ITEM_DATABASE).build()
    }

    companion object {
        lateinit var database: ItemDatabase
        lateinit var preferences: SharedPreferences
    }
}