
package com.konkuk.medicarecall.domain.model

data class Sleep(
    val date: String = "",
    val totalSleepHours: Int? = null,
    val totalSleepMinutes: Int? = null,
    val bedTime: String? = null,
    val wakeUpTime: String? = null,
)
