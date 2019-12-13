package com.cinema.entract.cache.repository

import com.cinema.entract.data.repository.UserPreferencesRepo

actual class CinemaUserPreferencesRepo : UserPreferencesRepo {

    //TODO

    override fun isPromotionalEnabled(): Boolean = true

    override fun setPromotionalPreference(enabled: Boolean) = Unit

    override fun isOnlyOnWifi(): Boolean = false

    override fun setOnlyOnWifi(onlyOnWifi: Boolean) = Unit

    override fun getPrefThemeMode(): String = ""

    override fun getThemeMode(): Int = 0

    override fun setThemeMode(mode: String) = Unit
}
