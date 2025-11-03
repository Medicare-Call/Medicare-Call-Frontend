package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.elders.EldersInfoService
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.repository.UpdateElderInfoRepository
import retrofit2.HttpException
import javax.inject.Inject

class UpdateElderInfoRepositoryImpl @Inject constructor(
    private val eldersInfoService: EldersInfoService,
) : UpdateElderInfoRepository {
    override suspend fun updateElderInfo(id: Int, request: ElderRegisterRequestDto): Result<Unit> =
        runCatching {
            val response = eldersInfoService.updateElder(id, request)
            if (response.isSuccessful) {
                response.body() ?: error("Response body is null")
            } else {
                throw HttpException(response)
            }
        }

    override suspend fun deleteElder(id: Int): Result<Unit> = runCatching {
        val response = eldersInfoService.deleteElderSettings(id)
        if (response.isSuccessful) {
            response.body() ?: error("Response body is null")
        } else {
            val body = response.errorBody()?.string().orEmpty()
            Log.e("DeleteElder", "HTTP ${response.code()} body=$body")
            throw HttpException(response)
        }
    }
}
