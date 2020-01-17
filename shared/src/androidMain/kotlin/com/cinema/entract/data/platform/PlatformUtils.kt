package com.cinema.entract.data.platform

import com.cinema.entract.shared.BuildConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class PlatformUtils {
    actual val applicationDispatcher: CoroutineDispatcher = Dispatchers.Default
    actual fun isDebug(): Boolean = BuildConfig.DEBUG
    actual fun platformName(): String = "android"
}
