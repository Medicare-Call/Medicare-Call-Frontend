package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.ElderInfo

interface UpdateElderInfoRepository {
    suspend fun updateElderInfo(elderInfo: ElderInfo): Result<Unit>
    suspend fun deleteElder(id: Long): Result<Unit>
}
