package com.konkuk.medicarecall.ui.feature.homedetail.statemental.data

import android.util.Log
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.model.MentalUiState
import java.time.LocalDate
import javax.inject.Inject
import kotlin.collections.orEmpty

class MentalRepositoryImpl @Inject constructor(
    private val mentalService: MentalService
) : MentalRepository {

    override suspend fun getMentalUiState(
        elderId: Int,
        date: LocalDate
    ): MentalUiState = try {
        val dto = mentalService.getDailyMental(elderId, date.toString())


        val comments = dto.commentList.orEmpty()
        Log.d("MENTAL", "comments=$comments")
        MentalUiState(
            mentalSummary = comments,
            isRecorded = comments.isNotEmpty()
        )
    } catch (e: Exception) {
        MentalUiState.Companion.EMPTY
    }
}
