/*
 * Copyright 2019 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cinema.entract.app.model

import com.cinema.entract.app.ext.formatToUi
import com.cinema.entract.app.ext.formatToUtc
import com.cinema.entract.data.model.MovieData
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.minutes

@ExperimentalTime
data class Movie constructor(
    val sessionId: String,
    val movieId: String,
    val title: String,
    val date: LocalDate,
    val schedule: LocalTime,
    val isThreeDimension: Boolean,
    val isOriginalVersion: Boolean,
    val isArtMovie: Boolean,
    val isUnderTwelve: Boolean,
    val isExplicitContent: Boolean,
    val coverUrl: String,
    val duration: Duration,
    val yearOfProduction: String,
    val genre: String,
    val director: String,
    val cast: String,
    val synopsis: String,
    val teaserId: String,
    val nextMovies: List<Movie>
) {

    constructor(model: MovieData) : this(
        model.sessionId,
        model.movieId,
        model.title,
        LocalDate.parse(model.date) ?: error("Unknown date"),
        getSchedule(model.schedule) ?: error("Incorrect schedule"),
        model.isThreeDimension,
        model.isOriginalVersion,
        model.isArtMovie,
        model.isUnderTwelve,
        model.isExplicitContent,
        model.coverUrl,
        getDuration(model.duration),
        model.yearOfProduction,
        model.genre,
        model.director,
        model.cast,
        model.synopsis,
        model.teaserId,
        model.nextMovies.map { Movie(it) }
    )

    fun mapToData(): MovieData = MovieData(
        sessionId,
        movieId,
        title,
        date.formatToUtc(),
        schedule.toString(),
        isThreeDimension,
        isOriginalVersion,
        isArtMovie,
        isUnderTwelve,
        isExplicitContent,
        coverUrl,
        duration.formatToUi(),
        yearOfProduction,
        genre,
        director,
        cast,
        synopsis,
        teaserId,
        nextMovies.map { it.mapToData() }
    )

    companion object {
        private fun getSchedule(literalSchedule: String?): LocalTime? =
            literalSchedule
                ?.split("h")
                ?.map { Integer.parseInt(it) }
                ?.let {
                    return LocalTime.of(it[0], it[1])
                }

        private fun getDuration(literalDuration: String?): Duration {
            var duration = Duration.ZERO
            literalDuration
                .takeIf { it?.isNotEmpty() == true }
                ?.split("h")
                ?.map { Integer.parseInt(it).toLong() }
                ?.forEachIndexed { index, time ->
                    duration = when (index) {
                        0 -> duration + time.hours
                        1 -> duration + time.minutes
                        else -> error("Incorrect format")
                    }
                }
            return duration
        }
    }
}
