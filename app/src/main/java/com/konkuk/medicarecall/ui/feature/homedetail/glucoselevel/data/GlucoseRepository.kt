package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.data

import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.model.GlucoseResponseDto

interface GlucoseRepository {
    suspend fun getGlucoseGraph(elderId: Int, counter: Int, type: String): Result<GlucoseResponseDto>
}
