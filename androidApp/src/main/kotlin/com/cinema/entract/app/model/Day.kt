package com.cinema.entract.app.model

import com.cinema.entract.data.model.DayData
import org.threeten.bp.LocalDate
import kotlin.time.ExperimentalTime

@ExperimentalTime
data class Day(
    val date: LocalDate,
    val movies: List<Movie>
) {

    constructor(data: DayData) : this(
        LocalDate.parse(data.date),
        data.movies.map { Movie(it) }
    )
}
