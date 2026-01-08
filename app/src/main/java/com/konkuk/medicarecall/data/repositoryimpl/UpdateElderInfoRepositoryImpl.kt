package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository

class UpdateElderInfoRepositoryImpl(
    private val eldersInfoService: EldersInfoService,
) : UpdateElderInfoRepository {
    override suspend fun updateElderInfo(id: Int, request: ElderRegisterRequestDto): Result<Unit> =
        runCatching {
            val response = eldersInfoService.updateElder(id, request)
            if (response.isSuccessful) {
                Unit
            } else {
                error("Update failed with code ${response.code}")
            }
        }

    override suspend fun deleteElder(id: Int): Result<Unit> = runCatching {
        val response = eldersInfoService.deleteElderSettings(id)
        if (response.isSuccessful) {
            Unit
        } else {
            val body = response.errorBody()?.toString().orEmpty()
            Log.e("DeleteElder", "HTTP ${response.code} body=$body")
            error("Delete failed: ${response.code}")
        }
    }
}
