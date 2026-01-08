package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
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
        return runCatching {
            mentalService.getDailyMental(
                elderId,
                date.toString(),
            )
        }.fold(
            onSuccess = { dto ->
                val comments = dto.commentList.orEmpty()
                Log.d("MENTAL", "comments=$comments")

                MentalUiState(
                    mentalSummary = comments,
                    isRecorded = comments.isNotEmpty(),
                )
            },
            onFailure = {
                Log.w(
                    "MentalRepository",
                    "Failed to fetch mental data, fallback to EMPTY",
                    it,
                )
                MentalUiState.EMPTY
            },
        )
    }
}
