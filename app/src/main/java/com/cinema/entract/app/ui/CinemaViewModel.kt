/*
 * Copyright 2018 Stéphane Baiget
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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cinema.entract.app.mapper.DateRangeMapper
import com.cinema.entract.app.mapper.MovieMapper
import com.cinema.entract.app.mapper.ScheduleMapper
import com.cinema.entract.app.model.DateRange
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.core.ui.Error
import com.cinema.entract.core.ui.Event
import com.cinema.entract.core.ui.Loading
import com.cinema.entract.core.ui.ScopedViewModel
import com.cinema.entract.core.ui.State
import com.cinema.entract.core.ui.Success
import com.cinema.entract.data.ext.longFormatToUi
import com.cinema.entract.data.interactor.CinemaUseCase
import kotlinx.coroutines.coroutineScope
import org.threeten.bp.LocalDate
import timber.log.Timber

class CinemaViewModel(
    private val useCase: CinemaUseCase,
    private val movieMapper: MovieMapper,
    private val scheduleMapper: ScheduleMapper,
    private val dateRangeMapper: DateRangeMapper
) : ScopedViewModel() {

    private val onScreenState = MutableLiveData<State<List<Movie>>>()
    private val scheduleState = MutableLiveData<State<List<ScheduleEntry>>>()
    private val detailedMovie = MutableLiveData<Movie>()
    private val eventUrl = MutableLiveData<Event<String>>()
    private val currentDate = MutableLiveData<String>()

    fun getCurrentDate(): LiveData<String> = currentDate

    fun getOnScreenState(): LiveData<State<List<Movie>>> {
        onScreenState.value ?: retrieveMovies()
        return onScreenState
    }

    fun retrieveMovies(date: LocalDate) {
        useCase.selectDate(date)
        retrieveMovies()
    }

    fun retrieveTodayMovies() {
        retrieveMovies(LocalDate.now())
    }

    fun retrieveMovies() {
        currentDate.postValue(useCase.getDate().longFormatToUi())
        onScreenState.postValue(Loading())
        launchAsync(::loadOnScreen, ::onLoadOnScreenError)
    }

    fun getDateRange(): DateRange? = dateRangeMapper.mapToUi(useCase.dateRange)

    private suspend fun loadOnScreen() = coroutineScope {
        val movies = useCase.getMovies().map { movieMapper.mapToUi(it) }
        onScreenState.postValue(Success(movies))
    }

    private fun onLoadOnScreenError(throwable: Throwable) {
        Timber.e(throwable)
        onScreenState.postValue(Error(throwable))
    }

    fun getScheduleState(): LiveData<State<List<ScheduleEntry>>> {
        scheduleState.value ?: retrieveSchedule()
        return scheduleState
    }

    fun retrieveSchedule() {
        scheduleState.postValue(Loading())
        launchAsync(::loadSchedule, ::onLoadScheduleError)
    }

    private suspend fun loadSchedule() {
        val schedule = useCase.getSchedule()
        scheduleState.postValue(Success(scheduleMapper.mapToUi(schedule)))
    }

    private fun onLoadScheduleError(throwable: Throwable) {
        Timber.e(throwable)
        scheduleState.postValue(Error(throwable))
    }

    fun getDetailedMovie(): LiveData<Movie> = detailedMovie

    fun selectMovie(movie: Movie) = detailedMovie.postValue(movie)

    fun getEventUrl(): LiveData<Event<String>> {
        eventUrl.value ?: launchAsync(::loadEventUrl, ::onLoadEventUrlError)
        return eventUrl
    }

    private suspend fun loadEventUrl() = coroutineScope {
        val url = useCase.getEventUrl()
        eventUrl.postValue(Event(url))
    }

    private fun onLoadEventUrlError(throwable: Throwable) {
        Timber.e("Event URL cannot be loaded: ${throwable.message}")
        eventUrl.postValue(Event(""))
    }
}