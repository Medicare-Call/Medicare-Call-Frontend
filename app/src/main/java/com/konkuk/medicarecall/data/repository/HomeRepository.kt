package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Home

interface HomeRepository {
    suspend fun requestImmediateCareCall(elderId: Int, careCallOption: String): Result<Unit>
    suspend fun getHomeSummary(elderId: Int): Result<Home>
}
