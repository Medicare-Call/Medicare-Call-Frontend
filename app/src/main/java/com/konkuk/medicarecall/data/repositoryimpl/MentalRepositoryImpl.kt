package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.elders.MentalService
import com.konkuk.medicarecall.data.repository.MentalRepository
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel.MentalUiState
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class MentalRepositoryImpl @Inject constructor(
    private val mentalService: MentalService,
) : MentalRepository {

    override suspend fun getMentalUiState(
        elderId: Int,
        date: LocalDate,
    ): MentalUiState = try {
        val dto = mentalService.getDailyMental(elderId, date.toString())

        val comments = dto.commentList.orEmpty()
        Log.d("MENTAL", "comments=$comments")
        MentalUiState(
            mentalSummary = comments,
            isRecorded = comments.isNotEmpty(),
        )
    } catch (e: HttpException) {
        Log.w("MentalRepository", "HTTP error fetching mental data: ${e.code()}", e)
        MentalUiState.Companion.EMPTY
    } catch (e: IOException) {
        Log.w("MentalRepository", "Network error fetching mental data", e)
        MentalUiState.Companion.EMPTY
    }
}
