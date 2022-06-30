package com.zalo.proyectmeli.utils.sharedPreferences

import com.zalo.proyectmeli.utils.appController.AppController

class SharedPreferencesML {
    var countItem: Int
        get() = prefs.getInt(COUNT_ITEM, 0)
        set(value) = prefs.edit().putInt(COUNT_ITEM, value).apply()

    var idRecentlySeen: String
        get() = prefs.getString(ID_RECENTLY_SEEN, "") ?: ""
        set(value) = prefs.edit().putString(ID_RECENTLY_SEEN, value).apply()

    var permaLinkRecentlySeen: String
        get() = prefs.getString(PERMALINK_RECENTLY_SEEN, "") ?: ""
        set(value) = prefs.edit().putString(PERMALINK_RECENTLY_SEEN, value).apply()

    var searchPosition: Int
        get() = prefs.getInt(SEARCH_POSITION, 0)
        set(value) = prefs.edit().putInt(SEARCH_POSITION, value).apply()

    companion object {
        val prefs = AppController.preferences
        const val COUNT_ITEM = "countItem"
        const val ID_RECENTLY_SEEN = "idRecentlySeen"
        const val PERMALINK_RECENTLY_SEEN = "permalinkRecentlySeen"
        const val SEARCH_POSITION = "searchPosition"
    }
}
