package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.MentalService
import com.konkuk.medicarecall.data.repository.MentalRepository
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel.MentalUiState
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class MentalRepositoryImpl(
    private val mentalService: MentalService,
) : MentalRepository {

    override suspend fun getMentalUiState(
        elderId: Int,
        date: LocalDate,
    ): MentalUiState {
        val response = mentalService.getDailyMental(
            elderId,
            date.toString(),
        )

        return if (response.isSuccessful) {
            val comments = response.body()?.commentList.orEmpty()

            MentalUiState(
                mentalSummary = comments,
                isRecorded = comments.isNotEmpty(),
            )
        } else {
            if (response.code == 404) {
                MentalUiState.EMPTY
            } else {
                error("Failed to fetch mental data: ${response.code}")
            }
        }
    }
}
