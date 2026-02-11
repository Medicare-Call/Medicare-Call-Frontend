package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Home

interface HomeRepository {
    suspend fun requestImmediateCareCall(elderId: Long, careCallOption: String): Result<Unit>
    suspend fun getHomeSummary(elderId: Long): Result<Home>
}
