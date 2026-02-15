package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Health
import kotlinx.datetime.LocalDate

interface HealthRepository {
    suspend fun getHealth(elderId: Long, date: LocalDate): Result<Health>
}
