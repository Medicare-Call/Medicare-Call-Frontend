package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.SetCallService
import com.konkuk.medicarecall.data.dto.request.SetCallTimeRequestDto
import com.konkuk.medicarecall.data.repository.SetCallRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.ui.model.CallTimes
import org.koin.core.annotation.Single

@Single
class SetCallRepositoryImpl(
    private val service: SetCallService,
) : SetCallRepository {
    override suspend fun saveForElder(
        elderId: Long,
        body: SetCallTimeRequestDto,
    ): Result<Unit> = runCatching {
        service.saveCareCallTimes(elderId, body).handleNullableResponse()
    }

    // 오버로드: UI에서 CallTimes만 넘기면 레포가 변환까지 처리
    override suspend fun saveForElder(
        elderId: Long,
        times: CallTimes,
    ): Result<Unit> = saveForElder(elderId, times.toRequestDto())

    // --- 내부 변환 유틸 ---
    private fun Triple<Int, Int, Int>.toHHmm(): String {
        val (amPm, h12, m) = this
        val h24 = when {
            amPm == 0 && h12 == 12 -> 0
            amPm == 1 && h12 < 12 -> h12 + 12
            else -> h12 % 24
        }
        return "%02d:%02d".format(h24, m)
    }

    private fun CallTimes.toRequestDto(): SetCallTimeRequestDto =
        SetCallTimeRequestDto(
            firstCallTime = requireNotNull(first).toHHmm(),
            secondCallTime = requireNotNull(second).toHHmm(),
            thirdCallTime = requireNotNull(third).toHHmm(),
        )
}
