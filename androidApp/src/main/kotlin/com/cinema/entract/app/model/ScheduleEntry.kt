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

import com.cinema.entract.app.ext.isTodayOrLater
import com.cinema.entract.app.ext.longFormatToUi
import com.cinema.entract.app.ext.sameMonth
import com.cinema.entract.app.ext.shortFormatToUi
import com.cinema.entract.data.model.WeekData
import org.threeten.bp.LocalDate
import kotlin.time.ExperimentalTime

sealed class ScheduleEntry

data class WeekHeader(val dateUi: String) : ScheduleEntry()
data class DayHeader(val dateUi: String, val date: LocalDate) : ScheduleEntry()
@ExperimentalTime
data class MovieEntry(val movie: Movie, val date: LocalDate) : ScheduleEntry()


@ExperimentalTime
fun List<WeekData>.mapToScheduleEntries(): List<ScheduleEntry> {
    val list = mutableListOf<ScheduleEntry>()
    map { Week(it) }
        .forEach { week ->
            val entries = mutableListOf<ScheduleEntry>()
            week.days
                .filter { it.movies.isNotEmpty() }
                .filter { it.date.isTodayOrLater() }
                .forEach { day ->
                    entries.add(DayHeader(day.date.longFormatToUi(), day.date))

                    day.movies.forEach { movie ->
                        entries.add(MovieEntry(movie, day.date))
                    }
                }
            if (entries.isNotEmpty()) {
                list.add(WeekHeader(formatWeekHeader(week)))
                list.addAll(entries)
            }
        }
    return list
}

@ExperimentalTime
private fun formatWeekHeader(week: Week): String =
    if (week.beginDay sameMonth week.endDay) {
        "Du ${week.beginDay.dayOfMonth} au ${week.endDay.shortFormatToUi()}"
    } else {
        "Du ${week.beginDay.shortFormatToUi()} au ${week.endDay.shortFormatToUi()}"
    }
