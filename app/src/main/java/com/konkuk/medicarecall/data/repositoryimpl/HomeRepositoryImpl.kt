package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.elders.HomeService
import com.konkuk.medicarecall.data.dto.request.ImmediateCallRequestDto
import com.konkuk.medicarecall.data.dto.response.HomeResponseDto
import com.konkuk.medicarecall.data.repository.HomeRepository
import org.koin.core.annotation.Single

@Single
class HomeRepositoryImpl(
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
            Log.e(
                "httplog",
                "전화 걸기 실패: ${response.code}",
            )
            error("Immediate care call failed with code=${response.code}")
        }
    }

    override suspend fun getHomeSummary(elderId: Int): HomeResponseDto {
        // DTO만 반환
        Log.d("HomeRepo", "[REQ] elderId=$elderId")
        val response = homeService.getHomeSummary(elderId)

        if (!response.isSuccessful) {
            error("Home summary fetch failed with code=${response.code}")
        }

        val res = response.body()
            ?: error("Home summary response body is null")

        val medicationStatus = res.medicationStatus
        val meds = medicationStatus?.medicationList.orEmpty()
        Log.d(
            "HomeRepo",
            "[RES] elderName=${res.elderName}, medsCount=${meds.size}, " +
                "totalTaken=${medicationStatus?.totalTaken}, totalGoal=${medicationStatus?.totalGoal}",
        )
        return res
    }
}
