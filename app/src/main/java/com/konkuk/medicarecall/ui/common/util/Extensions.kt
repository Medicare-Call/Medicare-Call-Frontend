package com.konkuk.medicarecall.ui.common.util

import com.konkuk.medicarecall.domain.util.now
import kotlinx.datetime.LocalDate

fun List<Int>.averageOrNull(): Double? {
    return if (this.isEmpty()) null else this.average()
}

fun String.formatAsDate(): String {
    return try {
        val inputFormatter = LocalDate.Format {
            year()          // yyyy (4자리 연도)
            monthNumber()   // MM (2자리 월)
            day()
        }
        val date = LocalDate.parse(this, inputFormatter)
        date.toString()

    } catch (_: IllegalArgumentException) {
        ""
    }
}

fun String.isValidDate(): Boolean {
    if (this.length != 8) return false

    return try {
        val year = this.substring(0, 4).toInt()
        val month = this.substring(4, 6).toInt()
        val day = this.substring(6, 8).toInt()

        val date = LocalDate(year, month, day)

        val minDate = LocalDate(1900, 1, 1)
        val today = LocalDate.now()

        date in minDate..today

    } catch (_: NumberFormatException) {
        false
    } catch (_: IllegalArgumentException) {
        false
    }
}
