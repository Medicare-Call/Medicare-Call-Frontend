package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.model.MyInfo

interface UserRepository {
    suspend fun getMyInfo(): Result<MyInfo>
    suspend fun updateMyInfo(myInfo: MyInfo): Result<MyInfo>
    suspend fun logout(): Result<Unit>
}
