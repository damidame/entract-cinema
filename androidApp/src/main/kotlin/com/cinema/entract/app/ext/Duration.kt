package com.cinema.entract.app.ext

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun Duration.formatToUi(): String = toComponents { hours, minutes, _, _ ->
    "${hours}h${minutes.toString().padStart(2, '0')}"
}
