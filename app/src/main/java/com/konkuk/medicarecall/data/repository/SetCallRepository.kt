package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.request.SetCallTimeRequestDto
import com.konkuk.medicarecall.ui.model.CallTimes

interface SetCallRepository {
    suspend fun saveForElder(elderId: Long, body: SetCallTimeRequestDto): Result<Unit>
    suspend fun saveForElder(elderId: Long, times: CallTimes): Result<Unit>
}
