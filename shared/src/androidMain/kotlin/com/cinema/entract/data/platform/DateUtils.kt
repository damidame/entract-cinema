package com.cinema.entract.data.platform

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

actual class DateUtils {
    actual fun todayUtc(): String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
}