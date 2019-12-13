package com.cinema.entract.app.ext

import org.threeten.bp.ZonedDateTime

fun ZonedDateTime.toEpochMilliSecond(): Long = this.toEpochSecond() * 1000
