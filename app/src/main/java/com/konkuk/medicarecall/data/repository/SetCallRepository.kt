package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.request.SetCallTimeRequestDto
import com.konkuk.medicarecall.ui.model.CallTimes

interface SetCallRepository {
    suspend fun saveForElder(elderId: Int, body: SetCallTimeRequestDto): Result<Unit>
    suspend fun saveForElder(elderId: Int, times: CallTimes): Result<Unit>
}
