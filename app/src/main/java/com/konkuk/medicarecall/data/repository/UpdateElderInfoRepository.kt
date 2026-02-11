package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.model.ElderInfo

interface UpdateElderInfoRepository {
    suspend fun updateElderInfo(elderInfo: ElderInfo): Result<Unit>
    suspend fun deleteElder(id: Int): Result<Unit>
}
