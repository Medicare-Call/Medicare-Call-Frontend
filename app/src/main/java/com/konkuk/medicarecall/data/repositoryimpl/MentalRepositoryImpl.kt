package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.MentalService
import com.konkuk.medicarecall.data.repository.MentalRepository
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel.MentalUiState
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class MentalRepositoryImpl(
    private val mentalService: MentalService,
) : MentalRepository {

    override suspend fun getMental(
        elderId: Int,
        date: LocalDate,
    ): Result<Mental> = runCatching {

        val response = mentalService.getDailyMental(
            elderId = elderId,
            date = date.toString(),
        ).handleResponse()

        Mental(
            mentalSummary = response.commentList.orEmpty(),
        )
    }
}
