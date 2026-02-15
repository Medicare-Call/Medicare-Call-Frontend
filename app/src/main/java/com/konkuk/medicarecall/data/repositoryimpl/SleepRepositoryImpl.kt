package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.SleepService
import com.konkuk.medicarecall.data.mapper.toModel
import com.konkuk.medicarecall.data.repository.SleepRepository
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Sleep
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Single

@Single
class SleepRepositoryImpl(
    private val sleepService: SleepService,
) : SleepRepository {
    override suspend fun getSleepData(
        elderId: Long,
        date: LocalDate,
    ): Result<Sleep> = runCatching {
        sleepService.getDailySleep(
            elderId = elderId,
            date = date.toString(),
        ).handleResponse().toModel()
    }
}
