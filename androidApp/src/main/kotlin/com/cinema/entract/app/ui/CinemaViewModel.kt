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

package com.cinema.entract.app.ui

import com.cinema.entract.app.ext.formatToUtc
import com.cinema.entract.app.ext.toEpochMilliSecond
import com.cinema.entract.app.model.DateParameters
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.app.model.mapToScheduleEntries
import com.cinema.entract.core.ui.BaseViewModel
import com.cinema.entract.data.interactor.CinemaUseCase
import io.uniflow.core.flow.UIEvent
import io.uniflow.core.flow.UIState
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit
import timber.log.Timber
import kotlin.time.ExperimentalTime

@ExperimentalTime
class CinemaViewModel(private val useCase: CinemaUseCase) : BaseViewModel() {

    init {
        setState { CinemaState.Init }
    }

    override suspend fun onError(error: Exception) {
        Timber.e(error)
        setState { CinemaState.Error(error) }
    }

    fun loadMovies() {
        setState { UIState.Loading }
        setState {
            val movies = useCase.getMovies()
            val date = useCase.getDate()
            val dateRange = useCase.getDateRange()
            CinemaState.OnScreen(
                movies.map { Movie(it) },
                DateParameters(date, dateRange)
            )
        }
    }

    fun selectDate(date: LocalDate) {
        useCase.setDate(date.formatToUtc())
    }

    fun loadSchedule() {
        setState { UIState.Loading }
        setState {
            val schedule = useCase.getSchedule()
            val date = useCase.getDate()
            CinemaState.Schedule(
                LocalDate.parse(date),
                schedule.mapToScheduleEntries()
            )
        }
    }

    fun loadMovieDetails(movie: Movie) {
        setState { UIState.Loading }
        setState(
            {
                val retrievedMovie = useCase.getMovie(movie.mapToData())
                val date = useCase.getDate()
                CinemaState.Details(
                    LocalDate.parse(date),
                    Movie(retrievedMovie)
                )
            },
            {
                CinemaState.Error(it, movie)
            }
        )
    }

    fun loadPromotional() {
        setState {
            val url = useCase.getEventUrl()
            url?.let { sendEvent(CinemaEvent.Promotional(it)) }
        }
    }

    fun getSessionSchedule(movie: Movie): Pair<Long, Long> {
        val beginDateTime = ZonedDateTime.of(
            movie.date.year,
            movie.date.month.value,
            movie.date.dayOfMonth,
            movie.schedule.hour,
            movie.schedule.minute,
            0,
            0,
            ZoneId.systemDefault()
        )

        val endTime = movie.schedule.plus(movie.duration.inMinutes.toLong(), ChronoUnit.MINUTES)
        val endDateTime = ZonedDateTime.of(
            movie.date.year,
            movie.date.month.value,
            movie.date.dayOfMonth,
            endTime.hour,
            endTime.minute,
            0,
            0,
            ZoneId.systemDefault()
        )

        return beginDateTime.toEpochMilliSecond() to endDateTime.toEpochMilliSecond()
    }
}

@ExperimentalTime
sealed class CinemaState : UIState() {
    object Init : CinemaState()

    data class OnScreen(
        val movies: List<Movie>,
        val dateParams: DateParameters
    ) : CinemaState()

    data class Schedule(val date: LocalDate, val schedule: List<ScheduleEntry>) : CinemaState()

    data class Details(val date: LocalDate, val movie: Movie) : CinemaState()

    data class Error(val error: Throwable, val movie: Movie? = null) : CinemaState()
}

sealed class CinemaEvent : UIEvent() {
    data class Promotional(val url: String) : CinemaEvent()
}
