package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Glucose

interface GlucoseRepository {
    suspend fun getGlucoseGraph(elderId: Int, counter: Int, type: String): Result<Glucose>
}
