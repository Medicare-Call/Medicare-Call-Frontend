package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.HealthService
import com.konkuk.medicarecall.data.repository.HealthRepository
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthUiState
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Single

@Single
class HealthRepositoryImpl(
    private val healthService: HealthService,
) : HealthRepository {
    override suspend fun getHealthUiState(elderId: Int, date: LocalDate): Result<HealthUiState> =
        runCatching {
            val response = healthService.getDailyHealth(elderId, date.toString()).handleResponse()
            HealthUiState(
                symptoms = response.symptomList.orEmpty(),
                symptomAnalysis = response.analysisComment.orEmpty(),
                isRecorded = response.symptomList!!.isNotEmpty() || !response.analysisComment.isNullOrBlank(),
            )
        }
}
