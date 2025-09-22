package com.konkuk.medicarecall.ui.common.util

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
        // "yyyyMMdd" 형식으로 날짜를 파싱
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val date = LocalDate.parse(this, formatter)

        // 기준 날짜 생성
        val minDate = LocalDate.of(1900, 1, 1)

        // 파싱된 날짜가 기준 날짜보다 이전이 아닌지 확인 + 현재 날짜보다 이후 날짜가 아닌지 확인
        !date.isBefore(minDate) && !date.isAfter(LocalDate.now())

    } catch (e: DateTimeParseException) {
        // 날짜 형식이 올바르지 않거나 존재할 수 없는 날짜(예: 20250230)이면 false 반환
        false
    }
}
