package com.konkuk.medicarecall.ui.homedetail.glucoselevel.data

import com.konkuk.medicarecall.ui.homedetail.glucoselevel.model.GlucoseResponseDto

interface GlucoseRepository {
    suspend fun getGlucoseGraph(elderId: Int, counter: Int, type: String): Result<GlucoseResponseDto>
}