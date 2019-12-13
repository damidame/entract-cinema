package com.cinema.entract.data.platform

expect class PlatformUtils {
    fun isDebug(): Boolean
    fun platformName(): String
}
