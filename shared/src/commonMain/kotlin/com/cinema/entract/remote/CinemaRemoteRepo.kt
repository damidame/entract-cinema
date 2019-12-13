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

package com.cinema.entract.remote

import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.platform.PlatformUtils
import com.cinema.entract.data.repository.RemoteRepo
import com.cinema.entract.remote.api.CinemaApi

class CinemaRemoteRepo(
    private val service: CinemaApi,
    private val platformUtils: PlatformUtils
) : RemoteRepo {

    override suspend fun getMovies(date: String): List<MovieData> =
        service.getMovies(date).map { it.mapToData() }

    override suspend fun getSchedule(): List<WeekData> =
        service.getSchedule().map { it.mapToData() }

    override suspend fun getPromotionalUrl(): String =
        service.getPromotional().mapToData()

    override suspend fun getDateRange(): DateRangeData? =
        service.getParameters().periode.mapToData()

    override suspend fun registerNotifications(token: String) {
        service.registerNotifications(platformUtils.platformName(), token)
    }

    override suspend fun tagSchedule() {
        service.tagSchedule()
    }

    override suspend fun tagPromotional() {
        service.tagPromotional()
    }

    override suspend fun tagDetails(sessionId: String) {
        service.tagDetails(sessionId)
    }

    override suspend fun tagCalendar(sessionId: String) {
        service.tagCalendar(sessionId)
    }
}
