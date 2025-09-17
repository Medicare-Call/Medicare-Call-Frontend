package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto

interface UpdateElderInfoRepository {
    suspend fun updateElderInfo(id: Int, request: ElderRegisterRequestDto): Result<Unit>
    suspend fun deleteElder(id: Int): Result<Unit>
}
