package com.cinema.entract.app.model

import com.cinema.entract.data.model.WeekData
import org.threeten.bp.LocalDate
import kotlin.time.ExperimentalTime

@ExperimentalTime
data class Week(
    val beginDay: LocalDate,
    val endDay: LocalDate,
    val days: List<Day>,
    val hasMovies: Boolean
) {

    constructor(data: WeekData) : this(
        LocalDate.parse(data.beginDay),
        LocalDate.parse(data.endDay),
        data.days.map { Day(it) },
        data.hasMovies
    )
}

