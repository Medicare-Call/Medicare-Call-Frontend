package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.auth.AuthService
import com.konkuk.medicarecall.data.api.member.SettingService
import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val settingService: SettingService,
    private val authService: AuthService,
    private val tokenStore: DataStoreRepository,
) : UserRepository {
    override suspend fun getMyInfo() = runCatching {
        val response = settingService.getMyInfo()
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body is null")
        } else {
            val errorBody = response.errorBody()?.toString() ?: "Unknown error"
            throw Exception("Error ${response.code}: $errorBody")
        }
    }

    override suspend fun updateMyInfo(userUpdateRequestDto: MyInfoResponseDto) = runCatching {
        Log.d("UserRepository", "updateMyInfo() 진입: $userUpdateRequestDto")
        val response = settingService.updateMyInfo(userUpdateRequestDto)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("Update failed with code ${response.code}")
        }
    }

    override suspend fun logout(): Result<Unit> {
        val result = runCatching {
            val refresh = tokenStore.getRefreshToken() ?: throw Exception("Refresh token is null")
            val response = authService.logout("Bearer $refresh")
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.toString() ?: "Unknown error"
                throw Exception("Logout failed: ${response.code} - $errorBody")
            }
            Unit
        }
        // 성공/실패와 무관하게 로컬 토큰 제거
        tokenStore.clearTokens()
        return result
    }
}
