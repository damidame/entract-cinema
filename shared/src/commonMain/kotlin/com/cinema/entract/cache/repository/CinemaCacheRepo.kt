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

package com.cinema.entract.cache.repository

import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.repository.CacheRepo

class CinemaCacheRepo : CacheRepo {

    private val moviesMap = mutableMapOf<String, List<MovieData>>()
    private var schedule: List<WeekData>? = null
    private var dateRange: DateRangeData? = null
    private var promotionalUrl: String? = null

    override fun getMovies(date: String): List<MovieData>? = moviesMap[date]

    override fun cacheMovies(date: String, movies: List<MovieData>): List<MovieData> =
        movies.also { moviesMap[date] = movies }

    override fun getSchedule(): List<WeekData>? = schedule

    override fun cacheSchedule(weeks: List<WeekData>): List<WeekData> =
        weeks.also { schedule = weeks }

    override fun getDateRange(): DateRangeData? = dateRange

    override fun cacheDateRange(range: DateRangeData?): DateRangeData? =
        range.also { dateRange = range }

    override fun getPromotionalUrl(): String? = promotionalUrl

    override fun cachePromotionalUrl(url: String): String =
        url.also { promotionalUrl = url }
}