package com.konkuk.medicarecall.domain.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

val LocalDate.Companion.MIN: LocalDate
    get() = LocalDate(-999_999_999, 1, 1)

val LocalDate.Companion.MAX: LocalDate
    get() = LocalDate(999_999_999, 12, 31)

@OptIn(ExperimentalTime::class)
fun LocalDate.Companion.now(): LocalDate =
    Clock.System.todayIn(TimeZone.currentSystemDefault())

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDate.weekStart(): LocalDate {
    val daysFromMonday = this.dayOfWeek.isoDayNumber - 1
    return this.minus(daysFromMonday, DateTimeUnit.DAY)
}

fun LocalDate.getWeekRange(): Pair<LocalDate, LocalDate> {
    val start = this.weekStart()
    val end = start.plus(6, DateTimeUnit.DAY) // 월요일 + 6일 = 일요일

    return start to end
}

fun LocalDate.getCurrentWeekDates(): List<LocalDate> {
    val daysFromSunday = (this.dayOfWeek.ordinal - DayOfWeek.SUNDAY.ordinal + 7) % 7
    val startOfWeek = this.minus(daysFromSunday, DateTimeUnit.DAY)

    return (0..6).map { dayOffset ->
        startOfWeek.plus(dayOffset, DateTimeUnit.DAY)
    }
}
