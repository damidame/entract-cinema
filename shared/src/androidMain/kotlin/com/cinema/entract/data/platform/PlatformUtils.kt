package com.cinema.entract.data.platform

import com.cinema.entract.shared.BuildConfig

actual class PlatformUtils {
    actual fun isDebug(): Boolean = BuildConfig.DEBUG
    actual fun platformName(): String = "android"
}
