package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.auth.AuthService
import com.konkuk.medicarecall.data.api.member.SettingService
import com.konkuk.medicarecall.data.mapper.UserMapper
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.UserRepository
import com.konkuk.medicarecall.data.util.handleNullableResponse
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.UserInfo
import org.koin.core.annotation.Single

@Single
class UserRepositoryImpl(
    private val settingService: SettingService,
    private val authService: AuthService,
    private val tokenStore: DataStoreRepository,
) : UserRepository {
    override suspend fun getMyInfo() = runCatching {
        val responseDto = settingService.getMyInfo().handleResponse()
        UserMapper.toDomain(responseDto)
    }

    override suspend fun updateMyInfo(userInfo: UserInfo) = runCatching {
        val requestDto = UserMapper.toRequestDto(userInfo)
        val responseDto = settingService.updateMyInfo(requestDto).handleResponse()
        UserMapper.toDomain(responseDto)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val refresh = tokenStore.getRefreshToken() ?: error("Refresh token is null")
        authService.logout("Bearer $refresh").handleNullableResponse()
        // 성공/실패와 무관하게 로컬 토큰 제거(보안/UX 측면에서 권장)
        tokenStore.clearTokens()
    }
}
