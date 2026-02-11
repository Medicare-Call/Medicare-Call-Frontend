package com.konkuk.medicarecall.domain.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

// 현재 시스템 날짜 반환
fun LocalDate.Companion.today(): LocalDate {
    val javaDate = java.time.LocalDate.now()
    return LocalDate(javaDate.year, javaDate.monthValue, javaDate.dayOfMonth)
}

/**
 * 주어진 요일이거나 이전 날짜 중 해당 요일을 찾음
 * Java Time API의 TemporalAdjusters.previousOrSame(DayOfWeek) 대체
 */
fun LocalDate.previousOrSame(dayOfWeek: DayOfWeek): LocalDate {
    val daysToSubtract = (this.dayOfWeek.ordinal - dayOfWeek.ordinal + 7) % 7
    return this.minus(daysToSubtract, DateTimeUnit.DAY)
}

/**
 * 주어진 날짜를 기준으로 해당 주의 날짜 리스트를 반환(일요일 시작)
 * @return 일요일부터 토요일까지의 LocalDate 리스트
 */
fun LocalDate.getCurrentWeekDates(): List<LocalDate> {
    val startOfWeek = this.previousOrSame(DayOfWeek.SUNDAY)
    return (0..6).map { startOfWeek.plus(it, DateTimeUnit.DAY) }
}
