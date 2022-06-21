package com.zalo.proyectmeli.sharedPreferences

import com.zalo.proyectmeli.utils.applicationClass.MLApplication

class SharedPreferencesML {
    var countItem: Int
        get() = prefs.getInt(COUNT_ITEM, 0)
        set(value) = prefs.edit().putInt(COUNT_ITEM, value).apply()

    var idRecentlySeen: String
        get() = prefs.getString(ID_RECENTLY_SEEN, "") ?: ""
        set(value) = prefs.edit().putString(ID_RECENTLY_SEEN, value).apply()

    companion object {
        val prefs = MLApplication.preferences
        const val COUNT_ITEM = "countItem"
        const val ID_RECENTLY_SEEN = "idRecentlySeen"
    }
}
