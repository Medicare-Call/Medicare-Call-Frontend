package com.konkuk.medicarecall.ui.common.util

import android.util.Log
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun List<Int>.averageOrNull(): Double? {
    return if (this.isEmpty()) null else this.average()
}

fun String.formatAsDate(): String {
    // String 자체가 "yyyyMMdd" 형식이므로, this를 사용합니다.
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val date = LocalDate.parse(this, inputFormatter)

        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        date.format(outputFormatter)
    } catch (e: DateTimeParseException) {
        // 형식이 잘못된 경우 null 반환
        ""
    }
}

fun String.isValidDate(): Boolean {
    if (this.length != 8) return false

    return try {
        val year = this.substring(0, 4).toInt()
        val month = this.substring(4, 6).toInt()
        val day = this.substring(6, 8).toInt()

        // 월 범위 확인
        if (month !in 1..12) return false

        // 윤년 판정
        val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

        // 각 월의 최대 일수
        val maxDay = when (month) {
            2 -> if (isLeapYear) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }

        // 일 범위 확인
        if (day !in 1..maxDay) return false

        // 날짜 객체 생성
        val date = LocalDate.of(year, month, day)
        val minDate = LocalDate.of(1900, 1, 1)

        // 날짜 범위 확인
        !date.isBefore(minDate) && !date.isAfter(LocalDate.now())
    } catch (e: NumberFormatException) {
        Log.w("Extensions", "Invalid date format: number parsing failed", e)
        false
    } catch (e: DateTimeException) {
        Log.w("Extensions", "Invalid date: date validation failed", e)
        false
    }
}
