package com.cinema.entract.data.platform

import kotlinx.coroutines.CoroutineDispatcher

expect class PlatformUtils {
    val applicationDispatcher: CoroutineDispatcher
    fun isDebug(): Boolean
    fun platformName(): String
}
