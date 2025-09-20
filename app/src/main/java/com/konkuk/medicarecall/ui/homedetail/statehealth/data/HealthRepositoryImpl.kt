package com.konkuk.medicarecall.ui.homedetail.statehealth.data

import android.util.Log
import com.konkuk.medicarecall.ui.homedetail.statehealth.model.HealthUiState
import java.time.LocalDate
import javax.inject.Inject

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