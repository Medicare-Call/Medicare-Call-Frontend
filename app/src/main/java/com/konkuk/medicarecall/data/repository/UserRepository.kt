package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.UserInfo

interface UserRepository {
    suspend fun getMyInfo(): Result<UserInfo>
    suspend fun updateMyInfo(userInfo: UserInfo): Result<UserInfo>
    suspend fun logout(): Result<Unit>
}
