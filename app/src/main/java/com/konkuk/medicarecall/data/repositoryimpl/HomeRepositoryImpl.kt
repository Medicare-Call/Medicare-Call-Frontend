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
        homeService.requestImmediateCareCall(
            ImmediateCallRequestDto(elderId, careCallOption)
        )
    }

    override suspend fun getHomeSummary(elderId: Int): HomeResponseDto {
        // DTO만 반환
        Log.d("HomeRepo", "[REQ] elderId=$elderId")
        val res = homeService.getHomeSummary(elderId) // DTO를 받음

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
