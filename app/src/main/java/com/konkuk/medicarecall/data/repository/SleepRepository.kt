package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Sleep
import kotlinx.datetime.LocalDate

interface SleepRepository {
    suspend fun getSleepData(elderId: Long, date: LocalDate): Result<Sleep>
}
