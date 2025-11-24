package com.konkuk.medicarecall.data.repository

import kotlinx.coroutines.flow.Flow

interface ElderIdRepository {
    suspend fun updateElderIds(elderIdMap: Map<Int, String>)
    suspend fun updateElderId(elderId: Int, name: String)
    suspend fun clearElderIds()
    suspend fun getElderIds(): Flow<Map<Int, String>>
}
