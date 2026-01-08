package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.auth.AuthService
import com.konkuk.medicarecall.data.api.member.SettingService
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.UserRepository
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val settingService: SettingService,
    private val authService: AuthService,
    private val tokenStore: DataStoreRepository,
) : UserRepository {
    override suspend fun getMyInfo() = runCatching {
        val response = settingService.getMyInfo()
        if (response.isSuccessful) {
            response.body() ?: error("Response body is null")
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            throw HttpException(response)
        }
    }

    override suspend fun updateMyInfo(userUpdateRequestDto: MyInfoResponseDto) = runCatching {
        Log.d("UserRepository", "updateMyInfo() 진입: $userUpdateRequestDto")
        val response = settingService.updateMyInfo(userUpdateRequestDto)
        if (response.isSuccessful) {
            response.body() ?: error("Response body is null")
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun logout(): Result<Unit> {
        val result = runCatching {
            val refresh = tokenStore.getRefreshToken() ?: error("Refresh token is null")
            val response = authService.logout("Bearer $refresh")
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                throw HttpException(response)
            }
            Unit
        }
        // 성공/실패와 무관하게 로컬 토큰 제거(보안/UX 측면에서 권장)
        tokenStore.clearTokens()
        return result
    }
}
