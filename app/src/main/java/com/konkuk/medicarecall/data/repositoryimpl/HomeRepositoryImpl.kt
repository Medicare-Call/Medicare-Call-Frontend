package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.elders.HomeService
import com.konkuk.medicarecall.data.dto.request.ImmediateCallRequestDto
import com.konkuk.medicarecall.data.dto.response.HomeResponseDto
import com.konkuk.medicarecall.data.repository.HomeRepository
import retrofit2.HttpException
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService,
) : HomeRepository {
    override suspend fun requestImmediateCareCall(
        elderId: Int, careCallOption: String,
    ): Result<Unit> = runCatching {
        val response = homeService.requestImmediateCareCall(
            ImmediateCallRequestDto(elderId, careCallOption),
        )
        if (response.isSuccessful) {
            Log.d(
                "httplog",
                "전화 걸림, 어르신: $Int, 시간: $careCallOption",
            )
        } else {
            val errorBody =
                response.errorBody()?.string() ?: "Unknown error(updating health info)"
            Log.e(
                "httplog",
                "전화 걸기 실패: ${response.code()} - $errorBody",
            )
            throw HttpException(response)
        }
    }

    override suspend fun getHomeSummary(elderId: Int): HomeResponseDto {
        // DTO만 반환
        Log.d("HomeRepo", "[REQ] elderId=$elderId")
        val res = homeService.getHomeSummary(elderId) // DTO를 받음

        val meds = res.medicationStatus.medicationList.orEmpty()
        Log.d(
            "HomeRepo",
            "[RES] elderName=${res.elderName}, medsCount=${meds.size}, " +
                "totalTaken=${res.medicationStatus.totalTaken}, totalGoal=${res.medicationStatus.totalGoal}",
        )

        return res
    }
}
