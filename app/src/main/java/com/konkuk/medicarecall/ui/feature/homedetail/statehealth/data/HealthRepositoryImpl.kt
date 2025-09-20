package com.konkuk.medicarecall.ui.feature.homedetail.statehealth.data

import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthUiState
import java.time.LocalDate
import javax.inject.Inject
import kotlin.collections.orEmpty
import kotlin.text.orEmpty

class HealthRepositoryImpl @Inject constructor(
    private val healthService: HealthService
) : HealthRepository {
    override suspend fun getHealthUiState(elderId: Int, date: LocalDate): Result<HealthUiState> =
        runCatching {
            val response = healthService.getDailyHealth(elderId, date.toString())
            HealthUiState(
                symptoms = response.symptomList.orEmpty(),
                symptomAnalysis = response.analysisComment.orEmpty(),
                isRecorded = response.symptomList!!.isNotEmpty() || !response.analysisComment.isNullOrBlank()
            )
        }


}
