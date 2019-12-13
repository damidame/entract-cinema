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

import com.cinema.entract.data.model.DateRangeData
import org.threeten.bp.LocalDate

data class DateParameters(
    val currentDate: LocalDate,
    val minimumDate: LocalDate?,
    val maximumDate: LocalDate?
) {

    constructor(currentDate: String, range: DateRangeData?) : this(
        LocalDate.parse(currentDate),
        range?.let { LocalDate.parse(it.minimumDate) },
        range?.let { LocalDate.parse(it.maximumDate) }
    )
}
