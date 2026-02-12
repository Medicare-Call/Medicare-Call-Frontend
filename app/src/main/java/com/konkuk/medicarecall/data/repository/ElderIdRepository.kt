package com.konkuk.medicarecall.data.repository

interface ElderIdRepository {
    suspend fun updateElderIds(elderIdMap: Map<Long, String>)
    suspend fun updateElderId(elderId: Long, name: String)
    suspend fun clearElderIds()
    suspend fun getElderIds(): Map<Long, String>
}
