package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.HealthService
import com.konkuk.medicarecall.data.repository.HealthRepository
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthUiState
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class HealthRepositoryImpl(
    private val healthService: HealthService,
) : HealthRepository {
    override suspend fun getHealthUiState(elderId: Int, date: LocalDate): Result<HealthUiState> =
        runCatching {
            val response = healthService.getDailyHealth(elderId, date.toString())
            HealthUiState(
                symptoms = response.symptomList.orEmpty(),
                symptomAnalysis = response.analysisComment.orEmpty(),
                isRecorded =
                    !response.symptomList.isNullOrEmpty() || !response.analysisComment.isNullOrBlank(),
            )
        }
}
